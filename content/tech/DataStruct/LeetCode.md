---
title: 「LeetCode」LeetCode
---



## 1



```go
 package main

import "fmt"

func main() {
	fmt.Println(twoSum([]int{2, 7, 11, 15}, 9))
}

func twoSum(nums []int, target int) []int {

	maps := make(map[int]int)

	for i, value := range nums {
		mid := target - value

		res, ok := maps[mid]
		if ok {
			return []int{res, i}
		} else {
			maps[value] = i
		}
	}
	return []int{}
}

```









## 120. 三角形最小路径和

>给定一个三角形 triangle ，找出自顶向下的最小路径和。
>
>每一步只能移动到下一行中相邻的结点上。相邻的结点 在这里指的是 下标 与 上一层结点下标 相同或者等于 上一层结点下标 + 1 的两个结点。也就是说，如果正位于当前行的下标 i ，那么下一步可以移动到下一行的下标 i 或 i + 1 。
>
> 
>
>示例 1：
>
>```sh
>输入：triangle = [[2],[3,4],[6,5,7],[4,1,8,3]]
>输出：11
>解释：如下面简图所示：
>   2
>  3 4
> 6 5 7
>4 1 8 3
>自顶向下的最小路径和为 11（即，2 + 3 + 5 + 1 = 11）。
>```
>
>示例 2：
>
>```sh
>输入：triangle = [[-10]]
>输出：-10
>```
>
>
>提示：
>
>```sh
>1 <= triangle.length <= 200
>triangle[0].length == 1
>triangle[i].length == triangle[i - 1].length + 1
>-104 <= triangle[i][j] <= 104
>```
>
>
>进阶：
>
>你可以只使用 O(n) 的额外空间（n 为三角形的总行数）来解决这个问题吗？

### 方法一：动态规划

自底向上递推

**状态定义：** 当前位置到叶子节点的最小值 $$DP[i.j] $$ 

**状态转移方程**： $$ DP[i,j] = min(DP[i+1,j], DP[i+1,j+1])+Triangle[i,j] $$



```go
func minimumTotal(triangle [][]int) int {

	// checkout input value
	if len(triangle) == 0 {
		return 0
	}
	if len(triangle) == 1 {
		return triangle[0][0]
	}
	//  algorithm
	raw := len(triangle)
	status := make([][]int, raw+1)
	status[raw] = make([]int, raw+1)
	for i := raw; i > 0; i-- {
		status[i-1] = make([]int, i)
		for j := 0; j < i; j++ {
			status[i-1][j] = getMin(status[i][j], status[i][j+1]) + triangle[i-1][j]
		}
	}
	return status[0][0]
}

func getMin(a, b int) int {
	if a > b {
		return b
	}
	return a
}
```

