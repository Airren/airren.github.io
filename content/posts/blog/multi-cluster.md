---
title: Multi-Cluster Kubernetes
---



## What is Multi-Clusters



 Multi-cluster Kubernetes allows you to create a multi-cloud environment with clusters living in different clouds and even in different countries. The topic of connecting and discovering services across clusters is becoming increasingly popular. 

However, network overlapping between multiple clusters and the diverse range of  CNI types often pose significant obstacles to achieving network connectivity across multiple clusters. Based on NRI  technology, we establish an independent parallel network between multiple clusters. This network is separate from the existing network and allows for unified planning of the multi-cluster network without affecting the original network. Simultaneously, we utilize WireGuard to establish an encrypted tunnel between multiple clusters and establish and Hub Mode service mesh among the clusters. 



This session will present the fundamental architecture of using this solution with Kubernetes to facilitate network connections between applications residing in different clusters. Additionally, a live demo will be provided to demonstrate how it can be utilized for cross-cluster collaboration.





cluster network connections: https://submariner.io/getting-started/

https://isovalent.com/data/multi-cluster-ebook.pdf

Multi  clusters server deployment: https://github.com/karmada-io/karmada

https://github.com/clusternet/clusternet 







## Submariner

Submariner allows pods to directly communicate between  Kubernetes clusters

Submariner is secure - it uses establishes IPsec tunnels between clusters

Submariner is CNI-agnostic, it operator at a layer independent of you network provider.







### Handling cluseters with overlapping CIDRs

Handling overlapping CIDRs is being developd by the submariner team, the implementation is based on a global overlay CIDR which will be used for colliding clusters.







## Question

Brief introduction of Openshift?Rancher?







Multi-cluster Kubernetes manages clusters residing in various clouds and even different countries, such as Clusternet and Karmada. The challenge of connecting and discovering services across these clusters awaits a solution.
However, network overlapping between multiple clusters and the diverse range of  CNI types often pose significant obstacles to achieving network connectivity across multiple clusters. Based on NRI, we establish an independent parallel network between multiple clusters. This network is separate from the existing network and allows for unified planning of the multi-cluster network without affecting the original network. Simultaneously, we create tunnels(eg. Wireguard) between multiple clusters to establish and hub mode or mesh mode network among the clusters and provide service discovery capabilities and Network policy.  All these functions are agnostic to k8s.

This session will present the fundamental architecture of using this solution with Kubernetes to facilitate network connections between applications residing in different clusters. Additionally, a live demo will be provided to demonstrate how it can be utilized for cross-cluster collaboration.







Reference:

[Understanding Multi-Cluster Kubernetes: Architecture, Benefits, and Challenges](https://traefik.io/glossary/understanding-multi-cluster-kubernetes/)

https://www.mirantis.com/cloud-native-concepts/getting-started-with-kubernetes/what-is-kubernetes-multi-cluster/

https://www.cncf.io/blog/2021/04/12/simplifying-multi-clusters-in-kubernetes/

