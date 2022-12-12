

## What is Service Mesh(服务网格)

https://jimmysong.io/blog/why-do-you-need-istio-when-you-already-have-kubernetes/











UPF



2 次链接

1 控制面 ip  register step 1-4

hub 公有网络直连接， 给edge pod分配全局IP，cluster api server CRD controller 监视CRs --> kube cofig



2 数据面

hub-device connection

hub 桥接



IPSec configuration -> 2 次分发

SNAT-> DNAT  IP 地址可能重合



### 测试环境部署

4 cluster

物理机 4 nuc

10.239.241.255 -- hub

switch 交换机



kubectl get iphost



kubectl describe iphost hubdevice1





kubectl get 

spce:  

pubkry

conn_type

proposal 加解密

mark route base VPN

mode: start

remote_sourceIP: 虚拟IP



私钥。公钥，CA 

remote： %any 接受所有外部链接

type: VTI-based

IPsec -> Interface + policy

kubectl describe 







cnf pod : openwrt 大部分网络 default pod

SNET

IPsec tunnel -》 点对点， 实际上应该是点对 域名



路由  hub 路由正常的main table

IP router、rule 、table

自己配置一个表号， 某些情景下使用IP rule。

所有的CR 类型，IPsec，firewall，



tcpdump



scc -》 overlay controller





sdewan- system  or default

rsync -> deploy resource

scc -> use rsync to deploy the CRs , 所有的call log，204，ok

mongo 存储注册内容，



test-scc











> IPtables