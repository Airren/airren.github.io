---
title: "「Bosun」 Bosun入门简介"
date: 2020-11-25T00:29:08+08:00
---



# Bosun, Time Series, and OpenTSDB's DataModel

An introduction to the data model of Bosun's Primary time series backend OpenTSDB. Time Series, Metircs and Tags, Aggregation, Rate Calculation, and Downsampling are covered. These features are demoed using Bosun's graphing interface.

## Time Series 

A Series of observations , each recorded as a Time and Date with an associated Value.

```sh
Observation on 08-22-2015 10:20:01 of 10
Observation on 08-22-2015 10:20:15 of 20 
Observation on ... of ...
```



## Open TSDB

**Time Series Database**

Each Time Series in the Database in Uniquely Identified by:

- A Metric Name

- A Set of Tags

  A Tag is made of :  `A Tag Key` & `A Tag Value`

