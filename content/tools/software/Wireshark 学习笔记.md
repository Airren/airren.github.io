#  Wireshark 学习笔记

## WireShark的前世今生

Gerald Combs

Wireshark 是目前世界上应用最广泛的抓包工具

WIreshark 可以提供分析报告



wireshark will always be opensource

## WireShark的概述与安装

### 网络监听软件原理

#### 网络监听软件是一种监视网络状态、数据流程以及网络上信息传输的管理工具。

- 可以将网络接口设定成监听模式(混杂模式)
- 可以截获网络上所传输的信息

#### 双刃剑--安全问题

![image-20191127170602832](/Users/airren/Dropbox/JavaNote/img/image-20191127170602832.png)

<img src="/Users/airren/Dropbox/JavaNote/img/image-20191127170611690.png" alt="image-20191127170611690" style="zoom:33%;" />

#### 防止窃听的方法：加密

![image-20191127170724679](/Users/airren/Dropbox/JavaNote/img/image-20191127170724679.png)



### 主要网络监听软件

常见的网络监听软件

- Wireshark
- Tcpdump
- Microsoft Network Monitor
- Kisnet 
- Fiddler

### Wireshark概述

#### 功能

- 支持unix、Linux、windows、mac平台
- 在接口实时捕捉包
- 能详细显示包的详细协议信息
- 可以打开/保存捕捉的包
- 可以导入导出其他捕捉程序支持的包数据格式
- 可以通过多种方式过滤包
- 多种方式查找包
- 通过过滤以多种色彩显示包
- 创建多种统计分析

#### 不能做的事情

- 不是入侵检测 ARP欺骗，但是可以观察网络发生的事情
- 不会处理网络事务 不会进行包的修改

### Wireshark下载与安装

Windows： Winpcap + Wireshark

Linux： libpacp + Wireshark

## WireShark的基本使用

#### 界面与基本操作

示例协议包 

![image-20191128121321927](/Users/airren/Dropbox/JavaNote/img/image-20191128121321927.png)



#### 捕获与保存

#### 过滤与过滤表达式

<img src="/Users/airren/Dropbox/JavaNote/img/image-20191128121529271.png" alt="image-20191128121529271" style="zoom: 33%;" />

#### 过滤表达式

协议过滤

- tap 只显示tcp协议

IP地址过滤

- ip.src == 192.168.1.1源地址
- ip.dst == 192.168.1.1 目标地址
- ip.addr = 192.168.1.1 

端口过滤

- tcp.port == 80 显示tcp端口为80
- tcp.srcport == 80 只显示tcp协议的源端口为80的

http模式过滤

- http.request.method == "GET"  只显示HTTP GET 方法

逻辑运算

- ip.src == 192.168.1.1 or ip.src == 192.168.1.2

#### 包列表与详细信息

![image-20191128122904377](/Users/airren/Dropbox/JavaNote/img/image-20191128122904377.png)

## 实验： 常见协议分析

### ARP

![image-20191128123034237](/Users/airren/Dropbox/JavaNote/img/image-20191128123034237.png)

数据链路层， 以太网层

Frame：

Ethernet

### ICMP的echo

<img src="/Users/airren/Dropbox/JavaNote/img/image-20191128174237695.png" alt="image-20191128174237695" style="zoom: 33%;" />

### DNS

![image-20191129134735762](/Users/airren/Dropbox/JavaNote/img/image-20191129134735762.png)

### TCP三次握手

每次握手时发送的数据是TCP报文

TCP段包含了源、目的地址、端口号、初始序号、滑动窗口大小、窗口扩大因子、最大报文长度等。还有一些标志位

SYN - 同步序号

ACK - 应答序号

RST - 复位连接，消除旧有的同步序号

PSH - 尽可能的将数据送往接收进程

FIN - 发送方完成数据发送

URG

![image-20191129143254241](/Users/airren/Dropbox/JavaNote/img/image-20191129143254241.png)

### HTTP

### Telnet