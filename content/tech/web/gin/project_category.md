---
title: 「Gin」 项目目录
subtitle: ''
author: Airren
catalog: true
header-img: ''
weight: 3
date: 2020-09-23 00:07:14
---







## Gin 项目目录

```sh
├─ Project Name
│  ├─ config          //配置文件
│     ├── ...
│  ├─ controller      //控制器层，验证提交的数据，将验证完成的数据提交给service
│     ├── ...
│  ├─ service         //业务层， 只完成业务逻辑得开发，不进行数据库的操作
│     ├── ...
│  ├─ repository      //数据库操作层  dal/ dao; 数据库操作层，写，多表插入，多表查询，不写业务代码
│     ├── ...
│  ├─ model           //数据库ORM
│     ├── ...
│  ├─ entity          //实体 写返回数据的结构体。写controller层方法参数验证的结构体
│     ├── ...
│  ├─ proto           //proto文件 写 gRPC 的 *.pb.go 文件。
│     ├── ...
│  ├─ router          //路由
│     ├── middleware  //路由中间件 （鉴权，日志，异常捕获）
│         ├── ...
│     ├── ...
│  ├─ util            //工具类，项目通用的工具包
│     ├── ...
│  ├─ vendor          //扩展包 第三方依赖，通常使用go mod 管理
│     ├── ...
│  ├─ main.go         //入口文件
```

参考资料：

https://cloud.tencent.com/developer/article/1490384

