---
title: multus
---

https://github.com/k8snetworkplumbingwg/multus-cn



Multus CNI enables attaching multiple netwrok interface to pods in Kubernets.



Multus CNI is a container nerwork interface plugin for Kubernets that enables attaching multiple network interfaces to pods. Typicaly, in Kubernetes each pod only has one network interface (apart from a loopback) -- with Multus you can create a multi-homed pod that has multiple interface. This is accomplished by Multus acting as a "meta-plugin", a CNI plugin that can call multiple other CNI plugins.



## Key concepts

Two things we'll refer to a number of times through this document are:

- Default Network -- This is you pod-to-pod network. This is how pods communicate among one another in you cluster, how they have connectivity. Generally speaking, this presented as the interface named eth0.  This interface is always attached to you pods, so that they can have connectivity among themselves. We'll add interface in addition to this.
- CRDs -- Custom Resource Definitions. Custom Resources are a way that the kubernets API is extended. We use these here to store some information that Multus can read. Primarily, we use this to store the configurations for each of the additional interfaces that are attached to you pods.





macvlan

