# Q1 2022

## Insight

- The SDEWAN  E2E demo installs and verifies process is simplified, shortened to about 40 minutes, and improved efficiency by optimizing the automatic deployment and test scripts. Help the Smart Edge team integrate SDEWAN with DEK, prepare guides, answer questions, and do hands-on debugging.

- Verified the POC in Ubuntu that StrongsWAN integrates with CTK through PKCS#11, which put the private key in -the SGX enclave to protect it. And propose ubuntu-SGX container as a sidecar work with CNF to provide the security IPsec Tunnel connection.

- Challenge: While building SGX SDK, CTK, and strongsWAN in Ubuntu encountered some barriers with C project toolchains. Finally, remove most of them. But while building OpenWRT with SGX, most of the compile seems complicated to solve, so choose another way instead of making that.

One Intel:

One Intel: Qiang proactivity helps Smart Edge team to integrate SDEWAN with DEK, prepare guides, answer questions, do hands-on debugging and always solve issues in time.

Fearless:  Qiang always keeps curiosity for the unknown region can complete the work with challenges. Qiang successfully complete the  POC strongsWAN integrated with CTK in ubuntu overcoming a lot of difficulties.

## OKR for Q2

### Object 1：Intergrate SGX with CNF to protect the private key.

- optimize the image size, strongsWAN-CTK image  base on Ubuntu, limit to 200M.
- Complete web server(Golang) in Ubuntu container and Client(Lua) in OpenWRT container, to accomplish start/stop/restart strongsWAN service in Ubuntu. 
- Test the sync cost time between two sides of the shared Volum. One side is in OpenWRT, and another is in Ubuntu.
- Expose RESTful API in Ubuntu-SGX to generate private keys in Enclave, and create the CSR for the private key in Enclave.

### Object 2: Refine SDEWAN auto-install script and complete the document

- Optimize auto-install script, make it suitable for VM deploy and physical deploy.
- Complete the SDEWAN  documentation along with the version updated.

### Object 3:  Deep dive into Kubernetes Network ,Custome Resource and Sapphire Rapids

- Read more about Virtual Network and CNI definition of Kubernetes
  - veth pair, tun/tap, Linux bridge, VXLAN, Macvlan
  - Calico, Flannel
- Write a Customer Controller Demo through Kubebuilder or Operator SDK.
- Read the Spec of  Intel Feature AMX, and prepare a presention for IDF sharing in 3rd, May.
- Try to join a K8s SIG-Netwoking, watch the latest 10 SIG-Networking meeting videos.

make gen-yaml IMG="integratedcloudnative/sdewan-controller:devtest1"

# Q2 2022

### Insight

- For the SDEWAN project:  Test several work modes of CNF, one is multiple interfaces that can use host-device mode or nodus direct mode to share the physical interface to CNF, and another is CNF work as a service export through NodePort mode. Support SGX HSM co-work with CNF as a sidecar and test SDEWAN pull mode through the script.

- Help the SmartEdge team finish the SASE EK release, by upgrading the auto-deploy script with the latest components.  And verified the possibility of using SDEWAN with Nodus in WAN  on AWS.

- Release a Sapphire rapids feature AMX presentation and verify the IDF proposal's feasibility that uses the AVX or AMX to accelerate the query speed of Time-Series Data.  And bring up an IDF proposal of Cloud Native HSM.

- Challenge:  While working for the SDEWAN SGX support model,  solved some tricky problems which are cross-compile the p11-kit tool for OpenWrt and changing the pkcs#11 token initialize arguments to fit CTK and squashing the HSM image with SGX to reach an ideal size.

Quality: Help the Smart Edge team release SDEWAN with the latest stable component. Ensure the stability and reliability of the auto-deploy script.

Result Driven: Even encountering many tricky problems while enabling SGX for CNF, Qiang solved the problem step by step and finally accomplish the component as the previous design architecture.

### OKR for Q2

### OKR for Q3

**OBJ1: Enable IA optimizations and innovations in open-source Edge Orchestration projects** 

KR1: Integrate and ship Nodus and SDEWAN 22.09 with SmartEdge-O release

