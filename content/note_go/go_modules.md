---
title: 「Go」依赖管理 Go Modues/ GOPATH
subtitle: '学会了go的依赖管理才能灵活使用Go'
author: Airren
catalog: true
header-img: ''
weight: 1
p: go_basic/go_modules
date: 2020-08-04 23:53:48
---


> 不要吝啬你的批评与感悟，敬请留言，我们一起进步。

> 如果你有过以下问题，欢迎阅读文章，提出意见与建议
>
> 1. go mod 怎么使用？
> 2. GOPATH是什么？
> 3. GO111MODULE=""  这个参数决定了什么？
> 4. go get、go download 有什么区别？
> 5. import到底import的什么东西？



## 依赖管理工具

用过Java 的同学都知道，对依赖的管理经历了从**原始的手动引入jar**包，到使用**maven等自动化管理工具**去引入第三方依赖的过程，从而可以使用别人已经开发好的优秀工具。如果使用过Python的同学可能会熟练的使用pip install 第三方的工具包。Java 和Python的第三方工具包都是集中式管理的，使用maven 或者是pip 都是从对应的管理中心下载更新依赖。当然还有 npm、yarn、gradle等其他语言的依赖版本工具。



在go语言中，第三方依赖的管理工具经过了一个漫长的发展过程。在GO1.11 发布之前govendor、dep等工具百花齐放。直到go mod 出现，开始一统天下。go 的依赖非常简单粗暴，只要依赖源码就可以了。例如：

```go
import  "github.com/jinzhu/gorm"
```

`github.com/jinzhu/gorm` 就是gorm的GitHub项目路径。

## GOPATH时期

Go 在1.11 之前使用`GOPATH模式`进行依赖的管理。安装部署go环境，使用go 进行开发的时候强制被要求要设置`GOPATH`（当然安装过程中也会默认指定`$GOPATH=~/go`）。 要在`GOPATH`路径下新建 `/src /bin /pkg`文件夹。

```sh
➜ ~/go
├── bin  # 存储go编译生成的二进制可执行文件，一般会把该路径配置到PATH中,PATH=$PATH:$GOPATH/bin
├── pkg  # 存储预编译的目标文件，以加快后续的编译速度 
└── src  # 存储Go的源代码，一般以$GOPATH/src/github.com/foo/bar的路径存放
```

```sh
➜  ~ go env |grep GOPATH
GOPATH="/Users/bytedance/go"
```



在这种模式下，如果使用`go get` 拉取外部依赖会自动下载并安装到`$GOPATH/src` 目录下。

这种模式下，`go get`没有版本管理的概念，无法处理依赖不同版本的问题，因为同一个依赖都存在同一个路径下面。

在Go官方还没有推出Go Modules 的时候，go的依赖管理工具可谓是百花齐放，例如 `govendor`， `dep`，但是最终Go Modules发布，平息了诸侯割据的局面。

## GO Modules

Go1.11 开始推出Go Modules ,Go1.13开始不再推荐使用GOPATH。意思就是说你可以在任何路径下存放你的Go源码文件, 不用再像以前一样非得放到`$GOPATH/src`中。 每一个go项目 都是一个 Modules。vgo 是Go Modules的前身。

在Go Modules环境下出现了一个很重要的环境变量`GO111MODULE`

```sh
➜ ~ go env
GO111MODULE="auto"  # 当为 aotu 模式时候如果当前目录中有go.mod则为GO Module模式
GOPROXY="https://proxy.golang.org,direct"
GONOPROXY=""
GOSUMDB="sum.golang.org"
GONOSUMDB=""
GOPRIVATE=""
```

如果要对go 的环境变量进行设置，可以使用

```sh
go env -w GO111MODULE=on  # 设置go 环境变量
go env -u # 恢复初始设置
```



### GO111MODULE

`GO111MODULE` 这个环境变量是用来作为使用Go Modules 的开关。可以说这个变量是历史产物，很有肯能会在将来Go的新版本中去除掉。

```sh
GO111MODULE="auto" # 只要项目包含了go.mod 文件的话就启用Go modules， 在Go1.11-1.14 中是默认值
GO111MODULE="on"   # 启用Go Modules
GO111MODULE="off"  # 禁用Go Modules， 对老的项目进行兼容
```

