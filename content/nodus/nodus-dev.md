---
Nodus Dev Note

---

## SFC for IPV6

vagrant file updte:

<img src="file:///C:/Users/qiangren/AppData/Roaming/marktext/images/2022-08-25-15-35-26-image.png" title="" alt="" data-align="left">

mount  a local file to the VM

```shell
config.vm.synced_folder "/home/ubuntu/qiang/nodus-1.24/SDEWAN-SetUp/", "/home/vagrant/mnt"
```

vagrant version update to 2.2.19

```shell
vagrant_version=2.2.19
```

enable ipv4_forward in calico configuration

update ovn deploy yaml:   update image tag, update ovn_subnet

```shell
 OVN_SUBNET: "10.154.141.0/18"
OVN_GATEWAYIP: "10.154.141.1/18"


 privileged: true
 mountPropagation: Bidirectional
```

```yaml
apiVersion: k8s.plugin.opnfv.org/v1alpha1
kind: NetworkChaining
metadata:
  name: example-networkchaining
spec:
  # Add fields here
  chainType: "Routing"
  routingSpec:
    namespace: "default"
    networkChain: "net=virtual-net1,app=slb,net=dync-net1,app=ngfw,net=dync-net2,app=sdewan,net=virtual-net2"
    left:
    - networkName: "left-pnetwork"
      gatewayIp: "172.30.10.2"
      subnet: "172.30.10.0/24"
      podSelector:
        matchLabels:
          sfc: head
      namespaceSelector:
        matchLabels:
          sfc: head
    right:
    - networkName: "right-pnetwork"
      gatewayIp: "172.30.20.2"
      subnet: "172.30.20.0/24"
      podSelector:
        matchLabels:
          sfc: tail
      namespaceSelector:
        matchLabels:
          sfc: tail
```

Question:

1. What the relationship between the life/right and NetworkChaining feild?

2. Why we can have multiple left/right?

3. In the `ValidateNetworkingChaining` I fount the network namespce is hard code to `default`, is that means all of the Virtual Network should be define in `default` namespace? 

4. Doese and the networkChain feild must start and end with a `VirtualNetwork`. Do you have a topolody of the ProvideNetwork and VirtualNetwork?
   
   ProvidedNetwork must combine with a host network interface.
   
   Virtual is just a OVN virtual switch?

5. SFC :  VirtualMode and ProviderMode
   
   as long as there is one end is not  VirtualNetwork, this is ProviderMode.
