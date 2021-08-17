# Java 集合类



#### 线程安全与线程不安全



#### 参考链接

https://www.cnblogs.com/williamjie/p/9099141.html

https://www.cnblogs.com/heyonggang/p/9112731.html

https://blog.csdn.net/andy_budd/article/details/81413464

https://blog.csdn.net/wufaliang003/article/details/80219296

https://blog.csdn.net/VIP_WangSai/article/details/70182933

https://blog.csdn.net/qq_41216743/article/details/101311040

https://blog.csdn.net/cn12306com/article/details/81318871



## 相关面试题

#### 1.  举例说明List 、Set、HashMap是线程不安全的

###### List

​    我们知道ArrayList 是线程不安全的，请编写一个不安全的案例并给出解决方案？对于List我们使用的大多数场景是在单线程下，如果在高并发的情况下，便会出现一些线程不安全的问题

```Java
public class ContainerNotSafeDemo {
    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<>();

//          3种解决方案
//        List<String> list = new Vector<>();
//        List list = Collections.synchronizedList(new ArrayList<>());
//        List list = new CopyOnWriteArrayList();

        for (int i = 0; i < 30; i++) {  // 30 个线程，每一个线程都有对list的写与读操作
            new Thread(() -> {
                list.add(String.valueOf(new Random().nextInt(100)));
//                System.out.println(list); // 如果没有这一行就不会抛异常,但是list中的数据也不是30个
            }, String.valueOf(i)).start();
        }


        Thread.sleep(3000);
        System.out.println(list+""+list.size());
    }
}

/*1.故障现象
*      报错java.util.ConcurrentModificationException
* 2.导致原因
*      并发争抢修改导致
* 3.解决方案
*      new Vector();
*      Collections.synchronizedList(new ArrayList<>());
*      new CopyOnWriteArrayList<>();
* 4.优化建议
*      在读多写少的时候推荐使用 CopeOnWriteArrayList 这个类
*/
```

`CopyOnWriteArrayList`说明：
CopyOnWrite容器即写时复制容器。往一个容器添加元素时，不直接往当前容器Object[] 进行Copy,复制出一个新的容器Object[] newElements, 然后新的容器setArray(newElements); 这样做的好处是可以对CopyOnWrite容器进行并发的读，而不需要加锁，因为当前容器不会添加任何元素，所以CopyOnWrite容器也是一直<font color=blue>读写分离的思想</font>，读和写是不同的容器。

CopyOnWriteArrayList 下的Add方法：

```Java
    public boolean add(E e) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            Object[] newElements = Arrays.copyOf(elements, len + 1);
            newElements[len] = e;
            setArray(newElements);
            return true;
        } finally {
            lock.unlock();
        }
    }
```



###### Set

Set 是线程不安全的，请编写一个不安全的案例并给出解决方案？

```Java
    public static void main(String[] args)  {
        Set<String> set = new HashSet<>();  //导致线程不安全
//        2种解决方案
//        Set<String> set = Collections.synchronizedSet(new HashSet<>());
//        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 1; i <= 30; i++) {
            new Thread(()->{
                set.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(set);
            },String.valueOf(i)).start();
        }
        //HashSet 底层是HashMap
    }
```



###### HashMap

HashMap 是线程不安全的，请编写一个不安全的案例并给出解决方案？

```Java
    public static void main(String[] args)  {
        Map<String,String> map = new HashMap<>(); //导致线程不安全     
//        2种解决方案
//         Map<String,String> map = Collections.synchronizedMap(new HashMap<>());
//        Map<String,String> map = new ConcurrentHashMap<>();
        for (int i = 0; i <= 30; i++) {
            new Thread(()->{
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,8));
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }
```

