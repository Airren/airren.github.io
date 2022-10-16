





# ESXI configuration and usage

![image-20220926132625596](CRE-Share/image-20220926132625596.png)



**ESXI admin interface : [https://10.239.241.100/](https://10.239.241.100/)**

```sh
user: root    passwd: CREshare-
```

**跳板机CRE-relay**

```sh
ssh cre@cre-relay.sh.intel.com # 10.239.241.116
user: cre passwd: 123456
```



## How to create a VM with SGX and NIC direct mode

### 1.select a creation type

![image-20220926133416803](CRE-Share/image-20220926133416803.png)

### 2.Select a name and guest OS

![image-20220926133734735](CRE-Share/image-20220926133734735.png)

### 3.Select storage

![image-20220926133940019](CRE-Share/image-20220926133940019.png)

### 4.Customize settings, CPU/Mem/Disk/SGX/PCI device, and select an OS image

#### SGX enable

![image-20220926134207310](CRE-Share/image-20220926134207310.png)

#### Add a PCI device 

![image-20220926134445193](CRE-Share/image-20220926134445193.png)

![image-20220926134710056](CRE-Share/image-20220926134710056.png)

#### Select an OS Image

![image-20220926135659729](CRE-Share/image-20220926135659729.png)

### 5.Start  your machine and check the status

![image-20220926143320020](CRE-Share/image-20220926143320020.png)

