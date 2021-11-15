

Service Mesh 浅析

Istio的自我救赎



https://github.com/cloudnativebooks/cloud-native-istio/tree/master/chapter-files/security

云原生架构的三驾马车

Kubenetes， Service Mesh , Serverless

Servcie Mesh  超时重试

16年概念提出。



技术选型？

ServiceMesh 的相关概念

Service Mesh的起源

<Micro Sercvice>

微服务架构的特性

1. 围绕业务构建团队

康威定律-> 团队结构决定了产品结构

2. 去中心化的数据管理

   团队层面： 内聚，独立业务开发，没有依赖

   产品层面： 服务彼此独立，独立部署，没有依赖

   访问量决定部署实例的数量

   

   银弹理论-> 人月神话 没有任何一种技术可以完美的解决软件开发中的问题。

   空间换时间 or 时间换空间

   

   微服务架构带来的缺点

   服务间网络通信问题

   分布式计算的8个谬论

   - 网络是可靠的 网络延时是0

   

   很难会把网络相关的需求考虑到我们的设计中。分布式系统中，网络问题是一个重要问题。

   如何管理和控制网络通信

   辅助注册、发现

   路由，流量转移

   弹性能力 熔断超时重试

   安全

   可观测性

   Patten： Service Mesh

   阶段一： 控制逻辑与业务逻辑耦合

   阶段二： 公共库：流控，重试 （人力，时间学习，语言蚌绑定，平台相关，代码侵入）

   阶段三：代理模式， 功能简陋Nginx

   阶段四： Sidecar 模式 2013-2105

   阶段五： Service Mesh 2016-2017

   ​    Service MeshV2 2018

   《What's service mesh? And why do I need one?》

   Data plane 



Service Mesh产生的原因

Service Mesh的核心功能

Istio

流量控制 路由，熔断，超时重试

安全

可观测性



Service Mesh 的主要功能

流量控制： 路由， 流量转移，超时重试，熔断， 故障注入， 流量镜像

策略： 流量限制， 黑白名单

网络安全：授权以及身份认证

可观测性： 指标手机和展示，日志收集， 分布式追踪



Kubernetes：

解决容器编排与调度问题， 本质上是管理应用生命周期， 调度器

Ingress ？k

Servcie Mesh:

解决服务网络通信问题，本质上是管理服务通信(代理)



标准

UDPA（Universal Data Plane API） 统一的数据平面API

 envoy



Cloud Native Computing  Foundation

应用层-> SMI-> 控制平面 -> UDPA-> 数据平面

SMI(Service Mesh Interface)



![image-20211104224250647](1.%20Istio/image-20211104224250647.png)





envoy xDS 协议

AWS： app mesh





## Istio 入门

Istio 是一个完全开源的服务网格， 作为透明的一怎

服务网格产品

Istio 希腊语，杨帆起航

kubernetes 希腊语，舵手

数据平面，控制平面

lyft ？

轻松构建服务网格，



灰度发布

蓝绿部署

AB测试

流量 安全 策略 可观测性

Mixer 废弃，转移到envoy中



Google ： Istio, gRPC，Kubernetes



![image-20211105010441022](1.%20Istio/image-20211105010441022.png)



![image-20211105010705390](1.%20Istio/image-20211105010705390.png)

性能问题加重

易用性

解耦 要让步于 取舍

MVP理论 《 精益创业》

![image-20211105011136576](1.%20Istio/image-20211105011136576.png) 

![image-20211105011416155](1.%20Istio/image-20211105011416155.png)

![image-20211105011517090](1.%20Istio/image-20211105011517090.png)