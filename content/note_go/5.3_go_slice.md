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
numList := make([]int, 10) 
// 产生的numList会存入10个0， 如果继续append 数据会导致numList的数据超过10
```







Slice 扩容

```golang
func TestSliceExtend(t *testing.T) {
	capacity := 0
	list := make([]int, 0)
	for i := 0; i < 4096; i++ {
		list = append(list, i)
		if capacity != cap(list) {
			times := float64(cap(list)) / float64(capacity)
			differ := cap(list) - capacity
			capacity = cap(list)
			if times == 2.0 {
				fmt.Printf("capacity is: %d \t times: %.2f \n", capacity, times)
			} else {
				fmt.Printf("capacity is: %d \t times: %.2f \t differ: %d \n", capacity, times, differ)
			}
		}
	}
}
```

