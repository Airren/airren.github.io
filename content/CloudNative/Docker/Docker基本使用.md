---
title: docker 基本使用方法
---

# docker基本使用方法

## centos 安装docker

把yum包更新到最新

```sh
yum update
```

安装需要的软件包

```shell
yum install -y yum-utils device-mapper-persistent-data lvm2
```

设置yum源

```shell
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```

查看所有仓库中所有docker版本，并选择特定版本安装

```shell
yum list docker-ce --showduplicates | sort -r

[root@MiWiFi-R3-srv ~]# yum list docker-ce --showduplicates | sort -r
 * updates: mirrors.aliyun.com
Loading mirror speeds from cached hostfile
Loaded plugins: fastestmirror
Installed Packages
 * extras: mirrors.aliyun.com
docker-ce.x86_64            3:18.09.0-3.el7                    docker-ce-stable 
docker-ce.x86_64            18.06.1.ce-3.el7                   docker-ce-stable 
docker-ce.x86_64            18.06.1.ce-3.el7                   @docker-ce-stable
docker-ce.x86_64            18.06.0.ce-3.el7                   docker-ce-stable 
docker-ce.x86_64            18.03.1.ce-1.el7.centos            docker-ce-stable 
docker-ce.x86_64            18.03.0.ce-1.el7.centos            docker-ce-stable 
......
```

安装Docker，命令：yum install docker-ce-版本号，我选的是18.06.1.ce-3.el7，如下

```shell
yum install docker-ce-18.06.1.ce-3.el7
```

启动Docker，命令：systemctl start docker，然后加入开机启动，如下

```shell
systemctl start docker
systemctl enable docker
```

验证是否安装成功 docker version

```csharp
[root@MiWiFi-R3-srv ~]# docker version
Client:
 Version:           18.06.1-ce
 API version:       1.38
 Go version:        go1.10.3
 Git commit:        e68fc7a
 Built:             Tue Aug 21 17:23:03 2018
 OS/Arch:           linux/amd64
 Experimental:      false

Server:
 Engine:
  Version:          18.06.1-ce
  API version:      1.38 (minimum version 1.12)
  Go version:       go1.10.3
  Git commit:       e68fc7a
  Built:            Tue Aug 21 17:25:29 2018
  OS/Arch:          linux/amd64
  Experimental:     false
```

#### 使用Docker 中国加速器

由于网络原因，我们在pull Image 的时候，从Docker Hub上下载会很慢。

修改文件

```shell
vi  /etc/docker/daemon.json
#添加后：
{
    "registry-mirrors": ["https://registry.docker-cn.com"],
    "live-restore": true
}
```

重起docker服务

```shell
systemctl restart docker
```





