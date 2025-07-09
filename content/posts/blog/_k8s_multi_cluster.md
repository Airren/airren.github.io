I did an investigation, the are two user stories about why we need cross-cluster connectivity and server discovery.

**1st**

Some companies may have an environment with three clusters belonging to three different providers (AWS, GCP, and private cloud) and they want applications running in the different clusters to be able to communicate with each other and:

- Cross-cluster Pod networking and encryption capabilities;
- Ability to access remote Kubernetes Service;
- Use policies to allow remote cluster-specific applications to access local endpoints.



**2nd**

Suppose a company has a server site, A and B, in two cities with very far physical distances. To provide users with faster responses, in general, end users will access the services of the local server site nearby. In this way, developers need a unified deployment entry when publishing the services, and services in two regions can be updated separately once deploy. Clusternet currently assumes that role, which can manage multiple clusters and deploy services to multi-clusters in different regions according to preset weights.

In this multi-server site disaster recovery scenario, if the service in A  crashes for some reason, at this time, the gateway in A  needs to seamlessly migrate user requests to B. At this time, cross-cluster Network connectivity and cross-cluster service discovery capabilities are needed.

In this case, we can provide developers with cross-cluster Mesh network and service discovery through NRI, without requiring developers to manually add remote service access configurations during deployment.



This is the benefits of using multi-cluster Service https://cloud.google.com/kubernetes-engine/docs/concepts/multi-cluster-services#benefits_of_using_mcs



Reference:

https://www.qovery.com/blog/kubernetes-multi-cluster-why-and-when-to-use-them

https://blog.51cto.com/u_15127570/2711101

https://developer.aliyun.com/article/748866





https://mp.weixin.qq.com/s?__biz=MzU0OTE4MzYzMw==&mid=2247521721&idx=4&sn=6a2c50575d4b311b90c03696580c144e&chksm=fbb11c47ccc69551c9594cf1198cebe92bea58adee5a80f960d13c43db92ac05eccbc917a8a4&scene=27

https://blog.51cto.com/ghostwritten/5345107

https://dwdraju.medium.com/a-pod-restarts-so-whats-going-on-fa12bb8a57ea



https://www.cnblogs.com/lianngkyle/p/15171630.html

https://zwforrest.github.io/post/kubelet%E4%B9%8Bcri%E5%92%8Ccni%E8%B0%83%E7%94%A8%E5%88%86%E6%9E%90/