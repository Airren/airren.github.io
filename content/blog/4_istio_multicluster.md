---
title: Istio MultiCluster
---



Multicluster Replicated Control Plane is an uses case to enable communication between two service in difference service mesh without using Ingress and can enable mutual TLS between the service.



> **Istio 1.8 Upgrade Notes**
>
> ### Multicluster `.global` Stub Domain Deprecation
>
> As part of this release, Istio has switched to a new configuration for multi-primary (formerly “replicated control planes”). The new configuration is simpler, has fewer limitations, and has been thoroughly tested in a variety of environments. As a result, the `.global` stub domain is now deprecated and no longer guaranteed to work going forward.



https://istio.io/v1.9/docs/setup/install/multicluster/multi-primary_multi-network/

## Control plane models



多集群部署也可以共享控制面(control plane)服务实例。在这种情况下，控制面实例可以被一个或者多个primary cluster发现。





**Reference**

https://zufardhiyaulhaq.com/istio-multicluster-replicated-control-plane/

https://istio.io/latest/docs/setup/install/multicluster/





1. 想了解一些run time 相关的东西
2. help shizhongjie set up sdewan



Isito 是对Service进行管理