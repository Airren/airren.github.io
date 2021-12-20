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
cat <<EOF| tee -a ~/.vimrc
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
sudo apt install zsh
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

proxy_on' | tee -a  ~/.bashrc

EOF

# Install zsh
cat <<EOF | sudo tee /etc/apt/apt.conf.d/proxy.conf
Acquire::http::Proxy "http://child-prc.intel.com:913";
Acquire::https::Proxy "http://child-prc.intel.com:913";
EOF
```



## Font

`hack`
