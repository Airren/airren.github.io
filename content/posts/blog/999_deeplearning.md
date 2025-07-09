---
Title: DeepLearning ENV
---



## Nvidia Cuda

ubuntu 22.04  install Nvidia driver:

Ban the default graphic driver:

```sh
sudo touch /etc/modprobe.d/blacklist-nouveau.conf

echo 'blacklist nouveau
options nouveau modeset=0' > blacklist-nouveau.conf
# update initramfs
sudo update-initramfs -u

# check the noveau
lsmod | grep nouveau
```

Install the grid dirver or standard driver

```sh
 wget https://storage.googleapis.com/nvidia-drivers-us-public/GRID/vGPU15.1/NVIDIA-Linux-x86_64-525.85.05-grid.run
 
 sudo bash NVIDIA-Linux-x86_64-525.85.05-grid.run
```



````sh
apt-get remove --purge nvidia-driver-530
apt-get autoremove
````



Install Cuda

cd

```sh
wget https://developer.download.nvidia.com/compute/cuda/repos/ubuntu2204/x86_64/cuda-keyring_1.0-1_all.deb
sudo dpkg -i cuda-keyring_1.0-1_all.deb
sudo apt-get update
sudo apt-get -y install cuda



```





check cuda

```sh
cd usr/local/cuda-11.4/samples/1_Utilities/deviceQuery/
# make一下
sudo make
# 执行检查版本
./deviceQuery
```





Environment set

```sh
export PATH= $PATH:/usr/local/cuda-11.4/bin

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/cuda-11.4/lib64

export CUDA_HOME=$CUDA_HOME:/usr/local/cuda-11.4
```



## Intel OneAPI

https://dgpu-docs.intel.com/driver/installation.html

export LD_LIBRARY_PATH=/opt/intel/oneapi/mkl/latest/lib:$LD_LIBRARY_PATH

````sh
export LD_LIBRARY_PATH=/opt/intel/oneapi/mkl/latest/lib:$LD_LIBRARY_PATH


sudo apt-get install intel-gpu-tools 

sudo intel_gpu_top

sudo xpu-smi stats -d 0



````



https://www.reddit.com/r/IntelArc/comments/11an12q/stable_diffusion_web_ui_for_intel_arc/

https://www.reddit.com/r/linux_gaming/comments/12ltgw6/what_is_the_current_situation_of_intel_arc_on/

https://www.reddit.com/r/IntelArc/comments/11an12q/stable_diffusion_web_ui_for_intel_arc/

## Jupyter

```sh
conda install nb_conda
pip install jupyter
jupyter notebook --generate-config
# generate password

# change the password on configration
jupyter notebook
```

