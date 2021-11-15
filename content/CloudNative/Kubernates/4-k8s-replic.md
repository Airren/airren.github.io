---
title: Kubernetes Replication Controller

---



## Replication Controller



```yaml
# replica.yaml
---
apiVersion: v1
kind: ReplicationController
metadata:
  name: first-replic
spec:
  replicas: 3
  template:
    metadata:
      name: simple-pod
      labels:
        app: simple-pod
    spec:
      containers:
        - name: timemachine
          image: lukelau/rest-docker:0.0.1 
          args:
            - -server.addr=0.0.0.0:8000
```



以上配置会保证Pod的数量稳定为3个。当我们删除一个Pod之后，Replication Controller就会创建出一个新的Pod来维持Pod的数量。

RC之所以会发现Pod已经挂掉了，是因为探针([Container probes](https://kubernetes.io/docs/concepts/workloads/pods/pod-lifecycle/#container-probes))的存在。在K8s中， kubelet会通过指定的探针方式去探测容器是否存活。

三种探针方式

三种类型的handler

- ExecAction： Executes a specified sommand inside the container. The diagnostic is considered successful if the command exits with a status code of 0.
- CPSocketAction: Performs a TCP check against the container's IP adress on a specified port. The diagnostic is consider successful if the port is open.
- TPGetAction: Performs and HTTP GET request against the Container's  IP adderess on a specified port and path. The diagnostic is considered successful if the response has a status code grater than or equal to 200 and less than 400.

当你的Pod的健康探针探测发现Pod的不健康次数超过设定的次数的时候，那么RC就会将这个有问题的Pod删除(没有restart操作)，然后再创建出一个新的来。RC还会检测Pod的当前数量，如果不足则会创建，如果小于则会关掉一些Pod。





## ReplicaSet



ReplicaSet 的Selector会比ReplicationController强大一些。

```yaml
---
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: first-replic-set
spec:
  selector:
    matchLabels:
      app: simple-pod-set
  replicas: 3

  template:
    metadata:
      name: simple-pod-set
      labels:
        app: simple-pod-set
    spec:
      containers:
        - name: timemachine
          image: lukelau/rest-docker:0.0.1 
          args:
            - -server.addr=0.0.0.0:8000
```



selector 变得复杂了，除了matchLabels之外，还支持matchExpressions。

Deployment已经将内置的replica集从ReplicationController转成ReplicaSet了。