### GOPROXY

GOPROXY 是Go Modules的代理，可以通过镜像站点快速拉取（很多公司都会设置自己的镜像站点），可以设置多个代理。

```sh
GOPROXY="https://proxy.golang.org,direct"
```

`direct`的意思是如果通过代理获取不到go get 就会通过源地址直接去抓取

### go mod 

创建Go Modules的基本命令

```sh
➜  ~ go mod
Go mod provides access to operations on modules.

Note that support for modules is built into all the go commands,
not just 'go mod'. For example, day-to-day adding, removing, upgrading,
and downgrading of dependencies should be done using 'go get'.
See 'go help modules' for an overview of module functionality.
# 所有的go commands 都支持 modules。
Usage:

        go mod <command> [arguments]

The commands are:

        download    download modules to local cache
        edit        edit go.mod from tools or scripts
        graph       print module requirement graph
        init        initialize new module in current directory 
        tidy        add missing and remove unused modules
        vendor      make vendored copy of dependencies
        verify      verify dependencies have expected content
        why         explain why packages or modules are needed

Use "go help mod <command>" for more information about a command.
```



**go mod init** 

 在当前目录初始化一个新的module， 就是说将该目录下的工程文件初始化为一个`Go Module.`

如果当前目录在GOPATH中，这条命令无需传入参数，参数默认为 `GOPATH/src` 到该目录的`相对路径`。

```sh
➜  demo pwd
/Users/bytedance/go/src/github.com/airren/demo  # 当前路径
➜  demo go mod init
go: creating new go.mod: module github.com/airren/demo
➜  demo ls
go.mod
➜  demo cat go.mod
module github.com/airren/demo

go 1.14
➜  demo
```

如果当前目录不在GOPATH中，要`手动指定`module的名字

```sh
➜  gotest pwd                                 
/Users/bytedance/Desktop/gotest
➜  gotest go mod init       # 如果不在GOPATH路径下                  
go: cannot determine module path for source directory /Users/bytedance/Desktop/gotest (outside GOPATH, module path must be specified)

Example usage:
        'go mod init example.com/m' to initialize a v0 or v1 module
        'go mod init example.com/m/v2' to initialize a v2 module

Run 'go help mod init' for more information.
➜  gotest go mod init github.com/airren/gotest 
go: creating new go.mod: module github.com/airren/gotest
➜  gotest cat go.mod 
module github.com/airren/gotest

go 1.14
```



**go mod download**

`go mod download` 命令会把package下载到 `GOPATH/pkg/mod`路径下，下载时要指定版本可以用`@latest`。可以任意路径下使用`go mod download`

```s
➜  jinzhu pwd
/Users/bytedance/go/pkg/mod/github.com/jinzhu
➜  jinzhu ls
gorm@v1.9.11-0.20190912141731-0c98e7d712e2    inflection@v1.0.0
gorm@v1.9.12                                  now@v1.0.1
inflection@v0.0.0-20180308033659-04140366298a now@v1.1.1
➜  jinzhu go mod download -x github.com/jinzhu/gorm@v1.9.8  # 要指定版本
➜  jinzhu ls
gorm@v1.9.11-0.20190912141731-0c98e7d712e2    inflection@v1.0.0
gorm@v1.9.12                                  now@v1.0.1
gorm@v1.9.8                                   now@v1.1.1
inflection@v0.0.0-20180308033659-04140366298a
➜  jinzhu cd gorm@v1.9.8
```



**vendor**

`go mod vendor`会在项目中创建一个vendor文件夹。在编译时，可以使用 `-mod=vendor` ，使用代码主目录文件夹下`vendor`目录满足依赖获取，`go build -mod=vendor`。此时，`go build` 忽略`go.mod` 中的依赖，（这里仅使用代码root目录下的vendor其他地方的将忽略）

```
GOFLAGS=-mod=vendor` 设置顶级vendor作为依赖 `go env -w GOFLAGS="-mod=vendor"` 进行设置。 取消 `go env -w GOFLAGS="-mod="
```

### import

> import 导入的是文件存储的`相对路径`（不能使用绝对路径）或者是`Modules Name`，并不是package的name，但是在调用Function的时候使用的是`package的name`。

