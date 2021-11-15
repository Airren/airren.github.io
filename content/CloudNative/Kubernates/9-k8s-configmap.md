---
title： K8s ConfigMap
---





## 给Pod传递参数

### 通过环境变量给Pod传递参数

可以在container的描述文件中加入一个env的参数，值是一个数组，每个元素都是键值对，值是string。

```sh
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
      image: nginx:1.17.0
      env:
        - name: INTERVAL
          value: 30s

```



### 通过命令行参数给Pod传递参数

加入一个args配置，命令行参数是一个字符串数组

