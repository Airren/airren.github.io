# Java8 新特性

Java8 速度更快，代码更少，强大的Stream API，便于并行 最大化减少空指针异常 optional

## Lambda表达式

Lambda 是一个匿名函数，我们可以把Lambda 表达式理解为是一段可以传递的代码（将代码像数据一样进行传递）。可以写出更简洁、更灵活的代码。作为一种更紧凑的代码风格，使 Java的语言表达能力得到了提升。

#### 从匿名类到Lambda 的转换

```Java
// 匿名内部类
Runnable r1 = new Runnable(){
  @Override
  public void run(){
    System.out.println("Hello world");
  }
}

//Lambda 表达式
Runnable r1 = () -> System.out.println();

//原来使用匿名内部类作为参数传递
TreeSet<String> ts = new TreeSet<>(new Comparator<String>(){
  @Override
  public int compare(String o1, String o2){
    return Integer.compare(o1.length(), o2.length());
  }
});

//Lambda 表达式作为参数传递
TreeSet<String> ts2 = new TreeSet<>(
										(o1, o2) -> Integer.compare(o1.length, o2.length));
 
```

#### Lambda表达式语法

Lambda表达式在Java语言中引入了一个新的语法元素和操作符。这个操作符为“->”，这个操作符被称为Lambda操作符或箭头操作符。它将Lambda分为两个部分：

左侧：指定了Lambda 表达式需要的所有参数

右侧：指定了Lambda体，即Lambda表达式要执行的功能

###### 语法格式

1. 无参，无返回值，Lambda 体只需一条语句

  ```Java
  Runnable r1= ()->System.out.println("Hello World");
  ```

2. Lambda 需要一个参数

   ```Java
   Consumer<String> fun = (args)-> System.out.println(args);
   ```

3. Lambda 只需一个参数时，参数的小括号可以省略

   ```Java
   Consumer<String> fun = args -> System.out.printlna(args);
   ```

4. Lambda 需要两个参数，并且有返回值

   ```Java
   BinaryOperator<Long> bo = (x,y) -> {
     System.out.println("实现函数接口方法！");
     return x+y;
   }
   ```

5. 当Lambda体只有一条语句时，return与大括号可以省略

   ```Java
   BinaryOperator<Long> bo = (x,y)-> x+y;
   ```

6. 参数的数据类型可以省略，因为可以由编译器推断得出，称为“类型判断”

   ```Java
   BinaryOperator<Long> bo = (Long x, Long y) -> {
     System.out.println("实现函数接口方法！");
     return x+y;
   }
   ```

#### 类型推断

上述 Lambda 表达式中的参数类型都是由编译器推断得出的。Lambda 表达式中无需指定类型，程序依然可以编译，这是因为 javac 根据程序的上下文，在后台推断出了参数的类型。Lambda 表达式的类型依赖于上下文环境，是由编译器推断出来的。这就是所谓的 “类型推断”

## 函数式接口

#### 什么是是函数式接口	

- 只包含一个抽象方法的接口，称为函数式接口。 
- 你可以通过 Lambda 表达式来创建该接口的对象。（若 Lambda 表达式抛出一个受检异常，那么该异常需要在目标接口的抽象方法上进行声明）。
- 我们可以在任意函数式接口上使用 @FunctionalInterface 注解， 这样做可以检查它是否是一个函数式接口，同时 javadoc 也会包含一条声明，说明这个接口是一个函数式接口。

#### 自定义函数式接口

```Java
@FunctionalInterface
public interface MyNumber{
  public double getValue();
}
```

函数式接口中使用泛型

```Java
@FunctionalInterface
public interface MyFunc<T>{
  public T getValue(T t);
}
```

函数式接口作为参数传递给Lambda表达式

```Java
public String toUpperString(Myfunc<String> mf, String str){
  return mf.getValue(str);
}

// 作为参数传递Lambda 表达式
String newStr = toUpperString(
	(str) -> str.toUpperCase, "abcdef";
);
Systen.out.println(newStr);

```

作为参数传递 Lambda 表达式：为了将 Lambda 表达式作为参数传递，接收Lambda 表达式的参数类型必须是与该 Lambda 表达式兼容的函数式接口 的类型。

## 方法引用与构造器引用



## Stream API



## 接口中默认方法与静态方法

jie

## 新日期时间 API



## 其他新特性

#### Optional类

Optional<T> 类(java.util.Optional) 是一个容器类，代表一个值存在或不存在， 原来用 null 表示一个值不存在，现在 Optional 可以更好的表达这个概念。并且可以避免空指针异常。 

**常用方法：**

` Optional.of(T t)` : 创建一个 Optional 实例 

`Optional.empty( `: 创建一个空的 Optional 实例 

`Optional.ofNullable(T t)`:若 t 不为 null,创建 Optional 实例,否则创建空实例 

`isPresent()` : 判断是否包含值

` orElse(T t)` : 如果调用对象包含值，返回该值，否则返回t 

`orElseGet(Supplier s)` :如果调用对象包含值，返回该值，否则返回 s 获取的值

` map(Function f)`: 如果有值对其处理，并返回处理后的Optional，否则返回 `Optional.empty() `

`flatMap(Function mapper)`与 map 类似，要求返回值必须是Optional

#### 重复注解与类型注解

两点改进： 可重复的注解及可用于类型的注解

```Java
@Target({TYPE, FIELD, METHOD, PARAMETER, CON})
```