import 首先会从`$GOROOT`中寻找，然后从`$GOPATH/src` 中寻找，如果是以`./` 或者 `../` 开头的import 直接去对应的相对路径寻找。

import 时候是`不区分大小写`的，所以在go项目中folder的name尽量是小写，可以有下划线_, 但是packagename一定不能有` _`，否则golint会有提示。

举个例子🌰文件结构如图所示

```sh
gotest
├── format_print
│   └── colorprintpath.go  # 文件名并不影响 import
└── sdemo
    ├── demo.go
```

**colorprintpath.go**

```go
package colorprintfile // 调用方法时候通过package name 调用
import "fmt"
// NewPirnt is the new format print  公开方法要有注释
func NewPrint(content string) {
	fmt.Printf("This is the content: %v \n", content)
}
```



**demo.go**  调用上述方法

```go
package main
import  "../format_print"  // import 使用的是相对路径
func main(){
	colorprintfile.NewPrint("hello")	 // 调用package中的公开方法
}
```

如果使用了`go mod`， 在项目文件下有了go.mod,  文件中会有

```sh
module github.com/airren/gotest
```

此时就不能使用相对路径引用package, 要通过`modules的方式`引用

```go
package main
import  "github.com/airren/gotest/format_print"
func main(){
	colorprintfile.NewPrint("hello")	 // 调用package中的公开方法
}
```



### go get 

可以在任意路径下执行`go get` 

- 用于从远程代码仓库(github, gitlab,gogs)上下载并安装代码包

- 支持的代码版本控制系统有: Git , Mercurial(hg)，SVN, Bazaar

- 指定的代码包会被下载到`$GOPATH`中包含的第一个工作区的src目录中，然后再安装

  常用参数

  ```sh
  -d         # 只执行下载动作，而不执行安装动作
  -fix       # 在下载代码包后先执行修正动作，而后再进行编译和安装
  -u         # 利用网络来更新已有的代码包及其依赖包
  -x         # 显示过程
  ```

例如使用`go get` 获取 gorm。 通过`-x` 参数可以展示详细的过程。

```sh
➜  ~ go get -u -x github.com/jinzhu/gorm
cd .
git clone -- https://github.com/jinzhu/gorm /Users/bytedance/go/src/github.com/jinzhu/gorm
cd /Users/bytedance/go/src/github.com/jinzhu/gorm
git submodule update --init --recursive
cd /Users/bytedance/go/src/github.com/jinzhu/gorm
git show-ref
cd /Users/bytedance/go/src/github.com/jinzhu/gorm
git submodule update --init --recursive
cd /Users/bytedance/go/src/github.com/jinzhu/inflection
git config remote.origin.url
cd /Users/bytedance/go/src/github.com/jinzhu/inflection
git pull --ff-only
cd /Users/bytedance/go/src/github.com/jinzhu/inflection
git submodule update --init --recursive
cd /Users/bytedance/go/src/github.com/jinzhu/inflection
git show-ref
cd /Users/bytedance/go/src/github.com/jinzhu/inflection
git submodule update --init --recursive
WORK=/var/folders/pz/w7jm4wm933lcspm82kff26600000gn/T/go-build644292157
```



