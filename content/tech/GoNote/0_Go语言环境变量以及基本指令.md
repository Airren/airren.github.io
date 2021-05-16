# Go语言环境变量以及基本指令

## 1. Go语言介绍

#### 1.1 Go 语言特点

1. 静态类型、编译型的开源语言

   **静态类型**是指要明确变量的类型，或者编译器可以推导出变量的类型

   要么在变量类型傍边指定变量的那个类型，要么是可以推导出变量类型

   **编译型**是指要编译成机器语言

   ```Go
   package main
   
   func main(){
     var num1 int = 1;
   }
   ```

   ```go
   package main
   func main(){
     var num1 int = 1;
     num2 :=2
   }
   ```

2. 脚本化的语法，支持多种范式编程

   函数式&面向对象

3. 原生、给力的并发编程支持

   注意： 原生支持和函数库支持的区别

#### 1.2 Go语言的优势和劣势

优势

1. 脚本化的语法
2. 静态类型+编译型，程序运行速度有保障
3. 原生的支持并发编程 - 降低开发、维护成本；程序可以更好的执行

劣势

1. 语法糖并没有Python和Ruby那么多- 1成是开发时间，9成维护时间
2. 目前的程序运行速度还不及c，但是目前已经赶超了C++和Java
3. 第三方库函数暂时不像绝对主流的编程语言那样多

#### 1.3 Linux 下的安装

FreeBSD Linux  Windows 32bit - 64bit

Linux 下的设置方法

有四个环境变量需要设置： GOROOT、GOPATH、GOBIN以及PATH

需要设置到某一个profile文件中

OS X->  ~/.bash_profile(单一用户) 或者 /etc/profile（所有用户）

##### GOROOT

该环境变量的值应该为**Go语言的当前安装目录**

##### GOPATH

该环境变量为**Go语言工作区的集合**

> go 1.12 版本中支持 go mod 不再使用go path

##### GOBIN

是你想存放**Go程序的可执行文件的目录**

```shell
export GOROOT=/usr/local/go
export GOPATH=~/golib:~/goproject
export GOBIN=~/gobin
```

##### PATH:

为了方便使用**Go语言命令和Go程序的可执行文件**，需要追加其值，如

```sh
export PATH=$PATH:$GOROOT/bin:$GOBIN

source profileName # 使修改生效
```



任意目录 go version



## 2. 基本规则

#### 2.1 工作区和GOPATH

工作区是放置Go源码文件的目录

一般情况下，Go源码文件都需要存放到工作区中

但对于**命令源码文件**来说，这不是必须的

每个工作区的结构都类似下图所示

```sh
/home/username/golib:
	src/ # 用于存放源码文件、以代码包为组织形式
	pkg/ # 用于存放归档文件（名称以.a）为后缀的文件 - 所有归档文件都会被存放到该目录下的平台相关目录中，同样以代码包为组织形式
	bin/ # 用于存放当前工作区中Go程序的可执行文件---GOBIN
			# 1. 当环境变量GOBIN已有效设置时，该目录会变的无意义
			# 2. 当GOPATH的值中包含多个工作区的路径时候，必须设置GOBIN，否则无法成功安装Go程序的可执行文件。
```

###### 平台相关目录

` pkg/darwin_amd64`

两个隐含的Go语言环境变量： GOOS和GOARCH

GOOS：操作系统

GOARCH:计算架构

平台相关目录是以`$GOOS_$GOARCH`为命名方式，如linux_amd64

<font color=red><工作区目录>/pkg/<平台相关目录>/<一级代码包>/<二级代码包>/<末级代码包>.a </font>

#### 2.1 源码文件的分类和含义

##### GO源码文件

- 名称以.go为后缀，内容以Go语言代码组织的文件

多个Go源码文件是需要用代码包组织起来的

分三类

命令源码文件、库源码文件 - go语言程序

测试源码文件-                           辅助源码文件

##### 命令源码文件

- 声明自己属于main代码包
- 包含无参数声明和结果声明的main函数

