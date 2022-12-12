



````go
package dal

import "testing"

func Empty() {
	str := "12312"
	if str == "" {

	}
}
func Lenzero() {
	str := ""
	if len(str) == 0 {

	}
}
func BenchmarkEmpty(b *testing.B) {
	for n := 0; n < b.N; n++ {
		Empty()
	}
}

func BenchmarkLenzero(b *testing.B) {
	for n := 0; n < b.N; n++ {
		Lenzero()
	}
}

````

![image-20210620155543250](string_operate/image-20210620155543250.png)