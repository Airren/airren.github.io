---
title: Unix Init
---

## Tools

```sh
apt install vim git tmux golang
```

## Vim

```sh
# ~/.vimrc
cat <<EOF | tee -a ~/.vimrc
set nu
syntax on
inoremap jj <ESC>


set fileencodings=utf-8,ucs-bom,gb18030,gbk,gb2312,cp936
set termencoding=utf-8
set encoding=utf-8

" show existing tab with 4 spaces width
set tabstop=4
" when indenting with '>', use 4 spaces width
set shiftwidth=4
" On pressing tab, insert 4 spaces
set expandtab

EOF
```

## oh my zsh

```sh
sudo apt install -y  zsh
sh -c "$(wget https://raw.github.com/ohmyzsh/ohmyzsh/master/tools/install.sh -O -)"

# sudo chsh -s $(which zsh)

```

## Golang

```sh
sudo apt install golang

echo '
GOPATH=~/go
GOBIN=$GOPATH/bin
PATH=$PATH:$GOBIN
'| tee -a ~/.bashrc
```

### Proxy

```sh
echo 'function proxy_on() {
    export no_proxy="localhost,127.0.0.1/8,.sh.intel.com,.localdomain.com,10.0.0.0/8,172.17.0.1/16,172.18.0.1/16"

    local proxy="http://proxy-prc.intel.com:913"
    export http_proxy="$proxy" \
           https_proxy=$proxy \
           all_proxy=$proxy \
           ftp_proxy=$proxy \
           rsync_proxy=$proxy \
           HTTP_PROXY=$proxy \
           HTTPS_PROXY=$proxy \
           FTP_PROXY=$proxy \
           RSYNC_PROXY=$proxy
}

function proxy_off(){
    unset http_proxy https_proxy all_proxy ftp_proxy rsync_proxy \
          HTTP_PROXY HTTPS_PROXY FTP_PROXY RSYNC_PROXY no_proxy
    echo -e "Proxy environment variable removed."
}

proxy_on' >>  ~/.bashrc


# Install zsh
cat <<EOF | sudo tee /etc/apt/apt.conf.d/proxy.conf
Acquire::http::Proxy "http://proxy-prc.intel.com:913";
Acquire::https::Proxy "http://proxy-prc.intel.com:913";
EOF

cat <<EOF |  tee /etc/apt/apt.conf.d/proxy.conf
Acquire::http::Proxy "http://proxy.ims.intel.com:911";
Acquire::https::Proxy "http://proxy.ims.intel.com:911";
EOF


 sudo snap set system proxy.https="http://proxy-prc.intel.com:913"
 sudo snap set system proxy.http="http://proxy-prc.intel.com:913"
```

## Font

`hack`



## Set timezone

```sh
sudo ls
export http_proxy=http://proxy-prc.intel.com:913
timedatectl set-timezone Asia/Shanghai 

 sudo date -s "$(curl -H'Cache-Control:no-cache' -sI google.com | grep '^Date:' | cut -d' ' -f3-6)Z"

host corp.intel.com
Ω
 sudo hwclock -w

# sync time to hardware
sudo apt-get install ntpdate
sudo ntpdate cn.pool.ntp.org

 sudo hwclock -w
  sudo hwclock --systohc
  
  
  
  host corp.intel.com
corp.intel.com has address 10.104.196.174
corp.intel.com has address 10.104.196.228
corp.intel.com has address 10.104.196.105
corp.intel.com has address 10.104.192.105
corp.intel.com has address 10.104.196.231
corp.intel.com has address 10.104.192.241
corp.intel.com has address 10.104.193.80
corp.intel.com has address 10.104.192.175
corp.intel.com has address 10.104.192.179
corp.intel.com has address 10.104.193.49
corp.intel.com has address 10.104.196.177
corp.intel.com has address 10.104.196.144
corp.intel.com has address 10.104.195.154
corp.intel.com has address 10.104.195.153
```

### Add new User

```sh
# useradd
sudo adduser airren
sudo usermod -aG sudo airren

sudo vi /etc/group
```

Wait for network

```sh
network:
  version: 2
  wifis:
    wlp3s0:
      access-points:
        HoneyHouse_5.0G:
          password: 1q2w3e4r%T
      dhcp4: true
      optional: true
```

```sh
sudo netplan generate

sudo netplan apply
```

## Ubuntu nick name

