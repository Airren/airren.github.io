---
title: What is VLAN
---

https://foofunc.com/how-to-configure-vlan-network-in-ubuntu/



First, ensure that the 802.1Q kernel module is loaded. In practice, this module is automatically loaded if you configure a VLAN subinterface. However, I'll manually enable it for the sake of demonstration:

```sh
lsmod | grep 8021q

modprobe 8021q

ubuntu@docker-node:~$ lsmod |grep 8021q
8021q                  32768  0
garp                   16384  1 8021q
mrp                    20480  1 8021q

modinfo 8021q

```



Verify that module is loaded by using the following command:

```sh
modinfo 8021q
```









Add a VLAN interface definition, ens3.101 for ens3 on PVID 101.

```sh
ip link add link ens3 name ens3.101 type vlan id 101

# check through - ip link
```

Configure the network settings for the VLAN interface.

```sh
ip addr add 192.168.100.2/23 dev ens3.101

# check through - ip a
```

Bring up the VLAN interface

```sh
ip link set ens3.101 up
```



If you subsequently need to delete the interface, use the following command to bring it down and remove the definition

```sh
ip link set ens3.101 down
ip link delete ens3.101
```



