---
title: Go 设置Golang开发环境
---



## 安装Go

开始写Golang之前，你首先需要下载并且安装Golang开发工具。可以在[Golang的官方网站](https://go.dev/dl/)下载最新的安装包。根据你的开发平台选择下载对应的版本。对于Mac用户(或者Windows用户)来说.pkg(或msi)安装包会自动把Golang安装到合适的位置，并将命令加入到环境变量，并且自动移除老版本。如果使用Mac更加推荐直接使用`brew install go`， 使用brew 可以方便的管理Go多个版本。

对于Linux或者FreeBSD用户可以直接下载对应的.tar.gz, 并解压到文件名为go的目录中。把这个文件Copy到`/usr/local/`下。然后把`/usr/local/go/bin`添加到`$PATH`中。

```sh
tar -C /usr/local -xzvf go1.17.3.linux-amd64.tar.gz
# change .profile to .bashrc or.zsh depend on you evnironment
echo "export PATH=$PATH:/usr/local/go/bin" >>$HOME/.profile
```

Go 程序编译完成后是一个独立的二进制文件，不需要依赖任何运行软件(例如，Python需要Python解释器，Java需要JVM)。仅在需要编译Go代码的环境上安装Go即可。

安装完成后，打开Terminal(Window 打开CMD)，验证Go是否安装成功。

```sh
go version
```

在所有的配置都正确的情况下，可以看到输出的版本信息

```sh
# mac intel cpu 64bit
go version go1.17.3 darwin/amd64
```

从上述信息中可以看出，Go的版本是1.17.3,使用的开发机器是 Mac(Darwin 是MacOS的Kernel Name, amd64 是指64-bit的x86 CPU架构)。

如果你没有得到正确的版本信息，很有可能是你没有把go加入到环境变量，或者有其他的程序名称也为go。对于Mac或者Unix-like用户，可以使用`which go`查看当前环境下的go的是否正确关联到`/usr/local/go/bin`。如果路径正确，也有可能是下载错了安装包，检查下安装包的位数是否与当前操作系统匹配，有可能在64-bit的系统上下载了32-bit的安装包。另外，也有可能是芯片架构选错了。

## Go 工作空间

从2009年Go开始使用，在开发者如果组织代码和管理依赖上经历了几次变化。在Go 1.11所有的代码必须保存在GOPATH之下，之后的版本用户可以在任意目录下存储自己的代码。但是Go依然希望有一个独立的工作空间可以存储通过`go install`安装额第三方包。默认的工作空间是`$HOME/go`, 下载的第三方包默认存储在`$HOME/go/src`,编译的二进制文件存储在`$HOME/go/bin`。你可以直接使用这个默认的工作空间，或者通过设置`$GOPAHT`指定一个工作空间。

无论你是否使用默认的wokespace，建议你把`$GOPATH/bin`加入到`$PATH`中。 通过指定`$GOPATH`可以清楚地描述当前环境的Go工作空间，把`$GOPATH/bin`加入到可执行路径中可以直接使用`go install `安装的第三方包。

如果是Unix-like的开发环境可以把下面几行加入到`$HOME/.profile`中。

```sh
# if use ubuntu you should add to .bashrc,if yuo use zsh, suggest add to .zshrc
export GOPATH=$HOME/go
export PATH=$PATH:$GOPATH/bin
```

添加完成之后需要执行`source $HOME/.profil`使当前窗口的terminal加载配置。

如果是Window 系统，可以运行如下两条命令,也可以通过环境变量管理界面配置。

```powershell
set GOPATH %USERPROFILE%\go
set path "%path%;%GOPATH%\bin"
```

运行完这两条指令后你需要关闭当前cmd窗口，重新打开后即可生效。

还有其他很多Go需要用到的环境变量，可以通过`go env`查看所有的变量列表。

```sh
➜  ~ go env
# 是否启用go mod,建议设置为 auto，兼容老的project
GO111MODULE=""
# cpu architecture for cross-compilation
GOARCH="amd64"
GOBIN="/Users/airren/go/bin"
# os type for cross-compilation
GOOS="darwin"
GOPATH="/Users/airren/go:/tmp/goc2p"
GOPROXY="https://goproxy.cn,direct"
GOROOT="/usr/local/Cellar/go/1.17.3/libexec"
GOSUMDB="sum.golang.org"
GOVERSION="go1.17.3"
GCCGO="gccgo"
AR="ar"
CC="clang"
CXX="clang++"
CGO_ENABLED="1"
GOMOD="/dev/null"
·····
```



## Go 常用命令

`go`提供了很多命令，这些命令包含compiler(编译)、code formatter(书写格式化)、linter(静态检查)、dependency manager(依赖管理)、test runner等等。



### go run 和 go build

这两个命令`go run`和`go build`非常相似，每个命令后面都可以跟一个或者多个Go文件，或者Go package。接下来我们创建一个简单的程序来看下这连个命令如何使用。

**go run**

任意路径下，新建一个目录hello，然后创建一个hello.go 的文件

```sh
# ~/hello/hello.go
package main
import "fmt"
func main(){
	fmt.Println("Hello Golang!")
}
```

保存后，在terminal中进入hello这个目录下，执行

```sh
# usage: go run [build flags] [-exec xprog] package [arguments...]
go run hello.go
```

你可以在console中看到打印的`Hello Golang!`。运行完成后，如果你查看hello这个目录下，你会发现只有我们创建的`hello.go`这个文件。你可能会想，go是一个编译型语言，为什么没有产生编译后的二进制文件呢？

实际上，`go run`确实编译了我们的源码文件。然而，这个源码文件是保存在一个临时目录中的，当运行结束之后，编译出来的二进制文件也就被删除了。这种方式很适合用来测试一些比较小的程序，也让Go用起来像是一种脚本语言。

**go build**

大多数情况下我们都会选择把程序编译成二进制来执行。我们使用`go build`来编译Go 源码文件。

```sh
# in hello directory
go build hello.go
```

执行完这条命令后，我们可以在当前目录下看到多了一个`hello`(或hello.exe)文件。运行这个文件，我们就可以看到"Hello Golang!"输出在屏幕上。

这个二进制文件的名字和你传递给`go run`的文件名字或者package名字是一致的。如果你想使用一个不同的名字或者存储到不同的路径可以使用`-o`参数。例如，我们把当前文件编译成为一个名为 "hello_golang"的二进制文件。

```sh
# usage: go build [-o output] [build fl
```



























































