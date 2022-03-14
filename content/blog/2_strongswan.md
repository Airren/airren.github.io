---
title: strongswan
---



When reading/adjusting any StrongSwan configurations, remember these important words:

`left is local to the machine it's stated on; right is remote in the same manner`

So, on the server side, left is local to the server and on the client side, left is local to that client.



证书check

```sh
openssl x509 -text -noout -in  /etc/ipsec.d/private/sunKey.pem
```





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
pki --gen --outform pem > caKey.pem
# 生成 root CA
pki --self --in caKey.pem --dn "C=CH, O=strongSwan, CN=strongSwan CA" --ca --outform pem > caCert.pem

# 生成 node-1 私钥
pki --gen --outform pem > moonKey.pem
# 使用 root ca签发 node-1 端证书
pki --issue --in moonKey.pem --type priv \
--cacert caCert.pem --cakey caKey.pem \
--dn "C=CH, O=strongSwan,CN=moon.strongswan.org" --san moon.strongswan.org \
--outform pem > moonCert.pem

# 生成 node-2 私钥
pki --gen --outform pem > sunKey.pem
# 使用 root ca签发 node-1 端证书
pki --issue --in sunKey.pem --type priv \
--cacert caCert.pem --cakey caKey.pem \
--dn "C=CH, O=strongSwan,CN=sun.strongswan.org" --san sun.strongswan.org \
--outform pem  > sunCert.pem


pki --issue --in root-nodeKey --type priv \
--cacert caCert.pem --cakey caKey.pem \
--dn "C=CH, O=strongSwan,CN=node-3" \
--outform pem> node-3Cert.pem
```



### Host-to-Host 配置

**Configuration on host *moon*:**

```sh
 cp caCert.pem /etc/swanctl/x509ca/strongswanCert.pem
 cp moonCert.pem /etc/swanctl/x509/moonCert.pem
 cp moonKey.pem /etc/swanctl/private/moonKey.pem

