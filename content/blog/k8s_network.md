---
title: K8s network
---



## 容器网络

每一个容器都可以有一个自己独立的网络栈，这个独立的网络栈是基于Linux的Network Namespace实现的。

这个独立的网络栈包含了： Network Interface、Loopback Device、Routing Table和IPtables规则。对于一个进程来说，这些要素就构成了它发起和响应网络请求的基本环境。

容器可以使用自己独立的网络栈(创建属于自己的Network Namespace)，也可以直接使用Host的网络栈(不创建Network Namespace)。

```sh
# Uset -net=host to share the host network
docker run -d -net=host --name nginx-host nginx
```



直接使用Host的网络栈可以提供良好的网络性能，但是不可避免的会引入网络资源共享的问题，比如端口冲突。大多数应用场景下，我们希望容器能够有自己独立的IP地址和端口，即有自己独立的Namespace。

这个时候，就会出现一个问题，在这个被隔离的容器进程中如何与其他的Network Namespace里的容器进程进行交互呢。

一般我们如果希望两台主机之间的通信，直接用网线把这两台主机连接起来即可；而如果是多台主机之间通信我们可以将其连接在同一台交换机上。



在Linux系统中，能够起到虚拟交换机作用的虚拟网络设备是Bridge，是二层网络设备。主要功能是根据MAC地址来将数据包转发到网桥的不同Port上。

为了实现上述目的，docker项目会在Host上创建一个docker0的网桥，凡是连接在docker0网桥上的容器，就相当于在同一个二层网络。

```sh
```



接下来就是如何把容器连接到docker0网桥上，这就需要veth pair的虚拟设备了。veth pair创建出来以后总是以两张虚拟网卡veth peer的形式成对出现的。并且从其中一个peer发出的数据包可以直接出现在与之对应的另一个peer上，即使veth pair的两端不在同一个Network Namespace 中。因此，veth pair常常用作连接不同Network Namespace的网线。