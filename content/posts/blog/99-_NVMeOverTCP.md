---
title: 什么是NVMe Over TCP
---







NVMe 最初是为高性能直连PCIe SSD设计的，后来以NVMe over Fabirc(NVMe-oF)的形式进行了扩展，以支持机架级(rack-scale)的远程SSD池。



NVMe-oF 最初的部署选项仅限于光纤通道(Fibre Channel)和远程直接内存访问(Remote Direct Memory Access，RDMA)结构。

NVMe over TCP(NVMe/TCP) 就可以使用简单高效的TCP/IP结构将NVMe扩展到整个数据中心。



基于NVMe/TCP 协议的意味着该协议从设计之初就具有广阔的生态系统，并且支持任何操作系统和网卡(NIC)。NVMe/TCP Linux驱动原生匹配Linux内核，可以用标准的Linux网络协议栈和NIC，无需任何修改。

这种协议为超大规模的数据中心量身定制，可以在不改变底层网络基础设施的情况下实现轻松部署。



### 2. 现在的数据中心如何处理数据

#### 2.1 直连式存储架构与NVMe

NVMe协议设计的初衷是要发挥SSD的全部性能。

NVMe协议所设计的并行能力发挥SSD的全部性能。 NVMe并未使用单一队列的iSCSI模式。

NVMe在CPU子系统和存储之间可支持多达64000个队列。

SSD是使用多个并行通信信道与多个SSD存储位置相连接的并行设备。

##### DSA 模式	

多个cpu/ 多个NVMe IO队列 、 并行SSD架构

对业界而言，挑战是将SSD 从可能具有多容量福利服务器转移到具有更高基础设置利用率且不会损失DAS性能。

### 2.2 前一代基于IP的存储架构

以前，iSCSI标准是通过TCP/IP 网络连接至块存储的唯一选择。它是在世纪之交开发的，当时大多数处理器都是单核器件。 

在SCSI中，应用(Initiator)和存储(Target)之间只有一个连接。对于iSCSI，也是只有一个TCP套接字(Socket)将客户端连接至块存储服务器。

现在，数据中心的处理器都是大规模并行多线程器件。 NVMe作为SATA和SAS(串行连接SCSI)的替代者出现了。

所有早期协议的开发都基于一个串行的旋转磁盘驱动器。

非易失性存储是一种并行存储技术， 它不需要一个或多个盘片在一个或一组磁头下面旋转。使用NVMe 存储设备可以并行访问许多存储单元，且具有较低的延迟。

iSCSI仍然适用于具有低到中等存储性能要求的场景。然而，iSCSI不能满足IO密集型应用的要求，这类应用需要在大规模下实现低延迟。

### 2.3	 其他替代方案和 NVMe/TCP 解耦方案

RDMA，基于聚合以太网的RDMA(RoCE), 以及基于光纤通道的NVMe(NVMe over FC), 也是试图解决耦合问题的其他网络存储协议。

然而这些替代方案要求在两端都安装昂贵的特殊硬件，例如具备RDMA功能的NIC。此外, 安装了RDMA硬件之后，在具备RDMA功能的交换结构中配置和管理流程也是很复杂的。

RDMA确实提供了适用于某些高性能计算环境的性能，但它要求更高的成本，并且需要进行非常复杂的部署。

TCP/IP已经被证明可以在超大规模环境中可靠、高效的工作。NVMe/TCP继承了这种可靠性和效率，它可以作为RDMA的互补方案与之共存，也可以完全取代RDMA。

## 数据中心闪存解耦和NVMe TCP方案

DAS通常会有意将存储配置为过剩的状态。

通过动态分配存储资源，可以避免过度配置(over-provisioning)开销，从而大大降低总体成本。



NVMe/TCP 不会影响应用的性能。实际上，它通常会改善应用的尾部延迟，从而提升用户体验，并让云服务提供商可以在相同的基础设施上支持更多的用户。







### 适合闪存的写算法

对于读写操作而言，闪存介质的延迟都很低。但是，SSD上的闪存控制器必须持续执行垃圾收集操作，以便为即将到来的写操作提供可用空间。与硬盘驱动器的写操作可以覆盖现有数据不同，闪存驱动器只允许将数据写入以前未写入或者已擦除的闪存块中。

