# 第8讲 设计模式

## 1. 单例模式 Singleton

#### 设计模式的提出：博士论文

设计 vs 语言限制
更多的模式： 并发模式、架构模式 从架构的思想上看

#### Singleton优缺点

- 确保全局至多只有一个对象
- 用于： 构造缓慢的对象，需要统一管理的资源
- 缺点： 很多全局状态， 线程安全性

#### Singleton的创建（创建非常慢的对象）

- 双重锁模式 Double checked locking
- 作为Java 类的静态变量（程序初始化的时候就要创建出来）
- 使用框架提供的能力

依赖注入的框架（DI框架 Spring, Google Juice）

## 2. State 模式 变继承关系为组合关系

#### 继承关系
- 描述is-a关系     复用，增加修改
- 不用用继承关系来实现复用
- 使用设计模式实现复用


#### 如果 Employee 升级成了 Manager ？

新建成一个Manager, 原先的引用应该被回收。

或者使用state模式

## 3. Decorator模式 装饰者模式

```
interface Runable{
  void run();
}
```

如何实现LoggingRunable, TransactionRunable, ....

开始运行，运行结束，运行持续是时间

commit， roll back

## 4. 如何创建对象

- 编译时就要确定是创建哪一个对象
- 