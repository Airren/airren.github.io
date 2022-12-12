---
title: Multi-Cluster Kubernetes
---



## What is Multi-Clusters



Multi-cluster Kubernetes is a kubernetes deployments method that consists of to or more clusters. This deployment method is highly flexible. You can have clusters on the same physical host or different hosts in the same data center. You can also create a multi-cloud environment with clusters living in different clouds and even in different countries.







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













Reference:

[Understanding Multi-Cluster Kubernetes: Architecture, Benefits, and Challenges](https://traefik.io/glossary/understanding-multi-cluster-kubernetes/)

https://www.mirantis.com/cloud-native-concepts/getting-started-with-kubernetes/what-is-kubernetes-multi-cluster/

https://www.cncf.io/blog/2021/04/12/simplifying-multi-clusters-in-kubernetes/

