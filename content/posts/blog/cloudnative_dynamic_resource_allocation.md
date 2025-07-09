---
title: Dynamic Resource allocation
date: 2023-01-02T13:41:16+08:00

---





# Dynamic resource allocation

This KEP introduces a new API for describing which of these new resources a pod needs. The API supports:

- Network-attached resources. The existing device plugin API is limited to hardware on a node.
- Sharing of a resource allocation between multiple containers or pods. The device manager API currently cannot share resources at all. It could be extended to share resources between containers in a single pod, but supporting sharing between pods would need a completely new API similar to the one in this KEP.
- Using a resource that is expensive to initialize multiple times in different pods. This is not possible at the moment.
- Custom parameters that describe resource requirements and initialization. Parameters are not limited to a single, linear quantity that can be counted. With the current Pod API, annotations have to be used to capture such parameters, and then hacks are needed to access them from a CSI driver or device plugin.

Support for new hardware will be provided by hardware vendor add-ons. It will not be necessary anymore to modify Kubernetes itself.

This KEP does not replace other means of requesting traditional resources(RAM/CPU, volumes, extended resources). The scheduler will serve as a coordinator between the add-ons that own resources(CSI driver, resources driver) and the resources owned and assigned by the scheduler(RAM/CPU, extended resources).



动态资源分配，为高级硬件提供扩展的资源管理。它带有一个用户有好的API来描述资源请求。随着处理不同硬件(如GPU或者FPGA)的需求不断增加，以及初始化设置和清理的需求，这一新功能将加速Kubernetes在科学研究或边缘计算等领域的采用。





## CPU管理的现状

k8s的cpu manger 完成节点测的cpu资源分配和隔离(core pinning and isolation). 处理流程

- 发现机器cpu的拓扑
- 上报k8s机器的可用资源
- 分配资源供workload运行
- 追踪pod的资源分配情况。



kubelet 将系统的CPU分为两个资源池

- exclusive pool： 同时只有一个任务可以分配到cpu
- share pool： 多个进程分配到cpu

原生的K8s cpumanger 目前只提供静态的CPU分配策略。 当K8s创建一个pod后，pod会被分类为一个Qos

