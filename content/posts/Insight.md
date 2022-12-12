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

## Insight

- For the SDEWAN project:  Test several work modes of CNF, one is multiple interfaces that can use host-device mode or nodus direct mode to share the physical interface to CNF, and another is CNF work as a service export through NodePort mode. Support SGX HSM co-work with CNF as a sidecar and test SDEWAN pull mode through the script.

- Help the SmartEdge team finish the SASE EK release, by upgrading the auto-deploy script with the latest components.  And verified the possibility of using SDEWAN with Nodus in WAN  on AWS.

- Release a Sapphire rapids feature AMX presentation and verify the IDF proposal's feasibility that uses the AVX or AMX to accelerate the query speed of Time-Series Data.  And bring up an IDF proposal of Cloud Native HSM.

- Challenge:  While working for the SDEWAN SGX support model,  solved some tricky problems which are cross-compile the p11-kit tool for OpenWrt and changing the pkcs#11 token initialize arguments to fit CTK and squashing the HSM image with SGX to reach an ideal size.

Quality: Help the Smart Edge team release SDEWAN with the latest stable component. Ensure the stability and reliability of the auto-deploy script.

Result Driven: Even encountering many tricky problems while enabling SGX for CNF, Qiang solved the problem step by step and finally accomplish the component as the previous design architecture.

## OKR for Q2

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









做的好的，以及需要改进的，value 2-3 个

3. 
