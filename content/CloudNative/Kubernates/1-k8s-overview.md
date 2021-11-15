---
title: K8s Overview
---



Node  节点主要负责容器的管理，用于运行容器和保证容器的状态。默认情况下Master节点不承担Node节点的功能，但是可以通过特殊的配置让Master节点也可作为Node节点。

Etcd用于存储Kubernetes的元数据，但是不要求一定要以容器的形式运行。



```sh
kubeadm init --pod-network-cidr=10.244.0.0/16 --apiserver-advertise-address=192.168.0.22
```

`--pod-network-cidr` pod 的ippool, `--apiserver-advertise-address` 为暴露的k8sAPI调用IP.



节点(Node)

一个Node是一个运行K8s的主机，作为K8s worker, 通常称之为Minion。每个节点都运行如下K8s关键组件：

- kubelet：主节点代理
- kube-proxy: service使用其将链接路由到Pod
- docker/rocket: k8s 使用容器技术创建容器

容器组(Pod)

一个Pod对应若干容器组成的一个容器组，同一个Pod内的容器共享一个存储卷(volume)，同一个Pod中的容器共享一个网络Namespace,可以使用localhost互相通信。

Pod是短暂的，不是持续性实体。一个Pod是一个特定的应用打包集合，包含一个或者多个容器。和运行的容器类似，一个Pod只有很短的运行周期。Pod被调度到一个Node运行，直到容器的生命周期结束或者其被删除。

容器组生命周期(Pod Status)

包含所有容器状态集合，包括容器组状态类型，容器组生命周期，事件，重启策略，以及replication controllers.

标签(labels)

标签是用来连接一组对象的，比如容器组Pod。lable可以用来组织和选择子对象。 一个Label是attach到Pod的一对键值对，用来传递用户定义的属性。

Replication Controllers

主要负责指定数量的Pod在同一时间一起运行。Replication controller 确保任意时间都有指定数量的Pod副本在运行。如果为某个Pod创建了Replication Controller并且指定为3副本，它会创建3个Pod,并持续监控他们。如果某个Pod不响应，那么Replication controller 会替换它，保持Pod总数为3.

当创建Replication Controller时，需要指定两个东西。

- Pod模板： 用来创建Pod副本的模板
- Label：Replication Controller 需要监控的Pod的Label

现在已经创建了Pod的一些副本，那么在这些副本上如何负载均衡呢，我们需要的是service

Service：

如果Pod是短暂的，那么重启时IP地址可能会变，怎么才能从前端容器正确可靠的指向后台容器呢。

Service是定义一系列Pod以及访问这些Pod的策略的一层抽象。Service通过Label找到Pod组。因为service是抽象的，所在在图表里通常看不到他们的存在。

现在假定有两个后台Pod,并且定义后台service名称为“backend-service”，label选择器为(tier=backend,app=myapp)。backend-service的Service会完成如下两件重要的事情：

- 会为Service创建一个本地集群的DNS入口，因此前端只需要DNS查找主机名为“backend-service”就能够解析出前端应用程序可用的IP地址
- 现在前端已经得到了后台服务的IP地址，但是它应该访问2个后台Pod中的哪一个呢。Service在这两个后台Pod之间提供透明的负载均衡，会将请求发给其中的任意一个。通过每个Node上运行的代理 kube-proxy完成。

Kubernetes Master

集群拥有一个K8s Master,K8s Master 提供集群的独特视角，并拥有一系列组件，如Kubernetes API server. API server 提供可以用来和集群交互的REST 端点。Master 节点包含用来创建和复制Pod的Replication Controller.















参考资料：

https://blog.csdn.net/hahachenchen789/article/details/80506699

https://liqiang.io/post/kubernetes-tutorial-part-3-pods-80a851a5
