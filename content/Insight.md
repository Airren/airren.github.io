



# Q1 2022

## Insight

- The SDEWAN  E2E demo installs and verifies process is simplified, shortened to about 40 minutes, and improved efficiency by optimizing the automatic deployment and test scripts. Help the Smart Edge team integrate SDEWAN with DEK, prepare guides, answer questions, and do hands-on debugging.

- Verified the POC in Ubuntu that StrongsWAN integrates with CTK through PKCS#11, which put the private key in -the SGX enclave to protect it. And propose ubuntu-SGX container as a sidecar work with CNF to provide the security IPsec Tunnel connection.

- Challenge: While building SGX SDK, CTK, and strongsWAN in Ubuntu encountered some barriers with C project toolchains. Finally, remove most of them. But while building OpenWRT with SGX, most of the compile seems complicated to solve, so choose another way instead of making that.



One Intel:

One Intel: Qiang proactivity helps Smart Edge team to integrate SDEWAN with DEK, prepare guides, answer questions, do hands-on debugging and always solve issues in time.

Fearless:  Qiang always keeps curiosity for the unknown region can complete the work with challenges. Qiang successfully complete the  POC strongsWAN integrated with CTK in ubuntu overcoming a lot of difficulties.

## OKR

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



## Q2 2022

###  Insight

### OKR

**OBJ1: Enable IA optimizations and innovations in open-source Edge Orchestration projects** 

KR1: Integrate and ship Nodus and SDEWAN 22.09 with SmartEdge-O release

KR2: Enhance SGX  E2E flow with Cert Manager(Useing SGX as externel issuer) support SDEWAN 22.09 release.

KR3: SDEWAN auto CICD process, optimize the version manager, and split the code repo



**OBJ2: Build Intel leadership in open-source Edge Orchestration projects with focus on individual career growth**

KR3: Deliver 1 technical submission on edge usages with SDEWAN (KubeCon China)

KR4: v












