---
titile: Kubernetest Pod
---



从底层技术角度看，Pod内不同容器之间共享存储和一些namespace。

PID namespace: Pod中不同应用程序看拿到的其他进程的ID。Sidecar 模式下只能看到一个进程？

Network Namespace: Pod 中的多个容器具有相同的网络配置，共享一个端口范围。

IPC Namespace： Pod中的多个容器能够使用SystemV IPC 或 POSIX消息队列进行通信。

UTS Namespace:  Pod中的多个容器共享一个主机名。



在Kubernetes的网络模型中，每台服务器上的容器有自己独立的IP段。

为了实现这一目标，重点解决一下两点：

- 各台服务器上的容器IP段不能重叠，所以需要某种IP段分配机制，为各台机器分配独立的IP段。
- 从某个Pod发出的流量到达所在的机器的Host上时，机器网络层应当根据目标IP地址，将流量转发到目标机器的能力。

综上，两个能力： IP地址分配和Route.



容器之间直接通信，不需要额外的NAT。  





### Pod to Pod

所有的Pod之间要保证3层网络的联通性

### Pod to Service

Servcie 总共有4种类型，其中组常用的就是Cluster IP. 这种类型的Service会分配一个仅集群内可以访问的虚拟IP。

Kubernetes通过kube-proxy组件实现Service Cluster IP的功能。kube-proxy 是一个`daemonset`，通过复杂的iptables/IPVS 规则在Pod和Service之间进行各种过滤和NAT.

### Pod到集群外

从Pod内部到集群外的流量，Kubernetes会通过SNAT来处理。



Kubernets 默认的组网方案是bridge，CNI主要是用来解决容器的跨机通信。典型的跨机通信方案有bridge和overlay。







创建Pod时候，首先会创建一个pause容器。占用一个 linux的network namespace。Pod内的其他容器共享这个network namespace。此时，只有一个lo设备。 CNI负责初始化pause container 中的网络设备。



### kubernetes主机内组网&跨节点组网

kubernetes 经典的主机内组网模型是veth pair+bridge。

跨机通信一般是bridge + overlay。 vxlan



downward API 通过HostAlias修改pod中的/etc/host(Pod在host network下不支持)



Pod的隔离中 network namspce 是最先创建的，如果ns使用了host模式，则uts也会使用host模式。



Pause扮演PID 1的角色，并在子进程成为“孤儿进程”时，通过wait() 收割这些僵尸子进程。



Unix的init进程的作用是当某个子进程由于父进程的错误退出而变成了“孤儿进程”，便会被init进程“收养”并在退出该进程时回收资源。