/etc/swanctl/swanctl.conf:

    connections {
        host-host {
            remote_addrs = 10.233.76.161

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
 cp caCert.pem /etc/swanctl/x509ca/strongswanCert.pem
cp sunCert.pem /etc/swanctl/x509/sunCert.pem
cp sunKey.pem /etc/swanctl/private/sunKey.pem

/etc/swanctl/swanctl.conf:

    connections {
        host-host {   # connection name
            remote_addrs = 10.233.76.163

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



## SmartCard demo with OpenSC

### Build Strongswan  with pcks11

```sh
# install essential dependency
sudo apt install  build-essential libgmp-dev libunbound-dev libldns-dev -y
git clone https://github.com/strongswan/strongswan.git
./autogen.sh
#  config
# ./configure --prefix=/usr --sysconfdir=/etc --enable-eap-mschapv2 --enable-kernel-libipsec --enable-swanctl --enable-unity --enable-unbound --enable-vici --enable-xauth-eap --enable-xauth-noauth --enable-eap-identity --enable-md4 --enable-pem --enable-openssl --enable-pubkey --enable-farp --enable-pkcs11
./configure --prefix=/usr --sysconfdir=/etc --enable-pkcs11 CFLAGS="-DDEBUG_LEVEL=1"
make
sudo make install
sudo systemctl daemon-reload
sudo systemctl restart strongswan-starter.service
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
sudo make install
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
export SOFTHSM2_CONF=/home/airren/SGX/virt_cacard/softhsm2.conf &&./virt_cacard
```

After that you should be able to access virtual smart card through OpenSC:

```sh
pkcs11-tool -L
```



```sh
pkcs15-tool --list-pins --list-keys --list-certificates
```

```sh
# Generate Key pair
openssl req -out pkcs11-new.csr -newkey rsa:2048 -nodes -keyout pkcs11-new.key -subj "/CN=pkcs11-new" 
# Generate Certificate
openssl x509 -req -days 365 -CA caCert.pem -CAkey caKey.pem -set_serial 1 -in pkcs11-new.csr -out pkcs11-new.crt
# Transform CA type to DER
 openssl rsa -in ./pkcs11-new.key -outform DER -out pkcs11-new.key.der
 openssl x509 -in ./pkcs11-new.crt -outform DER -out pkcs11-new.crt.der
 
 
 # Creating a token
#  pkcs11-tool  --init-token --label "pkcs11-new"  --slot 0--so-pin 12345678 --init-pin --pin 12345678
 
  # add private key
 pkcs11-tool  -login --pin 12345678 --login-type user --slot 0 --write-object pkcs11-new.key.der --type privkey --id 0001
 # add cert
 pkcs11-tool  -login --pin 12345678 --login-type user --slot 0 --write-object pkcs11-new.crt.der --type cert --id 0001
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
pkcs11-tool --module /usr/local/lib/libp11sgx.so -login --pin 1234 --login-type user --slot 0x18c37829 -
 #listobject
 pkcs11-tool --module /usr/local/lib/libp11sgx.so -login --pin 1234 --login-type user --slot 0x18c37829 -O
# delete private key
 pkcs11-tool --module /usr/local/lib/libp11sgx.so -login --pin 1234 --login-type user --slot 0x18c37829 --delete-object --type privkey -d 0001
 # delete public key
 pkcs11-tool --module /usr/local/lib/libp11sgx.so -login --pin 1234 --login-type user --slot 0x18c37829 --delete-object --type pubkey -d 0001
 
 # add private key
 pkcs11-tool --module /usr/local/lib/libp11sgx.so -login --pin 12345678 --login-type user --slot 0x18c37829 --write-object clientkey.der --type privkey --id 0001
 # add cert
 pkcs11-tool --module /usr/local/lib/libp11sgx.so -login --pin 1234 --login-type user --slot 0xc8cbdbc --write-object clientcrt.der --type cert --id 0001
 
 # create key paair
 pkcs11-tool --module /usr/local/lib/libp11sgx.so --login --pin 1234 --id 0001 --token "ctk" --keypairgen --key-type rsa:3072 --label "cert-key" --usage-sign  --slot 0x18c37829
```



## Smart Demo with Intel SGX CTK



[Build and Install SGX SDK](./3_intel_sgx.md)







### Build  & Install SDK

```sh
# Ubuntu 20.04
sudo apt-get install build-essential ocaml ocamlbuild automake autoconf libtool wget python-is-python3 libssl-dev git cmake perl -y
sudo apt-get install libssl-dev libcurl4-openssl-dev protobuf-compiler libprotobuf-dev debhelper cmake reprepro unzip -y

sudo apt-get install build-essential python -y

git clone https://github.com/intel/linux-sgx.git
cd linux-sgx && make preparation
sudo cp external/toolset/{current_distr}/{as,ld,ld.gold,objdump} /usr/local/bin
which as ld ld.gold objdump

make sdk
make sdk_install_pkg
# linux/installer/bin/sgx_linux_x64_sdk_${version}.bin  location /opt/intel

export SDK_INSTALL_PATH_PREFIX=/opt/intel
./sgx_linux_x64_sdk_${version}.bin --prefix $SDK_INSTALL_PATH_PREFIX
 source ${sgx-sdk-install-path}/environment
 # source /opt/intel/sgxsdk/environment
```

**verify SGX SDK**

```sh
$ cd ${sgx-sdk-install-path}/SampleCode/LocalAttestation
  $ make SGX_MODE=SIM
  $ cd bin
  $ ./app
```



### Build & Install PSW

```sh
make psw
make deb_psw_pkg
make deb_local_repo
# linux/installer/deb/sgx_debian_local_repo
# deb [trusted=yes arch=amd64] file: /home/ubuntu/linux-sgx/linux/installer/deb/local_repo_tool/../sgx_debian_local_repo focal main
sudo apt update
sudo apt-get install libsgx-launch libsgx-urts -y
```

 

### Build & Install  Intel-sgx-ssl

```sh
make all test
sudo make install
```



### Build & Install CTK

```sh
sudo apt-get install dkms  autoconf libcppunit-dev autotools-dev libc6-dev libtool build-essential -y
#libprotobuf10

make
sudo make install
```



### Build & Install pkcs11 Tool

```sh
git clone https://github.com/Mastercard/pkcs11-tools.git
./configure
sudo make install
```

use `p11req`  generate csr

```sh
p11req -i my-ec-key -d '/CN=my.site.org/O=My organization/C=BE' -e 'DNS:another-url-for-my.site.org' -v
```



### Initialize HSM & Generate Cert

```shell
# Init Token
pkcs11-tool --module /usr/local/lib/libp11sgx.so --init-token --label "sgx-pkcs11" --slot 0 --so-pin 12345678 --init-pin --pin 12345678
#Create Key Pair
pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678 --id 0001 --token "sgx-pkcs11" --keypairgen --key-type rsa:2048 --label "cert-key" --usage-sign

# Check slot info
pkcs11-tool --module /usr/local/lib/libp11sgx.so -L 

# Create csr, cert-key is the private lable
p11req -l /usr/local/lib/libp11sgx.so -i cert-key -d '/CN=sgx-node'  -s 0x5e6dceb4 -p 12345678 > new.csr
 
# Issuer the cert from root CA 
openssl x509 -req -days 365 -CA caCert.pem -CAkey caKey.pem -set_serial 1 -in new.csr -out client.crt
# Transfer to DER form
openssl x509 -in client.crt -outform DER -out clientcrt.der
# Add cert to HSM
pkcs11-tool --module /usr/local/lib/libp11sgx.so -login --pin 12345678 --login-type user --slot 0x5e6dceb4 --write-object clientcrt.der --type cert --id 0001
 
# Check private Key and Cert status
pkcs11-tool --module /usr/local/lib/libp11sgx.so --login --pin 12345678  -O  --slot 0x5e6dceb4 
```



```sh
#需要下载的 组件

crypto-api-toolkit  intel-sgx-ssl  linux-sgx  linux-sgx-driver  OpenSC  pkcs11  SDEWAN-SetUp  SGX.code-workspace  sgx-pkcs11  sgx-software-enable  strongswan  virt_cacard  vsmartcard
```



### Configure PCKS#11 Plugin of Strongswan

```sh
#strongswan.d/charon/pkcs11.conf
pkcs11 {
    load = yes
    modules {
        ctk{
                path=/usr/local/lib/libp11sgx.so
                os_locking=yes
                load_certs=yes
        }
    }

}
```



### Configure IPsec Tunnel  through swanctl.conf

```sh
# /etc/swanctl/swanctl.conf
connections {
    pkcs11-demo{   # connection name
       # remote_addrs = 10.95.62.25
       pools = client_pool

       local {
           auth = pubkey
           cert1{
               handle=0001
               slot=0x208efd0c
               module=ctk
           }
       }
       remote {
           auth = pubkey
           id = "C=CH, O=strongSwan,CN=sun.strongswan.org"
       }
       children {
           pkcs11-demo {
               start_action = trap
           }
       }
    }
}

pools{
    client_pool{
        addrs=192.168.0.1
    }
}l

secrets{
#    token_1{
#        handle=0001
#        slot=0
#        module=opensc
#        pin=12345678
#    }
    token_2{
        handle=0001
        slot=0x208efd0c
        module=ctk
        pin=12345678
    }
}


```





### Configure IPsec Tunnel Through ipsec.conf use different secret

```sh
# /etc/ipsec.conf

# ipsec.conf - strongSwan IPsec configuration file

# basic configuration

config setup
        # strictcrlpolicy=yes
        # uniqueids = no

# Add connections here.

# Sample VPN connections

#conn sample-self-signed
#      leftsubnet=10.1.0.0/16
#      leftcert=selfCert.der
#      leftsendcert=never
#      right=192.168.0.2
#      rightsubnet=10.2.0.0/16
#      rightcert=peerCert.der
#      auto=start




conn pkcs11-legacy
  left=%any
  right=%any
  ikelifetime=3h
  lifetime=1h
  margintime=9m
  keyingtries=%forever
  dpdaction=restart
  dpddelay=30s
  closeaction=restart
  leftauth=pubkey
  rightauth=pubkey
  leftcert=%smartcard:0001
  leftsendcert=yes
  rightsendcert=yes
  rightsourceip=192.168.0.8
  auto=start
  leftid="CN=sgx-node"
  rightid="C=CH, O=strongSwan,CN=sun.strongswan.org"
  keyexchange=ikev2
  mark=30
  esp=aes128-sha256-modp3072,aes256-sha256-modp3072
  ike=aes128-sha256-modp3072,aes256-sha256-modp3072
  type=tunnel



conn common-con
  left=%any
  right=%any
  ikelifetime=3h
  lifetime=1h
  margintime=9m
  keyingtries=%forever
  dpdaction=restart
  dpddelay=30s
  closeaction=restart
  leftauth=pubkey
  rightauth=pubkey
  leftcert=/etc/ipsec.d/certs/root-nodeCert.pem
  leftsendcert=yes
  rightsendcert=yes
  rightsourceip=192.168.0.9
  auto=start
  leftid="CN=root-node"
  rightid="C=CH, O=strongSwan,CN=node-3"
  keyexchange=ikev2
  mark=30
  esp=aes128-sha256-modp3072,aes256-sha256-modp3072
  ike=aes128-sha256-modp3072,aes256-sha256-modp3072
  type=tunnel
  
  
  
  # client
cp caCert.pem /etc/ipsec.d/cacerts/
cp sunKey.pem /etc/ipsec.d/private/
cp sunCert.pem /etc/ipsec.d/certs/

conn common-con
  left=%any
  right=10.233.76.147
  leftsourceip=%config
  ikelifetime=3h
  lifetime=1h
  margintime=9m
  keyingtries=%forever
  dpdaction=restart
  dpddelay=30s
  closeaction=restart
  leftauth=pubkey
  rightauth=pubkey
  leftcert=/etc/ipsec.d/certs/sunCert.pem
  leftsendcert=yes
  rightsendcert=yes
  auto=start
  leftid="C=CH, O=strongSwan,CN=sun.strongswan.org"
  rightid="CN=sgx-node"
  keyexchange=ikev2
  esp=aes128-sha256-modp3072,aes256-sha256-modp3072
  ike=aes128-sha256-modp3072,aes256-sha256-modp3072
  type=tunnel
```



ipsec.secrets

```sh
# /etc/ipsec.secrets
C=CH, O=strongSwan,CN=sun.strongswan.org : RSA sunKey.pem
: PIN %smartcard:0001 "12345678"

```





```sh

# 1. Add strongswan.d/chron/pkcs11.conf


# 2. This is the firsth 
```





/usr/sbin/ipsec start --nofork





##  OpenWRT with Ubuntu-SGX as a sidecar

Stratege1: shared a Mount Path,  Create Ipsec with PCKS#11 by ubuntu.





https_proxy=http://child-prc.intel.com:913;http_proxy=http://child-prc.intel.com:913 ./pip install -r ../../requirements.txt

git clone https://github.com/strongswan/strongMan.git



Stratege2: Use p11-kit to do a remote HSM Forwarding,   Create Ipsec with PCKS#11 in openwrt.



## StrangManAPI



```sh
curl 'http://sdewan-sgx.sh.intel.com:31515/certificates/add' \
  -X 'POST' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 1655' \
  -H 'Pragma: no-cache' \
  -H 'Cache-Control: no-cache' \
  -H 'Upgrade-Insecure-Requests: 1' \
  -H 'Origin: http://sdewan-sgx.sh.intel.com:31515' \
  -H 'Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryNLIZnXX6IucXXau8' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36' \
  -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9' \
  -H 'Referer: http://sdewan-sgx.sh.intel.com:31515/certificates/add' \
  -H 'Accept-Language: en-US,en;q=0.9,zh-CN;q=0.8,zh-TW;q=0.7,zh;q=0.6' \
  -H 'Cookie: AMCVS_AD2A1C8B53308E600A490D4D%40AdobeOrg=1; at_check=true; s_cc=true; intelresearchSTG=5; aam_uuid=49058956183404717814245980060213674706; ELQSTATUS=OK; _cs_c=0; ELOQUA=GUID=DC19EC9AFC43457EA1826BEBB4BBB7DE; kndctr_AD2A1C8B53308E600A490D4D_AdobeOrg_consent=general=in; utag_main=v_id:017e8a96b666001a9147b05b38cd05078005107001274$_sn:4$_se:1$_ss:1$_st:1644388152375$wa_ecid:49081350502256117724243741728847082187$ses_id:1644386352375%3Bexp-session$_pn:1%3Bexp-session$wa_adbchk:1%3Bexp-session; kndctr_AD2A1C8B53308E600A490D4D_AdobeOrg_identity=CiY0OTA4MTM1MDUwMjI1NjExNzcyNDI0Mzc0MTcyODg0NzA4MjE4N1IPCIPI3tToLxgBKgRKUE4z8AHDh8Lo7S8=; AMCV_AD2A1C8B53308E600A490D4D%40AdobeOrg=1585540135%7CMCIDTS%7C19033%7CMCMID%7C49081350502256117724243741728847082187%7CMCAAMLH-1644991154%7C11%7CMCAAMB-1644991154%7CRKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y%7CMCOPTOUT-1644393554s%7CNONE%7CvVersion%7C4.4.0; mbox=PC#16bb4f457c834f62af6d9bd0a5f5febb.38_0#1706247443|session#b274b1ad3c9c4b0eb07c0003870201f6#1644388216; _cs_id=5508117f-2589-a486-fa1a-55240eb57587.1643002642.5.1644386355.1644386355.1589385054.1677166642887; adcloud={%22_les_v%22:%22y%2Cintel.com%2C1644388157%22}; ph_mqkwGT0JNFqO-zX2t0mW6Tec9yooaVu7xCBlXtHnt5Y_posthog=%7B%22distinct_id%22%3A%2217efc29d9a38cb-0e6e8852bd03ad-133f685c-384000-17efc29d9a41172%22%2C%22%24device_id%22%3A%2217efc29d9a38cb-0e6e8852bd03ad-133f685c-384000-17efc29d9a41172%22%7D; currentmode=server; csrftoken=vAUiU7ihilLZIMWeRl1bMbG4cwVHX8LN5gAhDP6TN2jyipQtr8PM0xMIy0nFB6BC; sessionid=373uzttbwjiswel109rpxtlz7cyscpzk' \
  --compressed \
  --insecure
```



```sh





```




