---
title: OpenVPN Setup
---



## Configuration

### Server side

Server.conf

```sh
# /etc/openvpn/server/server.conf
local 10.0.0.230
port 1194
proto udp
dev tun
ca ca.crt
cert server.crt
key server.key
dh dh.pem
auth SHA512
tls-crypt tc.key
topology subnet
client-config-dir /etc/openvpn/ccd        # bind ip with client name
route  192.166.0.0 255.255.255.0          # route add to server side
push "route  192.167.0.0 255.255.255.0"   # route add to client side
server 10.8.0.0 255.255.255.0
push "redirect-gateway def1 bypass-dhcp"
ifconfig-pool-persist ipp.txt
push "dhcp-option DNS 10.0.0.1"
keepalive 10 120
cipher AES-256-CBC
user nobody
group nogroup
persist-key
persist-tun
verb 4                 # log level, 1-11, bigger more details
crl-verify crl.pem
explicit-exit-notify
```



Ccd configuration

Ccd configuration locate at `/etc/openvpn/ccd`. Every cluster has a configuration file named by client name.

```sh
# /etc/openvpn/ccr/node-1
ifconfig-push 10.8.0.2 255.255.255.0
iroute 192.166.0.0 255.255.255.0
```





Service Mangement

```sh
# server 
systemctl status openvpn-server@server.service
# client
systemctl stop openvpn@client.service

```

### Client side

Client.conf

```sh
# /etc/openvpn/client.conf

```



### NAT rule on the Pop

```sh
# If the sever side want to access the client side private network, you shoule add NAT rule on the client side 
# ip is the VPN vip range
sudo iptables -t nat -A POSTROUTING -s 10.8.0.0/24 -o ens160 -j MASQUERADE
# ip is the server side ip
sudo iptables -t nat -A POSTROUTING -s 10.0.12.0/24 -o ens160 -j MASQUERADE
# vi /etc/sysctl.conf
# net.ipv4.ip_forward = 1
sudo sysctl -p


sudo nginx -s reload

```



https://www.cyberciti.biz/faq/ubuntu-20-04-lts-set-up-openvpn-server-in-5-minutes/

https://github.com/Nyr/openvpn-install/blob/master/openvpn-install.sh
