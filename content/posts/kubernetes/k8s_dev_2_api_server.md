---
title: Kubernetes API Basic
---





## Controller and Operator

In this section you'll learn about `controllers` and `operators` in Kubernetes and how they work.

The API server has the following core responsibilities:

- To serve the Kubernetes API.  This API is used cluster-internally by the master components, the worker nodes, and you Kubernetes-native apps, as well as externally by clients such as `kubectl`.
- To proxy cluster components, such as the Kubernetes dashboard, or stream logs, services ports, or serve `kubectl exec` sessions.

Per the Kubernetes glossary, a controller implements a control loop, watching the shared state of the cluster throught the API Server and making changes in an attempt to move the current state toward the desired state.

Befor we dive into the controller's inner working, let's define our terminology:

- Controllers can act on core resources such as deployments or services, which are typically part of Kubernetes controller manager in the control plane, or can watch and manipulate user-defined custome resources.
- Operatirs are controllers that encode some opetational knowledge, such as application lifecycle management, along with the custome resources.



### The Control Loop

In general, the control loop looks as follows:

1. Read the state of resources, preferably event-driven.
2. Change the state of the objects in the cluster or the cluster-external world. For example, launch a pod, create a network endpoint, or query a cloud API.
3. Update status of the resource in step 1 via the API server in etcd.
4. Repeat cycle; return to step 1.



From an architectural point of view, a controller typically uses the following data structures.

#### Informers

Informers watch the desired state of the resources in a scalable and sustainable fashion. They also implement a resync mechanism that enforces periodic reconciliation, and is often used to make sure that the cluster state and the assumend state cached in memory do not drift (e.g.,  due bugs or network issues).

## The API Server	

The API server is the central management entity and **the only component that talks directly with the distributed storage components `etcd`**.

The API server has the following core responsibilities :

- To server the Kubernets API. This API is used cluster-internally by the master components, the work nodes, and you Kubernetes-native apps, as well as externally by clinets such as `kubectl`.
- To proxy cluster components, such as the Kubernetes dashboard, or to stream logs, service ports, or serve `kubectl exec` session.

Serving the API means:

- Reading state: getting single objects, listing them, and streaming changes
- Manipulating state: creating, updating, and deleting objects

State is persisted via `etcd`.

### The HTTP Interface of the API Server

From a clinet's perspective, the API service expose a RESTful HTTP API with JSON or protocol buffer(protobuf for short) payload, which is used mainly for cluster-internal communication, for performance reasons.

The API server HTTP interface handles HTTP requests to query and manipulate Kubernetes resources using the following HTTP verbs(or HTTP methods).



```sh
kubectl -n THENAMESPACE get pods
curl -XGET /api/v1/namespaces/THENAMESPACE/pods
```





### API Terminology

Before we get into the API business, let's first define the terms used in the context of the Kubernetes API server.

#### Kind

The type of an entity. Each object has a field `Kind`(lowercase kind in JOSN, capitalized Kind in Golang), which tells a client such as `kubectl` that it represent, for example, a pod. There are three categories of kinds.

- `Objects` represent *a persistent entity in the system* -- for example, Pod or Endpoints.  Objects have names, and many of the live in namespaces.
- `Lists` are collections of one or more kinds of entities.  List have a limit set of common metadata. Examples include PodLists or NodeLists. When you do a `kubectl get pods`, that's exactly what you get.
- `Special-purpose kinds` are used for specific actions on objects and for nonpersistent entities such as *`/binding`* or `/scale`. For discovery, Kubernetes use `APIGroup` and `APIResource`; for error results, it use `Status`.

In Kubernetes programs, **a kind directly corresponds with a Golang type**. Thus, as a Golang type, kinds are singular and begin with a capital letter.

#### API group

A collection of Kinds that are logically related. For example, all **batch objects** like `Job` or `ScheduledJob` are in the batch API group.

#### Version

