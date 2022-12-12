---
title: K8s DaemonSet

---





NodeExporter 用于采集一个Host上的各种性能指标，并且暴露给Prometheus 采集。因为NodeExporter需要采集每一个Host的数据状态，所以就产生了一个需求，在每一个K8s的机器上都要运行一个Pod, 而实现这个需求的K8s资源类型就叫做DaemonSet。

## DaemonSet



DaemonSet的功能就是保证每个Node都运行Pod， 但是如果某个K8s的Node下线之后，对应的Pod也下线，还是保持每个Node一个Pod。如果新增一个Node，就会在新增的Node中增加一个Pod，保证所有的Node中有且只有一个当前Pod。

```yaml
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: node-exporter
  namespace: kube-system
  labels:
    k8s-app: node-exporter
    kubernetes.io/cluster-service: "true"
    addonmanager.kubernetes.io/mode: Reconcile
    version: v1.2.2
spec:
  selector:
    matchLabels:
      k8s-app: node-exporter
      version: v1.2.2
  updateStrategy:
    type: OnDelete
  template:
    metadata:
      labels:
        k8s-app: node-exporter
        version: v1.2.2
    spec:
      priorityClassName: system-node-critical
      containers:
        - name: prometheus-node-exporter
          image: "prom/node-exporter:v1.2.2"
          imagePullPolicy: "IfNotPresent"
          args:
            - --path.procfs=/host/proc
            - --path.sysfs=/host/sys
          ports:
            - name: metrics
              containerPort: 9100
              hostPort: 9100
          volumeMounts:
            - name: proc
              mountPath: /host/proc
              readOnly:  true
            - name: sys
              mountPath: /host/sys
              readOnly: true
          resources:
            limits:
              memory: 50Mi
            requests:
              cpu: 100m
              memory: 50Mi
      hostNetwork: true
      hostPID: true
      volumes:
        - name: proc
          hostPath:
            path: /proc
        - name: sys
          hostPath:
            path: /sys

```



NodeExporter是针对Node的Pod，需要将Host中的一些系统文件透传给Pod，并让Pod使用主机网络。







额外需求

虽然每个节点都运行一个Pod是一个确实存在的需求，但是有些时候，却又不是要求每个Node都有一个Pod。例如，有10台机器用于运行HDFS。 HDFS有NameNode和DataNode之分，那么DataNode可能是每个Host都要有的，但是NameNode却不是必须的，可能只要3-5台就可以了。这样一个明显的好处就是可以节约成本。

DaemonSet 也是支持Selector的，而且是NodeSelector，通过我们给Node加上一些Label，然后给NameNode加上一些DaemonSet的NodeSelector。



```yaml
spec:
  selector:
    matchLabels:
      k8s-app: hdfs-namenode
  updateStrategy:
    type: OnDelete
  template:
    metadata:
      labels:
        k8s-app: hdfs-namenode
    spec:
      # node selector
      nodeSelector:
        host: high-cpu
```

这样就保证了Pod只会在特定的Host中以DaemonSet的特性启动



