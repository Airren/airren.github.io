---
title: strongswan
---



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


pki --issue --in node-1Key.pem --type priv \
--cacert caCert.pem --cakey caKey.pem \
--dn "C=CH, O=strongSwan,CN=node-1" \
--outform pem> node-1Cert.pem
```



### Host-to-Host 配置

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



上述配置是 是用key做认证，下面举个使用pre-shared key的例子

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



配置完成后，可以使用`swanclt --load-all` 使配置生效。



如果给initiator 分配一个[Virtual IP](https://wiki.strongswan.org/projects/strongswan/wiki/VirtualIp).

Initiator 获得虚拟IP后会再 IP table 220 中增加对应IP的路由方式。



## Build Strongswan  with pcks11

```sh
# install essential dependency
sudo apt install  build-essential libgmp-dev libunbound-dev libldns-dev
./autogen.sh
#  config
# ./configure --prefix=/usr --sysconfdir=/etc --enable-eap-mschapv2 --enable-kernel-libipsec --enable-swanctl --enable-unity --enable-unbound --enable-vici --enable-xauth-eap --enable-xauth-noauth --enable-eap-identity --enable-md4 --enable-pem --enable-openssl --enable-pubkey --enable-farp --enable-pkcs11
./configure --prefix=/usr --sysconfdir=/etc --enable-pkcs11
```





### Build virt_cacard

[virt_card](https://github. com/Jakuje/virt_cacard) using libcacard, vitualsmartcard's vpcd and [softhsm2](https://fossies.org/linux/softhsm/README.md) to provide PCSC accessible virtual smart card.

```sh
# install essential dependency, libcacard & softhsm2
sudo apt install libcacard-dev libglib2.0-dev softhsm2 gnutls-bin libnss3-tools -y
```

Build & Install [vsmartcard](https://frankmorgner.github.io/vsmartcard/virtualsmartcard/README.html)

```sh
sudo apt-get install -y help2man libpcsclite-dev
git clone https://github.com/frankmorgner/vsmartcard.git
cd vsmartcard/virtualsmartcard
autoreconf --verbose --install
./configure --sysconfdir=/etc
make
smake install
```

Build & Install virt_card

```sh
git clone https://github.com/Jakuje/virt_cacard.git
./autogen.sh
./configure
make
```

configure softhsm with default certificates and start virt_cacard

```sh
./setup-softhsm2.sh
./virt_cacard
```

After that you should be able to access virtual smart card through OpenSC:

```sh
pkcs11-tool -L
```





```sh
pkcs15-tool --list-pins --list-keys --list-certificates
```

**0**













```sh
# Generate Key pair
openssl req -out pkcs11-new.csr -newkey rsa:2048 -nodes -keyout pkcs11-new.key -subj "/CN=pkcs11-new" -passout pass:123456
# Generate Certificate
openssl x509 -req -days 365 -CA caCert.pem -CAkey caKey.pem -set_serial 1 -in pkcs11-new.csr -out pkcs11-new.crt
# Transform CA type to DER
 openssl rsa -in ./pkcs11-new.key -outform DER -out clientkey.der
 openssl x509 -in ./pkcs11-new.crt -outform DER -out clientcrt.der
 
 
 # Creating a token
 pkcs11-tool --module /usr/local/lib/libp11sgx.so --init-token --label "ctk" --slot 0x2 --so-pin 1234 --init-pin --pin 1234
 
 
  pkcs11-tool --module /usr/local/lib/libp11sgx.so --list-slot
```







### Common Tools of PKCS#11

```sh
# list slot
pkcs11-tool --module /usr/local/lib/libp11sgx.so -L
# list object of slot
pkcs11-tool --module /usr/local/lib/libp11sgx.so --slot 0x7316c269 -O

```



### Creating a token

```sh
pkcs11-tool --module /usr/local/lib/libp11sgx.so --init-token --label "ctk" --slot 0 --so-pin 1234 --init-pin --pin 1234
```

### Creating an RSA keypair

```sh
pkcs11-tool --module /usr/local/lib/libp11sgx.so --login --pin 1234 --id 0001 --token "ctk" --keypairgen --key-type rsa:3072 --label "cert-key" --usage-sign
```

### Listing the objects

```sh
pkcs11-tool --module /usr/local/lib/libp11sgx.so --list-objects -login --pin 1234 --login-type user
```



```shell
# login
pkcs11-tool --module /usr/local/lib/libp11sgx.so -login --pin 1234 --login-type user --slot 0x7316c269 -
 #listobject
 pkcs11-tool --module /usr/local/lib/libp11sgx.so -login --pin 1234 --login-type user --slot 0x7316c269 -O
# delete private key
 pkcs11-tool --module /usr/local/lib/libp11sgx.so -login --pin 1234 --login-type user --slot 0x7316c269 --delete-object --type privkey -d 0001
 # delete public key
 pkcs11-tool --module /usr/local/lib/libp11sgx.so -login --pin 1234 --login-type user --slot 0x7316c269 --delete-object --type pubkey -d 0001
 
 # add private key
 pkcs11-tool --module /usr/local/lib/libp11sgx.so -login --pin 1234 --login-type user --slot 0x7316c269 --write-object clientkey.der --type privkey --id 1001
 # add cert
 pkcs11-tool --module /usr/local/lib/libp11sgx.so -login --pin 1234 --login-type user --slot 0x7316c269 --write-object clientcrt.der --type cert --id 1001
 
 # create key paair
 pkcs11-tool --module /usr/local/lib/libp11sgx.so --login --pin 1234 --id 0001 --token "ctk" --keypairgen --key-type rsa:3072 --label "cert-key" --usage-sign  --slot 0x7316c269
```















































> Istio 使用同一个证书





