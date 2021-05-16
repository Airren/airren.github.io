---
title: '「TSDB」术语'
subtitle: ''
author: Airren
catalog: true
header-img: ''
tags:
  - TSDB
p: tsdb/tsdb_argot
date: 2020-08-10 00:24:13
---

## RRD(Round Robin Database)

RRD 数据库在创建的时候就已经定义好了大小，当存储空间满了之后，又从头开始覆盖旧的数据，适用于存储和时间序列相关的数据。RRD的大小可控，且不用维护。

> A specialized storage system known as a Round Robin Database allows one to store large amounts of series information such as temperatures, network bandwidth, and stock prices with a constant disk footprint. It does this by taking advantage of changing needs for precision.  As we will see later, the "round-robin " part comes from the basic data structure used to store data points: circular lists.



**参考资料：**

[https://jawnsy.wordpress.com/2010/01/08/round-robin-databases/#:~:text=A%20specialized%20storage%20system%20known,of%20changing%20needs%20for%20precision.](https://jawnsy.wordpress.com/2010/01/08/round-robin-databases/#:~:text=A specialized storage system known,of changing needs for precision.)