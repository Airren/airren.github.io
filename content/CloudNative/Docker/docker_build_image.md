---
title: Docker Images
---







### 镜像仓库	

Linux 本地镜像仓库：`/var/lib/docker/image`

镜像应该是分层存储的。

Docker images 是存储在`镜像仓库服务`Images Registry。Docker 客户端的镜像仓库服务是可配置的，默认使用的是Docker Hub。

镜像仓库服务包含多个`镜像仓库` Image Repository（同一个镜像的不同版本）。一个镜像仓库中包含多个镜像Image。

 Docker Hub 也分为Official Repository 和 Unofficial Repository。

```sh
docker pull <repository>:<tag>
// 如果省略tag默认会pull tag 为 latest的image。但是latest并不保证这是仓库中最新的镜像。
```



如果希望从第三方镜像服务仓库获取镜像(not Docker Hub)，则需要在镜像仓库名称前加上第三方镜像仓库服务的DNS名称。

```sh
# gcr.io -> Google Container Images Registry.
docker pull gcr.io/k8s-staging-nfd/node-feature-discovery:master
```



### Image Tag	

不同的Images Tag可以绑定同一个Image ID

通过`--filter` 来过滤`docker image ls` 返回的内容

```sh
docker image ls --filter dangling=true
```

> dangling image -> with out name & tag <none>:<none>.
>
> 通常因为构建新的镜像，为该镜像打了一个已经存在的标签。Docker会remove old image上的标签，将该标签标在新Image上。Old Image 就会变成 dangling image。

可以通过`docker image prune `移除所有的Dangling images。如果添加下了`-a`参数，Docker会移除所有没有使用的镜像。

### docker image 格式化输出

go text template

```sh
docker images --format "{{.Repository}}:{{.Tag}}:{{.Size}}"
```







### 从容器创建一个镜像

````shell
docker commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]
docker commit -a "bytegopher.com" -m "first commit" a404c6c174a2  mymysql:v1
# -a 提交镜像的作者
# -m 提交信息
````



Re-Tag The image

```sh
docker tag images:tag  new-image:new-tag
```



### 镜像分层

Docker 是由一些松耦合的只读镜像层组成，Docker负责堆叠这些层，并将他们表示为单个统一的对象。

docker pull images 的时候可以看到是逐层下载的。







## Builder Image

WORKDIR: 如果没有文件夹，会创建并进入。















Docker Build Use Proxy

```sh

docker build -f Dockerfile ./ -t image-name:tag  --build-arg http_proxy=$(HTTP_PROXY) --build-arg https_proxy=$(HTTPS_PROXY)


--build-arg http_proxy=http://proxy-prc.intel.com:913 --build-arg https_proxy=http://proxy-prc.intel.com:913
```

