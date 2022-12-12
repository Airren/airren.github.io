---
title: SDEWAN SGX Test Environment
---









## Prepare Machine

```sh
sudo ip route add default via 10.0.11.100 dev eno1

cat <<EOF | sudo tee /etc/resolv.conf
nameserver 127.0.0.53,10.248.2.5
options edns0 trust-ad
search sh.intel.com
EOF

sudo vi /etc/netplan/00-installer-config.yaml
network:
  ethernets:
    eno1:
      dhcp4: true
      nameservers:
        addressses: [10.248.2.5]
  version: 2



git clone -b sgx  http://sdewan.sh.intel.com:10880/airren/SDEWAN-SetUp.git




apt-cache search linux-image
sudo apt-get install linux-image-your_version_choice linux-headers-your_version_choice linux-image-extra-your_version_choice

# must reboot you machine
sudo apt update && sudo apt install -y linux-image-5.15.0-33-generic linux-headers-5.15.0-33-generic linux-modules-5.15.0-33-generic linux-modules-extra-5.15.0-33-generic
# https://packages.ubuntu.com/focal-updates/kernel/
```

