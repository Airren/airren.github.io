---
title: Cloud Native
---







4C8G 70%-80% 虚拟化浪费掉了

裸金属

2001 VMware虚拟化技术

2006 AWS推出EC2服务

2010 Openstack社区成立。 虚拟化技术，管理操作系统

2011.04 第一个 开源PaaS平台 CloudFoundry

2013.03 开源Docker发布。 操作系统之上的应用容器化。

2014.06 Google 发布Kubernetes,  应用编排

2015.07 Google 宣布成立CNCF基金会



Building sustainable ecosystems for cloud native software.

![https://www.bmc.com/blogs/saas-vs-paas-vs-iaas-whats-the-difference-and-how-to-choose/](https://s7280.pcdn.co/wp-content/uploads/2017/09/saas-vs-paas-vs-iaas.png)

IaaS Infrastructure-as-a-service 基础设施即服务

PaaS Platform as a service

SaaS Software as a service

CaaS container as a service 



优势 ：

稳定性： 几个9 SLA   0.999  年宕机时间

弹性扩展

安全性

成本

易用性

IDC 

单体架构

集群架构阶段（单集群，同时只有一个实例提供服务）

分布式架构阶段（负载均衡，同时提供服务）

微服务架构， 以业务天然分库

ServiceMesh： 网格化架构

RPC 远程调用/ Gateway 负载均衡-> 服务与IP映射  facade pattern ： 真正想做一件事，对外暴露统一访问接口：负载均衡、协议抓换、用户鉴权



Nginx 和API Gateway， 有交集，动态决定

Nginx：反向代理、负载均衡

Gateway： 鉴权，协议转换， 牵扯到业务代码的相关东西， 可以做更多的业务融合服务





分布式 和集群的区别？

分布式：把一个大型应用拆分出很多功能模块，各个功能部署再不同服务器上，所有的这些服务器联合起来提供完成服务。

集群： 很多一模一样的服务

软件  部署机器

A    5     -> 集群

B 4

C 8 

D  7 

很多机器都可以叫做集群；不同服务部署到不同服务器，才能称为分布式；

异地多活：



IP 漂移， keep alive



云上挑战

云机器的资源编排

云存储方案： 文件存储

云负载均衡方案

云缓存方案：CDN，数据库，中间件

云持久化： Mysql

云运维

云监控：

云容器技术：

云DevOps

云安全防护



技术变革：

动态扩容-> 水平扩容

应用上云无关语言



docker  -- docker shim-> k8s

因为 docker 没有实现CRI（Container Runtime Interface）， 之前是通过docker shim 调用，实现CRI interface，还是干掉 docker。

2021年底的最后一次更新会替换掉

docker-> 容器的封装层，  containd(runc)?

容器的所有思想都是通用的。容器，镜像。。。

应用上云的新型架构： Kubernetes + serviceMesh



## 云原生的生态系统

CloudNative 

- Microservice
- Containers
- Continuous Delivery
- DevOps

常见技术:

基础研究量





ServiceMesh

serverless： 

## 云原生的术语



graceful shutdown



蓝绿部署，绿色环境为实验环境





cka, ckad, cks 考证

pipeline







[docker containerd runc 之间的关系](https://xuanwo.io/2019/08/06/oci-intro/)

https://zhuanlan.zhihu.com/p/87602649

https://www.huweihuang.com/kubernetes-notes/runtime/runtime.html

