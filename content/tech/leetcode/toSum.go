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
