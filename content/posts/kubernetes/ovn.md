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

07/20 meeting minutes

- Kural     addressed the Qiang queries related to the CNI and architectural diagram.

- Kural     gave the following pointer to understand the CNI and CNI code

- - https://www.dasblinkenlichten.com/understanding-cni-container-networking-interface/
  - https://github.com/containernetworking/plugins/blob/main/plugins/main/bridge/bridge.go

- AR

- - For      Qiang and Jiahao
  
  - - https://github.com/akraino-edge-stack/icn-nodus/tree/master/demo/calico-nodus-secondary-sfc-setup
    
    - Change       the vagrant version to 2.2.19 in the line number https://github.com/akraino-edge-stack/icn-nodus/blob/master/demo/calico-nodus-secondary-sfc-setup/setup.sh#L14       and bring up the demo
    
    - OVS       and OVN ramp up
    
    - - OVS        deep dive - https://www.youtube.com/watch?v=x-F9bDRxjAM&ab_channel=OpenInfrastructureFoundation
      - OVN        deep dive - https://docs.ovn.org/en/latest/tutorials/ovn-sandbox.html        & video - https://www.youtube.com/watch?v=okralc7LrZo&ab_channel=OpenInfrastructureFoundation
    
    - Please       run the ovs-vsctl command in the ovn-controller pod and ovn-nbctl       command in the ovn-control-plane pod to understand the flow
    
    - Kural’s       presentation on the Nodus - https://www.youtube.com/watch?v=hGiOHIkxaoQ&t=3s&ab_channel=OpenvSwitch
    
    - Quick       start on understanding the Kubernetes operator framework - https://sdk.operatorframework.io/docs/building-operators/golang/quickstart/
  
  - For      Kural
  
  - - Get       the login for OPNFV lab and LF Edge lab login for Qiang and Jiahao.

### OVN  Demo

#### Function defination

```shell
# ADD_BR()
ovs-vsctl add-br br-int-1
# ADD_NAMESPACES(foo1)
ip netns add foo1
# NS_EXEC([namespace],[command])
ip netns exec foo1 ip link
# ADD_VETH(foo1, foo1, br-int, "192.168.1.2/24","f0:00:00:01:02:03",
# "192.168.1.1")
# ADD_VETH([port],[namespace],[ovs-br],[ip_addr],[mac_addr],[gateway],
# [ip_addr_flags])


ip link add $1 type veth peer name ovs-$1
ip link set $1 netns $2
ip link set dev ovs-$1 up
ovs-vsctl add-port $3 ovs-$1 -- \
set interface ovs-$1 external-ids:iface-id="$1"

ip netns exec $2 ip addr add $4 dev $1 $7
ip netns exec $2 ip link set dev $1 up
ip netns exec $2 ip link set dev $1 address $5
ip netns exec $2 ip route add default via $6

ip link del ovs-foo1
```

Setup Demo

```shell
function ADD_NAMESPACES(){
        ip netns add $1
}


function ADD_VETH(){
set -x

ip link add $1 type veth peer name ovs-$1
ip link set $1 netns $2
ip link set dev ovs-$1 up
ovs-vsctl add-port $3 ovs-$1 -- \
set interface ovs-$1 external-ids:iface-id="$1"

ip netns exec $2 ip addr add $4 dev $1 $7
ip netns exec $2 ip link set dev $1 up
ip netns exec $2 ip link set dev $1 address $5
ip netns exec $2 ip route add default via $6
}

ovs-vsctl add-br br-int-1
ovn-nbctl create Logical_Router name=R1
ovn-nbctl create Logical_Router name=R2 options:chassis=hv1

ovn-nbctl ls-add foo
ovn-nbctl ls-add bar
ovn-nbctl ls-add alice
ovn-nbctl ls-add join

# Connect foo to R1
ovn-nbctl lrp-add R1 foo 00:00:01:01:02:03 192.168.1.1/24
ovn-nbctl lsp-add foo rp-foo -- set Logical_Switch_Port rp-foo \
            type=router options:router-port=foo addresses='"00:00:01:01:02:03"'

# Connect bar to R1
ovn-nbctl lrp-add R1 bar 00:00:01:01:02:04 192.168.2.1/24
ovn-nbctl lsp-add bar rp-bar -- set Logical_Switch_Port rp-bar \
            type=router options:router-port=bar addresses='"00:00:01:01:02:04"'

# Connect alice to R2
ovn-nbctl lrp-add R2 alice 00:00:02:01:02:03 172.16.1.1/24
ovn-nbctl lsp-add alice rp-alice -- set Logical_Switch_Port rp-alice \
            type=router options:router-port=alice addresses='"00:00:02:01:02:03"'

# Connect R1 to join
ovn-nbctl lrp-add R1 R1_join 00:00:04:01:02:03 20.0.0.1/24
ovn-nbctl lsp-add join r1-join -- set Logical_Switch_Port r1-join \
            type=router options:router-port=R1_join addresses='"00:00:04:01:02:03"'

# Connect R2 to join
ovn-nbctl lrp-add R2 R2_join 00:00:04:01:02:04 20.0.0.2/24
ovn-nbctl lsp-add join r2-join -- set Logical_Switch_Port r2-join \
            type=router options:router-port=R2_join addresses='"00:00:04:01:02:04"'

# Static routes.
ovn-nbctl lr-route-add R1 172.16.1.0/24 20.0.0.2
ovn-nbctl lr-route-add R2 192.168.0.0/16 20.0.0.1



ADD_NAMESPACES foo1
ADD_VETH foo1 foo1 br-int-1 "192.168.1.2/24" "f0:00:00:01:02:03" "192.168.1.1"
ovn-nbctl lsp-add foo foo1 -- lsp-set-addresses foo1 "f0:00:00:01:02:03 192.168.1.2"
ADD_NAMESPACES alice1
ADD_VETH alice1 alice1 br-int-1 "172.16.1.2/24" "f0:00:00:01:02:04" "172.16.1.1"
ovn-nbctl lsp-add alice alice1 -- lsp-set-addresses alice1 "f0:00:00:01:02:04 172.16.1.2"
ADD_NAMESPACES bar1
ADD_VETH bar1 bar1 br-int-1 "192.168.2.2/24" "f0:00:00:01:02:05" "192.168.2.1"
ovn-nbctl lsp-add bar bar1 -- lsp-set-addresses bar1 "f0:00:00:01:02:05 192.168.2.2"
```

