



CNCN 项目的几个阶段



Libos

NIO BIO

DMA是什么



sr-iov

ebpf

ovs

cilium 

calico flannel



ipsec GRE



两个方向，一个是存储，一个是网络

1、有dpdk、ebpf、ovs、lvs、nginx、hyperplane等系统研发经验； 2、有智能网卡、硬件卸载、P4等高性能网络研发经验； 3、有VPC、NAT、负载均衡等云网络产品研发经验。

GRE/VxLAN/OpenFlow等协议；









除了开发之外的工作， 为公司带来价值

performance & power

customer orientation

IA hero feature













```sh
mv ~/.kube/config ~/kubeconfig
sudo chmod 666 ~/kubeconfig

KEY_DATA="$(yq eval '.users[] | select(.name == "kubernetes-admin") | .user.client-key-data' ~/kubeconfig | base64 -d)"
CERT_DATA="$(yq eval '.users[] | select(.name == "kubernetes-admin") | .user.client-certificate-data' ~/kubeconfig | base64 -d)"
CA_DATA="$(yq eval '.clusters[] | select(.name == "kubernetes") | .cluster.certificate-authority-data' ~/kubeconfig | base64 -d)"

curl -X POST \
  --cacert <(echo "${CA_DATA}") \
  --cert <(echo "${CERT_DATA}") \
  --key <(echo "${KEY_DATA}") \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  --data-binary @<(yq eval -j pod.yaml) \
   https://10.67.41.124:6443/api/v1/namespaces/default/pods?fieldManager=kubectl-create
   
   
   
   
   
   
```













```sh
etcdctl --endpoints=https://10.67.41.191:2379 --cacert=/etc/kubernetes/pki/etcd/ca.crt --cert=/etc/kubernetes/pki/etcd/server.crt --key=/etc/kubernetes/pki/etcd/server.key get /clusternet/network/config 


{
  "Network": "10.240.0.0/16",
  "Backend": {
    "Type": "vxlan",
    "VNI": 2
  }
}
```



Test

## 











I'll share some Mount Evans IPU storage performance test processes.

First, let me introduce some background about the test.

As you all know, our IPU is a competitor to Nvidia's DPU, so we referred to the test methods of Nvidia's BuleField2， which was published by IBM in 2021. The smart NICs, here, in the title, means the BlueField2 DPU.

Okay, next, let's see what is Storage Virtualization.
In a nutshell,  It uses an IPU/DPU to emulate a block device to the host or the VM. For the host or the VM, it just sees a block device like a physical disk. The host or the VM doesn't know what kind of storage backend. So, In IPU it always contains a Block Device emulator. 
Another important module in the IPU is the controller（Let’s call it controller for now. There are different implementations in different IPU/DPUs.）, which is used to connect to the remote distributed storage. in most scenarios, between the IPU and the Distributed Storage, it often uses the NVMe over the Faric protocol.
Okay, That's the background.

let's see the test details and the result.



we have 3 test cases. and the benchmark tool we used here is，Vdbench, which is used for the block device test. and the Bdevperf, which is used for the original spdk bdev performance test.
for case1: we call it IPU+Gateway+Ceph for short.  we use the Ceph cluster as the storage backend. There is a gateway, the function of the gateway is to convert the RADOS protocol to the NVMe over TCP protocol。

The function of Gateway is

The previous analysis is from the chart.Now let’s look at the numbers

get some preliminary conclusions