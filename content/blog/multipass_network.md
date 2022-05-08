









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
sudo iptables -I PREROUTING --destination 10.95.62.150/32  -p udp --dport 4500 -j DNAT --to-destination 10.233.121.204:4500 -t nat
sudo iptables -I PREROUTING --destination 10.95.62.150/32  -p udp --dport 500 -j DNAT --to-destination 10.233.121.204:500 -t nat

sudo iptables -I PREROUTING --destination 10.95.62.150/32  -p esp  -j DNAT --to-destination 10.233.121.204 -t nat


sudo iptables -I PREROUTING --destination 10.95.62.204/32  -p udp --dport 4500 -j DNAT --to-destination 10.233.123.204:4500 -t nat
sudo iptables -I PREROUTING --destination 10.95.62.204/32  -p udp --dport 500 -j DNAT --to-destination 10.233.123.204:500 -t nat



sudo iptables -D POSTROUTING -d 10.154.142.12/32 -j SNAT --to-source 10.154.142.7 -t nat
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







