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

```





## 插件管理

### Tmux Plugin Manager

Tmux需要安装插件可以通过Tmux Plugin Manager 这个插件进行安装， 这个插件相当于一个插件管理系统。

```sh
git clone https://github.com/tmux-plugins/tpm ~/.tmux/plugins/tpm


# vi .tmux.conf
# List of plugins
set -g @plugin 'tmux-plugins/tpm'
set -g @plugin 'tmux-plugins/tmux-sensible'

# Initialize TMUX plugin manager (keep this line at the very bottom of tmux.conf)
run '~/.tmux/plugins/tpm/tpm'


# 重新加载配置文件
 # type this in terminal if tmux is already running
 tmux source ~/.tmux.conf
```



然后就可以在Tmux中使用prefix+I(大写I)安装配置文件`.tmux.conf`中定义的插件了。



### Tmux Resurrect

当我们重启系统后Tmux的session会被清除，导致每次重启之后都要重建一堆session并且重建Pane。

`Tmux Resurrect`插件可以解决这个问题，保存和恢复Tmux Session

```sh
# 1st, add config
set -g @plugin 'tmux-plugins/tmux-resurrect'
# 2nd, instlal 
prefix +I
```



重启电脑之前先执行，保存 session ： prefix + Ctrl-s

重启之后，首先打开 tmux 然后  Restore Session： prefix + Ctrl-r

如果有多个session可以使用prefix +s 选择 session。











## 参考文档

http://www.ruanyifeng.com/blog/2019/10/tmux.html

https://www.scutmath.com/tmux_session_save_restore.html