---
Title: OS memory
---

What is the maximum size of the stack?

It depends on your operating system. On Windows, the typical maximum size for a stack is 1MB, whereas it is 8MB on a typical modern Linux, although those values are adjustable in various ways. If the sum of your stack variables (including low-level overhead such as return addresses, stack-based arguments, return value placeholders, and alignment bytes) in the entire call stack exceeds that limit, you get a stack overflow, which typically takes down your program without any chance at recovery.

A few kilobytes are usually fine. Tens of kilobytes are dangerous because it starts to sum up. Hundreds of kilobytes is a horrible idea.

Check the default size of the Linux

```sh
ulimit -s
```

```c++
#include <stdio.h>
int main() {
   int b[100];  // store in stack
   static int a[10000000];  // if the size exceed the stack size, it need to store in heap
   return 1;
}
```

栈是向低地址扩展的数据结构，是一块连续的内存区域。栈顶的地址和栈的最大容量是系统预先规定好的。在Windows栈的大小是1M，如果申请的空间超过栈的剩余空间时就会提示overflow。从栈能获取的空间较小。

堆是向高地址扩展的，是不连续的内存区域。这是由于系统是使用链表来存储空闲内存地址的，自然不是连续的。而链表的遍历是从低地址到高地址。堆的大小受限于计算机系统中有效的虚拟内存。