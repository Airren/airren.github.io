---
title: 「Go」context
subtitle: 'Go上下文 context'
author: Airren
catalog: true
Weight: 4
header-img: ''
tags:
  - Go
p: go_basic/go_context
date: 2020-08-11 23:53:48
---





`context.Context`用来设置截止日期、同步信号，传递请求相关值的结构体。上下文与`Goroutine`有非常密切的关系。

```go
type Context interface{
  Deadline()(deadline time.Time, ok bool)
  Done() <-chan struct{}
  Err() error
  Value(key interface{}) interface{}
}
```

`context.Context`有四个方法：

- `Deadline()` 返回`context.Context`被取消时间，也就是完成工作的截止日期；

- `Done() `          返回一个channel，这个channel 会在当前工作完成或者上下文被取消后关闭，多次调用Done方法返回的是同一个channel；

- `Err()`    返回`context.Context` 结束的原因，它只会在`Done`返回的`Channel`被关闭时才会返回非空的值
- 如果 context.Context 被取消，会返回Canceled错误；
  
- 如果 context.Context 超时，会返回DeadlineExceeded错误；
  
- Value 从context.Context 中获取键对应的值。对同一个上下文来说，多次调用value并传入相同的key会返回相同的结果，该方法用来传递请求特定的数据。





```go
func main() {
	ctx := context.Background() // new empty context

	ctx = context.WithValue(ctx, "org", "ali")
	ctx, _ = context.WithCancel(ctx)
	ctx, _ = context.WithDeadline(ctx, time.Now().Add(10*time.Second))
	ctx, _ = context.WithTimeout(ctx, time.Second)

}
```













参考文档：

https://draveness.me/golang/docs/part3-runtime/ch06-concurrency/golang-context/