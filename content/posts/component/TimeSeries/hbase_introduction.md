---
title: 「HBase」 简介
subtitle: 'Hbase简介'
author: Airren
catalog: true
header-img: ''
tags:
  - Hbase
p: tsdb/hbase_introduction
date: 2020-08-07 01:03:42
---





## HBase 简介

OpensTSDB支持多种底层存储，例如HBase、Cassandra。

HBase是分布式列存储系统，其底层依赖HDFS分布式文件系统。HBase是参考Google BigTable模型开发的，本质上是一个典型的KV存储，适用于海量结构化数据的存储。

HBase的优点：

- 集群部署，横向扩展方便
- 容错性高，相同的数据会复制多份，放到不同的节点上
- 同等硬件，相比传统数据库支持的数量级高
- 吞吐能力强，写入量高

不足：

- 只支持单行的事务
- 查询方式： 只能通过RowKey进行查询或者扫描
- 





> 1. HBase 和HDFS的关系？
> 2. 全面事务
> 3. 



