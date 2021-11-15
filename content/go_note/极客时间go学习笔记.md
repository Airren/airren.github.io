---
title: 极客时间GO学习笔记
---



软件开发的新挑战

- 多核硬件架构

- 超大规模分布式计算集群

- web模式下导致的前所未有的规模和更新速度

区块链开发语言，

Kubernetes

Docker

只有25个关键字

有垃圾回收机制，但是仍然可以直接使用指针访问内存

CSP并发机制

关键字

c 37

c++ 11 84

go 25



go 垃圾回收，使用指针直接内存访问.

复合和继承

docker kubernetes





go 默认使用静态连接，编译完成是一个独立的二进制

```go
package main // package name

import "fmt" // dependence

// functionality
func main() {
	fmt.Print("hello world \n")
}
```

应用程序入口， 

1. 必须是main包 package main
2. 必须是main方法 func main()
3. 文件名不一定是main.go

package 的名字不需要与目录保持一致



退出返回值

与其他主要编程语言的差异

- Go main函数不支持任何返回值
- 通过os.Exit 来返回状态

```go
package main

import (
	"fmt"
	"os"
)

func main() {
	fmt.Print("hello world \n")
	os.Exit(-1)
}
```



获取命令行参数

- main函数不支持传入参数

  func main(~~arg []string~~)

- 在程序中直接通过os.Args 获取命令行参数

  获取的第一个参数是文件执行的相对路径，也就是文件名

```go
package main

import (
	"fmt"
	"os"
)

func main() {
	if len(os.Args) > 1 {
		fmt.Println("hello world",os.Args[1])
	}
	os.Exit(1)
}
```



The master has failed more time than the beginner has tried.



编写测试程序

源码文件以_test 结尾，xxx_test.go

测试方法名以 Test 开头, func TestXXX( t *testing.T){...}



```sh
package test

import "testing"

func TestFirstTry(t *testing.T ){
	t.Log("hello test")
}
```

