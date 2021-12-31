---
title: 「tmux」虚拟终端
---





> ​		当使用ssh 连接一台远程计算机的时候，如果此时正在执行一个进程，突然断网了，那么这个进程也会被迫中断了。当重新ssh连接到这台远程计算机的时候，已经找不到之前正在执行的进程了。因为上一次连接的会话(Session)已经终止， 这次的重新连接又新建了一个会话。如果你遇到过这种问题你就会发现： 会话和进程是绑定的，会话终止，当前正在执行的进程也会终止。
>
> ​		为了解决上述问题，你可以尝试使用下Tmux。



## 安装及基本使用

```sh
# 安装在需要远程连接的远程服务器上
apt install tmux 
```



```sh
# 新建
tmux new -s <session name>

# 切换到某个session
tmux attach -t <session name>

# 退出某个session，依旧保留进程
tmux detach

# 分隔窗口
tmux split-window
tmux split-window -h # 水平分隔

# 切换窗口 【Ctrl】+【b】 然后按下 【；】

```



## 快捷键

前缀键 【Ctrl】+【b】。先按下前缀键后，在使用功能键。



## copy-mode use vi shortcuts

```sh
cat <<EOF | tee -a ~/.tmux.conf
setw -g mode-keys vi
set -g @plugin 'tmux-plugins/tpm'
set -g @plugin 'tmux-plugins/tmux-sensible'
set -g @plugin 'tmux-plugins/tmux-resurrect'



run '~/.tmux/plugins/tpm/tpm'
E
```





## 参考文档

http://www.ruanyifeng.com/blog/2019/10/tmux.html