---
title: "linux Sotrage"
date: 2022-12-14T15:47:59+08:00
draft: true
---



## Purpose

1. 代码中更新status的地方，更新 metric数值
2. host 上采集 不同block size 的平均速率





**监控面板**

1.  GA pool  每个node节点-> 每个磁盘资源池的变化( @zhuli)
2. Host 磁盘 Throughput MB/s 的变化。         不同bio的大小下的利用率(  blo_size)
3.  Application （ Container ） Throughput MB/s 的磁盘变化率



Ebpf - > pid , cgroup id 

​            pid -> container   n:1

​           Cgroup id -> container  1:1







- 指定pod(or container)的 bandwith不同blocksize的bandwidth
  - 数据基础：不同blocksize的 count
  - 



bosun query

```sh

```





不同进程 PID， 不同Cgroup，



> 磁盘写的速度与IOPS 以及biosize 的关系，
>
> 平均磁盘速写速率的计算
>
> Blkio 读写磁盘和读写buffer





## Disk性能指标

### HDD磁盘性能

影响HDD磁盘的关键因素是磁盘服务时间，即HDD磁盘完成一次I/O请求所花的时间，它由寻道时间、旋转延迟和数据传输时间三部分构成。

寻道时间，Tseek是将磁头移动到正确磁道上所需要的时间。寻道时间越短，IO操作越快，目前磁盘的寻道时间一般在3-15ms。

旋转延迟，Trotation是指盘片旋转，将请求数据所在的扇区移动到读写磁盘下方所要的时间。旋转延迟取决于磁盘转速，通常用磁盘旋转一周所需时间的1/2表示。 例如： 7200 rpm的磁盘平均旋转延迟大约为60*1000/7200/2=4.7ms, 而转速为15000rpm的磁盘其平均旋转延迟为2ms。

数据传输时间，Ttransfer是指完成传输所请求的数据需要的时间，它取决于数据传输率，其值等于数据大小除以数据传输率。目前IDE/ATA能达到133MB/s, SATA II 可以达到300MB/s的数据传输率，数据传输时间通常远小于前面两部分消耗的时间。 简单计算时可以忽略。

- IOPS
- Throughput

#### IOPS

IOPS(Input/Output per second) `每秒的读写次数`，即每秒系统能处理的IO请求数量。随机读写频繁的应用，如小文件存储等，关注随机读写性能，IOPS是关键衡量指标。IOPS = 1000/(Tseek+Trotation+Ttransger),  如果忽略数据传输时间，理论上可以计算出随机读写最大的IOPS。常见的HDD磁盘随机读写最大的IOPS为：

> 7200 rpm -> 76 IOPS
>
> 10000 rmp -> 111 IOPS
>
> 15000 rmp -> 166 IOPS

#### Throughput

Throughput，吞吐量，单位时间内可以成功传输的数据量,`每秒IO请求总的大小`。顺序读写频繁的应用，如视频点播，关注连续读写性能、数据吞吐量是关键衡量指标。吞吐率主要取决于磁盘阵列的架构，通道的大小以及磁盘的个数。不同的磁盘阵列存在不同的架构，但他们都有自己内部的带宽，一般情况下，内部带宽都设计足够充足，不会存在瓶颈。磁盘阵列与服务器之间的数据通道对吞吐量影响很大，比如一个2Gbps的光纤通道，其所支持的最大流量仅为250MB/s。当前面的瓶颈不存在时，硬盘越多的情况下，吞吐量越大。

> HDD的连续读写性能很好，但是随机读写的性能很差，这主要是因为磁头移动到正确的磁道上需要时间。随机读写时，磁头需要不停地移动，时间都浪费在了磁头寻址上，所以性能不高。

#### 响应时间

访问延迟，从发起IO请求到存储系统把IO请求处理完成的时间间隔。

#### 其他指标

diskbusy: 有IO请求的时间，占总时间的百分比



## SSD NAND Flash

Cell, Page, Block

一个Page包含很多的Cell, 典型的Page大小是4k, `Page 也是要读写的最小存储单元`。 SSD上没有“重写”的操作，不像HDD可以对任何字节重写覆盖。一个Page一旦写入内容之后就不能进行部分重写，必须和其他相邻page一致被整体擦除重置。

