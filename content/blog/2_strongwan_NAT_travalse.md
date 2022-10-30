---
title: StrongWAN configure with CNF.
---

1. Download the material
   
   ```shell
   wget -r -N -nd http://sdewan.sh.intel.com:8888/ipsec-demo/
   ```

2.  modify the node selector in `cnf-1.yaml` and `cnf-2.yaml` respectively. Create 2 pod on different node with host network.

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
```

4.  start service in container in `cnf-1` and `cnf -2`
   
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

5. edit `/var/ipsec/ipsec.conf` in `cnf-1` and `cnf -2`
   
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
     right=61.240.163.206
     rightsubnet=61.240.163.206/32
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

6.  run `sude ipsec start` in  the container to start the ipsec tunnel.
