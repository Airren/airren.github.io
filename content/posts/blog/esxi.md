---
title: ESXi
date: 2022-12-17T15:47:59+08:00
draft: true
---





SSD disk info

```sh
esxcli nvme device list
# Percentage Used:
esxcli nvme device log smart get -A vmhba3
```

