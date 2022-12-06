---
title: Dockerfile
---



Dockerfile 中的entrypoint 和CMD的区别

```sh
CMD executable  param1 param2  # 不用使用这种shell表示法，1 号进程为shell
CMD ["executable","param1","param2"] 
```

EntryPoint 和CMD都可以在执行的时候被覆盖。

组合使用ENTRYPOINT和CMD, ENTRYPOINT指定默认的运行命令, CMD指定默认的运行参数. 例子如下:

```dockerfile
FROM ubuntu:trusty
ENTRYPOINT ["/bin/ping","-c","3"]
CMD ["localhost"] 

```



docker 会把CMD的命令拼接到Entrypoint之后

