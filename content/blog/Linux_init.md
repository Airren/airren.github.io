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
    export no_proxy="localhost,127.0.0.1/8,arch,.localdomain.com,10.239.154.51/16"

    local proxy="http://child-prc.intel.com:913"
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

proxy_on' | tee -a  ~/.zshrc

EOF

# Install zsh
cat <<EOF | sudo tee /etc/apt/apt.conf.d/proxy.conf
Acquire::http::Proxy "http://child-prc.intel.com:913";
Acquire::https::Proxy "http://child-prc.intel.com:913";
EOF
```



## Font

`hack`





Set timezone

```sh
timedatectl set-timezone Asia/Shanghai 

sudo date -s "$(curl -H'Cache-Control:no-cache' -sI google.com | grep '^Date:' | cut -d' ' -f3-6)Z"
```



### Add new User

```sh
sudo adduser hairong
sudo usermod -aG sudo hairong

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





## Ubuntu Alisa

今天在Build Docker image的时候发现`sgx-sdk-demo`的base images是`ubuntu:bionic`, 然后设置的apt source list 也是`"....intel-sgx/sgx_repo/ubuntu focal main"`。同时 docker image `ubunt:18.04` 和 `ubuntu:bionic` 的Image ID 是完全相同的，猜测 bionic 应该是 ubuntu:18.04 的别名。于是查了[Ubuntu 的release Note](https://wiki.ubuntu.com/Releases) 果真如此。





update ubuntu linux kernel

```sh
apt-cache search linux-image
sudo apt-get install linux-image-your_version_choice linux-headers-your_version_choice linux-image-extra-your_version_choice

# must reboot you machine
# https://linuxhint.com/update_ubuntu_kernel_20_04/
```





- flux add proxy!!!! 装的时候手动修改env



```sh
git config --global --add http.proxy http://child-prc.intel.com:913
git config --global --add https.proxy http://child-prc.intel.com:913



export http_proxy=http://child-prc.intel.com:913
export https_proxy=http://child-prc.intel.com:913
```

