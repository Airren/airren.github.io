---
title: 「Go」slice
subtitle: ''
author: Airren
catalog: true
header-img: ''
tags:
  - null
p: go_basic/go_slice
date: 2020-09-02 11:40:22
---



### Slice的坑

```go
numList := make([]int, 10) // 产生的numList会存入10个0， 如果继续append 数据会导致numList的数据超过10
```