如果在具有`go.mod` 的项目的文件夹下使用go get , 会将对应的依赖以及版本写入`go.mod,`  go.sum 是自动生成的，具体介绍可以查看[https://studygolang.com/articles/25658](https://studygolang.com/articles/25658)

```sh
➜  gotest cat go.mod 
module github.com/airren/gotest

go 1.14
➜  gotest cat go.sum 
cat: go.sum: No such file or directory
➜  gotest go get -u github.com/jinzhu/gorm/  # 拉取gorm  
go: github.com/jinzhu/gorm upgrade => v1.9.12
➜  gotest cat go.mod 
module github.com/airren/gotest

go 1.14

require github.com/jinzhu/gorm v1.9.12 // indirect  # 新增依赖， 未被使用或者间接使用会有 indirect
➜  gotest cat go.sum 
github.com/denisenkom/go-mssqldb v0.0.0-20191124224453-732737034ffd/go.mod h1:xbL0rPBG9cCiLr28tMa8zpbdarY27NDyej4t/EjAShU=
github.com/erikstmartin/go-testdb v0.0.0-20160219214506-8d10e4a1bae5/go.mod h1:a2zkGnVExMxdzMo3M0Hi/3sEU+cWnZpSni0O6/Yb/P0=
github.com/go-sql-driver/mysql v1.4.1/go.mod h1:zAC/RDZ24gD3HViQzih4MyKcchzm+sOG5ZlKdlhCg5w=
github.com/golang-sql/civil v0.0.0-20190719163853-cb61b32ac6fe/go.mod h1:8vg3r2VgvsThLBIFL93Qb5yWzgyZWhEmBwUJWevAkK0=
github.com/golang/protobuf v1.2.0/go.mod h1:6lQm79b+lXiMfvg/cZm0SGofjICqVBUtrP5yJMmIC1U=
github.com/jinzhu/gorm v1.9.12 h1:Drgk1clyWT9t9ERbzHza6Mj/8FY/CqMyVzOiHviMo6Q=
github.com/jinzhu/gorm v1.9.12/go.mod h1:vhTjlKSJUTWNtcbQtrMBFCxy7eXTzeCAzfL5fBZT/Qs=
github.com/jinzhu/inflection v1.0.0 h1:K317FqzuhWc8YvSVlFMCCUb36O/S9MCKRDI7QkRKD/E=
github.com/jinzhu/inflection v1.0.0/go.mod h1:h+uFLlag+Qp1Va5pdKtLDYj+kHp5pxUVkryuEj+Srlc=
github.com/jinzhu/now v1.0.1/go.mod h1:d3SSVoowX0Lcu0IBviAWJpolVfI5UJVZZ7cO71lE/z8=
github.com/lib/pq v1.1.1/go.mod h1:5WUZQaWbwv1U+lTReE5YruASi9Al49XbQIvNi/34Woo=
github.com/mattn/go-sqlite3 v2.0.1+incompatible/go.mod h1:FPy6KqzDD04eiIsT53CuJW3U88zkxoIYsOqkbpncsNc=
golang.org/x/crypto v0.0.0-20190308221718-c2843e01d9a2/go.mod h1:djNgcEr1/C05ACkg1iLfiJU5Ep61QUkGW8qpdssI0+w=
golang.org/x/crypto v0.0.0-20190325154230-a5d413f7728c/go.mod h1:djNgcEr1/C05ACkg1iLfiJU5Ep61QUkGW8qpdssI0+w=
golang.org/x/crypto v0.0.0-20191205180655-e7c4368fe9dd/go.mod h1:LzIPMQfyMNhhGPhUkYOs5KpL4U8rLKemX1yGLhDgUto=
golang.org/x/net v0.0.0-20180724234803-3673e40ba225/go.mod h1:mL1N/T3taQHkDXs73rZJwtUhF3w3ftmwwsq0BUmARs4=
golang.org/x/net v0.0.0-20190404232315-eb5bcb51f2a3/go.mod h1:t9HGtf8HONx5eT2rtn7q6eTqICYqUVnKs3thJo3Qplg=
golang.org/x/sys v0.0.0-20190215142949-d0b11bdaac8a/go.mod h1:STP8DvDyc/dI5b8T5hshtkjS+E42TnysNCUPdjciGhY=
golang.org/x/sys v0.0.0-20190412213103-97732733099d/go.mod h1:h1NjWce9XRLGQEsW7wpKNCjG9DtNlClVuFLEZdDNbEs=
golang.org/x/text v0.3.0/go.mod h1:NqM8EUOU14njkJ3fqMW+pc6Ldnwhi/IjpwHt7yyuwOQ=
google.golang.org/appengine v1.4.0/go.mod h1:xpcJRLb0r/rnEns0DIKYYv+WjYCduHsrkT7/EB5XEv4=

```

> 不要吝啬你的批评与感悟，敬请留言，我们一起进步。





## 参考资料

> https://github.com/golang/go/wiki/Modules
>
> https://blog.golang.org/using-go-modules
>
> https://juejin.im/post/5e57537cf265da57584da62b
>
> https://learnku.com/go/t/39086
>
> https://studygolang.com/articles/25658