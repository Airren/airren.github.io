---
title: K8s 源码阅读
---











## How the API Server Process the request



API HTTP handler -> authn & authz -> Mutating adminssion -> Object schema validation -> Validating admission -> Presisting to etcd





K8s version v1.23.0



#### apis/batch

```sh
k get --raw /apis/batch/v1 | jq -c  '.resources[]|{name,kind,shortNames,categories}'
```

![image-20220212224012052](k8s_source_code_2.assets/image-20220212224012052.png)





#### api/v1

![image-20220212224529473](k8s_source_code_2.assets/image-20220212224529473.png)





#### apis/apps/v1

![image-20220212230119903](k8s_source_code_2.assets/image-20220212230119903.png)

































### deepcopy-gen 

deepcopy-gen 是一个自动生成DeepCopy函数的代码生成器。给定一个包的目录路径作为输入源，它可以为其生成DeepCopy相关函数，这些函数可以有效的执行每种类型的深复制操作。