KR2: Enhance SGX  E2E flow with Cert Manager(Useing SGX as externel issuer) support SDEWAN 22.09 release.

KR3: SDEWAN auto CICD process, optimize the version manager, and split the code repo

**OBJ2: Build Intel leadership in open-source Edge Orchestration projects with focus on individual career growth**

KR3: Deliver 1 technical submission on edge usages with SDEWAN (KubeCon China)

KR4: v

promiscuous

### Q3 Insight



**Works in Q3**

- Support Smart Edge Team design and verify service function chain in the HUB. Help verify SDEWAN with nodus work on a multi-node cluster and IPsec cross NAT.

- Finished SDEWAN release with SGX feature enhancement and test a new feature for SDL supported.

- Nodus support. Ramp up, test and verify the patch for K8s latest version, and SFC for IPV6.

**Plan for Q4**

Deliver SDEWAN 22.12 releases. Features include: SGX e2e flow enhancement for SDL requirements.

 Integrate SDEWAN with SmartEdge release.

**Intel Values**

OneIntel:  Help Smart Edge team set up new features based on SDEWAN, design and verify the proposals. Such as, service function chains, nodus work in multi node and IPsec tunnel cross NAT.

Fearless: While working with nodus, facing the relatively unfamiliar fields of OVN and CNI, Qiang reads related materials, dives into this field, and gets involved with Nodus
relevant job.

**points for improvement：**

Learning: Qiang should continuously learn Kubernetes related technologies and improve software design skill to deliver high quality product

Innovation: Qiang should continuously learn intel platform features and innovate on how to prompt integration in edge computing domain.



### Q4 OKR

**OBJ1: Enable IA optimizations and innovations in open-source Edge Orchestration projects**

 Deliver SDEWAN 22.12 releases. Features include: SGX e2e flow enhancement for SDL requirements.

**OBJ2: Build Intel leadership in open-source Edge Orchestration projects with focus on individual career growth**

Submit 1 edge orchestration related IDF submission









# 2022 Q4 

## Insight	

### Results History

Ramp up with the Clusternet project, and commit a patch for the  Mcs-API demo.

Ramp up with ODMS, and be ready to adopt the observability technology in K8s cloud-native solutions.

Investigate the CI/CD,  and set up a pipeline with Jenkins and ArgoCD.

Investigate ebpf, and help the BOA project collect disk info by ebpf to metrics.

**Coaching and Goal Alignment**

- Support the observability work of the CESG service framework
- Investigate NRI or Dynamic resource allocation

### **Behaviors History**

**Feedback and Appreciation**

Fearless: Facing with the new domain ODMS observability, reads related materials, dives into this field, and use observability technology in related work.

One Intel: Ramp up with ODMS project about the observability, and support of the BOA project to accomplish the observability feature.

**Coaching and Growth opportunities**

Quality: Qiang should continuously improve his software design skills to deliver high-quality products.

Learning: Qiang should continuously learn intel platform features, and cloud-native related technologies and innovate on optimizing cloud-native solutions through intel technologies.

OBJ1: Increase upstream leadership and visibility in strategic upstream projects (K8s, Containerd, CRI-O, etc.) for L5 enabling

KR1: Write a blog about Cgroup, compare Cgroup v1 and v2, and introduce the usage of Cgroup in Kubernetes.



OBJ2: Deliver customer-ready K8s/container runtime solutions that utilizes Intel optimization and drive end-to-end customer adoption

KR1：Collect disk metrics through ebpf to achieve observability of BOA projects.

KR2:  Investigate NRI/DRA  projects and look for opportunities to combine with OpenCloudOS(eg. Clusternet).









## 2023 Q1 								

### Results History

**Feedback and Appreciation**

Designed and implemented the BOA observability technology stack and supported BOA development and test data collection and visualization.

Ramp up with NRI, wrote a Chinese introduction blog and post to CNCF WeChat Official Account, and verified the feasibility of integration with CNI.

Investigate topology awareness based on NRI (migrate from CRI-RM), compared with Kubernetes internal resource manager policy. 

