





### fork 

create process

fork 被调用一次，可以返回两次。

在调用fork之后的代码会执行两次，一次在父进程中执行，返回的是创建成功的子进程的Id，一次是在子进程中执行，返回的是0；如果出现错误，fork返回的是负值。

```c
/*linux下：*/
 
#include <stdio.h>
#include <unistd.h>
 
int main() {
    pid_t pid;
    pid = fork();
    if(pid  == 0) //返回子进程
    {
        printf("child pid: %d\n", getpid());
    } else {
        printf("pid: %d\n", pid);//父进程中返回子进程的pid
        printf("father pid: %d\n", getpid());
    }
}
```



```sh
pid: 2876921
father pid: 2876920
child pid: 2876921
```





fork的两种用法

1. 父进程希望复制自己，使父子进程同时执行不同的代码段。

   比如在网络服务程序中，父进程等待客户端的服务请求。当请求到达时，父进程调用fork()使子进程处理此请求；而父进程继续等待下一个请求。

2. 一个进程要执行不同的程序

   这个在shell下比较常见，这种情况下， fork()之后一般立即接exec函数。