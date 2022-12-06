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

