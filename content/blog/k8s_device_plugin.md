---
title: Device Plugin
---



[GitHub: Intel Device plugins for Kubernetes](https://github.com/intel/intel-device-plugins-for-kubernetes)

## Device Plugins

> **Device Plugins**
>
> Use the Kubernetes device plugin framework to implement plugins for GPUs, NICs, FPGAs, InfiniBand, and similar resources that require vendor-specific setup.



Instead of customizing the code for Kubernetes itself, vendors can implements a device plugin that you deploy either manually or as a `DaemonSet`.  The targeted device include GPUs, high-performance NICs, FPGAs, InfiniBand adapters, and other similar computing resources that may require vendor specific initialization and setup.



### Device plugin registration 

The kubelet exports a Registration RPC service:

```
service Registration{
    rpc Register(RegisterRequest) return (Empty){}
}
```









Then, user can request devices in a Container specification as they request other types of resources, with the following limitations:

- Extended resources are only supported as integer resources and cannot be overcommitted.
- Devices cannot be shared among Containers.

Here is an example of a pod requesting this resource to run a demo workload.

```sh
---
apiVersion: v1
kind: Pod
metaData:
	name: demo-pod
spec:
	containers:
	- name: demo-container-1
	  images: k8s.gcr.io/pause:2.0
	  resource:
	  	limit:
	  		hardware-vendor.example/foo: 2
# This Pod need 2 of the hardware-vendor.example/foo devices and can only schedule onto a Node
# that's able to satisfy the need.
# If the Node has more than 2 of those device avaiable, the remainder would be available for 
# other Pods to use.
```

