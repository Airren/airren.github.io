---
title: StrongWAN configure with CNF.
---

1. Download the material

   ```sh
   opkg update
   opkg install strongswan-default luasocket strongswan-mod-af-alg && \
       opkg install strongswan-mod-pkcs11 strongswan-swanctl
   ```

   

   ```shell
   wget -r -N -nd http://sdewan.sh.intel.com:8888/ipsec-demo/
   ```

2. modify the node selector in `cnf-1.yaml` and `cnf-2.yaml` respectively. Create 2 pod on different node with host network.

```yaml
  nodeSelector:
    # change to the specific node
    kubernetes.io/hostname: node
```

3. Copy cert to the CNF Pod.
   
   Find the container id for `cnf-1` and `cnf-2`.



```shell
# For cnf-1, copy sunCert to it
docker cp ./cert/caCert.pem       $(kubectl describe po cnf-1|grep docker:|awk -F / '{print $3}'):/etc/ipsec.d/cacerts
docker cp ./cert/sunCert.pem      $(kubectl describe po cnf-1|grep docker:|awk -F / '{print $3}'):/etc/ipsec.d/certs
docker cp ./cert/sunKey.pem       $(kubectl describe po cnf-1|grep docker:|awk -F / '{print $3}'):/etc/ipsec.d/private
# For node-2, copy moonCert to it

docker cp ./cert/caCert.pem        $(kubectl describe po cnf-2|grep docker:|awk -F / '{print $3}'):/etc/ipsec.d/cacerts
docker cp ./cert/moonCert.pem      $(kubectl describe po cnf-2|grep docker:|awk -F / '{print $3}'):/etc/ipsec.d/certs
docker cp ./cert/moonKey.pem       $(kubectl describe po cnf-2|grep docker:|awk -F / '{print $3}'):/etc/ipsec.d/private



k cp ./cert/caCert.pem sdewan:/etc/ipsec.d/cacerts
k cp ./cert/sunCert.pem  sdewan:/etc/ipsec.d/certs
k cp ./cert/sunKey.pem    sdewan:/etc/ipsec.d/private

k cp ./cert/caCert.pem sdewan:/etc/ipsec.d/cacerts
k cp ./cert/moonCert.pem   sdewan:/etc/ipsec.d/certs
k cp ./cert/moonKey.pem      sdewan:/etc/ipsec.d/private
```

4. start service in container in `cnf-1` and `cnf -2`

   ```shell
       /sbin/procd &
       /sbin/ubusd &
       sh /etc/init.d/log start
       sh /etc/init.d/ipsec start
   ```

5. Edit `/var/ipsec/ipsec.secrets` in `cnf-1` and` cnf -2`

   ```shell
   
   # For cnf-1
   echo '  : RSA /etc/ipsec.d/private/sunKey.pem' > /var/ipsec/ipsec.secrets
   # For cnf-2
   echo '  : RSA /etc/ipsec.d/private/moonKey.pem' > /var/ipsec/ipsec.secrets
   
   ```

6. edit `/var/ipsec/ipsec.conf` in `cnf-1` and `cnf -2`

   ```shell
   # For server side, cnf-1
   conn connection-11
     left=%any
     right=%any
     rightsubnet=192.168.9.134/32
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
     # rightsourceip=192.169.0.1
     auto=start
     leftid="C=CH, O=strongSwan, CN=sun.strongswan.org"
     rightid="C=CH, O=strongSwan, CN=moon.strongswan.org"
     leftupdown=/etc/updown
     keyexchange=ikev2
     mark=30
     esp=aes128-sha256-modp3072,aes256-sha256-modp3072
     ike=aes128-sha256-modp3072,aes256-sha256-modp3072
     type=tunnel
   
   
   # For client side, cnf-2
   conn connection12
     left=%any
     right=124.223.99.93
     rightsubnet=10.32.0.0/16
     # leftsourceip=%config
     ikelifetime=3h
     lifetime=1h
     margintime=9m
     keyingtries=%forever
     dpdaction=restart
     dpddelay=30s
     closeaction=restart
     leftauth=pubkey
     rightauth=pubkey
     leftcert=/etc/ipsec.d/certs/moonCert.pem
     leftsendcert=yes
     rightsendcert=yes
     auto=start
     leftid="C=CH, O=strongSwan, CN=moon.strongswan.org"
     rightid="C=CH, O=strongSwan, CN=sun.strongswan.org"
     leftupdown=/usr/lib/ipsec/_updown iptables
     keyexchange=ikev2
     esp=aes128-sha256-modp3072,aes256-sha256-modp3072
     ike=aes128-sha256-modp3072,aes256-sha256-modp3072
     type=tunnel
   ```

7. run `sude ipsec start` in  the container to start the ipsec tunnel.





````sh
# shanghai 43.153.132.137
sudo iptables -I PREROUTING --destination 10.0.12.16/32  -p esp  -j DNAT --to-destination  10.32.65.9 -t nat
sudo iptables -I PREROUTING --destination 10.0.12.16/32  -p udp --dport 4500 -j DNAT --to-destination  10.32.65.9:4500 -t nat
sudo iptables -I PREROUTING --destination 10.0.12.16/32  -p udp --dport 500 -j DNAT --to-destination  10.32.65.9:500 -t nat



# tokyo 43.156.122.205
sudo iptables -I PREROUTING --destination 10.0.0.10/32  -p esp  -j DNAT --to-destination 10.33.32.9 -t nat
sudo iptables -I PREROUTING --destination 10.0.0.10/32  -p udp --dport 4500 -j DNAT --to-destination 10.33.32.9:4500 -t nat

sudo iptables -I PREROUTING --destination 10.0.0.10/32  -p udp --dport 500 -j DNAT --to-destination 10.33.32.9:500 -t nat


10.241.1.4
````





```sh
ip r add 10.32.0.0/12 via 10.32.65.9 dev eth99

# shanghai
sudo iptables -A POSTROUTING -d 10.33.0.0/16 -j SNAT --to-source 10.32.65.9 -t nat

# tokyo
ip r add 10.32.0.0/12 via 10.33.32.9 dev eth99
sudo iptables -A POSTROUTING -d 10.32.0.0/16 -j SNAT --to-source 10.33.32.9 -t nat

```