被安装后，相应的可执行文件会被存放到`GOBIN指向的目录或者<当前工作区目录>/bin下`

命令源码文件是Go程序的入口，但是不建议把程序都写在一个文件中

> 注意： 同一个代码包中强烈不建议直接包含多个命令源码文件

##### 库源码文件

不具备命令源码文件的两个特征的源码文件

被安装后，相应的归档文件会被存放到`<当前工作目录>/pkg/<平台相关目录>`下

##### 测试源码文件

不具备命令源码文件那两个特征的源码文件

- 名称以`_test.go`为后缀

- 其中至少有一个函数的名称以`Test`或`Benchmark`为前缀

- 并且，该函数接受一个类型为`*testing.T`或`*testing.B`的参数

  ```go
  // 功能测试函数
  func TestFind(t *testing.T){
    // 省略若干条语句
  }
  
  // 基准测试函数或性能测试函数
  func BenchmarkFind(b *testing.B){
    // 省略若干条语句
  }
  ```

#### 2.3 代码包的相关知识

##### 代码包的作用

编译和归档Go成语的最基本单位

代码划分、集结和依赖的有效组织形式，也是权限控制的辅助手段

##### 代码包的规则

一个代码包实际上就是一个由**导入路径**代表的目录

导入路径即`<工作区目录>/src`或`<工作区目录>/pkg/<平台相关目录>`之下的`某段子路径`

例如：代码包hypermind.cn可以对应于

`/home/username/golib/src/hypermind.cn`

(其中，/home/username/golib 是一个工作区目录)

##### 代码包的声明

- 每个源码文件必须声明其所属的代码包

- 同一个代码包中的所有源码文件声明的代码包应该是相同的

##### 代码包声明与代码包导入路径的区别

代码包声明语句中的包名称应该是该代码包的导入路径的`最右子路径`

例如 `hypermind.cn/pkgtool` -> `package pkgtool`

##### 代码包的导入

代码包导入语句中使用的包名称应该与其导入路径一致，例如

flag、fmt、strings

```go
import (
	"flag"
  "fmt"
  "strings"
)
```

##### 代码包的导入方法

- 带别名的导入

```go
import str "strings"
str.HasPrefix("abc","a")
```

- 本地化的导入

```go
import . "strings"
HasPrefix("abc","a")
```

- 仅仅初始化

```go
import _ "strings"
// 仅仅执行代码包中的初始化函数
```

##### 代码包的初始化

- 代码包初始化函数即：无参数声明和结果声明的init函数
- init函数可以被声明在任何文件中，且可以有多个

1. init 函数的执行时机---单一代码包内

对所有全局变量进行求值---->>>>>执行所有init函数

>  单个代码包内多个init函数的执行顺序是不确定的

2. init 函数的执行时机---不同代码包之间

执行被导入的代码包中init函数----->>>> 执行导入它的那个代码包的init函数

导入顺序 A -> B -> C

init 的执行顺序 C B A

> 注意： 我们不应该对在同一个代码包中被导入的多个代码包的init函数的执行顺序作出假设！

3. init函数的执行时机----所有涉及到的代码包

每一个init函数只会被执行一次



## 3. 命令基础

```go
go run
go build 和 go install
go get
```

#### 3.1  go run

- 用于运行`命令源码文件`
- 只能接受`一个命令源码文件`以及若干个`库源码文件`作为文件参数
- 其内部操作步骤是： 先编译源码文件再运行； 源文件--compile(放入临时文件夹)-->1可执行文件2. 归档文件

示例 

https://github.com/hyper-carrot/goc2p



`ds`命令和`psd`命令

`ds`命令的源码文件`goc2p/src/helper/ds/showds.go` 用于显示指定目录的目录结构

`pds`命令的源码文件`goc2p/src/helper/ds/showpds.go` 用于显示指定代码包的依赖关系



go run 常用标记的使用

