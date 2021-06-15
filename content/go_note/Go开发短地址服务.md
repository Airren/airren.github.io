









## 2 主服务模块

###  API接口

### Mux

gorilla/mux 处理router 和handler

<img src="/Users/bytedance/Documents/JavaNote/GoNote/immoc/img/image-20191226200615163.png" alt="image-20191226200615163" style="zoom:50%;" />

```go
func main(){
  r := mux.NewRouter()
  r.HandleFunc("/", HomeHandler)
  r.HandleFunc("/product", ProductsHandler)
  r.HandleFunc("/articles",ArticlesHandler)
  http.Handle("/",r)
}
```

### 实现router和handler

#### 工程代码

```fh
➜  goshorten tree
.
├── app.go
└── main.go
```

##### main.go

```go
package main

func main() {
    a := App{}
    a.Initialize()
    a.Run(":8000")
}

```

##### app.go

```go
package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"

	"github.com/gorilla/mux"
	"gopkg.in/validator.v2"
)

// App encapsulate Env, Router and middleware
type App struct {
	Router *mux.Router
}

type shortenReq struct {
	URL                 string `json:"url" validate:"nonzero"`
	ExpirationInMinutes int64  `json:"expiration_in_minutes" validate:"min=0"`
}

type shortlinkResp struct {
	Shortlink string `json:"shortlink"`
}

// Initialize is initialization of app
func (a *App) Initialize() {
	// set log formatter
	log.SetFlags(log.LstdFlags | log.Lshortfile)
	a.Router = mux.NewRouter()
	a.initializeRouters()
}

func (a *App) initializeRouters() {

	a.Router.HandleFunc("/api/shorten", a.createShortlink).Methods("POST")
	a.Router.HandleFunc("/api/info", a.getShortlinkInfo).Methods("GET")
	a.Router.HandleFunc("/{shortlink:[a-zA-Z0-9]{1,11}}", a.redirect).Methods("GET")
}

func (a *App) createShortlink(w http.ResponseWriter, r *http.Request) {
	var req shortenReq
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		return
	}
	if err := validator.Validate(req); err != nil {
		return
	}
	defer r.Body.Close()

	fmt.Printf("%v\n", req)
}

func (a *App) getShortlinkInfo(w http.ResponseWriter, r *http.Request) {

	vals := r.URL.Query()
	s := vals.Get("shortlink")

	fmt.Printf("%s\n", s)
}

func (a *App) redirect(w http.ResponseWriter, r *http.Request) {

	vars := mux.Vars(r)
	fmt.Printf("%s\n", vars["shortlink"])
}

// Run starts listen adn server
func (a *App) Run(addr string) {
	log.Fatal(http.ListenAndServe(addr, a.Router))
}

```

#### 测试API

```sh
curl -X POST \
  http://localhost:8000/api/shorten \
  -d '{"url":"www.baidu.com","expiration_in_minutes":1}'
  
curl -X GET \
  'http://localhost:8000/api/info?shortlink=hi' 
  
curl -X GET \
  http://localhost:8000/A \
```



### 错误处理接口定义

- An interface type is defined as a set of method signatures.

  一个接口是一些列方法签名的集合

- A values of interface type can hold any value that implements those methods.

  一个接口的类型 可以接受任何实现了接口方法的对象

```go
// Go 的内置接口
type error interface{
  Error() string
}
```

```go
err := errors.New("Error message!")
if err != nil{
  fmt.Print(err)
}
```

#### 工程代码

```go
.
├── app.go
├── error.go
└── main.go
```

##### error.go

```go
// 自定义 Error
package main

type Error interface {
    error
    Status() int
}

type StatusError struct {
    Code int
    Err  error
}

func (se StatusError) Error() string {  // (se *StatusError)   区别
    return se.Err.Error()
}

func (se StatusError) Status() int {
    return se.Code
}

```

##### app.go

```go
func (a *App) createShortlink(w http.ResponseWriter, r *http.Request) {
    var req shortenReq
    if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
        responseWithError(w, StatusError{http.StatusBadRequest,
            fmt.Errorf("prase parameters failed %v", r.Body)})
        return
    }
    if err := validator.Validate(req); err != nil {
        responseWithError(w, StatusError{http.StatusBadRequest,
            fmt.Errorf("validate parameters failed %v", req)})
        return
    }
    defer r.Body.Close()

    fmt.Printf("%v\n", req)
}

func responseWithError(w http.ResponseWriter, err error) {
    switch e := err.(type) {
    case Error:
        log.Printf("HTTP %d - %s", e.Status(), e)
        responseWithJSON(w, e.Status(), e.Error())
    default:
        responseWithJSON(w, http.StatusInternalServerError,
            http.StatusText(http.StatusInternalServerError))
    }

}

func responseWithJSON(w http.ResponseWriter, code int, payload interface{}) {
    resp, _ := json.Marshal(payload)
    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(code)
    w.Write(resp)
}
```

## 3 中间件模块 Middleware

![image-20191227145907981](/Users/bytedance/Documents/JavaNote/GoNote/immoc/img/image-20191227145907981.png) 

### Log Middleware + Recover Middleware

#### middleware.go

```go
package main

import (
    "log"
    "net/http"
    "time"
)

type Middleware struct {
}

//LoggingHandler lgo the time-consuming of http request
func (m Middleware) LoggingHandler(next http.Handler) http.Handler {
    fn := func(w http.ResponseWriter, r *http.Request) {
        t1 := time.Now()
        next.ServeHTTP(w, r)
        t2 := time.Now()
        log.Printf("[%s] %q %v", r.Method, r.URL.String(), t2.Sub(t1))
    }
    return http.HandlerFunc(fn)
}

// RecoverHandler recover panic
func (m Middleware) RecoverHandler(next http.Handler) http.Handler {
    fn := func(w http.ResponseWriter, r *http.Request) {
        defer func() {
            if err := recover(); err != nil {
                log.Printf("Recover from panic: %+v", err)
                http.Error(w, http.StatusText(500), 500)
            }
        }()
        next.ServeHTTP(w, r)
    }
    return http.HandlerFunc(fn)
}
```



### Alice包的使用

- Alice provide a convenient way to chain your HTTP middleware function and the app handler.

```go
Middleware1(Middlerware2(Middlerware3(app)))

alice.New(Middlerware1, Middleware2, Middleware3).Then(app)
```



## 4 存储模块 Storage 

### 如何生成短地址

<img src="/Users/bytedance/Documents/JavaNote/GoNote/immoc/img/image-20191227155742062.png" alt="image-20191227155742062" style="zoom:50%;" />

-  INCR key

  ```sh
  redis> SET mykey "10"
  "OK"
  redis>INCR mykey 
  (integer) 11
  redis>get mykey
  "11"
  redis>
  ```

  > Redis 客户端
  >
  > Medis
  > TablePlus
  >
  > Redis-cli

### Storage接口设计

<img src="/Users/bytedance/Documents/JavaNote/GoNote/immoc/img/image-20191227160110874.png" alt="image-20191227160110874" style="zoom:50%;" />

### 实现shorten 方法



![image-20200103164939939](/Users/bytedance/Documents/JavaNote/GoNote/immoc/img/image-20200103164939939.png)

 