```
title: Linux Network Virtualization
```



Linux 为了实现隔离性性，提供了6个Namespace分别对不同的资源进行隔离， 分别是：

| Namespace         | Description            |               |
| ----------------- | ---------------------- | ------------- |
| Mount Namespace   | 用于隔离文件系统挂载点 | CLONE_NEWNET  |
| UTS Namespace     |                        | CLONE_NETUTS  |
| IPC Namespace     |                        | CLONE_NEWIPC  |
| PID Namespace     |                        | CLONE_NEWPID  |
| Network Namespace |                        | CLONE_NEWNS   |
| User Namespace    | 用于隔离用户权限       | CLONE_NEWUSER |

