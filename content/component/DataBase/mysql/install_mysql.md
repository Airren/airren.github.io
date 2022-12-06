---
title: 安装配置Mysql
---









修改表字段

```sh
# 新增一个字段
alert table rules add effect_time 
```





mysql sql 8.0 认证问题

 connect to 10.227.4.115:3306 err: this authentication plugin is not supported

![image-20210417132924881](install_mysql/image-20210417132924881.png)

```sql
alter user root@% identified with mysql_native_password by "123456"
```





```sql
CREATE DATABASE IF NOT EXISTS echo_bio DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
```

