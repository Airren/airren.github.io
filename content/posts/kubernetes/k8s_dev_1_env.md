---
Title: User Kuberbuilder to create a CRD operator
---



## Develop Environment

### Kind

`kind` is a tool for running local Kubernetes clusters using Docker container "nodes". kind was primarily designed for testing Kubernetes itself but may be used for local development or CI.

```sh
kind load docker-image --name <kind-cluster-name> --nodes <node-name> <image-name>:latest 
```







### Certmangaer

```sh
helm repo add jetstack https://charts.jetstack.io
helm repo update
helm install \
  cert-manager jetstack/cert-manager \
  --namespace cert-manager \
  --create-namespace \
  --version v1.10.1 \
  --set installCRDs=true
```







Question

1. K8s 的manifest 是如何生成的
2. 代码的架构
3. controller-gen的作用，如何根据对应的tag生成 k8smanifest
4. clientgo 中的client set
5. GVK和GVR
6. 每一个controller 是如何注册到主程序中的
7. 常用的依赖，core？ controller runtime
8. 应用部署的时候，首先创建crd资源以及role 权限，然后部署应用





k8s finalizaer



1. Reconcile 函数什么时候回被调用到，
2. Reconcile函数的返回值中如果含有err如何处理，err 为nil的时候如何处理
3. Renconcile Result 什么情况下需要requeue，



Yaml 中的apiVersion

pod

deployment 

Code中的struct defination

## k8s.io/apimachinery/pkg/apis/meta/v1

ResourceList



### Unstructured 



golang 的type assert

### Scheme: resource register

新增一个crd的时候需要注册到scheme

> **UnversionedType**: metav1.Status, metav1.APIVersions, metav1.APIGroupList, metav1.APIGroup, metav1.APIResourceList
>
> **KnownType:** Pod

AddKnowTypes？ or Add UnversionedTypes

```go
// k8s.io/apimachniery/pkg/runtime/scheme.go
type Scheme struct {
	// gvkToType allows one to figure out the go type of an object with
	// the given version and name.
	gvkToType map[schema.GroupVersionKind]reflect.Type

	// typeToGVK allows one to find metadata for a given go object.
	// The reflect.Type we index by should *not* be a pointer.
	typeToGVK map[reflect.Type][]schema.GroupVersionKind
  //....
}
```



### Codec

> **Serializer**:
>
> **Codec:** Serializer is one kind of Codec 





> What is protobuf, wirte a demo





### Converter: resource Version convert





## Kubectl





```sh
kubectl run pod --image=nginx:latest # create one or more pod

kubectl expose


kubectl rolling-update # uses replication controller roll update

kubectl cluster-info
kubectl top
```





### Cobra







### Informer machanism

### WorkQueue





Golang的优雅退出



## K8s 代码生成器

tags

- Package Tag
- Type Tag

```go
//+k8s:deepcopy-gen=package
//+groupName=example.com

// Local Tag
// +genclient
```

