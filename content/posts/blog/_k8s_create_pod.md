https://juejin.cn/post/7147255278906376228



kubelet 原理

https://www.likakuli.com/posts/kubernetes-pod-create/



```sh
ETCDCTL_API=3 etcdctl --endpoints=https://127.0.0.1:2379 --cacert=/etc/kubernetes/pki/etcd/ca.crt --cert=/etc/kubernetes/pki/etcd/server.crt --key=/etc/kubernetes/pki/etcd/server.key get / --prefix --keys-only


ETCDCTL_API=3 etcdctl --endpoint=https://10.67.41.253:2379 --ca-file=/etc/kubernetes/pki/etcd/ca.crt --cert-file=/etc/kubernetes/pki/etcd/server.crt --key-file=/etc/kubernetes/pki/etcd/server.key get "" --from-key
```

