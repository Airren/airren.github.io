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

### The HTTP Interface of the API Server



### API Terminology

Before we get into the API business, let's first define the terms used in the context of the Kubernetes API server.

#### Kind

The type of an entity. Each object has a field `Kind`, which tells a client such as `kubectl` that it represent, for example, a pod. There are three categories of kinds.

- Objects represent *a persistent entity in the system* -- for example, Pod or Endpoints.  Objects have names, and many of the live in namespaces.
- Lists are collections of one or more kinds of entities.  List have a limit set of common metadata. Examples include PodLists or NodeLists. When you do a kubectl get pods, that's exactly what you get.
- `Special-purpose kinds` are used for specific actions on objects and for nonpersistent entities such as */binding* or /scale. For discovery, Kubernetes use `APIGroup` and `APIResource`; for error results, it use `Status`.

In Kubernetes programs, a kind directly corresponds with a Golang type. Thus, as a Golang type, kinds are singular and begin with a capital letter.

#### API group

A collection of Kinds that are logically related. For example, all batch objects like Job or ScheduledJob are in the batch API group.

#### Version

Each API group can exist in multiple versions, and most of them do. For examples, a group first appears as v1alpha1 and is then promoted to v1beta1 and finally graduates to v1. An object created in one version can be retrieved in each of the supported version. The API server does the lossless conversion to return objects in the requested version. From the cluster user's point of view, versions are just different representations of the same objects.



> **TIP**
>
> There is no such thing as "on object is in v1 in the cluster, and another object is in v1beta1 in the cluster."  Instead, every object can be returned as v1 representation or v1alpha1 representation, as the cluster user desires.



#### Resource

A usually lowercase, plural word(e.g., pods) identifying as a set of HTTP endpoints(paths) exposing the CRUD semantics of a certain object type in the system. Common path are:

- The root, such as .../pods, which list all instances of that type.
- A path for individual named resources, such as ..../pods/nginx.

Typically,  each of this endpoints returns and receives one kind(a PodList in the first case, and a Pod in the second). But in other situations,  a Status kind object is returned.

In addition to the main resource with full CRUD semantics, a resource can have further endpoints to perform specific actions(e.g., .../pod/nginx/port-forward, ../pod/nginx/exec, or .../pod/nginx/logs). We call these `subresources`. These usually implement custom protocols instead of REST -- for example, some kind of streaming connections via WebSocket or imperative APIs.



> TIP
>
> Resources and kinds are often mixed up. Note the clear distinction:
>
> - Resources correspond to HTTP paths
> - Kind are the type of objects returned by and received by these endpoints, as well as persisted into etcd.



### Kubernets API Versioning



### Declarative State Management





## How the API Server Process the request



API HTTP handler -> authn & authz -> Mutating adminssion -> Object schema validation -> Validating admission -> Presisting to etcd





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


![image-20220212224529473](k8s_source_code_2.assets/image-20220212224529473.png)



#### apis/batch
```sh
k get --raw /apis/batch/v1 | jq -c  '.resources[]|{name,kind,shortNames,categories}'
```


![image-20220212224012052](k8s_source_code_2.assets/image-20220212224012052.png)



#### apis/apps/v1
```sh
k get --raw /apis/apps/v1 | jq -c  '.resources[]|{name,kind,shortNames,categories}'
```


![image-20220212230119903](k8s_source_code_2.assets/image-20220212230119903.png)

































### deepcopy-gen 

deepcopy-gen 是一个自动生成DeepCopy函数的代码生成器。给定一个包的目录路径作为输入源，它可以为其生成DeepCopy相关函数，这些函数可以有效的执行每种类型的深复制操作。