- Guaranteed]

  这些 Pod 不可以获得超出其指定 limit 的资源。这些 Pod 也可以使用 [`static`](https://kubernetes.io/zh-cn/docs/tasks/administer-cluster/cpu-management-policies/#static-policy) CPU 管理策略来使用独占的 CPU。

- Burstable

  在由于 Node 资源压力导致 Pod 被驱逐的情况下，只有在所有 `BestEffort` Pod 被驱逐后 这些 Pod 才会被驱逐。

- BestEffort

  如果节点遇到资源压力，kubelet 将优先驱逐 `BestEffort` Pod。

  

并且kubelet允许管理员通过`-reserved-cpus`指定保留的CPU提供给系统进程或者kube 守护进程。保留的这部分资源主要给系统进程使用。可以作为共享池分给非guaranteed 的pod容器。 但是guaranteed 类pod无法分配这些cpus。

目前K8s的节点侧依据cpuManger的分配策略来分配 numa node的cpuset， 能够做到

- 容器分配到一个numa node上
- 容器分配到一组共享的numa node上。

cpuManger当前限制：

- 最大numa node数量不能大于8，防止state explosion
- 策略只支持静态分配cpuset，未来会支持在容器生命周期内动态调整cpuset
- 调度器不感知节点上的拓扑信息。
- 对于线程布局（thread placement）的应用，防止物理核的共享和邻居干扰。 CPUmanger 当前不支持。









## API

The new `resource.k8s.io/v1alpha1`API group provides four new types:

**ResourceClass**

Defines which resource driver handles a certain kind of resource and provides common parameters for it. `ResourceClasses` are created by a cluster administrator when installing a resource driver.

**ResourceClaim**

Defines a particular resource instance that is required by a workload. Created by a user (lifecycle managed manually, can be shared between different Pods) or for individual Pods by the control plane based on a `ResourceClaimTemplate`(automatic lifecycle, typically used by just one Pod).

**ResourceClaimTemplate**

Defines the spec and some metadata for creating `ResourceClaims`. Created by the user when deploying a workload.

**PodScheduling**

Used internally by the control plane and resource drivers to coordinate pod scheduling when `ResourceClaims` need to be allocated for a Pod.

Parameters for ResourceClass and ResourceClaim are stored in separate objects, typically using the type defined by a CRD that was created when installing a resource driver.

With this alpha feature enabled, the `spec` of Pod defines ResourcesClaims that are needed for Pod to run: this information goes into a new `resourceClaims` field. Entries in that list reference either a ResourceClaim or a ResourceClaimTemplate. When referencing a ResourceClaim, all Pods using this `.spec` share the same ResourceClaim instance. When referencing a ResourceClaimTemplate, each Pod gets its own ResourceClaim instance.

For a container defined with a Pod, the `resource.claims` list defines whether that container gets access to these resource instances, which makes it possible to share resources between one or more containers inside the same Pod. For example, an init container cloud set up the resource before the application use it.



Here is an example of a fictional resource driver. Two ResourceClaim objects will get created for this Pod and each container gets sccess to one of them.



Assuming a resource driver called `resource-driver.example.com` was installed together with the following resource class:

```yaml
apiVersion: resource.k8s.io/v1alpha1
kind: ResourceClass
name: resource.example.com
driverName: resource-driver.example.com
```



An end-user cloud then allocates two specific resources of type `resource.example.com` as follows:

```yaml
---
apiVersion: cat.resource.example.com/v1
kind: ClaimParemeters
name: large-black-cats
spec:
	color: black
	size: large
---
apiVersion: resource.k8s.io/v1alpha1
kind: ResourceClaimTemplate
metadata:
  name: large-black-cats
spec:
  spec:
    resourceClassName: resource.example.com
    parametersRef:
    	apiGroup: cats.resource.example.com
    	kind: ClaimParameters
    	name: large-black-cats
    	
---
apiVersion: v1
kind: Pod
metadata:
  name: pod-with-cats
spec:
  containers:
  - name: first-example
    image: ubuntu:22.04
    command: ["sleep","infinity"]
    resources:
      claims:
      - name: cat-0
  - name: second-example
    image: ubuntu:22.04
    command: ["sleep","infinity"]
    resources:
      claims:
      - name: cat-1
  resourceClaims:
  - name: cat-0
    source:
      resourceClaimTemplateName: large-black-cats
  - name: cat-1
    source:
      resourceClaimTemplateName: large-black-cats
```



## Scheduling	

In contrast to native resources (such as CPU or RAM) and extended resources(managed by a device plugin, advertised by kubelet), the scheduler has no knowledge of what dynamic resources are available in a cluster or how they cloud be split up to satisfy the requirements of a specific ResourceClaim. Resource drivers are responsible for that. Drivers mark ResourceClaims as allocated once resources for it are reserved. This also then tells the scheduler where in the cluster a claimed resource is actually available.

ResourceClaims can get resources allocated as soon as the ResourceClaim is created(immediate allocation), without considering which Pods will use the resource.  The default (wait for the first consumer) is to delay allocation until a Pod that relies on the ResourceClaim becomes eligible for scheduling. This design with two allocation options is similar to how Kubernetes handles storage provisioning with PersistentVolumes and PersistentVolumeClaims.

In the wait for the first consumer mode, the scheduler checks all  ResourceClaims needed by a Pod. If the Pods have any ResoruceClaims, the scheduler creates a PodScheduling()





## Writing a resource driver

A dynamic resource allocation driver typically consists of two separate-but-coordinating components: a centralized controller, and a DaemonSet of node-local kubelet plugins. Most of the work required by the centralized controller to coordinate with the scheduler can be boilerplate code. Only the business logic required to actually allocate ResourceClaims against the ResourceClasses owned by the plugin need to be customized. As such, Kubernetes provides the following package, including APIs for invoking this boilerplate code as we as a Driver interface that you can implement to provide their custom business logic.

- [k8s.io/dynamic-resource-allocation/controller](https://github.com/kubernetes/dynamic-resource-allocation/tree/release-1.26/controller)

Likewise, boilerplate code can be used to register the node-local plugin with the kubelet, as well as start a gRPC server to implement the kubelet plugin API. For drivers written in Go, the following package is recommended:

- [k8s.io/dynamic-resource-allocation/kubeletplugin](https://github.com/kubernetes/dynamic-resource-allocation/tree/release-1.26/kubeletplugin)

It is up to the driver developer to decide how these two components communicate. The KEP outlines an approach using CRDs.



## Running the test driver		











Dynamic resource allocation References

issue: https://github.com/kubernetes/enhancements/issues/3063

https://github.com/kubernetes/enhancements/blob/master/keps/sig-node/3063-dynamic-resource-allocation/README.md

https://kubernetes.io/blog/2022/12/15/dynamic-resource-allocation/

https://kubernetes.io/docs/concepts/scheduling-eviction/dynamic-resource-allocation/



