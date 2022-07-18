---
title: CNI Container Networking Interface
---







## Docker CNM













## CNI



Kubernetes Networking Requirements

- Each Pod get their own IP addresss
  - Containers within a Pod share network namespace
- All pod can communicate with all other pods without NAT(Network Address Translation)
- All nodes can communicate with all pods without NAT
- The IP of the Pod is same throughout the cluster





- `runtime` is the program responwsible for executing CNI plugins.
- `plugin` is a program that applies a specified network configuration.





[7/15/2022 10:33 AM] Ramakrishnan, Kuralamudhan https://www.dasblinkenlichten.com/understanding-cni-container-networking-interface/ 
 [7/15/2022 10:36 AM] Ramakrishnan, Kuralamudhan https://github.com/containernetworking/cni/blob/main/SPEC.md 
 [7/15/2022 10:36 AM] Ramakrishnan, Kuralamudhan https://github.com/containernetworking/plugins/tree/main/plugins/main/bridge 
 [7/15/2022 10:37 AM] Ramakrishnan, Kuralamudhan https://github.com/containernetworking/plugins/blob/main/plugins/main/bridge/bridge.go