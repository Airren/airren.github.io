---
title: Redis
---





>
>
> bitmap
>
>



## 背景知识





文件：

​	数据可以存在文件里，通过grep awk 查找文件。

​	如果文件变大，10M -> 1T , 查找会变慢。全量扫描IO。

数据库：（受限于IO）

​	mysql 存储， 存储分块，可以通过索引直接获取datapage中的数据。

- 索引也是数据块

- 二级索引，给索引建立索引

表很大，如果连接比较少，读 如果命中索引，查询还是毫秒级别

如果并发很大（足够大），如果每个查询的数据都是独立的，会收到吞吐的限制。



Redis+数据库：（内存与磁盘的折中方案）

- nosql -> key vale

  短域名-> 长域名，计数

  关联表的数据也放置在value中。只关注每条记录自身。

- 基于内存的
- worker 单线程
- 6.x IO threads
- value 是有类型的 string、list、set、hash、zset；且每种类型有自己的本地方法。



>数据向计算移动
>
>计算向数据移动





连接池：

- socket list

线程池：

- 可以使用一个线程去处理连接池中的连接（nio，多路复用，epoll）







内存数据库：（受限于成本）

 	Hana



https://bytedance.feishu.cn/docs/doccnwV2ZxHYiLagaPOQSZ3ldlr



> 常识：s<- ms <-us <-ns
>
> 硬盘：
>
> - 带宽、吞吐：百兆，1-2G  pci-e/ nvme 3G/s
>
> - 寻址时间 ms
>
> 内存:
>
> - 寻址时间 ns



## redis 安装

http://db-engines.com

http://redis.io

编译安装

README.md

```sh

```