**Coaching and Goal Alignment**

- Contribute to BOA resource boundary solution release on observability metric integration and deployment environment maintenance.
- Propagate NRI/DRA in the K8s project in the ClusterNet project in OpenCloudOS

### **Behaviors History**

**Feedback and Appreciation**

Customer First: Ramp up PRC middle-stream cloud native technologies (e.g. OpencloudOS, Crane), drive NRI/DRA feature enabling to support customer requirements.

One Intel:  Support BOA  resource boundary solution phase 2 release on observability metric integration and deployment environment maintenance.

**Coaching and Growth opportunities**

Quality: Qiang should continuously improve his software design skills to deliver high-quality products.

Learning: Qiang should continuously learn intel platform features, and cloud-native related technologies and innovate on optimizing cloud-native solutions through intel technologies.





## 2023 Q2 OKR

- BOA e2e scenario integration: test environment build,  observability components deployment and maintenance and support  for network IO isolation.

- NRI/DRA/BOA engagement in OpenCloudOS/PRC middle-stream:  Draft initial design on the cross-cluster service mesh for Clusternet based on NRI with customer.

- Investigate the kernel features related to the enhancement of CloudNative in the OpencloudOS community, and explore the possibility of application in the BOA project.

### Results History

**Feedback and Appreciation**

Support BOA e2e scenario integration:  contribute to IOI project about unix socket connection and service lifecycle mange by systemd, and conducted API server performance test.

BOA project maintenance: constructed a CICD environment by Jenkins, created several pipelines for the test, and static syntax check. Upgrade the monitor dashboard to the new version metrics, and prepare a demo environment in US Lab for customers.

 Implemented dedicated CNI with multi Flannel instance to create the parallel network,  use IPsec behind NAT to create cross-cluster connection, and create a Poc Env co-work with the Clusternet community. Draft initial design on the cross-cluster service mesh for Clusternet based on NRI with the customer and submit a proposal for Kubecon China.

Investigate the kernel features related to the enhancement of CloudNative in the OpencloudOS community, and explore the possibility of application in the BOA project.

Support storage team: ramp up with VMware Nvme of Fabrics technology, and verified the feasibility of integrating Esxi v7.03U and v8.0 with SPDK NVMe target which uses Ceph as the storage. Completed instruction document of that for IBM team.

**Coaching and Goal Alignment**

- Contribute to BOA resource boundary solution release on observability metric integration and deployment environment maintenance.
- Propagate NRI in the K8s project in the ClusterNet project in OpenCloudOS
- support the storage team to investigate the feasibility of using IPU to enhance the VMware storage.

### **Behaviors History**

**Feedback and Appreciation**

Customer First:  Prapare demo environment for customers from Pinterest which contains IOI-related components deployment, do test to verify and accessibility management for the customer.
One Intel: Supported storage team verified the VMware integrated with SPDK and Ceph, and prepared an instruction document for customers from IBM.

**Coaching and Growth opportunities**

Quality: Qiang should continuously improve his software design skills to deliver high-quality products.

Learning: Qiang should continuously learn intel platform features, and cloud-native related technologies and innovate on optimizing cloud-native solutions through intel technologies.





## 2023 Q3 OKR 



- BOA project includes real-world load testing (stateful workloads such as Kafka), deployment environment maintenance, and metric monitoring optimization.

- Conduct research on QoS for IPU storage based on SPDK, and the feasibility of fine-grained resource management for IPU storage in cloud-native scenarios.

  

- Implement cross-cluster Pod network unified planning leverage NRI POC, which creating a dedicated global network across multiple clusters for cross-cluster service discovery and interconnection. 

  

- Support storage team in validating the integration of Ceph with VMware through NVMe over TCP, as well as conducting research on VMware storage APIs to explore the feasibility of IPU acceleration.



## 2023 Q3  Insight	



### Results Feedback

Complete testing of real workloads (eg. Kafka, Rocksdb) on BOA according to customer needs, and provide test process documentation for Pinterest.

Supports BOA release 4.0 version release, including Grafana Dashboard, SDL in Jenkins, and aggregator performance test. 