**Each API group can exist in multiple versions**, and most of them do. For examples, a group first appears as `v1alpha1` and is then promoted to `v1beta1` and finally graduates to `v1`. An object created in one version can be retrieved in each of the supported version. The API server does the lossless conversion to return objects in the requested version. From the cluster user's point of view, versions are just different representations of the same objects.



> **TIP**
>
> There is no such thing as "on object is in v1 in the cluster, and another object is in v1beta1 in the cluster."  Instead, every object can be returned as v1 representation or v1alpha1 representation, as the cluster user desires.



#### Resource

A usually lowercase, **plural word**(e.g., pods) identifying as **a set of HTTP endpoints(paths)** exposing the CRUD semantics of a certain object type in the system. Common path are:

- The root, such as .`../pods`, which list all instances of that type.
- A path for individual named resources, such as `..../pods/nginx`.

Typically,  each of this endpoints returns and receives one kind(a `PodList` in the first case, and a `Pod` in the second). But in other situations,  a `Status` kind object is returned.

In addition to the main resource with full CRUD semantics, a resource can have further endpoints to perform specific actions(e.g., `.../pod/nginx/port-forward`, `../pod/nginx/exec`, or `.../pod/nginx/logs`). We call these `subresources`. These usually implement custom protocols instead of REST -- for example, some kind of streaming connections via **WebSocket** or **imperative APIs**.



> TIP
>
> Resources and kinds are often mixed up. Note the clear distinction:
>
> - Resources correspond to HTTP paths
> - Kind are the type of objects returned by and received by these endpoints, as well as persisted into etcd.



Resources are always part of an API group and a version, collectively referred to as GroupVersionResource(or GVR). A GVR unique defines an HTTP path.

### Kubernets API Versioning



### Declarative State Management

> TIP
>
> State vs. Status
>
> **State:** the particular condition that someone or something is in at a specific time.
>
> **Status:** the situation at a particular time during a process.
>
> **State** is used to describe a stage in a process (e.g. pending/dispatched).
>
> **Status** is used to describe an outcome of an operation (e.g. success/fail).
>
> **Status** is a final (resulting) State.

Let's talk a little more about spec(desired state) versus status(observed state) in the context of the API server.



## How the API Server Processes the requests



API HTTP handler -> authn & authz -> Mutating adminssion -> Object schema validation -> Validating admission -> Presisting to etcd



So, what actually happens now when an http requests hits the Kubernetes API? On a high level, the following interactions take place:





K8s version v1.23.0



name

kind

shortNames

categories



resources




#### api/v1
```sh
k get --raw /api/v1 | jq -c  '.resources[]|{name,kind,shortNames,categories}'
```






#### apis/batch
```sh
k get --raw /apis/batch/v1 | jq -c  '.resources[]|{name,kind,shortNames,categories}'
```






#### apis/apps/v1
```sh
k get --raw /apis/apps/v1 | jq -c  '.resources[]|{name,kind,shortNames,categories}'
```




































### deepcopy-gen 

deepcopy-gen 是一个自动生成DeepCopy函数的代码生成器。给定一个包的目录路径作为输入源，它可以为其生成DeepCopy相关函数，这些函数可以有效的执行每种类型的深复制操作。



### Groups and Versions

An API Group in Kubernetes is simply a collection of related functionality. Each group has one or more versions, 



### Kinds and Resources



Each API group-version contains one or more API types, which we call Kinds. 

A resource is simply a use of Kind in the API. Often, there's a one-to-one maping between Kinds and resources.  For intance, the pods resource crooesponds to the Pod Kind. However, sometimes, the same Kind may be returned by multiple resources. For instances, the Scale Kind is returned by all scale subresources, like deployments/scale or replicatsets/scale.  With CRD, however, each Kind will correspond to a single resource.



When we refer to a kind in a particular group-version, we'll call it a GroupVersionKind, or GVK for short. Same with resources and GVR. As we'll see shortly, each GVK corresponds to a given root Go type in package.





### Scheme

The Scheme is simply a way to keep track of what Go type corresponds to a given GVK.













