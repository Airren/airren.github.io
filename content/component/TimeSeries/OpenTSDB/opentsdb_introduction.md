---
title: 「TSDB」时序数据&时序数据库简介
subtitle: '时序数据库'
author: Airren
catalog: true
header-img: ''
tags:
  - TSDB
  - OpenTSDB
p: tsdb/opentsdb_introduction
date: 2020-08-06 00:24:42、
---

> 什么是时序数据？
>
> 时序数据的应用场景和特征？
>
> 时序数据库？

## 时序数据

时序数据，就是与时间强相关度的一系列数据。关注的是某一时刻的数据值，而不是最终的数据。是一个过程而不是一个结果。时序数据描述的是一个数据（指标）在时间维度上的变化。例如： 股票K线、环境监测。

时序数据的特征：

- 数据以一定的时间间隔产生，生产速率稳定。
- 写入多，查询少
- 时序数据不允许更新
- 时序数据主要是按时间范围查询

## 时序数据库

传统的数据库并不适合存储时序数据，针对时序数据的特征，时序数据库的基本要求如下：

- 支持高并发、高吞吐量的写入
- 支持海量数据存储
- 高可用（时序数据在互联网公司常用作报警数据源）
- 支持复杂的多维度的查询
- 易于横向扩展

[常见的时序数据库](https://db-engines.com/en/ranking/time+series+dbms) 

![image-20200806004226361](opentsdb_introduction/image-20200806004226361.png)



## 时序数据基本概念

一条时序数据是由多个DataPoint构成的。每个DataPoint包含以下几个方面

- metric： 一般也叫metric name，是时序数据的指标名

- tags: 一个或者多个tag组合，用户描述metric的不同维度。每个Tag由tagk&tagv组成。例如：一个请求的来源 host=10.20.178.23，dc=cn。tags标明数据的维度。
- value： 表示对应的数值。例如：请求的latency 或者qps等。
- timestamp： 时序数据的具体时间，可以是秒级或者毫秒级别的Unix时间戳。

例如： `JVM_Heap_Memory_Usage_MB{host=127.0.0.1, instanceId=jvm01}`



Downsampling
