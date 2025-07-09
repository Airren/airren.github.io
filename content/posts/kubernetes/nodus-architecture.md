---
title: Nodus
---

## Image ovn4nfv-k8s-plugin

### NFN-Operator

- Exposes  virtual, provider chaning CRDs to external world
- Programs OVN to create L2 switch
- Watches for PODs being coming up
- Assigns IP address ofr every network of the deployments
- Looks for replics and auto create routes for chaning to work
- Create LBs for distributing the load across CNF replicas

### OVN4NFV

### NFN-Agent

- Performs CNI operations
- Configuration VLAN and Routes in Linux Kernel(in case of ruotes, it cloud do it in both root and network namespace)
- Communicates with OVSDB to inform og provider interface. ( create ovs bridge and creates external-ids: ovn-bridge-mappings)

## OVN-Images

OVN control plane and OVN controller take care of OVN configuration and installation in each node in Kubernetes. NFN operator runs in the Kubernetes master and NFN agent run as daemonset in each node.

```shell
# /usr/local/bin/ovn4nfv-k8s.sh
```

### OVN-Control-Plane / Deployment： replicas=1

```sh
expose: 6441/ 6442

command: ["ovn4nfv-k8s", "start_ovn_control_plane"]

Probe: "ovn4nfv-k8s", "check_ovn_control_plane"
```

### OVN-Controller / DaemonSet

```sh
command: "ovn4nfv-k8s", "start_ovn_controller"
Probe: "ovn4nfv-k8s", "check_ovn_controller
```

OVN command

Libvirt install 

```sh
sudo apt install -y qemu libvirt-bin ebtables dnsmasq-base
sudo apt install -y libxslt-dev libxml2-dev libvirt-dev zlib1g-dev ruby-dev
```

## Open vSwitch

`ovs-vsctl` Configures ovs-vswitchd, but really a high-level interface for database

ovsdb-tool: command line for managing database file

ovsdb-tool show-log [-mmm] <file>