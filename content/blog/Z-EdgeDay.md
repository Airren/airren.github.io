---
title: Edge Day
---





SDEWAN intergrated with Istio will provide a solution for the complex  Edge computing senario 



How to do load balance in different edge cluster.

How to make a service discoverage between Edge cluster.





### Submission

SDWAN typically replaces the connectivity of traditional branch routers with virtualized or appliance-based software that routes traffic to/from remote locations in a seamless, secure and efficient manner, using centralized policy control and management. 

But In the Edge-Computing scenario, the current SD-WAN Solution addresses some of the challenges, but not all. It needs a new pattern of network definition to solve the challenges this is SD-EWAN(with E, Edge).

This talk will share you with an Edge Computing Load Balance Solution that is based on SD-EWAN integrated with Istio. It will create an overlay network between multiple Kubernetes clusters which have public IP or not, in a secure way, and do load balance between different edges.



### Benefits to the Ecosystem

Edge-computing with Multi-tenancy requires logical overlays connecting multiple edges with public/private clouds. Some of the challenges in connectivity include: K8s clusters may have overlapping subnets, K8s clusters may not be behind a static public IP address, K8s clusters may not even have a dynamic public IP address. These challenges prohibit both inbound connections to K8s API server from central management systems and even to the applications from both connectivity and security perspectives.

Current SD-WAN solutions and IOT overlay networks address some of the challenges, but not all. SD-EWAN (with E) intends to address these challenges. 
This is the proposal we made in Akraino ICN projects as part of R3 release.  This talk will help to manage the edge service, especially in load balance scenarios, and all of the traffic is in a  certain secure way. You can use SD-EWAN as a load balancer to K8s services, SD-EWAN acts as a load balancer to ISTIO-ingress service. 





Ren Qiang, +86 1881733030

Ren Qiang works at Intel as an Cloud Native Software Engineer. He is focus on Edge Cloud Native and as the Contributer of SDEWAN poject. 