---
title: K8s Cpu 调度管理的现状与限制
date: 2022-12-12T13:41:16+08:00

---



History and Background

Various extension mechanisms over the years

OCI hooks

- custom actions executed at various container lifecycle events
- runc , cri-o ?

Runtime wrapper using runtime classes

- Custom actions implemented by custom runtime wrapper
- a bit of kludge
- runc/crun/etc.. containers/cri-o

NRI

- custom actions at pod or container creation/stop
- containerd

NRI evolution

- Subject of this presentation?

  

What is NRI

NRI: Node resource interface, a common framework for

- Plugging extensionsinto OCI-compatible runtimes

- Implementing custom container configuration logic

common

- plugins work identically i supported runtimes
- Support present in commonly used runtimes

framework

- multiple NRI pieces
- collectively achieve NRI's goal

pluggable extensions

- Alter behavior without runtime modifications

Custom logic

- Your cluster, you plugin, your rules
- within the allowed boundaries imposed by NRI

plumbing to enforce how containers are configured

- Initially, during container creation
- Subsequently, by container updates
- Potentially, in response to some external events 

=> you cluster, you plugin, your rules

- within the allowed boundaries imposed by NRI



How Does it work?



What can it do?



Write An NRI Plugin













### CPU管理的现状

k8s的cpu manger 完成节点测的cpu资源分配和隔离(core pinning and isolation). 处理流程

- 发现机器cpu的拓扑
- 上报k8s机器的可用资源
- 分配资源供workload运行
- 追踪pod的资源分配情况。



kubelet 将系统的CPU分为两个资源池

- exclusive pool： 同时只有一个任务可以分配到cpu
- share pool： 多个进程分配到cpu

原生的K8s cpumanger 目前只提供静态的CPU分配策略。 当K8s创建一个pod后，pod会被分类为一个Qos

- Guaranteed
- Burstable
- BestEffort

并且kubelet允许管理员通过`-reserved-cpus`指定保留的CPU提供给系统进程或者kube 守护进程。保留的这部分资源主要给系统进程使用。可以作为共享池分给非guranteed 的pod容器。 但是guaranteed 类pod无法分配这些cpus。

目前K8s的节点侧依据cpuManger的分配策略来分配 numa node的cpuset， 能够做到

- 容器分配到一个numa node上
- 容器分配到一组共享的numa node上。

cpuManger当前限制：

- 最大numa node数量不能大于8，防止state explosion
- 策略只支持静态分配cpuset，未来会支持在容器生命周期内动态调整cpuset
- 调度器不感知节点上的拓扑信息。
- 对于线程布局（thread placement）的应用，防止物理核的共享和邻居干扰。 CPUmanger 当前不支持。







### Node Resource Interface

containerd 主要工作在平台(docker or k8s)和更底层的 runtime (runc , kata)之间。containerd提供容器进程的管理， 镜像的管理，文件系统快照以及元数据和依赖管理。



NRI plugin: 节点资源接口插件，管理cgroups 和拓扑

NRI可以用来解决批量计算，延迟敏感性服务的性能问题， 以满足服务SLA/SLO,优先级等用户需求。 例如性能需求， 通过将容器的CPU分配统一NUMA node，来保证numa内的内存调用。除了NUMA，还有CPU，L3 cache 等资源拓扑亲和性。

当前kubelet的实现的是通过cpumanager 的处理对象只能是guaranteed 类的pod， topologyManger 通过cpuManager提供的hints实现资源分配。

kubelet 当前也不适合处理多种需求的扩展，因为在kubelet增加细粒度的资源分配会导致kubelet 和CRI的界限越来越模糊。

而NRI，则是在CRI同期生命周期间做调用，适合做 resource pinning 和节点的拓扑感知。并且在CRI内部做插件定义和迭代，可以做到上层 kubenetes 以最小的代价来适配变化。 



在容器的生命周期中，CNI/NRI插件能够注入容器初始化进程的Create和start之间：

Create-> NRI-> Start

以官方例子 clearfs： 在启动容器前， 依据Qos类型调用cgroup命令，cpu.cfs_quota_us为-1 表示不设上限。

NRI直接控制cgroup，所以有更底层的资源分配方式。 不过越接近底层，处理逻辑越复杂。





Why NRI:

支持定制化扩展，kubelet 可以直接载入扩展配置，无需修改自身代码

通过与CRI交互，kubelet 可以将部分复杂的CPU分配需求下方到 runtime 来处理。



应用程序如何独占一个CPU？

NUMA 简介？





docker -> containerd -> runc?



https://www.modb.pro/db/462537
