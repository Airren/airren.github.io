





## User Story: Why Multi-Cluster

Some companies may have an environment with three clusters belonging to three different providers (AWS, GCP, and private cloud) and they want applications running in the different clusters to be able to communicate with each other and:

- Cross-cluster Pod networking and encryption capabilities;
- Ability to access remote Kubernetes Service;
- Use policies to allow remote cluster-specific applications to access local endpoints.



Suppose a company has a server site, A and B, in two cities with very far physical distances. To provide users with faster responses, in general, end users will access the services of the local server site nearby. In this way, developers need a unified deployment entry when publishing the services, and services in two regions can be updated separately once deploy. Clusternet currently assumes that role, which can manage multiple clusters and deploy services to multi-clusters in different regions according to preset weights.

In this multi-server site disaster recovery scenario, if the service in A  crashes for some reason, at this time, the gateway in A  needs to seamlessly migrate user requests to B. At this time, cross-cluster Network connectivity and cross-cluster service discovery capabilities are needed.





多集群通信目前存在的问题

多集群之间Pod的IP存在overlap，跨集群Pod之间可能存在相同的Pod IP。

部分跨集群通信方案需要指定类型的CNI，例如submariner仅支持5种类型的CNI。

Overlapping IP addresses between pods in different clusters,Pods in different clusters may have the same IP address.

Some cross-cluster communication solutions require a specific type of CNI (Container Network Interface).For example, Submariner only supports 5 types of CNI.









In order to meet the needs of the above scenarios, we can provide developers with cross-cluster Mesh network and service discovery through NRI, without requiring developers to manually add remote service access configurations during deployment.









Clusternet-CNI: 提供一个DedicatedCNI，Kubernetes 不感知这个CNI的存在，默认会给所有的pod增加一个额外的interface。这个interface主要用来跨集群通信。

CNF: 负责在集群间建立隧道， 打通集群间的通信

Hub: 用于联通多集群，实现流量转发以及流量控制

 







提供本地Kubelet 无感知的 Dedicated netwrok interface，全局CIDR，dedicated interface IP全局唯一。

提供跨集群的服务发现。

结合clusternet 可以实现自动的全局集群CIDR配置，以及dedicatedCNI服务部署。















我们的方案是如何解决这些问题的

