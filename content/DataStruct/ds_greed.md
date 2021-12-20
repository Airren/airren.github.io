---
title: 「算法」贪心算法

---



## 例题

【简单】n个活动时间，选择可以参与最多的活动

> 优先选择结束最早的活动



```go
package main

import (
	"fmt"
	"sort"
)

type node struct {
	startAt int
	endAt   int
}

var (
	total    int
	now      int
	res      int
	nodeList = make([]node, 0)
)

func main() {
	_, _ = fmt.Scanf("%d", &total)
	nodeList = make([]node, total)
	for i := 0; i < total; i++ {
		_, _ = fmt.Scanf("%d", &nodeList[i].startAt)
		_, _ = fmt.Scanf("%d", &nodeList[i].endAt)
	}

	sort.Slice(nodeList, func(i, j int) bool {
		return nodeList[i].endAt < nodeList[j].endAt
	})

	now = 0
	res = 1
	for i := 1; i < total; i++ {
		if nodeList[now].endAt <= nodeList[i].startAt {
			now = i
			res++
			fmt.Printf("%v\n", nodeList[i])
		}
	}

	fmt.Printf("%v", res)
}

/* Input
4
1 3
4 6
2 5
1 7

*/
```



[简单]种树问题，居民要求一定

