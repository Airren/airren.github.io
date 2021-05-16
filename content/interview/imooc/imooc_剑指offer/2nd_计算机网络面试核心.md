# 计算机网络面试核心


## 7. HTTP 简介

#### 超文本传输协议的主要特点

是应用层的请求响应的无状态协议

- 支持客户/服务器模式 ： 浏览器通过url向服务端发送请求， 服务端返回响应信息
- 简单快速 ： 请求方法 get post delete 通讯速度快
- 灵活： 数据格式灵活，允许任意格式的数据类型
- 无连接： 每次连接只处理一个请求，收到应答之后就断开连接 ， 1.1 之后使用了长连接  下层实现对上层透明， keep alive
- 无状态： 对事务处理没有记忆能力，缺少状态， version 1.1 引入了 keep alive 持续连接机制 2.0 升级成本太大

#### HTTP 请求结构

![image-20191024142449740](/Users/airren/Dropbox/JavaNote/imooc/imooc_剑指offer/img/image-20191024142449740.png)

HTTP请求报文结构

```sh
GET /baidu/com HTTP1.1
  Host: www.baidu.com
  Connection:keep-alive  # close 1.1之前
  User-Agent： Mozilla/5.0
  Accept-Encoding
Cookie: XXX
```
#### HTTP响应结构

<img src="/Users/airren/Dropbox/JavaNote/imooc/imooc_剑指offer/img/image-20191024143052552.png" alt="image-20191024143052552" style="zoom:50%;" />

```sh
HTTP 1.1 200 OK
	Server:
	Accept-Ranges:
	Content-Type:
	Content-Languge:
	Content-Length:
	Date:
```

#### 请求/响应步骤

- 客户端连接到WEB服务器
- 发送HTTP请求
- 服务器接受请求并返回HTTP响应
- 释放TCP连接： 服务器主动关闭TCP连接，浏览器被动释放TCP连接
- 客户端浏览器解析HTML内容

#### 在浏览器地址键入http 开头的url，按下回车之后经历的流程

- DNS解析 ：逐层查询路由器中的DNS缓存，浏览器-系统-路由器-IPS服务器-根域名服务器缓存-顶级域名服务器缓存，返回对应IP
- TCP连接：IP+80端口 三次握手 http协议版本
- 发送HTTP请求：
- 服务器处理并返回HTTP报文
- 浏览器解析渲染页面
- 连接结束

#### HTTP状态码

1XX：指示信息——表示请求已接收，继续处理

2XX：成功——表示请求已被成功接收、理解、接受

3XX：重定向——要完成请求必须进行更进一步的操作

4XX：客户端错误——请求有语法错误或请求无法实现

401 （未授权） 请求要求身份验证。 对于需要登录的网页，服务器可能返回此响应。
403 （禁止） 服务器拒绝请求。5XX：服务端错误——服务端未能实现合法的请求

https://www.cnblogs.com/gitnull/p/9532129.html



## 8. HTTP 介绍2

#### 常见状态码

- 200 OK： 正常返回信息
- 400 Bad Request： 客户端请求有语法错误，不能被服务器理解
- 401 Unauthorized： 请求未经授权，这个状态码必须和WWW-Authenticate报头域一起使用
- 403 Forbidden： 服务器收到请求，但是拒绝提供服务
- 404 Not Found：请求资源不存在，eg，输入了错误的url
- 500 Internal Server Error： 服务器发生不可预期的错误
- 503 Server Unavailable：服务器当前不能处理客户端的请求，一段时间后可能回复正常

#### GET和POST的区别

- HTTP报文层面：GET将请求信息放在URL中，POST放在报文体中， get的请求信息长度是有限制的。post是没有限制的
- 数据库层面： GET符合幂等性和安全性，POST不符合
- 其他层面： GET请求可以被缓存、被存储，而POST不行

#### Cookie 和Session的区别

###### Cookie 简介

- 是服务器发送给客户端的特殊信息，以文本的形式存放在客户端， 存放在Response Header中
- 客户端再次请求的时候，会把Cookie回发
- 服务器接收到后，会解析Cookie生成与客户端相对应的内容

###### Cookie的设置以及发送过程

<img src="/Users/airren/Dropbox/JavaNote/imooc/imooc_剑指offer/img/image-20191024152244843.png" alt="image-20191024152244843" style="zoom:50%;" />

###### Session简介

- 服务器端的机制，在服务器上保存信息
- 解析客户端请求，并操作session id，按需保存状态信息

###### Session的实现方式

- 使用Cookie来实现：  JSESSIONID

- 使用url回写来实现

区别

- Cookie数据存放在客户的浏览器上，session数据存放在服务端
- Session相对于Cookie更安全
- 若考虑减轻服务器负担，应当使用Cookie


## 9. HTTP 和HTTPS 的区别

<img src="/Users/airren/Dropbox/JavaNote/imooc/imooc_剑指offer/img/image-20191024131017298.png" alt="image-20191024131017298" style="zoom:33%;" />

###### SSL （Security Socket Layer， 安全套接层）

- 为网络通信提供安全及数据完整性的一种安全协议
- 是操作系统对外的API，SSL3.9后更名为TSL
- 采用身份验证和数据加密保证网络通信的安全和数据的完整性

###### 加密的方式

- 对称加密： 加密和解密使用同一个密钥
- 非对称加密： 加密使用的密钥和解密使用的密钥是不相同的
- 哈希算法： 将任意长度的信息转换我固定长度的值，算法不可逆 MD5
- 数字签名： 证明某个消息或者文件是某人发出/认同的

###### HTTPS数据传输流程

- 浏览器支持的加密算法信息发送给服务器

- 服务器选择一套浏览器支持的加密算法，以证书的形式回发浏览器

- 浏览器验证证书的合法性，并结合证书公钥加密信息发送给服务器

- 服务器使用私钥解密信息，验证哈希，加密响应消息回发浏览器

- 浏览器解密响应消息，并对消息进行验真，之后进行加密交互数据

###### 区别

- HTTPS 需要到CA申请证书， HTTP不需要

- HTTPS密文传输，HTTP明文传输

- 连接方式不同，HTTPS默认使用443端口，HTTP使用80端口
- HTTPS = HTTP+加密+认证+完整性保护，较HTTP安全

HTTPS真的很安全吗

- 默认浏览器填充http://, 请求需要进行跳转，有被劫持的风险
- 可以使用HSTS(HTTP Strict Transport Security)优化

##   10. Socket相关

进程间通信 pid唯一标识本地的进程，本地唯一

Socket是对TCP/IP协议的抽象，是操作系统对外开放的接口

<img src="/Users/airren/Dropbox/JavaNote/imooc/imooc_剑指offer/img/image-20191024135405676.png" alt="image-20191024135405676" style="zoom:50%;" />

Socket起源于Unix，一切皆是文件

#### Socket通信流程

![image-20191024135524568](/Users/airren/Dropbox/JavaNote/imooc/imooc_剑指offer/img/image-20191024135524568.png)

## 11. 网络知识总结

OSI七层架构 TCP/IP 协议

- TCP
  - 三次握手
    - SYN Flood 攻击
    - TCP报文头
    - 为什么要进行三次握手
  - 四次挥手
    - TIME_WAIT
    - CLOSE_WAIT
    - 为什么要进行四次挥手
  - 滑动窗口
  - TCP和UDP的区别

- HTTP
  - 请求结构
  - 响应结构
  - 请求响应步骤
  - 浏览器键入URL经历的流程
  - 状态码
  - GET和POST的区别
  - Cookie和Session的区别
  - HTTP和HTTPS的区别
- Socket