多个Page组合成Block。一个Block典型的大小是512K或1MB，也就是大约128 或者 256 个Page。Block是擦除的基本单位，`每次擦除都是整个Block内的所有Page都被重置`。



**写入放大**(Write Amplification, or WA), 这是SSD相对于HDD的一个缺点，即实际写入SSD的物理数据量，有可能是应用层写入数据量的多倍。一方面，Page级别的写入需要移动已有的数据来腾空Page。另一方面，GC操作也会移动用户数据来进行Block级别的擦除。所以对SSD真正的写操作的数据可能比实际写的数据量大，这就是写入放大。 一块SSD只能进行有限的擦除次数，也称为编程/PE周期，所以写入放大会缩短SSD的寿命。

耗损平衡 (Wear Leveling) 。对每一个块而言，一旦达到最大数量，该块就会死亡。对于 SLC 块，P/E 周期的典型数目是十万次；对于 MLC 块，P/E 周期的数目是一万；而对于 TLC 块，则可能是几千。为了确保 SSD 的容量和性能，我们需要在擦除次数上保持平衡，SSD 控制器具有这种“耗损平衡”机制可以实现这一目标。在损耗平衡期间，数据在各个块之间移动，以实现均衡的损耗，这种机制也会对前面讲的写入放大推波助澜。

> SSD 友好程序设计
>
> - 避免就地更新，可以写到一个新的block中，避免擦除
> - 冷热数据分离
> - 避免长而繁重的写入
> - 避免SSD存储太满



HDD vs SSD

| Type      | Random 4K IO latency | IO Throughput | Random IOPS |
| --------- | -------------------- | ------------- | ----------- |
| HDD       | 8ms                  | 150MB/s       | 120         |
| SSD(NVMe) | 10us                 | 4GB/s         | 400K        |



## 操作系统层的优化



## fio测试								

fio可以直接压测裸盘，如果磁盘上有数据或者是系统盘，`使用fio直接压测会破坏磁盘的分区表`??? not sure。

```sh
# sequential read
fio -name iops -rw=read      -bs=4k -runtime=60 -iodepth 32 -filename /dev/sdb1 -ioengine libaio -direct=1 -timeout 5
# rand read
fio -name iops -rw=randread  -bs=4k -runtime=60 -iodepth 32 -filename /dev/sdb1 -ioengine libaio -direct=1 -timeout 5
# sequential write
fio -name iops -rw=write     -bs=4k -runtime=60 -iodepth 32 -filename /dev/sdb1 -ioengine libaio -direct=1 -timeout 5
# randwrite // need support direct=1/buffered=0
fio -name iops -rw=randwrite -bs=4k -runtime=60 -iodepth 32 -filename /dev/sda6 -ioengine libaio -direct=1 -timeout 5

fio -name iops -rw=randwrite -bs=4k -runtime=60 -iodepth 32 -filename /tmp/testfile-1 -ioengine libaio -direct=1 -timeout 500 -size=100G

fio -name iops -rw=randread -bs=4k -runtime=60 -iodepth 32 -filename /tmp/testfile-1 -ioengine libaio -direct=1 -timeout 500 -size=100G

# 4K random test
fio -ioengine=libaio -bs=4k -direct=1 -thread -rw=randread -size=100G -filename=/dev/vdb 
-name="EBS 4KB randread test" -iodepth=8 -runtime=60

# 512K sequential write 
fio -ioengine=libaio -bs=512k -direct=1 -thread -rw=write -size=100G -filename=/dev/vdb 
-name="EBS 512KB seqwrite test" -iodepth=64 -runtime=60


fio -name iops -rw=write     -bs=4k -runtime=60 -iodepth 32 -filename /tmp/test1 -ioengine libaio -direct=1 -timeout 5
```



## Cgroup 是如何限制IO的

cgroup 控制的是IOPS



## ebpf

https://hub.docker.com/r/hrexed/ebpf_exporter_v2/tags

