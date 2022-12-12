# 高级知识点
## 1. 并行计算
- 将数据拆分到每个节点上  -> 如何拆分
- 每个节点并行的给出计算结果 -> 中间结果
- 将结果汇总 -> 如何汇总

## 2. 外部排序分析
如何排序100G个元素？

只能有一部分数据放到内存
#### 归并排序
- 将数据分为左右两半，分别归并排序，再把两个有序数据归并
- 如何归并
  <img src="img/2019-09-16-00-15-06.png" style="zoom:33%;" />

归并节点的排序-- K路归并 使用堆实现 Priority Queue
![](img/2019-09-16-00-21-17.png)
**使用Iterable<T> 接口 **
- 不断获取下一个元素
- 元素存储/获取方法被抽象, 与归并节点无关
- Iterable<T>  merge(List\< Iterable<T>> sortData);
![](img/2019-09-16-00-26-30.png)










## 3. 死锁分析

多线程 线程安全
加锁， 锁的粒度，性能

#### 死锁分析

```Java
void transfer(Account form, Account to, int amount){
    synchronized(form){
        synchronized(to){
            from.setAmount(for.getAmount() - amount);
            to.setAmount(to.getAmount() + amount)
        }
    }
}
```
- synchronized(form) -> 别的线程在等待from
- synchronized(to) -> 别的线程已经锁住了to
- 可能出现死锁：transfer(a,b,100) 和 transfer(b,a,100)同时进行
  
  1. 在任何地方都可以进行线程切换，甚至在一句语句中间
  2. 要尽力设想对自己最不利的情况

#### 死锁条件，必须同时满足
- 互斥等待 没有锁的必须等锁
- hold and wait  拿到一个锁去等待另一个锁
- 循环等待 拿a等b 拿b等a
- 无法剥夺的等待

#### 死锁防止
- 破除互斥等待 -> 一般无法破除
- 破除 hold and wait -> 一次性获取所有资源，大多数语言不支持  可以使用超时释放
- 破除 循环等待 -> 按顺序获取资源
- 破除 无法剥夺的等待 -> 不得已的办法，加入超时




## 4. 线程池介绍
#### 线程池
- 创建线程开销大
- 线程池： 预先建立好线程，等待任务的派发
![](img/2019-09-15-23-11-27.png)

如果Blocking Queue的队列为空，所有的线程都会被block；
如果有任务进来，线程就会抢任务进行处理；
如果所有的线程都在处理任务，多余的任务就会在Blocking Queue中排队；
如果Blocking Queue中的也满了之后，应该采取一定的拒绝策略；

#### 线程池的参数
`corePoolSize`: 线程池中初始线程的数量，可能处于等待状态
`maximumPoolSize`: 线程池中最大允许线程数量
`keepAliveTime`: 超出`corePoolSize`部分线程如果等待这些时间将被回收

## 5. 线程池 Java executor FrameWork
- 线程池的创建
- 任务派发
- 利用Future检查任务结果

线程数为30是生产环境中比较常见的一个数字
```Java
public class ExecutorTester {

  public static void main(String[] args)
      throws InterruptedException, ExecutionException {
    ExecutorService executor = Executors.newFixedThreadPool(3);

    List<Future<?>> taskResults = new LinkedList<>();
    for (int i = 0; i < 10; i++) {
      taskResults.add(executor.submit(new CodingTask(i)));
    }
    System.out.println("10 tasks dispatched successfully.");

    for (Future<?> taskResult : taskResults) {
      taskResult.get();
    }

    System.out.println("All tasks finished.");
    executor.shutdown();
  }
}

```

## 6. 资源管理
#### Java 垃圾回收
- 不被引用的对象会被回收   环形引用也会被回收掉
- 垃圾回收包括 Minor GC 和 Full GC
- 垃圾回收时所有运行暂停

内存泄露， 对象创建太多太快也会触发GC
#### Java 资源管理
**数据库的连接**
- 内存会被回收， 资源不会释放
- databaseConnection需要databaseConnection.close()来释放

旧写法
```Java
try{
    Database databaseConnection = connect(...);
    databaseConnection.beginTransaction();
    // do work
}catch(Exception e){
    databaseConnection.rollBack();
}finally{
    databaseConnection.close(); // 关闭连接时候也有可能出现异常
}
```
新写法 Java 1.7以后
```Java
try(Database databaseConnection = connect(...)){
    databaseConnection.beginTransaction();
    // do work
}catch(Exception e){
    databaseConnection.rollBack();
}
```


#### C++ 资源管理
- C++ 没有finally, 没有try with resource
- C++ 有析构函数

```c++
void dowork(){
    Database databaseConnection();
    try{
        databaseConnection.beginTransaction();
        // do work
    }catch(Exception&e){
        databaseConnection.rollback();
    }
}

Database::~Database(){
    // close databaseConnection;
}
```





