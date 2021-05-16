# 4.每日一面

> 携程面试

1. 问项目， 注意项目细节

   Netty 

   BIO 同步阻塞IO，一个线程只有一个连接

   NIO 同步非阻塞IO ，一个线程有多个连接，一个线程中有很多Channel，通过selector 选择线程

   AIO 异步非阻塞IO

2. 双重检验单例

   ```Java
   public class Singleton{
     private volatile static Singleton singleton;
     private Singleton(){
       
     }
     public Singleton getSingleton(){
       if(singleton == null){
         synchronized(Singleton.class){
           if(singleton == null){
             singleton = new Singleton();
           }
         }
       }
       return singleton;
     }
   }
   ```

3. http1.1  长连接， 心跳包， http2 http3

   HTTP 1.1 支持长连接(PersistentConnection)和请求的流水线(Pipelining)处理,在一个TCP连接上可以传送多个HTTP请求，减少了建立和关闭连接的消耗和延迟。在HTTP1.1中默认开启Connection：keep-alive，一定程度上弥补了HTTP 1.0 每次请求都要创建连接的缺点。  

   在header 中设置connection = keep-alive，keep-alive：timeout = 60；        

   HTTP 2.0 多路复用 多个请求可以在同一个连接上并行执行。

4. 线程池，参数列表，阻塞队列的默认值

5. 垃圾回收 G1，实际项目中怎么优化

   https://www.cnblogs.com/diegodu/p/9849611.html

   年轻代大小选择

   - 响应时间优先的应用：尽可能设大，直到接近系统的最低响应时间限制（根据实际情况选择）。在此种情况下，年轻代收集发生的频率也是最小的。同时，减少到达年老代的对象。
   - 吞吐量优先的应用：尽可能的设置大，可能到达Gbit的程度。因为对响应时间没有要求，垃圾收集可以并行进行，一般适合8CPU以上的应用。

6. 1000万条数据中选取最小的100个，分析每一步的时间复杂度，除了堆还有什么方法

7. 设计索引

8. 多线程限流

9. 设计模式在工作中的使用？redis原理，消息中间件原理，jvm调优，类加载机制，spring，mybatis原理，针对面试官问的这些内容，这名网友认为这些问题不太实用，工作中都是找轮子调api，写业务逻辑代码

    

