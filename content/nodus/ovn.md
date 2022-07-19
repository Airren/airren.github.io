---
title: OVN Open virtual Network
---

Open Virtual Network



OVN(Open Virtual Network) is a series of daemons for the Open vSwitch that translate virtual network configuration into OpenFlow.



OVN provides a higher-layer of abstraction than Open vSwitch, working with logical routers and logical switches, rather than flows.





Why did we choose OVN for Nodus?

One of the best programmable controller

Hides OVS complexity

Broader eco-system

L2 CNI - Support for unicast, multicast, broadcast applications

One site level IPAM - No IP address restriction with number of nodes

Possible to implement critical features with table-based pipline

(Firewall, Routing, Switching, Load balancing, Network Policy)

SmartNIC( Smart Network Interface Card) friendly



### Nodus Architecture blocks

NFN Operator	

- Expose virtual, provider, chaining CRDs to external world
- Programs OVN to create L2 switches.
- Watch for PODs being coming up
  - Assigns IP address for every network of the deployment
  - Looks for replicas and auto create routes for chaining to work
  - Create LBs for distributing the load across CNF replicas

NFN Agent

- Performs CNI operations
- Configures VLAN and Routes in linux kernel(in case of routes, it cloud do it both root and network namespace )
- Communicates with OVSDB to inform of provider interface.(create ovs bridge and creates external-ids:ovn-bridge-mappings)