---
title: 「Iterm」 Iterm
---





Iterm ssh 导致卡死

```sh
vi /etc/ssh/ssh_config
# 增加如下两行
ServerAliveInterval 60  
ServerAliveCountMax 2  
```