```sh
-a # 强制编译相关代码，不论他们的编译结果是否已是最新的
-n # 打印编译过程中所需运行的命令，但不会真正执行他们
-p -n # 并行编译，其中n为并行的数量    n为逻辑核的数量
-v # 列出被编译代码包的名称
-a -v # 列出所有被编译的代码包的名称 1.4 版本后不包含Go语言自带的标准库的代码包
-work # 显示编译时创建的临时工作目录的路径，并且不删除它
-x # 打印编译过程中所需的命令，并执行他们 注意与-n的区别
```



#### 3.2 go build

- 用于编译源码文件或代码包

- 编译非命令源码文件不会产生任何执行结果(只是检查语法的有效性)

- 编译命令源码文件会在该命令的`执行目录中`生成一个可执行文件

- 执行该命令且不追加任何参数时，他会试图把当前目录作为代码包并编译

- 执行该命令且以代码包的导入路径作为参数时，改代码包及其依赖会被编译

  （-a 标记后所有涉及到的代码包都会被重新编译 ）

- 执行该命令以若干源码文件作为参数时，只有这些文件会被编译（如果缺少依赖会编译错误）



例如：` /Users/airren/go/src/github.com/airren/day01/helloworld/main.go`

如果对`main.go`进行编译，

- 可以直接在`hello word`路径下执行`go build`
- 或者再任意路径执行`go build github.com/airren/day01/helloworld`， 项目的路径是从`$GOPATH/src`后开始写起

编译生成的可执行文件在执行`go build`的当前路径下。



```sh
go build -o hello  # 指定编译后的文件名
```





#### 3.3 go install 

先执行 go build 后copy

- 用于编译并安装代码包或源码文件
- 安装代码包会在当前工作区的 `pkg/<平台相关目录>`下生成归档文件
- 安装命令源码文件会在`当前工作区的bin目录`或`$GOBIN目录`下生成可执行文件
- 执行该命令且不追加任何参数时，它会试图把当前目录作为代码包并安装
- 执行该命令且以代码包的导入路径作为参数时，该代码包及其依赖会被安装
- 执行改命令且以命令源码文件以及相关库源码文件作为参数时，只有这些文件会被编译并安装

>  代码包安装之后，在pkg中会有 .a 文件, 在其他的代码中就可以引入已经安装的代码包

#### 3.4 go get

- 用于从远程代码仓库(github, gitlab,gogs)上下载并安装代码包
- 受支持的代码版本控制系统有: Git , Mercurial(hg)，SVN, Bazaar
- 指定的代码包会被下载到`$GOPATH`中包含的第一个工作区的src目录中，然后再安装

```
go get -x github.com/go-errors/errors
```

go get常用标记的使用

```sh
-d # 只执行下载动作，而不执行安装动作
-fix # 在下载代码包后先执行修正动作，而后再进行编译和安装
-u # 利用网络来更新已有的代码包及其依赖包
-x # 显示过程
```

### 3.5 godoc

```
go get golang.org/x/tools/cmd/godoc
```



## 4. 跨平台编译

交叉编译

默认我们`go build`的可执行文件都是当前操作系统可执行的文件，如果我想在windows下编译一个linux下可执行文件，那需要怎么做呢？

只需要指定目标操作系统的平台和处理器架构即可：

```bash
SET CGO_ENABLED=0  // 禁用CGO
SET GOOS=linux  // 目标平台是linux
SET GOARCH=amd64  // 目标处理器架构是amd64
```

使用了cgo的代码是不支持跨平台编译的

然后再执行`go build`命令，得到的就是能够在Linux平台运行的可执行文件了。

Mac 下编译 Linux 和 Windows平台 64位 可执行程序：

```bash
CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build
CGO_ENABLED=0 GOOS=windows GOARCH=amd64 go build
```

Linux 下编译 Mac 和 Windows 平台64位可执行程序：

```bash
CGO_ENABLED=0 GOOS=darwin GOARCH=amd64 go build
CGO_ENABLED=0 GOOS=windows GOARCH=amd64 go build
```

Windows下编译Mac平台64位可执行程序：

```bash
SET CGO_ENABLED=0
SET GOOS=darwin
SET GOARCH=amd64
go build
```