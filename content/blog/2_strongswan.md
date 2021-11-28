

When reading/adjusting any StrongSwan configurations, remember these important words:

**`left is local to the machine it's stated on; right is remote in the same manner`**

So, on the server side, left is local to the server and on the client side, left is local to that client.





## Ubuntu Set up IPsec Tunnel 

###  Install StrongsWan

```sh
sudo apt install strongswan strongswan-swanctl strongswan-pki strongswan-charon charon-cmd charon-systemd -y
```



在如下两台机器之间 建立 host-to-host的ipsec tunnle, 以下配置过程整理自[Strongswan](https://github.com/strongswan/strongswan/blob/master/README.md#generating-a-host-or-user-end-entity-certificate).

```sh
| 10.95.62.25 | === | 10.95.62.114 |
     moon                sun
```

### 生成证书

使用strongswan的[PKI tool](https://wiki.strongswan.org/projects/strongswan/wiki/SimpleCA) 创建证书。

```sh
# 创建 root 私钥
pki --gen --outform pem > strongswanKey.pem
# 生成 root CA
pki --self --in caKey.pem --dn "C=CH, O=strongSwan, CN=strongSwan CA" --ca --outform pem > strongswanCert.pem

# 生成 node-1 私钥
pki --gen --outform pem > moonKey.pem
# 使用 root ca签发 node-1 端证书
pki --issue --in moonKey.pem --type priv \
--cacert strongswanCert.pem --cakey strongswanKey.pem \
--dn "C=CH, O=strongSwan,CN=moon.strongswan.org" --san moon.strongswan.org \
--outform pem > moonCert.pem

# 生成 node-2 私钥
pki --gen --outform pem > sunKey.pem
# 使用 root ca签发 node-1 端证书
pki --issue --in sunKey.pem --type priv \
--cacert strongswanCert.pem --cakey strongswanKey.pem \
--dn "C=CH, O=strongSwan,CN=sun.strongswan.org" --san sun.strongswan.org \
--outform pem> sunKey.pem
```



### Strongswan 配置

**Configuration on host *moon*:**

```sh
/etc/swanctl/x509ca/strongswanCert.pem
/etc/swanctl/x509/moonCert.pem
/etc/swanctl/private/moonKey.pem

/etc/swanctl/swanctl.conf:

    connections {
        host-host {
            remote_addrs = 10.95.62.114

            local {
                auth=pubkey
                certs = moonCert.pem
            }
            remote {
                auth = pubkey
                id = "C=CH, O=strongSwan, CN=sun.strongswan.org"
            }
            children {
                net-net {
                    start_action = trap
                }
            }
        }
    }
```

**Configuration on host *sun*:**

```sh
/etc/swanctl/x509ca/strongswanCert.pem
/etc/swanctl/x509/sunCert.pem
/etc/swanctl/private/sunKey.pem

/etc/swanctl/swanctl.conf:

    connections {
        host-host {   # connection name
            remote_addrs = 10.95.62.25

            local {
                auth = pubkey
                certs = sunCert.pem
            }
            remote {
                auth = pubkey
                id = "C=CH, O=strongSwan, CN=moon.strongswan.org"
            }
            children {
                host-host {
                    start_action = trap
                }
            }
        }
    }
```



上述配置是 是用key做认证，下面举个使用pre-share key的例子

```sh
  # configuration on moon
  connections {
        host-host {
            remote_addrs = 10.95.62.114

            local {
                auth=psk
                id = "moon.strongswan.org"
            }
            remote {
                auth = psk
                id = "sun.strongswan.org"
            }
            children {
                net-net {
                    start_action = trap
                }
            }
        }
    }
 secrets{
    ike-h2h{
        id-moon =  "moon.strongswan.org"
        id-sun =  "sun.strongswan.org"
        secret = mysecret
    }
}
    
    # configuration on sun
    connections {
        host-host {   # connection name
            remote_addrs = 10.95.62.25

            local {
                auth = psk
                id = "sun.strongswan.org"
            }
            remote {
                auth = psk
                id = "moon.strongswan.org"
            }
            children {
                host-host {
                    start_action = trap
                }
            }
        }
    }
    secrets{
        ike-h2h{
            id-moon =  "moon.strongswan.org"
            id-sun =  "sun.strongswan.org"
            secret = mysecret
        }
    }
```