今天在Build Docker image的时候发现`sgx-sdk-demo`的base images是`ubuntu:bionic`, 然后设置的apt source list 也是`"....intel-sgx/sgx_repo/ubuntu focal main"`。同时 docker image `ubunt:18.04` 和 `ubuntu:bionic` 的Image ID 是完全相同的，猜测 bionic 应该是 ubuntu:18.04 的别名。于是查了[Ubuntu 的release Note](https://wiki.ubuntu.com/Releases) 果真如此。

## update the ubuntu Linux kernel

```sh
apt-cache search linux-image
sudo apt-get install linux-image-<your_version_choice> linux-headers-<your_version_choice> linux-image-extra-<your_version_choice>

# must reboot you machine
# https://linuxhint.com/update_ubuntu_kernel_20_04/
# https://packages.ubuntu.com/focal-updates/kernel/
```

- flux add proxy!!!! 装的时候手动修改env

```sh
git config --global --add http.proxy http://proxy-prc.intel.com:913
git config --global --add https.proxy http://proxy-prc.intel.com:913



export http_proxy=http://proxy-prc.intel.com:913
export https_proxy=http://proxy-prc.intel.com:913




cat <<EOF | sudo tee /etc/apt/apt.conf.d/proxy.conf
Acquire::http::Proxy "http://proxy-prc.intel.com:913";
Acquire::https::Proxy "http://proxy-prc.intel.com:913";
EOF

cat <<EOF |  tee /etc/apt/apt.conf.d/proxy.conf
Acquire::http::Proxy "http://proxy-prc.intel.com:913";
Acquire::https::Proxy "http://proxy-prc.intel.com:913";
EOF
```







### kubectl autocomplete







#### ssh set proxy

```sh
# Host github.com
#       Hostname ssh.github.com
#       # ProxyCommand nc -X connect -x child-prc.intel.com:914 %h %p
#       # # ProxyCommand connect -H child-prc.intel.com:914 %h %p
#       Port 443
#       # ServerAliveInterval 20
#       # User git
# Host github.com
#    IdentityFile ~/.ssh/id_rsa
#    ProxyCommand nc -x child-prc.intel.com:1080 %h %p


# ProxyCommand connect -S proxy-prc.intel.com:1080  %h %p
Host node-2
  HostName 124.223.99.93
  Port 3302
  User airren
  ProxyCommand connect -S proxy-prc.intel.com:1080  %h %p
  # ProxyJump  proxy-prc.intel.com:1080

Host sdewan-sgx.sh.intel.com
  HostName sdewan-sgx.sh.intel.com
  User airren

Host airrens-mini.sh.intel.com
  HostName airrens-mini.sh.intel.com
  User airren

Host sdewan.sh.intel.com
  HostName sdewan.sh.intel.com
  User ubuntu

Host 10.239.154.53
  HostName 10.239.154.53
  User airren

```







## Docker utils

```sh
HOSTNAME=chrome
VNC_SCREEN_SIZE=1920x1080

 docker run -d -p 5909:5900 --name chrome \
 -e VNC_SCREEN_SIZE=1920x1080 -e HOSTNAME=chrome\
 -v /home/airren/NFSShared:/data \
 siomiz/chrome 
```



## DNS conf





linux检测ssd 寿命

https://blog.51cto.com/u_12461028/5454231

```sh
# Wear Levelling Count(颗粒平均擦写次数)
smartctl -a /dev/sda
# 
smartctl -l devstat /dev/sdb
```







## Ipmi Manger



```sh
# Fan to a specific percent
ipmitool -I lanplus -H 10.0.0.218 -U root -P calvin raw 0x30 0x30 0x01 0x00
ipmitool -I lanplus -H 10.0.0.218 -U root -P calvin raw 0x30 0x30 0x02 0xff 0x08

# Power manager  reset/off/on/status
ipmitool -I lanplus -H 10.0.0.218 -U root -P calvin power status
ipmitool -I lanplus -H 10.0.0.218 -U root -P calvin power on
ipmitool -I lanplus -H 10.0.0.218 -U root -P calvin power off
ipmitool -I lanplus -H 10.0.0.218 -U root -P calvin power reset
 
 
```



ubuntu enable cgroup v2

```sh
enable cgroup v2 manually by modifying the kernel boot arguments.

sudo sed -i -e 's/^GRUB_CMDLINE_LINUX=""/GRUB_CMDLINE_LINUX="systemd.unified_cgroup_hierarchy=1"/' /etc/default/grub
sudo update-grub
sudo mkdir -p /etc/systemd/system/user@.service.d
cat <<EOF | sudo tee /etc/systemd/system/user@.service.d/delegate.conf
[Service]
Delegate=cpu cpuset io memory pids
EOF
sudo systemctl daemon-reload
sudo reboot
```







### 开机挂载硬盘	



![image-20230320165358636](./Linux_init.assets/image-20230320165358636.png)

```sh
sudo blkid /dev/sdb
UUID="2e92d81f-8e9d-4b89-b573-2c39c1f1920b" /var/lib/kubelet ext4 defaults 0 2

# vi /etc/fstab
[UUID=************] [挂载磁盘分区] [挂载磁盘格式] 0 2 

添加的行是13行，UUID和挂载目录/home/sqp/Data以及硬盘格式ext4

第一数字0，0是开机不检查磁盘，1是开机检查磁盘

第二个数2，0表示交换分区，1表示启动分区，2表示普通分区

然后执行全部挂载，或者重启生效
sudo mount -a
```



##### 附：fstab中 每个字段代表的含义

###### 第一列可以是实际分区名，也可以是实际分区的卷标（Lable）。

如果磁盘是SATA接口，且有多个磁盘，则每个磁盘被标记为 /dev/hda 、 /dev/hdb、 /dev/hdc 等以此类推；而每个磁盘的分区被标记为 /dev/hda1、 /dev/hda2等。
 如果磁盘是SCSI类型，则多个磁盘会被分别标记为 /dev/sda、/dev/sdb等等。分区同理。

###### 第二列是挂载点。

挂载点必须为当前已经存在的目录，为了兼容起见，最好在创建需要挂载的目标目录后，将其权限设置为777，以开放所有权限。

###### 第三列为此分区的文件系统类型。

Linux可以使用ext2、ext3等类型，此字段须与分区格式化时使用的类型相同。也可以使用 auto 这一特殊的语法，使系统自动侦测目标分区的分区类型。auto通常用于可移动设备的挂载。

###### 第四列是挂载的选项，用于设置挂载的参数。

常见参数如下：
 auto: 系统自动挂载，fstab默认就是这个选项
 defaults: rw, suid, dev, exec, auto, nouser, and async.
 noauto 开机不自动挂载
 nouser 只有超级用户可以挂载
 ro 按只读权限挂载
 rw 按可读可写权限挂载
 user 任何用户都可以挂载
 请注意光驱和软驱只有在装有介质时才可以进行挂载，因此它是noauto

###### 第五列是dump备份设置。

当其值设置为1时，将允许dump备份程序备份；设置为0时，忽略备份操作；

###### 第六列是fsck磁盘检查设置。

其值是一个顺序。当其值为0时，永远不检查；而 / 根目录分区永远都为1。其它分区从2开始，数字越小越先检查，如果两个分区的数字相同，则同时检查。



作者：二班老许
链接：https://www.jianshu.com/p/5adb37efd478
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。





### DNS Config

使用传统方法，手工修改 /etc/resolv.conf

- 修改 /etc/NetworkManager/NetworkManager.conf 文件，在main部分添加 “dns=none” 选项：

  ```
  [main]
  plugins=ifcfg-rh
  dns=none
  ```

- NetworkManager重新装载上面修改的配置

  ```
  systemctl restart NetworkManager.service
  ```

- 手工修改 /etc/resolv.conf

  ```
  nameserver 114.114.114.114
  nameserver 8.8.8.8
  
  
  
  
  search *
  nameserver 10.248.2.1
  nameserver 10.248.2.4
  ```





```sh
shell which "ls" >/dev/null 2>&1;if [ $? -eq 0 ]; then echo "podman"; else echo "docker"; fi
```



atop 命令

https://www.oldcai.com/linux/atop-dsk/





## iptables 锁	

iptables相关命令(如iptables-restore)在向内核写入iptables规则时候，为了避免多个实例并发写入，会利用file lock来做同步，Linux下该文件一般为`/run/xtables.lock`.  iptables-restore 在执行时，首先尝试获取file lock，如果当前有其他进程持有锁，则退出。

对于要调用iptables相关命令的Pod, 如kube-proxy, kube-router 以及客户侧的HostNetwork Pod，如果没有挂载该文件，可能发生并发写入的错误。

```sh
Failed to execute iptables-restore: exit status 1 (iptables-restore: line xxx failed)
```



对于要调用iptables相关命令的Pod,需要将Host的`/run/xtables.lock`文件挂载到Pod中， 配置方式如下

```yaml
        volumeMounts:
        -   mountPath: /run/xtables.lock
          name: xtables-lock
          readOnly: false
      volumes:
      -   hOStPath:
          path: /run/xtables.lock
          type: FileOrCreate
        name: xtables-lock
```



iptables-restore 提供了一个-w(--wait) 选项，如设置-w=5s时，iptables-restore 会在拿锁操作堵塞5s，这5s内一旦其他进程释放锁，iptables-restore可以继续操作。





# netdata



ubuntu config wifi https://ubuntu.com/core/docs/networkmanager/configure-wifi-connections

- 原因：存在多网卡时，只要有任意一个网卡没有连接到网络，就会长时间在此状态等待连接

- 解决方案：等待进入系统后，编辑 `/etc/netplan/00-installer-config.yaml`，将不是一直在线的网卡设为**可选**。

  > ```yaml
  > # This is the network config written by 'subiquity'
  > network:
  > ethernets:
  > enp1s0:
  > dhcp4: true
  > enp3s0:
  > dhcp4: true
  > optional: true		# 添加这一行，将网卡 enp3s0 设为非必须的。
  > version: 2
  > ```
  >
  > 然后执行：
  >
  > ```shell
  > sudo netplan apply
  > ```







文件系统错误



使用`fsck.ext4 -yvf /dev/sda` 尝试修复





```sh
grafana-cli admin reset-admin-password <new password>
```





v2ray

```sh
cat v2ray/config.json

{
    "inbounds": [
        {
            "port": 10086,
            "protocol": "vmess",
            "settings": {
                "clients": [
                    {
                        "id": "b831381d-6324-4d53-ad4f-8cda48b30811"
                    }
                ]
            }
        }
    ],
    "outbounds": [
        {
            "protocol": "freedom"
        }
    ]
}


docker run -d --name v2ray -v /home/ubuntu/v2ray/config.json:/etc/v2ray/config.json -p 10086:10086 v2fly/v2fly-core run -c /etc/v2ray/config.json

docker run  -e PANDORA_CLOUD=cloud -e PANDORA_SERVER=0.0.0.0:8899 -p 8899:8899 -d pengzhile/pandora
```



Terraform

https://lonegunmanb.github.io/introduction-terraform/

## Grub

Ubuntu update grub

```sh
vi  /etc/default/grub

sudo update-grub
sudo reboot
```



选取image number `1> 4` ， `>`后面要有空格

```sh
vi /boot/grub/grub.cfg
```





修改ubuntu 启动参数, Hugepage 设置

```sh
default_hugepages=1G hugepagesz=2M hugepages=1024 isolcpus=0-2
```





## configure ubuntu as a router

```shell
sudo iptables -t nat -A POSTROUTING -o eth1 -j MASQUERADE
sudo iptables -A FORWARD -i eth1 -o eth0 -m state --state RELATED,ESTABLISHED -j ACCEPT
sudo iptables -A FORWARD -i eth0 -o eth1 -j ACCEPT


vi /etc/sysctl.conf

net.ipv4.ip_forward = 1   # 没有则添加，有修改为1（0禁止，1开启）

sysctl -p




sudo iptables -t nat -A POSTROUTING -s 10.8.0.0/24 -o ens160 -j MASQUERADE
sudo iptables -t nat -A POSTROUTING -s 10.0.12.0/24 -o ens160 -j MASQUERADE



vi /etc/sysctl.conf

net.ipv4.ip_forward = 1   # 没有则添加，有修改为1（0禁止，1开启）

sysctl -p

```



## Show NIC details type

```sh
ip -d link show xxx


# TYPE := { bareudp | bond | bond_slave | bridge | bridge_slave |
#          dummy | erspan | geneve | gre | gretap | ifb |
#          ip6erspan | ip6gre | ip6gretap | ip6tnl |
#         ipip | ipoib | ipvlan | ipvtap |
#          macsec | macvlan | macvtap |
#          netdevsim | nlmon | rmnet | sit | team | team_slave |
#          vcan | veth | vlan | vrf | vti | vxcan | vxlan | wwan |
#          xfrm }
```



Cache sync
```sh
sync; echo 3 > /proc/sys/vm/drop_caches
```

