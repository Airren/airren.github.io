---
title: Mac Dev Environment
date: 2021-2-12T13:41:16+08:00
---



## HomeBrew

https://brew.sh/

```sh
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

Use homebrew install package
```sh
brew install wget 
brew install go
```

## Oh-my-zsh

https://ohmyz.sh/

```sh
sh -c "$(curl -fsSL https://raw.github.com/ohmyzsh/ohmyzsh/master/tools/install.sh)"

# sudo chsh -s $(which zsh)
```

## Modify Keys

settings -> Keyboard -> modifyKeys 

```sh
Caplock -> Command
```



## Software

**highly recommended**

- Iterm2:  Mac terminal replacement

- Alfred 
- Dash
- Istat Menus
- Xnip: for screenshot

**Recommend**

- Wireshark
- Sublime
- Chrome
- Typora



**Others**

- alt tab
- Irvue: for desktop picture
- Magnet: for window split
- Cleanmymac



## Cli Tools

```sh
# tmux
brew install tmux

```

## Vim  Config

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



## Golang

```sh
brew install golang

echo '
GOPATH=~/go
GOBIN=$GOPATH/bin
PATH=$PATH:$GOBIN
'| tee -a ~/.zshrc
```



## Font

`hack`: A typeface designed for source code

https://github.com/source-foundry/Hack

## K8s for zsh

```sh
echo 'source <(kubectl completion zsh)' >>~/.zshrc
echo 'alias k=kubectl' >>~/.zshrc
echo 'compdef __start_kubectl k' >>~/.zshrc
source ~/.zshrc
```

## Network Tools

```sh
# for ip command
brew install iproute2mac
```

