---
title: Kubernetes Service

---



Pod是最小的单元， 往往我们在运行一个应用的时候，对Pod有一些额外的要求，例如高可用或者多实例，这就意味着如果你只记住一个Pod的IP，那么很多时候时有问题的。 例如高可用，可能会因为Pod的重建而改变，这样你记住的那个IP就失效了。一个很自然的想法就是固定IP。



为什么要使用service

- 因为K8s里面的Pod是可以被调度的，并且重建的，所以没有固定的IP
- Pod的数量可能不只一个，当有多个Pod实例的时候负载均衡的需求。



```yaml
# service.yaml
---
apiVersion: v1
kind: Service
metadata:
  name: first-service
spec:
  type: NodePort
  selector:
    app: nginx
  ports:
    - protocol: TCP
      port: 5580
      targetPort: 80
      nodePort: 32280
```



- selector 过滤携带label `app=nginx`的pod

- targetPort： pod 提供服务的端口

- service代理Pod的内部端口为5580： 内部访问，固定IP：Port

- service代理Pod的外部端口为32280， 通过k8s集群的IP，可以进行 外部可访问

  



```sh
# create service
kubectl apply -f service.yaml
# get service status
kubectl get service first-service
```



外部访问service

上面这个配置之所以可以在外部访问，是因为制定了Service的Type为NodePort， 在K8s中如果不指定这个Type的话，service时只能在K8s集群内部访问的，集群外部是访问不了的。

集群外部访问的设置方式：

- NodePort
- LoadBalancer： 一般只有在共有云上使用
- Ingress: 这个不是L4的代理，而是L7的方式。

内部访问service

对于service的访问， 内部是通过DNS来进行的。也就是说，当你定义了一个service之后，K8s就相应的生成了一条DNS记录。这个service对应的内部域名就是`<cluster-name>.<namespace>.svc.cluster.local`,在没有贴别制定service类型的时候，他返回的时service的ClusterIP；如果你设置了service的ClusterIP为None， 那么返回的就是所有被代理的Pod的IP地址的集合。



- ClusterIP模式的Service提供的是一个Pod的稳定IP地址，即VIP，这里Pod 和service的关系是通过Lab确认的。
- Headless Service提供的则是一个Pod稳定的DNS名字，并且这个名字是通过Pod名字和Service名字拼接出来的。