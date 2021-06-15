---
title: 「剑指offer」 Go语言版本
---



> 编码过程中首先要校验输入数据的合法性。
>
> 写代码之前首先想好有哪些测试用例，要提高代码的测试覆盖率。

## 3. 数组中重复的数字

> 找出数组中重复的数字。
>
> 在一个长度为 n 的数组 nums 里的所有数字都在 0～n-1 的范围内。数组中某些数字是重复的，但不知道有几个数字重复了，也不知道每个数字重复了几次。请找出数组中任意一个重复的数字。
>
> 2 <= n <= 100000



如果使用Map，则时间复杂度为O(n), 空间复杂度为O(n)。题目中的关键信息为`长度为n的数组，且所有数字都在0~n-1的范围内`，所以可以不用额外开辟空间。



```go
func findRepeatNumber(nums []int) int {
	var tmp int
	for i, v := range nums {
		if v != i {
			if nums[v] == v {
				return v
			}
			tmp = nums[v]
			nums[v] = v
			nums[i] = tmp
		}
	}
	return -1
}

// 时间复杂度为O(n),空间复杂读为O(1)
```

## 4. 二维数组中的查找

> 在一个 n * m 的二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个高效的函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
>
> 示例:
>
> 现有矩阵 matrix 如下：
>
> ```
> [
>   [1,   4,  7, 11, 15],
>   [2,   5,  8, 12, 19],
>   [3,   6,  9, 16, 22],
>   [10, 13, 14, 17, 24],
>   [18, 21, 23, 26, 30]
> ]
> ```
>
> 给定 target = 5，返回 true。
>
> 给定 target = 20，返回 false。
>
>  
>
> 限制：
>
> 0 <= n <= 1000
>
> 0 <= m <= 1000
>



矩阵从左到右递增，从上到下递增。如果右上角看，则往左递减，往下递增，很像一棵搜索二叉树。

[思考]： 递增 or  非递减

```go
func findNumberIn2DArray(matrix [][]int, target int) bool {
	n := len(matrix)
	if n == 0 {
		return false
	}
	m := len(matrix[0])
	if m == 0 {
		return false
	}
	for j, i := m-1, 0; j >= 0 && i < n; {
		tmp := matrix[i][j]
		if tmp == target {
			return true
		} else if tmp > target {
			j--
		} else {
			i++
		}
	}
	return false
}

// 时间复杂度为O(m+n)，空间复杂度为O(1)
```





## 5. 替换空格

> 请实现一个函数，把字符串 s 中的每个空格替换成"%20"。
>
>  
>
> 示例 1：
>
> ```sh
> 输入：s = "We are happy."
> 输出："We%20are%20happy."
> ```
>
>
> 限制：
>
> 0 <= s 的长度 <= 10000
>
> 



- 直接使用内置函数

```go
func replaceSpace(s string) string {
    return strings.ReplaceAll(s," ","%20")
}
```

- 使用`bytes.Buffer`

```go
func replaceSpace(s string) string {
	l := len(s)
	newStr := bytes.Buffer{}
	for i := 0; i < l; i++ {
		v := s[i]
		if v == ' ' {
			newStr.WriteString("%20")
			continue
		}
		newStr.WriteByte(v)
	}
	return newStr.String()
}
```

## 6.  从尾到头打印链表

> 输入一个链表的头节点，从尾到头反过来返回每个节点的值（用数组返回）。
>
>  
>
> 示例 1：
>
> ```sh
> 输入：head = [1,3,2]
> 输出：[2,3,1]
> ```
>
>
> 限制：
>
> 0 <= 链表长度 <= 10000
>



- 使用栈, 先入后出

```go
func reversePrint(head *ListNode) []int {
   res := make([]int, 0)
   for head != nil {
      res = append(res, head.Val)
      head = head.Next
   }
   for i, j := 0, len(res)-1; i < j; i, j = i+1, j-1 {
      res[i],res[j] = res[j], res[i]
   }
   return res
}
```