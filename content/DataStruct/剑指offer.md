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

- 链表反转



## 7. 重建二叉树

>输入某二叉树的前序遍历和中序遍历的结果，请重建该二叉树。假设输入的前序遍历和中序遍历的结果中都不含重复的数字。
>
>
>
>例如，给出
>
>```
>前序遍历 preorder = [3,9,20,15,7]
>中序遍历 inorder = [9,3,15,20,7]
>```
>
>
>返回如下的二叉树：
>
>```
>    3
>   /  \
>  9    20
> /      \
>15       7
>```
>
>
>
>
>限制：
>
>0 <= 节点个数 <= 5000









```go
//* Definition for a binary tree node.
type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func buildTree(preorder []int, inorder []int) *TreeNode {

	if len(preorder) == 0 {
		return nil
	}

	if len(preorder) == 1 {
		return &TreeNode{
			Val:   preorder[0],
			Left:  nil,
			Right: nil,
		}
	}

	val := preorder[0] // root val
	index := 0         // inorder root index
	for i, v := range inorder {
		if val == v {
			index = i
			break
		}
	}
	root := TreeNode{
		Val:   val,
		Left:  buildTree(preorder[1:index+1], inorder[0:index]),
		Right: buildTree(preorder[index+1:], inorder[index+1:]),
	}
	return &root
}

```





## 9. 用两个栈实现队列



>用两个栈实现一个队列。队列的声明如下，请实现它的两个函数 appendTail 和 deleteHead ，分别完成在队列尾部插入整数和在队列头部删除整数的功能。(若队列中没有元素，deleteHead 操作返回 -1 )
>
> 
>
>示例 1：
>
>输入：
>
>```
>["CQueue","appendTail","deleteHead","deleteHead"]
>[[],[3],[],[]]
>输出：[null,null,3,-1]
>```
>
>示例 2：
>
>输入：
>
>```
>["CQueue","deleteHead","appendTail","appendTail","deleteHead","deleteHead"]
>[[],[],[5],[2],[],[]]
>输出：[null,-1,null,null,5,2]
>```
>
>提示：
>
>```
>1 <= values <= 10000
>最多会对 appendTail、deleteHead 进行 10000 次调用
>```



使用 List的Double List作为Stack

```go
type CQueue struct{
    stack1, stack2 *list.List
}

func Constructor() CQueue{
    return CQueue{
        list.New(),
        list.New(),
    }
}

func (q *CQueue) AppendTail(value int){
    q.stack1.PushBack(value)
}

func (q *CQueue) DeleteHead() int{
    if q.stack2.Len() == 0{
        for this.stack1.Len() > 0{
            this.stack2.PushBack(this.stack1.Remove(this.stack1.Back()))
        }
    }
}
```



自定义Stack

```go
type CQueue struct{
    stack1, stack2 Stack
}

func Constructor() CQueue{
    return CQueue{
        Stack{},
        Stack{},
    }
}

func (q *CQueue) AppendTail(value int){
    q.stack1.Push(value)
}

func (q *CQueue) DeleteHead() int{
    if q.stack2.Len== 0{
        for q.stack1.Len > 0 {
            q.stack2.Push(q.stack1.Pop())
        }
    }
    if q.stack2.Len != 0{
        return q.stack2.Pop()
    }
    return -1
}


type Stack struct {
    Values []int
    Len int
}

func(s *Stack) Push(value int){
    if len(s.Values) < (s.Len+1){
        s.Values = append(s.Values,0)
    }
    s.Values[s.Len] =  value
    s.Len++   
}

func(s *Stack) Pop()int{
    e := s.Values[s.Len-1]
    s.Len--
    return e
} 

```



## 

## 53. 在排序数组中查找数字



## 59. 滑动窗口的最大值

> 
> 给定一个数组 `nums` 和滑动窗口的大小 `k`，请找出所有滑动窗口里的最大值。
>
> **示例:**
>
> ```
> 输入: nums = [1,3,-1,-3,5,3,6,7], 和 k = 3
> 输出: [3,3,5,5,6,7] 
> 解释: 
> 
>   滑动窗口的位置                最大值
> ---------------               -----
> [1  3  -1] -3  5  3  6  7       3
>  1 [3  -1  -3] 5  3  6  7       3
>  1  3 [-1  -3  5] 3  6  7       5
>  1  3  -1 [-3  5  3] 6  7       5
>  1  3  -1  -3 [5  3  6] 7       6
>  1  3  -1  -3  5 [3  6  7]      7
> ```
>
>  
>
> **提示：**
>
> 你可以假设 *k* 总是有效的，在输入数组不为空的情况下，1 ≤ k ≤ 输入数组的大小。





````go
package main

import (
	"container/heap"
	"sort"
)

var a []int

type hp struct{ sort.IntSlice }

func (h hp) Less(i, j int) bool  { return a[h.IntSlice[i]] > a[h.IntSlice[j]] }
func (h *hp) Push(v interface{}) { h.IntSlice = append(h.IntSlice, v.(int)) }
func (h *hp) Pop() interface{}   { a := h.IntSlice; v := a[len(a)-1]; h.IntSlice = a[:len(a)-1]; return v }

func maxSlidingWindow(nums []int, k int) []int {
	a = nums
	q := &hp{make([]int, k)}
	for i := 0; i < k; i++ {
		q.IntSlice[i] = i
	}
	heap.Init(q)

	n := len(nums)
	ans := make([]int, 1, n-k+1)
	ans[0] = nums[q.IntSlice[0]]
	for i := k; i < n; i++ {
		heap.Push(q, i)
		for q.IntSlice[0] <= i-k {
			heap.Pop(q)
		}
		ans = append(ans, nums[q.IntSlice[0]])
	}
	return ans
}

func main() {
	maxSlidingWindow([]int{1, 3, 9, -3, 5, 3, 6, 7}, 3)
}

````

