









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



sudo iptables -D POSTROUTING -d 10.154.142.12/32 -j SNAT --to-source 10.154.142.7 -t nat
```







get a ip address through dhcp

```sh
udhcpc -i net1
```

