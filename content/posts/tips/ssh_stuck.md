---
title: 「SSH」Iterm ssh长时间卡死
---

```sh
sudo vi /etc/ssh/ssh_config

# 增加如下两行
ServerAliveInterval 50 #每隔50秒就向服务器发送一个请求
ServerAliveCountMax 3  #允许超时的次数，一般都会响应

vim /etc/ssh/sshd_config
，找到ClientAliveInternal 将后面的数字0改为60 ，注意去掉前面的#，因为如果最前面是井号的话是注释掉的
```

Iterm ssh 导致卡死

```sh
vi /etc/ssh/ssh_config
# 增加如下两行
ServerAliveInterval 60  
ServerAliveCountMax 2  
```

