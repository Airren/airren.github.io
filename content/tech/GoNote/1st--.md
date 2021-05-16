



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