```shell
# destroy the env
ip netns del foo1
ip netns del bar1
ip netns del alice1    
ovn-nbctl lr-del R1
ovn-nbctl lr-del R2
ovn-nbctl ls-del foo
ovn-nbctl ls-del bar
ovn-nbctl ls-del alice
ovn-nbctl ls-del join
ovs-vsctl del-br br-int-1
```

### OVS demo

```shell
# Create 2 VRFs(namespasce) VRF1 and VRF2
ip netns add VRF1
ip netns add VRF2
ip netns list
# Create virtual ethernet port vEth1 and vEth2 and connect them to eatch other
ip link add veth1 type veth peer name veth2
# Create virtual ethernet port vEth3 and vEth4 and connect them to each other
ip link add veth3 type veth peer name veth4
# Move vEth1 to VRF1
ip link set veth1 netns VRF1
# Move vEth3 to VRF2
ip link set veth3 netns VRF2
# Assign IP addresses to vEth
# ip netns exec VRF1 ifconfig veth1 10.10.10.1/24 up
ip netns exec VRF1 ip addr add 10.10.10.1/24 dev veth1 
ip netns exec VRF1 ip link set veth1 up
# ip netns exec VRF1 ifconfig
ip netns exec VRF1 ip a
# ip netns exec VRF2 ifconfig veth3 10.10.10.2/24 up
ip netns exec VRF2 ip addr add 10.10.10.2/24 dev veth3
ip netns exec VRF2 ip link set veth3 up
ip netns exec VRF2 ip a

# Create vSwitch1
ovs-vsctl add-br vSwitch1
# Assign vEth2 and vEth4 to vSwitch1
ovs-vsctl add-port vSwitch1 veth2
ovs-vsctl add-port vSwitch1 veth4

ip link set veth2 up
ip link set veth4 up 
# Test connectivity
ip netns exec VRF1 ping -c 3 10.10.10.2
ip addr add 10.10.10.3/24 dev vSwitch1
ping -c 3 10.10.10.1
ping -c 3 10.10.10.2
# Enable Spanning Tree of vSwith1
ovs-ctl set bridge vSwitch1 stp_enable=true
# the port from listening to forwarding, it can work to receive packet
ovsdb-client dump
# ovs-ctl set bridge vSwitch1 stp_enable=false
# ovsdb-client dump


# Assign IP address to SVI(Bridge Interface,switch virtual interface) of vSwitch1

# Check the MAC address table of vSwitch1
ovs-appctl fdb/show vSwitch1
```

## Reference

[7/15 10:33 AM] Ramakrishnan, Kuralamudhan https://www.dasblinkenlichten.com/understanding-cni-container-networking-interface/ 
 [7/15 10:36 AM] Ramakrishnan, Kuralamudhan https://github.com/containernetworking/cni/blob/main/SPEC.md 
 [7/15 10:36 AM] Ramakrishnan, Kuralamudhan https://github.com/containernetworking/plugins/tree/main/plugins/main/bridge 
 [7/15 10:37 AM] Ramakrishnan, Kuralamudhan https://github.com/containernetworking/plugins/blob/main/plugins/main/bridge/bridge.go

[9:13 AM] Ramakrishnan, Kuralamudhan https://github.com/ovn-org/ovn/blob/main/tests/system-ovn.at 
 [9:14 AM] Ramakrishnan, Kuralamudhan https://github.com/ovn-org/ovn/blob/main/tests/system-ovn.at#L24 
 [9:16 AM] Ramakrishnan, Kuralamudhan https://github.com/ovn-org/ovn/blob/main/tests/system-ovn.at#L612 
 [9:18 AM] Ramakrishnan, Kuralamudhan https://man7.org/linux/man-pages/man8/ovn-trace.8.html 
 [9:27 AM]  9:27 AM Meeting ended: 52m 22s 
Please read my blog on the future work - https://medium.com/@rkamudhan/service-function-chaining-in-kubernetes-using-squid-proxy-for-sase-providers-7c477a76893e like 1

Till now, I have a rough understanding of the OVS and the OVN usage, Instead of the OVN sandbox, I try the common OVS and  OVN command in the ovn-controller pod to construct a network. 

Because I failed to set up the OVN sandbox environment,  I don't know if should I run the OVS first and then set up the ovn sand-box environment.

And last Friday, the Poland colleague introduced the recent work they did about the nodus, the main focus on the recent pr on the ICN gerrit.