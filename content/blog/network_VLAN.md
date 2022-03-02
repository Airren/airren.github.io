---
title: What is VLAN and Virtual Network Interface
---



In our day-to-day life, we have seen LAN and WAN architectures mostly because we have to deal with only one IP address on one interface. We either connect our system with LAN cable or with WiFi.

In this article, we will discuss the VLAN and how to create the VLAN on the Ubuntu server, but let first understand what is VLAN and why we use VLAN.

## What is VLAN

Virtual Local Area Network(VLAN) is a logical concept of breaking large broadcast domains into small domains. The VLAN protocol is based on IEEE802.1Q. VLAN can be considered as a subnet. Two different subnets cannot communicate with each other without a bridge or router.

The above image is an example showing how the network can be divided in the office. There is a clear separation of the department network which is done by separating through VLANs.

There are many advantages of using VLAN in our network architecture mentioned the following:

- Logically divide the broadcast domain which reduce the size of the domains.
- Add additional layer of security.
- Make device management easier.
- QoS or other network policies are easy to implement.
- Also make network scalable.





## VLAN installation

With the background knowledge out of the way, It's time to get your hand dirty with configuration.

First, ensure that the 802.1Q kernel module is loaded. In practice, this module is automatically loaded if you configure a VLAN subinterface. However, I'll manually enable it for the sake of demonstration:

```sh
# check the 8021q stauts
ubuntu@node-1:~$ lsmod | grep 8021q

# enable 8021q
ubuntu@node-1:~$ sudo modprobe 8021q

ubuntu@node-1:~$ lsmod |grep 8021q
8021q                  32768  0
garp                   16384  1 8021q
mrp                    20480  1 8021q
```

Verify that module is loaded by using the following command:

```sh
modinfo 8021q
```

We will first add a VLAN interface definition, ens3.100 for ens3 on PVID 100.

```sh
sudo ip link add link ens3 name ens3.100 type vlan id 100
```

Use `ip link` command to check does above command has add the VLAN interface.

And then, configure the network settings for the VLAN interface, add a IP address to it.

```sh
ip addr add 192.168.100.1/24 dev ens3.100
```

Use `ip addr` command to verify the VLAN interface configuration.

Bring up the VLAN interface

```sh
ip link set ens3.100 up
```

If you subsequently need to delete the interface, use the following command to bring it down and remove the definition

```sh
ip link set ens3.100 down
ip link delete ens3.100
```



This configuration is not permanent, for permanent configuration use Netplan.

### Configure VLAN with Netplan

Netplan related configuration files can be found in the `/etc/netplan` directory. `/etc/netplan` directory has multiple YAML files.  In our Ubuntu20.04 server the YAML file which is responsible for network configuration is names as `50-cloud-init.yaml`. The network configuration file's name maybe different in some setups.

To assign a static IP address on the network interface. Configuration file look like shown in below mentioned.

```yaml
network:
    ethernets:
        ens3:
            dhcp4: true
            match:
                macaddress: 52:54:00:68:b2:b6
            set-name: ens3
    vlans:
    		ens3.100:
    				id: 100
    				link: ens3
    				addresses: [192.169.100.1/24]
```

Once done, save the file and apply the changes by running the following command:

```sh
sudo netplan apply
```



> `ifconfig`: net-tools; configuration file's path `/etc/network/interfaces`
>
> `ip`: iproute2



## Catch the Traffic

Do the same thing on another node -- node-2, but specify the IP to 192.169.100.2.

Now, we have two  machines:

- Node-1  ens3.100 192.169.100.1
- Node-2  ens3.100 192.169.100.2 



While we ping Node-2 on Node-1 through the VLAN ip,   use `tcpdump` to catch the traffic.









## Create a Virtual Network Interface

A virtual interface is a network interface, that mimic a physical interface. With the help of the virtual interface creating virtual machines or containers are possible.

Add a virtual interface is a very simple and straight task. This can be done with `ip` command and with some arguments. In the below-mentioned command, I have added an interface with the name vr-br.

Use the following command to add a nonpersistent interface. In the following command dummy is the kernel module.

```sh
sudo ip link add name <virtual_interface_name> type dummy
```

Example of the above command and its verification is shown in the following code section.



You can then play with this Interface and you can also assign IP address to this interface. This type of assignment is not persistent, which means after a reboot of you machine you won't find a network interface.



## Summary	

In this article, we learned about the VLAN and how to configure the VLAN in Ubuntu20.04. We discussed two different strategies to configure the VLAN in Ubuntu.



If you face any issue don't hesitate to comment.



## Reference

https://foofunc.com/how-to-configure-vlan-network-in-ubuntu/

https://foofunc.com/how-to-create-a-virtual-network-interface-in-ubuntu-20-04/

https://www.redhat.com/sysadmin/vlans-configuration

https://www.redhat.com/sysadmin/vlans-sysadmins-basics
