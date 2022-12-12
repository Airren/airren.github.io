---
title: Go Assembly
---



Golang的汇编是基于Plan9的的汇编，是一个中间汇编的方式，这样可以忽略底层CPU架构的差别。

汇编主要了解各种寄存器的使用以及寻址方式。根据Golang的汇编我们可以深入理解Golang的底层实现。比如内存如何分配，栈如何扩张，接口如何转变。





如何从go语言获取对应的汇编

```sh
go build -gcflags "-N -l" -ldflags=-compressdwarf=false =o main.out main.g
go tool objdump -s "main.main" main.out > main.S
# or
go tool compile -S main.go
# or
go build -gcflags -S main.go
```



## 汇编基础语法



### 通用寄存器

寄存器与物理机架构有关，不同的架构有不同的物理寄存器。

在amd64架构上提供了16个通用寄存器给用户使用。

Plan9汇编语言提供了如下映射，这样就可以在汇编语言中这几应用就可以使用物理寄存器了。

x64架构中所有的寄存器都是64位

| amd64 | Plan9 |
| ----- | ----- |
| rax   | AX    |
| rbx   | BX    |
| rcx   | CX    |
| rdx   | DX    |
| rdi   | DI    |
| rsi   | SI    |
| rbp   | BP    |
| rsp   | SP    |
| r8    | R8    |
| r9    | R9    |
| r10   | R10   |
| r11   | R11   |
| r12   | R12   |
| r13   | R13   |
| r14   | R14   |
| rip   | PC    |

### 虚拟寄存器	

伪寄存器不是真正的寄存器，而是由工具链维护的虚拟寄存器，例如帧指针。



FP: Frame pointer: arguements and locals。 帧指针，快速访问`函数的参数`和`返回值`

`0(FP)` represent the first arguement, `8(FP)` represents the second parameter(AMD64 arch). `first_arg+0(FP)`表示把第一个参数地址绑定到符号first_arg



SP: Stack pointer: the highest address within the local stack frame. 指向当前栈顶。栈指针，指向`局部变量`。

`localvar0-8(SP)`在plan9中表示函数的第一个局部变量。物理寄存器中也有SP,硬件SP才是真正表示栈顶的位置。所以为了区分SP到底是指硬件SP还是虚拟寄存器。Plan9代码中需要以特定的格式来区分。

`symbol+offset(SP)`表示虚拟寄存器SP。`offset(SP)`表示硬件SP.

PC: Program counter: jumps and branchs。 程序计数器，指向下一条指令的地址。在amd64时其实就是rip寄存器。

除了个别跳转治理，一般用不到

SB: Static base pointer: global symbols。静态基址指针，全局符号。

表示`全局内存`起点。foo(SB)表示符号foo作为内存地址使用。这种形式用于声明全局函数、数据。`foo+4(SB)`表示foo往后4字节的地址。<>限制符号只能在当前源文件使用。



所有用户定义的符号都作为偏移量写入伪寄存器 FP 和 SB。

汇编代码中需要表示用户定义的符号(变量)时，可以通过 SP 与偏移还有变量名的组合，比如`x-8(SP)` ，因为 SP 指向的是栈顶，所以偏移值都是负的，`x`则表示变量名



### 寻址模式

汇编语言的一个很重要的概念就是它的寻址模式，Plan 9 汇编也不例外，它支持如下寻址模式：

```
R0              数据寄存器
A0              地址寄存器
F0              浮点寄存器
CAAR, CACR, 等  特殊名字
$con            常量
$fcon           浮点数常量
name+o(SB)      外部符号
name<>+o(SB)    局部符号
name+o(SP)      自动符号
name+o(FP)      实际参数
$name+o(SB)     外部地址
$name<>+o(SB)   局部地址
(A0)+           间接后增量
-(A0)           间接前增量
o(A0)
o()(R0.s)


```

symbol+offset(SP) 引用函数的局部变量，offset 的合法取值是 [-framesize, 0)
    局部变量都是 8 字节，那么第一个局部变量就可以用 localvar0-8(SP) 来表示

如果是 symbol+offset(SP) 形式，则表示伪寄存器 SP
如果是 offset(SP) 则表示硬件寄存器 SP





### Assembler Directives



#### DATA&GLOBL

Fomat: Declare a global variable using DATA and GLOBL



全局数据符号以DATA指令起始和一个GLOBL指令定义。每个DATA指定初始化相应内存的一部分。未初始化的内存为零。一般形式为

```assembly
DATA sysbol+offset(SP)/width, value
```

