# Java 虚拟机



# 虚拟机调优
## Java 虚拟机调优参数

- `-Xms` 起始内存 JVM堆内存
- `-Xmx` 最大内存  JVM堆内存
- `-Xmn` 新生代内存 
- `-Xss` 栈大小。 就是创建线程后，分配给每一个线程的大小
- `-XX:NewRatio` 设置年轻代和老年代的比值。默认为2:1。如果为3，表示年轻代与老年代的比值为1:3，年轻代占整个年轻代和老年代的1/4。
- `-XX:SurvivorRatio`年轻代中Eden区和两个Survivor区的比值。如果为3，表示Eden:Survivor = 3:2, 因为有两个Survivor区域，所以一个Survivor区占整个年轻代的1/5.
- `-XX:MaxPermSize`设置持久代的大小。

收集器的设置
- `-XX:+UseSerialGC`设置串行收集器
- `-XX:+UseParallelGC`设置并行收集器
- `-XX:+UseParallelOld`设置并行老年代收集器
- `-XX:+UseConcMarkSweepGC`设置并发收集器
  
垃圾回收统计信息
- `-XX:PrintGC`
- `-XX:PrintGCDetails`
- `-XX:PrintGCTimeStamps`
- `-Xloggc:filename`

并行收集器设置
- `-XX:ParallelGCThreads=n`设置并行收集器收集时使用的CPU数。并行收集线程数。
- `-XX:MaxGCPauseMillis=n`设置并行收集最大暂停时间
- `-XX:GCTimeRatio=n`设置垃圾回收时间占程序运行时间的百分比。公式为1/(1+n)

并发收集器设置
- `XX:+CMSIncrementalMode`设置为增量模式。适用于单CPU情况。
- `XX:ParallelGCThreads=n`设置并发收集器年轻代收集方式为并行收集时，使用的CPU数。并行收集线程数。

`-Xms1G -Xmx2G -Xmn500M -XX:MaxPermSize=64M -XX:+UseConcMarkSweepGC -XX:SurvivorRatio=3`

- `-Xms1G` JVM初始内存1G  这一部分是不是应该是堆内存
- `-Xmx2G` JVM最大内存 2G
- `-Xmn500M` 新生代内存 500M
- `-XX:MaxPermSize` 持久代内存 64M
- `-XX:SurvivorRatio=3` 新生代中Eden:Servivor = 3:2 

## JVM 调优
- `-Xms2G` `-Xmx2G` 将JVM最大内存与初始内存设置相等，避免JVM垃圾回收后重新分配内存。
- `-Xmn1G` 年轻代内存Sun官方推荐配置为整个堆的3/8,通常设置为1/3或1/4。
- `-Xss256K`减小每条线程栈的大小，能生成更多的线程
- `-XX:NewRatio=4` 调整年轻代与老年代的比例 年轻代:老年代=1:4
- `-XX:MaxTenuringThreshold=0` 设置晋升到老年代的对象的年龄。 如果设置为0，则年轻代对象不经过Survivor区，直接进入老年代。如果设置一个较大的值，则年轻代对象会在Survivor区进行多次复制。
- `-XX:+UseConcMarkSweepGC` JVM在server模式下默认使用`PararrelScavenge`＋`SerialOld`的收集器组合进行内存回收，不支持与用户线程并发执行。可使用`ParNew`+`CMS`+`SerialOld`的收集器组合进行内存回收（`SerialOld`收集器做为CMS收集器出现`ConcurrentModeFailure`失败后的后备收集器使用），减少`stop-the-world`时间。
- `-XX:CMSFullGCsBeforeCompaction` 使用CMS时，设置CMS收集器在进行若干次垃圾收集后再启动一次内存碎片
