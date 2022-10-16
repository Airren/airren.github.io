---
title: cross complile
---

## openWRT Cross compile

```sh
export STAGING_DIR=/home/airren/openwrt/staging_dir/toolchain-x86_64_gcc-8.4.0_musl


export TOOLCHAIN_DIR=$STAGING_DIR
export TOOLCHAIN_PATH=$TOOLCHAIN_DIR/bin
export CXX=$TOOLCHAIN_PATH/g++-uc
export AR=$TOOLCHAIN_PATH/x86_64-openwrt-linux-musl-ar
export CXXFLAGS="-O2"


export CROSSCOMPILE_PATH=$TOOLCHAIN_DIR/usr
# export CFLAGS="-I$CROSSCOMPILE_PATH/jhhhhinclude"


export LDCFLAGS="-L$TOOLCHAIN_DIR/usr/lib -lz"
export LD_LIBRARY_PATH=$TOOLCHAIN_DIR/usr/lib
export PATH=$TOOLCHAIN_PATH:$PATH
```

```sh
./autogen.sh --build=x86_64-pc-linux-gnu --host=i486-openwrt-linux
./autogen.sh --build=x86_64-pc-linux-gnu --host=x86_64-openwrt-linux

make CC=i486-openwrt-linux-gcc LD=i486-openwrt-linux-ld
make CC=x86_64-openwrt-linux-gcc LD=x86_64-openwrt-linux-ld
```

build openwrt in a docker 

```shell
apt update
apt install -y git wget  build-essential gawk gcc-multilib flex git gettext libncurses5-dev libssl-dev python3-distutils rsync unzip zlib1g-dev

apt update
apt install build-essential ccache ecj fastjar file g++ gawk \
gettext git java-propose-classpath libelf-dev libncurses5-dev \
libncursesw5-dev libssl-dev python python2.7-dev python3 unzip wget \
python-distutils-extra python3-setuptools python3-dev rsync subversion \
swig time xsltproc zlib1g-dev 




# Update the feeds
./scripts/feeds update -a
./scripts/feeds install -a

# Configure the firmware image and the kernel
make menuconfig
```
