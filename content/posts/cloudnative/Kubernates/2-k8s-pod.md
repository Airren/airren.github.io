---
title: Kubenetes Pod
---





Pod

在Kubernetes中，一切都是资源，你可以通过create/get/describe/delete 来操作这些资源。

在操作一种资源之前，我们需要先对这个资源进行定义，在k8s中常用的是yaml配置文件配置。

```sh
# 00-siample-pod.yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: first-pod
  labels:
    app: nginx
spec:
  containers:
    - name: 00-simple-pod-nginx
      images: nginx:1.17.0
```



apiVersion：资源的版本，可以理解为你要创建的是 PodV1{}还是PodVn{}

kind: 资源的类型

metadata：

​	name： 创建出来的资源的名字

​    labels：与其他资源粒度或者操作的关联

spec: 资源的参数



通过kubectl  apply -f创建资源

```sh
kubectl apply -f 00-simple-pod.yaml
```

如果需要更新资源，修改yaml后，重新kubectl apply -f  xxx.yaml 就可以。



获取Pod状态

```sh
kubectl get pod first-pod
# get more detail
kubectl get pod first-pod -o wide
# get all pods of all-namespace
kubectl get po -A
```



获取Pod 详情

```sh
kubectl describe pod first-pod
```

Pod 状态

- Pending： K8s已经接受了Pod的配置，但是还没有创建容器，可能还在拉取镜像或者调度不成功
- Running：Pod已经调度成功，并且已经和某个node绑定了，所有的容器都被创建
- Succeeded: Pod中所有容器都已经成功运行完毕并退出
- Failed: Pod中至少有一个容器以不正常的状态退出。
- UnKnown: Pod的状态不能被kubelet汇报给kube-apiserver， 这可能是work和master的通讯出现了问题。

Container状态

- Waiting： Default state of container. If container not in Running or Terminated state, it is in Waiting state. A container in Waiting state still runs its required operations, like pulling images, applying secrets, etc. Along with this state, a message and reson about the state are dispalyed to provide more infomation.
- Running: Indicates that the container is executing without issues. Once a container enters into Running, `postStart hook`(if any) is executed. This state also displays the time when the container entered Running state.
- Terminated: Indicates that the container completed its execution and has stoped running.  A container enters into this when it has successfully completed execution or when it has failed for some reason. Regardless, a reason and exit code is displayed, as well as the container's start and finish time. Before a container enters into Terminated, `preStop hook(`if any) is executed.

日志

```sh

# 查看pod日志
kubectl logs <pod-name>
# 查看pod中container的日志
kubectl logs <pod-name> -c <container-name>
```

- 每天或者每次日志到达10MB大小时，容器日志都会自动轮替。kubectl logs仅能显示最后一次轮替后的日志条目。
- 只能获取仍然存在的pod的日志。当一个pod被删除时，他的日志也会被移除。

外地访问pod

将本地网络端口转发到pod中的端口 

```sh
kubectl port-forward
```



注解

向kubernetes 引入新特性时，通常也会使用注解。一般来说，新功能的alpha和beta版本不会向API对象引入任何新字段，因此使用的是注解而不是字段，一旦所有的API变更变得清晰，并且得到所有相关人员的认可，就会引入新的字段，并废弃相关注解。

大量使用注解可以为每个pod或其他API对象添加说明。以便每个使用该集群的人都可以快速查找有关每个单独对象的信息。

Pod 探针

三种类型的handler

- ExecAction： Executes a specified sommand inside the container. The diagnostic is considered successful if the command exits with a status code of 0.
- CPSocketAction: Performs a TCP check against the container's IP adress on a specified port. The diagnostic is consider successful if the port is open.
- TPGetAction: Performs and HTTP GET request against the Container's  IP adderess on a specified port and path. The diagnostic is considered successful if the response has a status code grater than or equal to 200 and less than 400.

三种探针结果

- Success: The container  passed the diagnostic
- Failure: The container failed the diagnostic
- Unknown: The diagnostic failed, so no action should be taken.

重启策略

- Always
- OnFailure
- Never

