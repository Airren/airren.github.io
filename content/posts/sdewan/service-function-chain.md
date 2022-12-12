---
title: Service function chain
---





```sh
node-1 10.151.128.13
node-2 10.151.128.14
```





```sh
# Hub
ip rule add iif vti_192.169.0.1 lookup 51
ip rule add iif vti_192.169.0.2 lookup 51
ip route add default via <net pod> dev net1 table 51
#ip route add default via 10.151.128.13 dev net1 table 51

# Node-1
ip route add 192.169.0.0/24  via 10.151.128.14 dev net1

# Node-2
ip route add 192.169.0.0/24  via 10.151.128.12 dev net1


sysctl -w net.ipv4.ip_forward=1
 echo 1 > /proc/sys/net/ipv4/ip_forward
```

