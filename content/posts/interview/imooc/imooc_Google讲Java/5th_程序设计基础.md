# 程序设计语言基础
## 程序设计语言

**类型检查**
- 编译时：C, C++, Java, Go
- 运行时： Python, Perl, JavaScript, Ruby

**运行/编译**
- 编译为机器代码运行：C, C++
- 编译为中间代码，在虚拟机运行： Java, C#
- 解释执行： Python, Perl, JavaScript
  
**编程范式** Programming Paradigm
- 面向过程: C, Visual Basic
- 面向对象: Java, C++, C#, Scala
- 函数式: Haskell, Erlang

## 数据类型和补码
**数据类型**
- boolean, byte, char
- short, int, long, float, double
- String, Enum, Array
- Object...

**补码**
- 32位int 范围 -2^31 ~ 2^31 -1
  ```
  1000...0     -2^32
  1111...1     -1
  0000...0     0
  0111...1     2^31-1

  -1 + 1 = 0
  ```
## 浮点数与定点数
  **浮点数** (+/-)1.XXXXXX * 2^y
  - 符号位| 指数部分 | 基数部分
  - 64 位double 范围： +/- 10^308
  - 64 位double 精度： 10^15 15位有效小数位

**浮点数比较**

- a == b?
- Math.abs(a-b) < eps
- 使用BigDecimal 算钱 定点数

## Java 拆箱与装箱
**Primitive type vs Object**
 - primitive type: int, long
   值类型， 可以用a = b 去判断
 - Object: Integer, Long, Float, String
  引用类型， a == b 判断是否为同一个Object， 用a.equals(b) 或者 Object.equals(a,b)][a 可以为 null]判断值是否相等。
**Boxing and Unboxing**
- Integer a = 2; // Boxing
- Integet b = new Integer(2); // Boxing
- int v = a.intValue();

```Java
new Integer(2) ==2;  // true  自动拆箱
new Integer(2) == new Integer(2); // false
Integer.value(2) == Integer.value(2); // true  系统分配的箱子 IntegerCache.low  IntegerCache.high (-128 127)
Integer.value(2).intvalue() == 2; // true
new Integer(2).equals(new Integer(2)) // true

```
  

