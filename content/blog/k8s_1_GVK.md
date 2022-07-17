---
title: K8s Groups and Versions and Kinds
---



### Groups and Versions

An API Group in Kubernetes is simply a collection of related functionality. Each group has one or more versions, 



### Kinds and Resources



Each API group-version contains one or more API types, which we call Kinds. 

A resource is simply a use of Kind in the API. Often, there's a one-to-one maping between Kinds and resources.  For intance, the pods resource crooesponds to the Pod Kind. However, sometimes, the same Kind may be returned by multiple resources. For instances, the Scale Kind is returned by all scale subresources, like deployments/scale or replicatsets/scale.  With CRD, however, each Kind will correspond to a single resource.



When we refer to a kind in a particular group-version, we'll call it a GroupVersionKind, or GVK for short. Same with resources and GVR. As we'll see shortly, each GVK corresponds to a given root Go type in package.





### Scheme

The Scheme is simply a way to keep track of what Go type corresponds to a given GVK.



