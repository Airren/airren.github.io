

api-server 是Kubernetes与外界交互的入口， api-server 提供了RESTful风格的API。用户可以通过这些API来对K8s的资源进行管理。

> etcd的性能限制了K8s最大节点的扩展
>
> 最多支持5000个node，
>
> 最多支持150000个pod
>
> 