垃圾收集会导致“写放大”。顾名思义, SSD执行垃圾收集时，应用服务器发出的单个写操作会被进行垃圾收集的SSD控制器在实际的闪存上放大为更多的写操作。写入放大会增加闪存驱动器的损耗，这将影响它的长期使用。

此外，后台的垃圾收集会导致即将到来的IO的延迟增加，并且随着更多随机写操作被写入闪存驱动器，垃圾收集会显著增加。很大比例的IO都是随机的。





什么是librbd, 非常消耗资源

NPI pass nvme device to host

会议以及资料



Performance Result

https://zhuanlan.zhihu.com/p/535426991

Lightbits https://www.lightbitslabs.com/cn/ty-prs-lightbits-intro-cn/

### Reference

https://www.lightbitslabs.com/cn/blog/why-nvmetcp/

https://cdn2.hubspot.net/hubfs/4565714/Downloadable%20Asset_Chinese/2_Disaggregation%20with%20NVMe%20over%20TCP%20Chinese.pdf

https://ee.ofweek.com/2021-07/ART-11000-2812-30508160.html#:~:text=NVMe%20over%20TCP%EF%BC%88NVMe%EF%BC%8FTCP,%E6%89%A9%E5%B1%95%E5%88%B0%E6%95%B4%E4%B8%AA%E6%95%B0%E6%8D%AE%E4%B8%AD%E5%BF%83%E3%80%82&text=NVMe%E8%A7%84%E8%8C%83%E5%B7%B2%E7%BB%8F%E6%88%90%E4%B8%BA%E9%AB%98,%E5%92%8C%E5%A4%9A%E9%98%9F%E5%88%97%E4%BD%93%E7%B3%BB%E7%BB%93%E6%9E%84%E3%80%82



Fio test



```sh





hd=default, vdbench=/root/vcbench/, user=root, shell=ssh
hd=hd1,system=hostname   # define the VM
sd=sd1, host=hd1,lun=/dev/sda, threads=32 # define the block device
wd=wd1, sd=sd1,xfersize=4k, rdpct=100,openflags=o_direct
wd=wd2, sd=sd1,xfersize=4k, rdpct=0,openflags=o_direct
wd=wd3, sd=sd1,xfersize=4k, seekpct=seq, rdpct=0,openflags=o_direct
wd=wd4, sd=sd1,xfersize=4k, seekpct=random, rdpct=0,openflags=o_direct

rd=run1, wd=(wd1-wd4),iorate=max, elapsed=60,interval=1

./vdbench -t
./vdbench -f testfile.name





fio -filename=/dev/nvme0n1 \
-iodepth=128 \
-thread \
-rw=write \
-fdatasync=0 \
-direct=1 -bs=4k,4k \
-size=10g  -numjobs=1 \
-time_based=1 -group_reporting \
-name=test-1 --eta-interval=1 --eta-newline=1 --eta=always



fio -filename=/tmp/file1 \
-iodepth=128 \
-thread \
-rw=write \
-fdatasync=0 \
-direct=1 -bs=32k,32k \
-size=10g  -numjobs=1 \
-time_based=1 -group_reporting \
-name=test-1 --eta-interval=1 --eta-newline=1 --eta=always



fio -filename=/tmp/test \

-iodepth=128 \
-thread \
-rw=write \
-fdatasync=0 \
-direct=1 -bs=4k,4k \
-size=10g  -numjobs=1 \
-time_based=1 -group_reporting \
-name=test-1 --eta-interval=1 --eta-newline=1 --eta=always



 fio --filename=/tmp/test \
 --direct=0 \
 --runtime=300 
 --size=1g \
 --rate=1m,83m \
 --ioengine=sync \
 --rw=randwrite \
 --bs=4k,4k \
 --iodepth=1 
 --thread 
 --numjobs=1 
 --time_based 
 --group_reporting 
 --name=test-1 
 --eta-interval=1 \
 --eta-newline=1 \
 --eta=always; time sleep 300s
```





VDbench 

https://www.cnblogs.com/luxf0/p/13321077.html







Dell NVME Performance test

https://www.delltechnologies.com/asset/en-us/products/storage/industry-market/h18892-nvme-transport-performance-comparison.pdf

