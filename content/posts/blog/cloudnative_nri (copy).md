---
NRI

---



## NRI Test ENV

```sh
ssh airren@nuc-ubuntu22.sh.intel.com

use: airren
passwd: 123-

Path: /home/airren/nri
```





## NRI deploy



Download the latest release version with NRI Path

```sh
git clone -b v1.7.0-beta.3 https://github.com/containerd/containerd.git
sudo apt install libbtrfs-dev # ubuntu22/20
cd containerd 
make
```



Replace the old containerd

```sh
sudo mv bin/containerd /usr/local/bin/containerd 
```



Enable nri 

```sh
  [plugins."io.containerd.nri.v1.nri"]
    config_file = "/etc/nri/nri.conf"
    disable = false
    plugin_path = "/opt/nri/plugins"
    socket_path = "/var/run/nri.sock"
```



 let NRI itself know that it is enabled. /etc/nri/nri.conf

```sh
disableConnections: false
```



## NRI Plugin	



下载并编译nri plugin

```sh
git clone https://github.com/containerd/nri
cd nri
git checkout pr/proto/draft
make
```



### NRI 请求日志插件	

这个插件记录的NRI的请求和事件。你可以配置插件的订阅内容。 通过这个插件可以往容日中配置环境变量或者annotation 用来测试。

```sh
./build/bin/logger -idx 00
```



此时新建一个container，可以看到 loggr-plugin 会捕获以下events： `RunPodSandBox,CreateContainer,PostCreateContainer,StartContainer, PostStartContainer`

如果我们我们删掉这个container，也可以捕获到对应的event:

`StopContainer,StopPodSandbox,RemoveContainer, RemovePodSandbox`。

![image-20230209152309986](./cloudnative_nri.assets/image-20230209152309986.png)



我们可以捕获到container 创建的事件，此时，我们就可以修改 podspec的信息。 例如增加/修改环境变量，或者增加/修改annotation.

修改之后变动通过`k descirbe pod nginx` 是看不到修改信息的。 NRI的修改只作用于OCI层，对CRI层来说是无感知的。





### NRI Differ Plugin

Differ 插件可以注入到其他NRI插件之前/之后/或者其他插件之间，track 和显示其他NRI插件修改了容器请求的那些内容。这个插件支持多次注册到多个索引位置。 所以一个Differ Plugin实例可以用来跟踪和显示所有对Container做的修改。



在测试Differ Plugin时可以为其指定Index，例如 `nri-differ --indices 00,20,99 --yaml` 。

然后就可以看到 修改后的yaml diff。



### Device Injector Plugin



这个插件可以使用Pod 的annotation往Container中注入device或者挂载存储路径。

#### Device annotations					

annotation key 的格式是`devices.nri.io/container.$CONTAINER_NAME`, 这样就可以把设备注入到名为`$CONTAINER_NAME`的容器中。如果使用`devices.nri.io` 或者`devcies.nri.io/pod`作为key，就会把设备注入到所有的容器中。

设备注入的语法如下：

```yaml
- path: /dev/dev0
  type: {c|b}
  major: 1
  mainor: 3
  file_mode: <permission mode>
  uid: <user ID>
  gid: <group ID>
  
# file_mode, uid and gid can be omitted, the rest are mangatory
```



#### Mount Annotation

mount 的annotation格式和Device基本一致，annotation的前缀是`mounts.nri.io`

```sh
- mount: <mount source0>
  destination: <mount destionation0>
  type: <mount type>
  options:
    - option0
    - option1
```

```sh
device-injector -id 10
```



#### OCI Hook Inject Plugin

支持OCI的runtime的配置具有hook 功能，可以自定义功能，关联到Container的生命周期中。这个插件使用Podman的HookManger实现了在容器中注入OCI hook. 使用这个包同时也可以实现CRI-O的原生hook注入。这个插件基本上实现了CRI-O兼容的OCI hook注入。

```sh
hook-injector -idx 10
```







