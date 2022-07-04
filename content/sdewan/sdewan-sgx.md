---
title: SDEWAN: Leverage SGX to provide Secure Access Service Edge
---



SD-WAN handles the network connectivity issues between distributed applications in a seamless, secure, and efficient manner by replacing traditional branch routers with virtualized or appliance-based software. However, as the concept of edge prevails recently, distributed applications are usually deployed across multiple resource-constrained k8s edges. Then how to expose the services and manage the connections in a cloud-native way becomes a critical issue for users. 

The open-source project Software Define Edge WAN(SD-EWAN) under the Akraino community’s ICN blueprint is definitely a comprehensive solution. Its CNF, which is integrated with multiple network functions based on OpenWRT, is able to serve the distributed applications with secure connections and easy management against networking and the CNF can be automatic thru the overlay controller within SD-EWAN to achieve multi-edge collaboration. 



This post is not covering the archtecture of SDEWAN. If you want to lean more about SDEWAN you can find more information form https://github.com/akraino-edge-stack/icn-sdwan. Intention of this post is to cover the CNF which co-works with Intel Software Guard Extensions to create a secruity IPsec tunnel.

## Security Connection Between Edge Cluster Through CNF

One of the functionality of SD-EWAN is to set up secure connection between two edge cluster.  SDEWAN functionality is realized via CNF(Containerized Network Function) and deployed by K8s. SD-EWAN creates IPsec tunnles between two CNF Pod across K8s cluster. The CNF here is based on OpenWRT open source and enhancing, optimizing for Intel IA sccelerator and make it ready for cloud native Edge scenarios.

In the CNF, we use Strongswan to manage the IPsec tunnel and connect two Edges in a private network the a traffice Hub. The traffice Hub has an publice IP, and two different Edges can access the Hub through the public IP bind on the hub.



## Traditional Configuration of IPsec 

The Traditional configuration put the private key and certificate in the Pod filesystem located at `/etc/ipsec.d`. 

## Leverage SGX to Create Secure Tunnle



### Archtecture

The architecture of the CNF which work with CTK is like that.







### Enable PKCS#11 for Strongswan



### Build p11-kit-client for OpenWRT



### After build the p11-kit-client, we shoud add 





### Setup IPsec Tunnel with SGX







