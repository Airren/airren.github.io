查看端口的占用情况

```sh
netstat -atunlp

# 用来查看系统当前网络状态信息， 包括端口、连接情况等
# -t: TCP端口
# -u: UDP端口
# -l: 仅显示监听Socket LISTEN状态的Socket
# -p: 显示进程标识符和程序名称，每一个socket都属于一个程序
# -n: 不进行DNS解析
# -a: 显示所有连接的端口




lsof
# 列出当前系统打开文件(list open files)
# -i:[num] 指定端口
```

```sh
nslookup # 域名解析
host
dig    
```
