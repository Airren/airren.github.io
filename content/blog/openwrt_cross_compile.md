---
title: cross complile
---





## openWRT Cross compile



```sh
export STAGING_DIR=/home/airren/openwrt/staging_dir/toolchain-i386_pentium4_gcc-8.4.0_musl

export TOOLCHAIN_DIR=$STAGING_DIR
export TOOLCHAIN_PATH=$TOOLCHAIN_DIR/bin
export CXX=$TOOLCHAIN_PATH/g++-uc
export AR=$TOOLCHAIN_PATH/i486-openwrt-linux-musl-ar
export CXXFLAGS="-O2"


export CROSSCOMPILE_PATH=$TOOLCHAIN_DIR/usr
# export CFLAGS="-I$CROSSCOMPILE_PATH/include"


export LDCFLAGS="-L$TOOLCHAIN_DIR/usr/lib -lz"
export LD_LIBRARY_PATH=$TOOLCHAIN_DIR/usr/lib
export PATH=$TOOLCHAIN_PATH/bin:$PATH
```









```sh
/autogen.sh --build=x86_64-pc-linux-gnu --host=x86_64-openwrt-linux --without-libtasn1
make CC=x86_64-openwrt-linux-gcc LD=x86_64-openwrt-linux-ld
```
