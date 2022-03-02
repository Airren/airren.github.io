---
title: 「Go」Struct
---





匿名struct比较

```go
func main() {
	sn1 := struct {
		age  int
		name string
	}{age: 11, name: "qq"}

	sn2 := struct {
		age  int
		name string
	}{age: 11, name: "qq"}

	if sn1 == sn2 {
		fmt.Println("sn1 == sn2",sn1)
	}

	fmt.Printf("sn1 addr %p\n",&sn1)
	fmt.Printf("sn2 addr %p\n",&sn2)
}
```



```sh
sn1 == sn2 {11 qq}
sn1 addr 0xc0000a6020
sn2 addr 0xc0000a6040
```