Investigated and dived into VAAI and VAAS, verified the NVMe over TCP Multi Path by CephGateway, and did a report about IBM's effort on SPDK for VMware. 

Ramp up with IPDK, SPDK, and NVMe over TCP, and investigate the Qos of IPDK. Conduct a performance test of MEV IPU to compare NVIDIA's solution.

Design the workflow about how to create a dedicated parallel network based on NRI and mcs leverage the Clusternet. and finished the POC of with the Clusternet team in Tencent Cloud.

### Results Coaching

Contribute to BOA resource boundary solution release on real-workload test and deployment environment maintenance.

Propagate NRI in the ClusterNet project in OpenCloudOS 

Support the storage team to promote the implementation of IPU in storage scenarios

### Behaviors Feedback

Customer First: Conducted the real-workload performance test on the BOA project according to customer needs, which provided data support for the implementation of the BOA.

One Intel: Supported storage team conducted the MEV IPU storage performance test in different modes, and got a convincing result to guide the next step on IPU-related work.

### Behaviors Coaching

Quality: Qiang should continuously improve his software design skills to deliver high-quality products.

Learning: Qiang should continuously learn intel platform features, and cloud-native related technologies and innovate on optimizing cloud-native solutions through intel technologies



## 2023 Q4 OKR



基于实际通过负载(eg. Kafka)完成BOA 网络管理的E2E测试.

探索IPU通过CSI与K8s集成的可能性，以及可落地的方案。进一步但探索基于IPU的IO带宽细粒度控制方案。

支持存储组， 对比NVIDIA的方案，进行不同场景的的性能测试，探索MEV  IPU在存储加速领域的可落地方案。

Clusternet： 基于POC验证，展开各个组件的开发工作。进一步解耦Dedicated CNI对Origin Kubernetes的依赖，逐步实现Tunnel的自动化创建。



- Conduct E2E testing of the BOA network isolation feature based on actual loads (eg. Kafka, iperf).
- Explore the possibility of IPU integrating with K8s through CSI, as well as possible landing solutions.  Finish the design of fine-grained control schemes for IO bandwidth based on IPU.

- Support storage groups, compare NVIDIA’s solution, conduct performance tests in different scenarios, explore the possible landing solutions of MEV IPU in the storage acceleration field.
- OpenCloudOS:  Based on POC verification to expand the development work of each component. Gradually decouple Dedicated CNI’s dependence on Origin Kubernetes and realize automatic creation of Tunnel.



## 2023 Q4  Insight	



### Results Feedback

Complete testing of real workloads (eg. Kafka, Rocksdb) on BOA according to customer needs, and provide test process documentation for Pinterest.

Supports BOA release 4.0 version release, including Grafana Dashboard, SDL in Jenkins, and aggregator performance test. 

Investigated and dived into VAAI and VAAS, verified the NVMe over TCP Multi Path by CephGateway, and did a report about IBM's effort on SPDK for VMware. 

Ramp up with IPDK, SPDK, and NVMe over TCP, and investigate the Qos of IPDK. Conduct a performance test of MEV IPU to compare NVIDIA's solution.

Design the workflow about how to create a dedicated parallel network based on NRI and mcs leverage the Clusternet. and finished the POC of with the Clusternet team in Tencent Cloud.

### Results Coaching

Contribute to BOA resource boundary solution release on real-workload test and deployment environment maintenance.

Propagate NRI in the ClusterNet project in OpenCloudOS 

Support the storage team to promote the implementation of IPU in storage scenarios

### Behaviors Feedback

Customer First: Conducted the real-workload performance test on the BOA project according to customer needs, which provided data support for the implementation of the BOA.

One Intel: Supported storage team conducted the MEV IPU storage performance test in different modes, and got a convincing result to guide the next step on IPU-related work.

### Behaviors Coaching

Quality: Qiang should continuously improve his software design skills to deliver high-quality products.

Learning: Qiang should continuously learn intel platform features, and cloud-native related technologies and innovate on optimizing cloud-native solutions through intel technologies



















