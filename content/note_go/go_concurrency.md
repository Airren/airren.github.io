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

