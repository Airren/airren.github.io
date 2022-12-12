---
title: 「Go」并发实现
subtitle: '使用通信代替共享变量'
author: Airren
catalog: true
weight: 3
header-img: ''
tags:
  - Go
p: go_basic/go_concurrency
date: 2020-08-05 00:14:06
---



> 如何用go实现一个简单的并行任务



## Golang并发



```go
package main

import (
	"fmt"
	"sync"
)

var wg sync.WaitGroup

func printer(ch chan int) {
	for i := range ch {
		fmt.Printf("Received %d ", i)
	}
	wg.Done()   //
}

// main is the entry point for the program.
func main() {
	c := make(chan int)
	go printer(c)
	wg.Add(1)

	// Send 10 integers on the channel.
	for i := 1; i <= 10; i++ {
		c <- i
	}

	close(c)
	wg.Wait()
}
```

正常情况下goroutine的结束过程是不可控制的。

存在这么一种情况，主程序已经结束了，但是主程序中新激活的goroutine并没有运行完。主程序一旦结束，主程序中的goroutine也就结束了。

```go
import (
	"fmt"
	"time"
)

func main() {

	go func() {
		for index := 0; index < 10; index++ {
			fmt.Print(index, " ")
			time.Sleep(time.Second * 1)
		}
	}()

	fmt.Println("end")
}
// 运行结果为
// end
```

匿名函数中的代码并没有运行,使用`sync.WaitGroup`修改代码如下

```go
package main

import (
	"fmt"
	"sync"
	"time"
)

var wg sync.WaitGroup

func main() {
	wg.Add(1)   // 表示等待一个 goroutine运行结束
	go func() {
		for index := 0; index < 10; index++ {
			fmt.Print(index, " ")
			time.Sleep(time.Second*1)
		}
		wg.Done()
	}()

	wg.Wait()
	fmt.Println("end")
}
// 运行结果为
// 0 1 2 3 4 5 6 7 8 9 end
```

当`wg.Add(1)`，且有两个执行速度不一样的线程时候

```go
func main() {
	wg.Add(1)
	go func() {
		for index := 0; index < 10; index++ {
			fmt.Print(index, " ")
			time.Sleep(time.Second*1)
		}
		wg.Done()
	}()

	go func() {
		for index := 0; index < 10; index++ {
			fmt.Print(index, "+ ")
			time.Sleep(time.Second*10)
		}
		wg.Done()
	}()

	wg.Wait()
	fmt.Println("end")
}
// 运行结果为
// 0+ 0 1 2 3 4 5 6 7 8 9 1+ end
```

`sycc.WaitGroup` 有三个方法

`Add()`用来设置或者添加需要等待完成的goroutine的数量， `Add(2)` 或者两次调用`Add(1)`表示要等待两个goroutine完成

`Done()`在goroutine真正完成之前调用本方法来表示goroutine已经完成了，会对等待计数器的值减一。  `Done()`的数目一定要与`Add()`的数量一致,否则会造成永久阻塞而出现死锁。`fatal error: all goroutines are asleep - deadlock!`

`Wait()` 在等待计数器减为0之前，Wait() 会一直阻塞当前的goroutine。



## 简单的协程池

开辟固定数量的协程池处理任务，通过`chan` 传递Task。

```go
package main

import (
	"fmt"
	"sync"
	"sync/atomic"
	"time"
)

var (
	count int64
	wg    = sync.WaitGroup{}
	ch    = make(chan int)  // no buffer chan need consume first
)

func main() {

	// total task N
	const N = 1000

	// build a pool with 5 goroutine, every goroutine deal the data form the chan
	for i := 0; i < 5; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			for val := range ch {
				count2 := atomic.AddInt64(&count, 1)
				<-time.After(100 * time.Millisecond)
				fmt.Printf("%vvar: %v\n", count2, val) // count2 is a new local variable
			}
		}()
	}

	fmt.Println(">>> Start Work")
	startAt := time.Now()

	// task producer
	for i := 0; i < N; i++ {
		ch <- i
	}

	close(ch) // need to close the chan first, or lead to deadlock due to all goroutines are asleep.
	wg.Wait()

	fmt.Println(">>> Finished")
	totalTime := time.Now().Sub(startAt)
	fmt.Printf("Total time is %v, total task: %v, avg time: %v", totalTime, count, totalTime/time.Duration(N))
}
```

```
>>> Finished
Total time is 20.359523649s, total task: 1000, avg time: 20.359523ms
```



如果数据处理的量远远小于开辟的协程池的数量无疑是资源的浪费，但是每次都复用协程，减少了协程创建和销毁的开销

## 动态协程池

当每一个任务来临的时候创建一个协程，创建的协程的最大数量由有容量的`chan` 控制

```go
package main

import (
	"fmt"
	"sync"
	"sync/atomic"
	"time"
)

var (
	count    int64
	wg       = sync.WaitGroup{}
	concurCh = make(chan bool, 5)
)

func main() {
	// total task N
	const N = 1000

	fmt.Println(">>> Start Work")
	startAt := time.Now()

	for i := 0; i < N; i++ {
		wg.Add(1)
		go func(val int) {
			defer func() {
				<-concurCh
				wg.Done()
			}()
			concurCh <- true
			count2 := atomic.AddInt64(&count, 1)
			<-time.After(100 * time.Millisecond)
			fmt.Printf("%2d var: %v\n", count2, val)
		}(i)
	}

	wg.Wait()
	defer close(concurCh)
	fmt.Println(">>> Finished")
	totalTime := time.Now().Sub(startAt)
	fmt.Printf("Total time is %v, total task: %v, avg time: %v", totalTime, count, totalTime/time.Duration(N))
}
```

```
>>> Finished
Total time is 20.392798952s, total task: 1000, avg time: 20.392798ms
```



需要不断地创建和销毁`goroutine`可能会造成额外的开销



> 实际测试下来这两种写法的性能差别不是很大。

> Goroutine 的资源开销非常小，要避免过渡的滥用。特别是在并发请求的时候，如果Goroutine设置的过多很容易把下游打挂





```go
func TestConcurrency(t *testing.T) {
	ids := make([]int, 0)
	for i := 0; i < 100; i++ {
		ids = append(ids, i)
	}

	work := func(i, j int) {
		<-time.After(1 * time.Second)
		println(i)

	}

	blchan := make(chan int, 10)

	startAt := time.Now()
	wg := sync.WaitGroup{}
	for i, v := range ids {
		wg.Add(1)
		blchan <- 1
		go func(i, j int) {
			defer wg.Done()
			work(i, j)
			<- blchan
		}(i, v)
	}
	wg.Wait()
	close(blchan)
	println(time.Now().Sub(startAt).Round(time.Second).String())
}
```

