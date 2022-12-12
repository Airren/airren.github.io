---
title: 「Go」pprof
subtitle: ''
author: Airren
date: 2020-08-06 00:54:12
---







## Test

### Test

```sh
go test --bench ./ 5s
```



### Code coverage

```sh
go test -cover
```



### Profile

引发性能问题的原因，=执行时间过长、内存占用过多，以及意外堵塞。

1. 在测试时保存并输出相关数据，进行初次评估
2. 在运行阶段通过web接口获得实时数据，分析一段时间内的健康状况



```sh
go test -run NONE -bench . -memprofile mem.out -cpuprofile cpu.out net/http
```



服务开启debug 端口

 ```sh
go tool pprof http://10.152.50.69:9452/debug/pprof/profile\?second\=60
 ```

-alloc_space











pprof 常用指令



Web