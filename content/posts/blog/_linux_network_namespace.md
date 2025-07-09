---
Title: Linux Network name spece
---



Start with an example



Connect network namespace with veth



what is veth

Basic user of network namespaces

Netwrok namespace restrict a process from "seeing" the network interface, IP address, routes, and firewall entries from the reset of the system. iproute2 utility can used to manage the network namespace.

### Basic Usage

Create network namespace

```sh
ip netns add foo
ip netns add bar
```

Attach an interface to the network namespace

```sh
# add interface eno1 to the foo network namespace
ip link set dev eno1 netns foo
```



Config the interface which attached to the network namespace

```sh
ip netns exec foo ip l set dev eno1 up
ip netns exec foo ip a add 192.168.0.3/24 dev eno1
ip netns exec foo ip r add default via 192.168.0.1
```

Run a process inside the network namespace

```sh
ip netns exec foo nc -lkp 8080 << OK
```

### Connecting Betweeen system and namespace

In this example , we will use the newly created veth pair to connect the system to the netwrok namespace foo. Veth is a type of virtual interface that is always created as a pair. Veth can be thought of as a "virtual crossover cable", it creates two virtul NICs that are connected.



Defining a veth pair

```sh
ip link add veth_left type veth peer veth_right
```

Create a bridge interface on the system

```sh
ip linke add bridge0 type bridge
ip link set bridge0 up
ip addr add 10.11.0.1/24 dev bridge
```

Attach the left veth interface to the bridge

```sh
ip link set veth_left master bridge0 up
```

Attach the right veth interface to the network namespace

```sh
ip link set veth_right netns foo
```

Bring up the right interface inside the namespace

```sh
ip netns exec foo ip link set veth_right name eth0
ip netns exec foo ip link set dev eth0 up
ip netns exec foo ip addr add 10.11.0.2/24 dev eth0
```



### Connecting between namespaces

conneting one netwok namespce to another follows the same process as connecting a network namespace with the host. Create a veth pair and attache each side to the appropriate namespace.

Create a veth pair

```sh
ip link add veth1_left type veth peer veth2_right
```

Attache the left veth to the foo namespace

```sh
ip link set veth1_left netns foo
```

configure ip for the foo namespace

```sh
ip netns exec foo ip l set veth1_left name eth1
ip netns exec foo ip l set eth1 up
ip netns exec foo ip addr add 10.12.0.1/24 dev eth1
ip netns exec foo ip r add 0.0.0.0/0 via 10.12.0.1
```



Attache the right veth to the foo namespace

```sh
ip link set veth1_right netns bar
```

configure ip for the foo namespace

```sh
ip netns exec foo ip l set veth1_left name eth1
ip netns exec foo ip l set eth1 up
ip netns exec foo ip addr add 10.12.0.1/24 dev eth1
ip netns exec foo ip r add 0.0.0.0/0 via 10.12.0.1
```



attach the right veth to the bar namespace

```sh
ip link set veth1_right nets bar
```

configure IP for the bar network namespace

```sh
ip netns exec bar ip l set veth1_right name eth0
ip netns exec bar ip l set eth0 up
ip netns exec bar ip addr add 10.12.0.2/24 dev eth0
ip netns exec bar ip r add 0.0.0.0/0 via 10.12.0.1
```



Routing acceoss namespaces

Recall that we created a namespace(foo) that has both a veth link from the default namespace(host) and a veth link to a second namespace(bar). As a proof concept, let's configure connectivity between the system and the remote namespace.

create a static route on the system	

```sh
ip route add 10.12.0.0/23 via 192.168.0.3
```



Verify connectivity

```sh
traceroute -n 10.12.0.2
```



Now the system shoule be able to reach 10.12.0.2(network namespace bar) routing via network namespace "
foo". Connectivity can be verified with a ping or traceroute.





















network namespace 

https://cloud.tencent.com/developer/article/1531989

https://www.51cto.com/article/632950.html

https://www.zhaohuabing.com/post/2020-03-12-linux-network-virtualization/

https://segmentfault.com/a/1190000018391069

https://developer.aliyun.com/article/64855

https://blog.csdn.net/tycoon1988/article/details/39055149

https://blog.csdn.net/zhonglinzhang/article/details/64441263

https://www.testerfans.com/archives/linux-namespaces-overview

https://zhuanlan.zhihu.com/p/73248894

https://chengqian90.com/Linux%E5%86%85%E6%A0%B8/Linux-Namespace%E7%AE%80%E4%BB%8B.html