在给定的offset和width处初始化该符号的内存为value。DATA必须通过增加的偏移量来写入给定符号的指令。

GLOBL指令声明符号是全局的。参数是可选标志，并且数据的大小被声明为全局，除非DATA指令已初始化，否则初始值将为全0.

```assembly
DATA divtab<>+0x00(SB)/4, $0xf4f8fcff  # set value for divtab from 0 to 4
DATA divtab<>+0x04(SB)/4, $0xf3f4f8f9
....
DATA divtab<>+0x3c(SB)/4, $0xf3f4f8f9  # divtab 64bytes
GLOBL divtab<>(SB),RODATA,$64          # add flag RODATA means readonly, 
                                       #$64 is the var's length 64byte

GLOBL runtime.tlsoffset(SB),NOPTR,$4   # NOPTR This data contains no pointers. 
```

[Rumtime Flag](https://github.com/golang/go/blob/c53315d6cf1b4bfea6ff356b4a1524778c683bb9/src/runtime/textflag.h)



#### TEXT

```assembly
TEXT runtime.profileloop(SB),NOSPLIT,$8 # NOSPIT // Don't insert stack check preamble.
		MOVQ $runtime.profileloop(SB),CX
		MOVQ CX, 0(SP)
		CALL runtime.externalthreadhandler(SB)
		RET
```



这段代码成为一个TEXT block，runtime.profileloop(SB)后面有一个NOSPLIT flag，紧随其后的$8表示`frame_size`, 通常frame_size的构成都是形如`$8-24`,表示这个text block运行需要占用8byte的内存空间，参数和返回值要占用24Byte的内存空间。(这24个字节占用的是调用方栈帧里的空间)。

如果有NOSPLIT这个flag，则可以忽略参数和返回值占用的空间，如上述例子，只有一个$8。表示frame_zise只有8字节大小。这从汇编中也可以看出来 `MOVQ CX,0(SP)`， 因为MOVQ表示这个操作的操作数是8Bytes.







FUNCDATA 和PCDATA指令包含了有垃圾回收器使用的信息，由编译器引入



### Instruction

常用指令有以下几类：数据移动类，例如MOV；跳转，无条件跳转或者有条件跳转；逻辑运算和算数运算；还有指令的prefix, 例如LOCK



| Instructoin | Size    |
| ----------- | ------- |
| MOVB        | 1 Bytes |
| MOVW        | 2 Bytes |
| MOVL        | 4 Bytes |
| MOVQ        | 8 Bytes |
| JMP         |         |
| MOVEQ       |         |
| LEAQ        |         |
| SUBQ        |         |
| ANDQ        |         |
| CALL        |         |
| PUSHQ       |         |
| POPQ        |         |
| CLD         |         |
| CMPQ        |         |
| CPUID       |         |
| JEQ         |         |

​	







## 运行时协调

为保证垃圾回收的正确运行，在大多数的栈帧中，运行时必须知道所有的全局数据的指针。Go编译器会将这一部分信息耦合到Go源码文件中，但是汇编程序必须进行显式的定义。

被标记为 `NOPTR` 标志的数据符号会视为不包含指向运行时分配数据的指针。 带有 `R0DATA` 标志的数据符号在只读存储器中分配，因此被隐式标记为 `NOPTR`。 总大小小于指针的数据符号也被视为隐式标记 `NOPTR`。 在一份汇编源文件中是无法定义包含指针的符号的，因此这种符号必须定义在 Go 源文件中。 一个良好的经验法则是 `R0DATA` 在 Go 中定义所有非符号而不是在汇编中定义。

每个函数还需要注释，在其参数，结果和本地堆栈框架中给出实时指针的位置。 对于没有指针结果且没有本地堆栈帧或没有函数调用的汇编函数， 唯一的要求是在同一个包中的 Go 源文件中为函数定义 Go 原型。 汇编函数的名称不能包含包名称组件 （例如，`syscall` 包中的函数 `Syscall` 应使用名称 `·Syscall` 而不是 `syscall·Syscall` 其 TEXT 指令中的等效名称）。 对于更复杂的情况，需要显式注释。 这些注释使用标准 `#include` 文件中定义的伪指令 `funcdata.h`。

如果函数没有参数且没有结果，则可以省略指针信息。这是由一个参数大小 `$n-0` 注释指示 `TEXT` 对指令。 否则，指针信息必须由 Go 源文件中的函数的 Go 原型提供，即使对于未直接从 Go 调用的汇编函数也是如此。 （原型也将 `go vet` 检查参数引用。）在函数的开头，假定参数被初始化但结果假定未初始化。 如果结果将在调用指令期间保存实时指针，则该函数应首先将结果归零， 然后执行伪指令 `GO_RESULTS_INITIALIZED`。 此指令记录结果现在已初始化，应在堆栈移动和垃圾回收期间进行扫描。 通常更容易安排汇编函数不返回指针或不包含调用指令; 标准库中没有汇编函数使用 `GO_RESULTS_INITIALIZED`。

如果函数没有本地堆栈帧，则可以省略指针信息。这由 `TEXT` 指令上的本地帧大小 `$0-n` 注释表示。如果函数不包含调用指令，也可以省略指针信息。否则，本地堆栈帧不能包含指针，并且汇编必须通过执行伪指令 `TEXTNO_LOCAL_POINTERS` 来确认这一事实。因为通过移动堆栈来实现堆栈大小调整，所以堆栈指针可能在任何函数调用期间发生变化：甚至指向堆栈数据的指针也不能保存在局部变量中。

汇编函数应始终给出 Go 原型，既可以提供参数和结果的指针信息，也可以 `go vet` 检查用于访问它们的偏移量是否正确。



## Demo

### Runtime CAS



接下来，我们一起阅读 `asm_amd64.s`中的汇编

第一个汇编：实现 CAS 操作

```assembly
// asm_amd64.s

// bool Cas(int32 *val, int32 old, int32 new)
// Atomically:
//	if(*val == old){
//		*val = new;
//		return 1;
//	} else
//		return 0;
TEXT runtime∕internal∕atomic.Cas(SB),NOSPLIT,$0-17
	MOVQ	ptr+0(FP), BX
	MOVL	old+8(FP), AX
	MOVL	new+12(FP), CX
	LOCK
	CMPXCHGL	CX, 0(BX)
	SETEQ	ret+16(FP)
	RET
```

我们先看第一个汇编，使用汇编实现 CAS (compare and swap)操作

我们一条一条的看，先看`TEXT runtime∕internal∕atomic·Cas(SB),NOSPLIT,$0-17` 。`$0-17`表示的意思是这个`TEXT block`运行的时候，需要开辟的栈帧大小是 0 ，而`17 = 8 + 4 + 4 + 1 = sizeof(pointer of int32) + sizeof(int32) + sizeof(int32) + sizeof(bool)` （返回值是 bool ，占据 1 个字节

然后我们再看 block 内的第一条指令 ， 这里的 FP，是伪寄存器(pseudo) ，里边存的是 Frame Pointer, FP 配合偏移 可以指向函数调用参数或者临时变量

`MOVQ ptr+0(FP), BX` 这一句话是指把函数的第一个参数`ptr+0(FP)`移动到 BX 寄存器中

`MOVQ` 代表移动的是 8 个字节,Q 代表 64bit ，参数的引用是 `参数名称+偏移(FP)`,可以看到这里名称用了 ptr,并不是 val,变量名对汇编不会有什么影响，但是语法上是必须带上的，可读性也会更好些。

后边两条 MOVL 不再赘述

`LOCK` 并不是指令，而是一个指令的前缀 (instruction prefix)，是用来修饰 `CMPXCHGL CX,0(BX)` 的

> The LOCK prefix ensures that the CPU has exclusive ownership of the appropriate cache line for the duration of the operation, and provides certain additional ordering guarantees. This may be achieved by asserting a bus lock, but the CPU will avoid this where possible. If the bus is locked then it is only for the duration of the locked instruction

`CMPXCHGL` 有两个操作数，`CX` 和 `0(BX)` ,`0(BX)` 代表的是 val 的地址
`offset(BX)` 是一种 `addressing model` , 把寄存器里存的值 + offset 作为目标地址

CMPXCHGL 指令做的事情，首先会把 `destination operand`(也就是 `0(BX)`)里的值 和 AX 寄存器里存的值做比较，如果一样的话会把 CX 里边存的值保存到 `0(BX)` 这块地址里 (虽然这条指令里并没有出现 AX，但是还是用到了，汇编里还是有不少这样的情况)
CMPXCHGL 最后的那个 L 应该表示的是操作长度是 32 bit ，从函数的定义来看 old 和 new 都是 int32 函数返回一个 Bool 占用 8bit ，`SETEQ` 会在 AX 和 CX 相等的时候把 1 写进 ret+16(FP) (否则写 0





### AVX512



Reference

https://programmer.ink/think/introduction-to-golang-assembly.html

https://golang.design/under-the-hood/zh-cn/part1basic/ch01basic/asm/

