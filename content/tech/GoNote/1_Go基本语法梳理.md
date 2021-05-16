# 基本语法梳理

## 源文件

原文件使用UTF-8编码，对Unicode支持良好。每个源文件都属于包的一部分，在文件头部用package声明所属包的名称

```go
package main
func main(){
  println("hello world!!")
}
```

以`.go`作为文件扩展名，语句结束分号会被默认省略，支持C样式注释。<font color=green>入口函数main没有参数，且必须放在main包中</font>

用import导入标准库或第三方包

```go
package main
import{
  "fmt"
}

func main(){
  fmt.Println("hello world!")
}
```

可以直接运行或者编译为可执行文件

## 变量

使用var定义变量，支持类型推断。基础数据类型划分清晰明确，有助于编写跨平台应用。编译器确保变量总是被初始化为0，避免出现意外状况。

```go
package main
func main(){
  var x int32
  var s="hello world!"
  // 两个数据之间默认使用空格隔开
  println(x,s)
}
```

在**函数内部**，还可以省略var关键字，使用更简单的定义模式。

```go
package main

//y := 200   // 该声明方式仅在函数内部使用，不可用来声明全局变量
func main() {
	x := 100
	println(x)
}

```

> 编译器将未使用的局部变量定义当做错误

## 表达式

Go仅有三种流控制语句

### if

```go
package main

func main() {
	x := 100
	if x > 0 {
		print("x")
	} else if x < 0 {
		print("-x")
	} else {
		print("0")
	}
}
```

### switch

```go
package main

func main() {
	x := 0
	switch {
	case x > 0:
		print("x")
	case x < 0:
		print("-x")
	default:
		print("0")
	}
}
```

### for

```go
func main() {
	for i := 0; i < 5; i++ {
		println(i)
	}
	for i := 4; i >= 0; i-- {
		println(i)
	}
}
```



```go
package main

func main() {
	x := 0
  for x < 5 {   // 相当于 while(x<5)
		println(x)    
		x++
	}
}
```



```go
package main

func main() {
	x := 4
	for { // 相当于while(true)
		println(x)
		x--
		if x < 0 {
			break
		}
	}
}
```

在迭代遍历时，for...range除了元素外，还可以返回索引

```go
package main

func main() {
	x := []int{100, 101, 102}
	for i, n := range x {
		println(i, ": ", n)
	}
}
```



## 函数

#### 多个返回值

函数可以定义多个返回值，甚至对其命名

```go
package main

import (
	"errors"
	"fmt"
)

func main() {
	a, b := 10, 0  // 定义多个变量
	c, err := div(a, b) // 接收多个返回值
	fmt.Println(c, err)
}

func div(a, b int) (int, error) {
	if b == 0 {
		return 0, errors.New("division by zero")
	}

	return a / b, nil
}
```

#### 返回函数

函数是第一类型，可以作为参数或者返回值

```go
package main

func main() {
	x := 100
	f := test(x);
	f()
}

func test(x int) func() { // 返回函数类型
	return func() {         // 匿名函数
		println(x)            // 闭包
	}
}
```

用defer定义延迟调用，无论函数是否出错，它都确保结束前被调用

```go
package main

func main() {
	test(10, 0)
}

func test(a, b int) {
	defer println("dispose....") // 常用来释放资源、解除锁定、或执行一些清理操作
										           // 可以定义多个defer，按照FILO顺序执行
	println(a / b)                      
}
```



## 数据

#### 切片 slice

切片(slice)可以实现类似动态数组的功能

```go
package main

import "fmt"

func main() {
	x := make([]int, 0, 5)   // 创建容量为5的切片
	for i := 0; i < 8; i++ {
		x = append(x, i)    // 追加数据。当超出容量限制时候，自动分配更大的存储空间
	}
	fmt.Println(x)
}
```

#### 字典 map

字典(map) 类型内置，可以直接从运行层面获得性能优化

```go
package main

import "fmt"

func main() {
	m := make(map[string]int) // 创建字典类型对象
	m["a"] = 999              // 添加或设置
	x, ok := m["a"]           // 使用ok-idiom获取值，可知道key/value是否存在
	fmt.Println(x, ok)
	delete(m, "a")       // 删除
}

// 999 true
// 0 false
```

> 所谓 ok-idiom模式，是指在多返回值中用一个名为ok的布尔值来表示操作是否成功。因为很多操作默认返回零，所以额外说明

#### 结构体

结构体(struct)可以匿名嵌入其他类型

```go
package main

import "fmt"

func main() {
	var m manager
	m.name = "Tom"
	m.age = 29
	m.title = "CTO"   // 直接访问匿名字段成员

	fmt.Println(m)
}

type user struct {    // 结构体类型
	name string
	age  byte
}

type manager struct {
	user             // 匿名嵌入其他类型
	title string
}

// {{Tom 29} CTO}
```

## 方法

#### 类型的方法

可以为当前包内的任意类型定义方法

```go
package main

type X int

func (x *X) inc() { // 名称前的参数称作receiver,作用类似Python self
	*x++
}

func main() {
	var x X
	x.inc()
	println(x)
}
```

#### 继承

还可以直接调用匿名字段的方法，这种方式可实现与继承类似的功能

```go
package main

import "fmt"

type user struct {
	name string
	age  byte
}

func (u user) ToString() string {
	return fmt.Sprintf("%+v", u)
}

type manager struct {
	user
	title string
}

func main() {
	var m manager
	m.name = "Tom"
	m.age = 29
	println(m.ToString()) // 调用user.ToString
}
```

## 接口

结构采用了`duck type`方式, 也就是无须在实现类型上添加显式声明

```go
package main

import "fmt"

type user struct {
	name string
	age  byte
}

func (u user) Print() {
	fmt.Printf("%+v\n", u)
}

type Printer interface { // 接口类型
	Print()
}

func main() {
	var u user
	u.name = "Tom"
	u.age = 29

	var p Printer = u   // 只要包含接口所需的全部方法，即表示实现了该接口
	p.Print()
}
```

另有`空接口`类型interface{}, 用途类似OPP里的system.Object,可以接收任意类型的对象



## 并发

整个运行时完全并发设计。凡你能看到的，几乎都在以`goroutine`方式运行。这是一种比普通协程或线程更加高效的并发设计，能轻松创建和运行成千上万的并发任务。

```go
package main

import (
	"fmt"
	"time"
)

func task(id int) {
	for i := 0; i < 5; i++ {
		fmt.Printf("%d: %d\n", id, i)
		time.Sleep(time.Second)
	}
}

func main() {
	go task(1) // 创建 goroutine
	go task(2)

	time.Sleep(time.Second * 6)
}
```

通道(channel) 与goroutine搭配，实现通信代替共享内存的CSP模型

```go
package main

import "time"

// 消费者
func consumer(data chan int, done chan bool) {
	for x := range data { // 接收数据，直到通道被关闭
		println("recv: ", x)
	}
	done <- true // 通知main， 消费结束
}

// 生产者
func producer(data chan int) {
	for i := 0; i < 4; i++ {
		data <- i // 发送数据
		println("send: ",i)
		time.Sleep(time.Second)
	}
	close(data) // 生产结束，关闭通道
}

func main() {
	done := make(chan bool) // 用于接收消费结束信号
	data := make(chan int)  // 数据管道

	go consumer(data, done) // 启动消费者
	go producer(data)       // 启动生产者

	<-done // 阻塞，直到消费者发回结束信号
}

```

