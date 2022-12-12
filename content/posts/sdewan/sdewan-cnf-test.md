



#### openwrt login

```sh
curl -XPOST  curl --location -v --request POST 'https://10-233-76-144.default.pod.cluster.local/cgi-bin/luci/?luci_username=root&luci_password=root1' --cacert ./ca.pem

  # Cert is the cnf-default-cert, must use the h
curl --location -v --request POST 'https://10-233-76-144.default.pod.cluster.local/cgi-bin/luci/' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'luci_username=root' \
--data-urlencode 'luci_password=root1' \
--cacert ./ca.pem 

```





**Create CSR**

````sh
curl -XGET 'https://10-233-76-144.default.pod.cluster.local/cgi-bin/luci/sdewan/nat/v1/nats' --cacert ./ca.pem 
````



```sh
curl -XGET 'https://10-233-76-144.default.pod.cluster.local/cgi-bin/luci/sdewan/pkcs11/v1/crs' --cacert ./ca.pem 
```





```bash
#!/bin/bash

set -x

cnf_ip="10-233-76-144.default.pod.cluster.local"
cert_label="node-1"
cert_subject="/CN=node-1"

# you alway a same csr, even you try many times
curl --location --request POST "https://${cnf_ip}/cgi-bin/luci/sdewan/pkcs11/v1/crs" \
--header 'Content-Type: application/json' \
--data-raw "{
    \"cert\": {
        \"key_pair\": {
            \"key_type\": \"rsa:2048\",
            \"label\": \"${cert_label}\",
            \"id\": \"0001\"
        },
        \"subject\": \"${cert_subject}\",
        \"pem\": \"\"
    }
}" --cert ca.pem | tee  new.csr

openssl x509 -req -days 365 -CA caCert.pem -CAkey caKey.pem -set_serial 1 -in new.csr -out client.crt

cert="-----BEGIN CERTIFICATE-----\n$(cat client.crt|awk "NR>1{print $1}"|sed '$d'|tr -d "\n")\n-----END CERTIFICATE-----"

curl --location --request POST "https://${cnf_ip}/cgi-bin/luci/sdewan/pkcs11/v1/cert" \
--header 'Content-Type: application/json' \
--data-raw "{
    \"token\": {
        \"label\": \"sdewan-sgx\",
        \"slot\": 0,
        \"so_pin\": \"12345678\",
        \"pin\": \"12345678\"
    },
    \"cert\": {
        \"key_pair\": {
            \"key_type\": \"rsa:2048\",
            \"label\": \"node-1\",
            \"id\": \"12345678\"
        },
        \"subject\": \"/CN=node-1\",
        \"pem\": \"${cert}\"
    }
}"




```

````sh
pkcs11-tool --module /lib/x86_64-linux-gnu/pkcs11/p11-kit-client.so -L
    2  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -O
    3  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -b --type cert --label node-1
    4  pkcs11-tool --module /usr/local/lib/libp11sgx.so  -L
    5  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -b --type cert --label node-1 --slot 0x7bfa3749
    6  pkcs11-tool --module /usr/local/lib/libp11sgx.so  -L
    7  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -O
    8  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -b --type cert --label node-1 --slot 0x7bfa3749
    9  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -O
   10  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -b --type cert --label node-1 --slot 0x7bfa3749
   11  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -O
   12  pkcs11-tool --help > help
   13  vi help
   14  apt update
   15  apt install vim
   16  ls
   17  vi help
   18  -O
   19  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -O
   20  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -b --type cert --label node-1 --slot 0x7bfa3749
   21  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -O
   22  pkcs11-tool --module /usr/local/lib/libp11sgx.so  -L
   23  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -O
   24  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -b --type cert --label node-1 --token sdewan-sgx
   25  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -O
   26  pkcs11-tool --module /usr/local/lib/libp11sgx.so  --login --pin 12345678  -b --type cert --label node-1 --token sdewan-sgx



 certificatesigningrequests.certificates.k8s.io
````





































































