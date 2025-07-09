---
Title: Multus

---







Multus 支持几种类型的CNI  

不同类型的CNI的部署顺序是否有影响，multus是如何处理的。 不处理

是否允许多次部署同一种类型的CNI. 不允许

对于Flannel这种CNI是何如处理的。 不处理





### Demo NetworkAttachmenct

```shell
# Execute following command at Kubernetes master
cat <<EOF | kubectl create -f -
apiVersion: "k8s.cni.cncf.io/v1"
kind: NetworkAttachmentDefinition
metadata:
  name: macvlan-conf-1
spec:
  config: '{
            "cniVersion": "0.3.0",
            "type": "macvlan",
            "master": "eth1",
            "mode": "bridge",
            "ipam": {
                "type": "host-local",
                "ranges": [
                    [ {
                         "subnet": "10.10.0.0/16",
                         "rangeStart": "10.10.1.20",
                         "rangeEnd": "10.10.3.50",
                         "gateway": "10.10.0.254"
                    } ]
                ]
            }
        }'
EOF

```





Add another flannel in a same network subnet

```sh
cat <<EOF | kubectl create -f -

apiVersion: "k8s.cni.cncf.io/v1"
kind: NetworkAttachmentDefinition
metadata:
  name: flannel
spec:
  config: |
    {
        "cniVersion": "0.3.1",
        "plugins": [
          {
            "type": "flannel",
            "delegate": {
              "hairpinMode": true,
              "isDefaultGateway": true
            }
          },
          {
            "type": "portmap",
            "capabilities": {"portMappings": true},
            "snat": true
          }
        ]
    }

EOF
```



### Demo Pod

```yaml
cat <<EOF | kubectl create -f -
apiVersion: v1
kind: Pod
metadata:
  name: pod-case-09
  annotations:
    k8s.v1.cni.cncf.io/networks: macvlan-conf-1, macvlan-conf-2, flannel
spec:
  containers:
  - name: pod-case-01
    image: docker.io/centos/tools:latest
    command:
    - /sbin/init
EOF
```









Reference-deploy

https://github.com/k8snetworkplumbingwg/reference-deployment/tree/master

https://ubuntu.com/kubernetes/docs/cni-multus

https://devopstales.github.io/kubernetes/multus/

https://www.redhat.com/en/blog/multus-takes-leading-role-container-networking\

https://canonical.com/blog/multus-how-to-escape-the-kubernetes-eth0-prison