# 文件权限

## 文件权限表示
```sh
drwxrwxrwx    3 root  wheel     96 Oct 13 20:30 opt
drwxr-xr-x    6 root  wheel    192 Oct  8 18:57 private
drwxr-xr-x@  64 root  wheel   2048 Oct  8 18:55 sbin
```

【文件或文件夹】【owner权限】【group权限】【others权限】【 文件数量】 【文件所有者】【文件所在组】【文件夹最后操作日期和时间】



- d 表示文件类型为 `文件夹` ， - 表示文件类型为 `文本文件`， l 表示`链接文件`
- r 读权限read 4, w 写权限write 2, x 操作权限execute 1 :   rwx 按二进制位置 111， 所以对应 421

##  修改文件权限

```sh
chmod 权限数字 文件名
chmod -R 744 /mnt/fileA  # 表示将整个/mnt/fileA目录与其中的文件和子目录的权限都设置为744
chmod o w xxx.xxx #表示给其他人授予写xxx.xxx这个文件的权限
chmod go-rw xxx.xxx #表示删除xxx.xxx中组群和其他人的读和写的权限

# u 代表所有者（user）
# g 代表所有者所在的组群（group）
# o 代表其他人，但不是u和g （other）
# a 代表全部的人，也就是包括u，g和o
# r 表示文件可以被读（read） 4
# w 表示文件可以被写（write）2
# x 表示文件可以被执行（如果它是程序的话）1
# - 表示没有任何权限 0
```



 