```sh
# The total sum of observations for: Block IO latency histogram
ebpf_exporter_bio_latency_seconds_sum{device="nvme0n1"}
# The total number of observations for: Block IO latency histogram
ebpf_exporter_bio_latency_seconds_count{device="nvme0n1"}
# The total count of observations for a bucket in the histogram: Block IO latency histogram
ebpf_exporter_bio_latency_seconds_bucket{device="nvme0n1"}


# The total sum of observations for: Block IO size histogram with kibibyte buckets
ebpf_exporter_bio_size_bytes_sum{device="nvme0n1"}
# The total number of observations for: Block IO size histogram with kibibyte buckets
ebpf_exporter_bio_size_bytes_count{device="nvme0n1"}
# The total count of observations for a bucket in the histogram: Block IO size histogram with kibibyte buckets
ebpf_exporter_bio_size_bytes_bucket{device="nvme0n1"}
```

**Cadvisor**

```sh
# Blkio Device bytes usage
container_blkio_device_usage_total
```





### Ebpf helper function

https://man7.org/linux/man-pages/man7/bpf-helpers.7.html

### Kprobe

```sh
sudo cat /boot/System.map-5.11.0-27-generic | grep "blk_account_io_done"
```



## go-ebpf	

```sh
require github.com/iovisor/gobpf v0.2.1-0.20221005153822-16120a1bf4d4



sudo fio -filename=/dev/sda2 -iodepth=128 -thread -rw=randread -ioengine=libaio -bs=4k -size=50G -numjobs=1 -time_based=1 -runtime=300 -group_reporting -name=mytest -cgroup=test --output=./fio1.log --output-format=json -direct=1
```



## Helm chart deployment

Prometheus



Bosun for query

```sh
$val1=promrate("ebpf_exporter_bio_size_bytes_bucket", "cgroup", '''cgroup='user.slice',device="nvme0n1",operation="read",le="524288"''', "sum", "1m","1m", "1h", "0")

$val2=promrate("ebpf_exporter_bio_size_bytes_bucket", "cgroup", '''cgroup='user.slice',device="nvme0n1",operation="read",le="262144"''', "sum", "1m","1m", "1h", "0")

$val=$val1-$val2


$res=(512*$val)/1024

$res
```





Grafana bosun Query

```sh
$start


$tag_<tag>

promrate("ebpf_exporter_bio_size_bytes_bucket", "cgroup", '''cgroup='user.slice',device="nvme0n1",operation="read",le="524288"''', "sum", "1m","$ds", "$start", "0")

```





Grafana



Ebpf-exporter

- Image 
- helm chart





ebpf / bcc build

```sh
rm build -rf  
cmake -H. -Bbuild   
```



K



Based on Cloudflare ebpf



Pod 的cgroup的存储路径在哪里？  

Cadvisor 是如何采集bio的数据的， BPS？

Cloudflare 是如何采集ebpf 数据的







```sh
helm install --namespace ioi-system --create-namespace \
ebpf ./cloudflare-ebpf

helm install --namespace ioi-system --create-namespace \
ebpf-bak ./cloudflare-ebpf-bak

helm install --namespace ioi-system --create-namespace \
prome ./prometheus

helm install --namespace ioi-system --create-namespace \
bosun ./bosun


helm delete -n ioi-system ebpf
helm delete -n ioi-system prome
helm delete -n ioi-system bosun


helm upgrade --namespace ioi-system --create-namespace \
ebpf ./cloudflare-ebpf

helm upgrade --namespace ioi-system --create-namespace \
prome ./prometheus




helm install --namespace ioi-system --create-namespace \
grafana-1 ./g


helm install --namespace prome --create-namespace \
prome ./prometheus



```



`
```
1. Get your 'admin' user password by running:

   kubectl get secret --namespace ioi-system grafana-1 -o jsonpath="{.data.admin-password}" | base64 --decode ; echo


2. The Grafana server can be accessed via port 80 on the following DNS name from within your cluster:

   grafana-1.ioi-system.svc.cluster.local

   Get the Grafana URL to visit by running these commands in the same shell:
     export NODE_PORT=$(kubectl get --namespace ioi-system -o jsonpath="{.spec.ports[0].nodePort}" services grafana-1)
     export NODE_IP=$(kubectl get nodes --namespace ioi-system -o jsonpath="{.items[0].status.addresses[0].address}")
     echo http://$NODE_IP:$NODE_PORT

3. Login with the password from step 1 and the username: admin
```





## cgroup



## Reference

https://tech.meituan.com/2017/05/19/about-desk-io.html

https://juejin.cn/post/6854573209501564941





traceroute: findsaddr: write: No such process
➜  ~ traceroute 180.76.76.76
traceroute: findsaddr: write: No such process

=





