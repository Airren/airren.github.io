---
title: 「Gin」Gin Http 请求处理
subtitle: ''
author: Airren
catalog: true
header-img: ''
weight: 2
date: 2020-09-08 00:01:52
---



https://www.yoytang.com/go-gin-doc.html#%e8%b7%af%e7%94%b1(Router)



## 1. Gin的HTTP请求

### 1.1 Gin支持的各种HTTP请求

```go
func main() {
	// Creates a gin router with default middleware:
	// logger and recovery (crash-free) middleware
	router := gin.Default()

	router.GET("/someGet", getting)
	router.POST("/somePost", posting)
	router.PUT("/somePut", putting)
	router.DELETE("/someDelete", deleting)
	router.PATCH("/somePatch", patching)             // 
	router.HEAD("/someHead", head)                   //
	router.OPTIONS("/someOptions", options)

	// By default it serves on :8080 unless a
	// PORT environment variable was defined.
	router.Run()
	// router.Run(":3000") for a hard coded port
}
```


https://www.jianshu.com/p/a31e4ee25305




