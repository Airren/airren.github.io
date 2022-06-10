









## LXC







**Set proxy**

```
sudo lxc config set core.proxy_https http://username:password@<IP>:<port>/
```





LXC

https://www.linode.com/docs/guides/beginners-guide-to-lxd-reverse-proxy/

https://www.digitalocean.com/community/tutorials/how-to-install-and-configure-lxd-on-ubuntu-20-04









## SDEWAN - direct connect



Edge-1

```sh
# PREROUTTING
sudo iptables -I PREROUTING -d 10.10.70.49/32 -p tcp -m tcp --dport 6443 -j DNAT --to-destination 10.96.0.1:443 -t nat
```



Hub

```sh
# PREROUTTING

sudo iptables -I PREROUTING --destination 10.95.62.68/32  -p esp  -j DNAT --to-destination 10.233.108.10 -t nat
sudo iptables -I PREROUTING --destination 10.95.62.68/32  -p udp --dport 4500 -j DNAT --to-destination 10.233.108.10:4500 -t nat
sudo iptables -I PREROUTING --destination 10.95.62.68/32  -p udp --dport 500 -j DNAT --to-destination 10.233.108.10:500 -t nat





sudo iptables -I PREROUTING --destination 10.95.62.119/32  -p esp  -j DNAT --to-destination 10.233.65.140 -t nat
sudo iptables -I PREROUTING --destination 10.95.62.119/32  -p udp --dport 4500 -j DNAT --to-destination 10.233.65.140:4500 -t nat
sudo iptables -I PREROUTING --destination 10.95.62.119/32  -p ud --dport 500 -j DNAT --to-destination 10.233.65.140:500 -t nat

sudo iptables -I PREROUTING --destination 10.95.62.119/32  -p tcp --dport 4500 -j DNAT --to-destination 10.233.65.140:4500 -t nat
sudo iptables -I PREROUTING --destination 10.95.62.119/32  -p tcp --dport 500 -j DNAT --to-destination 10.233.65.140:500 -t nat




sudo iptables -D POSTROUTING -d 10.154.142.12/32 -j SNAT --to-source 10.154.142.7 -t nat

sudo iptables -I POSTROUTING -d 192.168.0.8/32 -j SNAT --to-source 10.20.0.118 -t nat

sudo iptables -I POSTROUTING -d 10.20.0.118/32 -j SNAT --to-source 192.169.0.4 -t nat


192.169.0.4/32 === 10.20.0.118/32
```







get a ip address through dhcp

```sh
udhcpc -i net1
```





```sh
export HTTP_PROXY=http://child-prc.intel.com:913
export HTTPS_PROXY=http://child-prc.intel.com:913
export NO_PROXY="10.95.62.1/16,192.169.0.1/24,192.168.0.1/24,10.233.0.1/16"


export GITHUB_TOKEN=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
flux bootstrap github \
  --token-auth \
  --owner=Airren \
  --repository=flux \
  --branch=main \
  --path=./clusters-/huba \
  --personal
```





````sh
patches:
  - patch: |
      apiVersion: apps/v1
      kind: Deployment
      metadata:
        name: all
      spec:
        template:
          spec:
            containers:
              - name: manager
                env:
                  - name: "HTTPS_PROXY"
                    value: "http://child-prc.intel.com:913"
                  - name: "HTTP_PROXY"
                    value: "http://child-prc.intel.com:913"
                  - name: "NO_PROXY"
                    value: "10.95.62.1/16,192.169.0.1/24,192.168.0.1/24,10.233.0.1/16,localhost,10.96.0.1/24,192.168.174.0/24,172.17.0.1/24,.cluster.local.,.cluster.local,.svc"
    target:
      kind: Deployment
      labelSelector: app.kubernetes.io/part-of=flux
````



```sh
 k patch deployments.apps -n flux-system source-controller --patch-file patch.yaml
 
 k patch deployments.apps -n sdewan-system rsync --patch-file rsync-patch.yaml
```





### Overlay use Owctl to register

```sh
# modify config

```





- new nodus with sdewan -> test failed -> test host direct mode
- **4**
- 







````sh

conn localtodevice1-Conndevice1
  left=192.168.0.1
  right=%any
  leftsubnet=192.168.0.1/32
  rightsourceip=192.168.0.5
  rightsubnet=192.168.0.5/32
  ikelifetime=3h
  lifetime=1h
  margintime=9m
  keyingtries=%forever
  dpdaction=restart
  dpddelay=30s
  leftauth=pubkey
  rightauth=pubkey
  leftcert=/etc/ipsec.d/certs/localtodevice1_public.pem
  leftsendcert=yes
  rightsendcert=yes
  auto=start
  leftid="CN=sdewan-controller-base"
  rightid="CN=device-device-1-cert"
  leftupdown=/etc/updown
  keyexchange=ikev2
  mark=30
  esp=aes128-sha256-modp3072,aes256-sha256-modp3072
  ike=aes128-sha256-modp3072,aes256-sha256-modp3072
  type=tunnel

````



```sh
conn localtodevice1-Conndevice1
  left=%any
  right=10.95.62.217
  rightsubnet=192.168.0.1/32
  # rightsubnet=10.233.108.12/32
  leftsourceip=%config
  ikelifetime=3h
  lifetime=1h
  margintime=9m
  keyingtries=%forever
  dpdaction=restart
  dpddelay=30s
  leftauth=pubkey
  rightauth=pubkey
  leftcert=/etc/ipsec.d/certs/localtodevice1_public.pem
  leftsendcert=yes
  rightsendcert=yes
  auto=start
  leftid="CN=device-device-1-cert"
  rightid="CN=sdewan-controller-base"
  leftupdown=/usr/lib/ipsec/_updown iptables
  keyexchange=ikev2
  esp=aes128-sha256-modp3072,aes256-sha256-modp3072
  ike=aes128-sha256-modp3072,aes256-sha256-modp3072
  type=tunnel

```



