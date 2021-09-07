---
title: 「Regex」正则表达式
---



正则表达式主要用来进行字符串匹配操作



```sh
^[0-9]+abc$
# ^ 表示以xxx开头的字符串
# [0-9] 表示匹配0-9之间的一个数字，例如 8； [0-9]+表示匹配0-9之间的多个数字，例如：867
# abc$ 表示以abc结尾的字符串，$表示以xxx结尾
```



```sh
^[a-z0-9_-]{5,16}$
# 只能含有小写字母，数字，下划线,减号，且长度为5-16的字符串
```



| 字符 | Description                                  |
| ---- | -------------------------------------------- |
| `.`  | 除了`\r \n`以外的任何单字符                  |
| `\w` | 匹配字母、数字、下划线，等价于`[A-Za-Z0-9_]` |
|      |                                              |
| `*`  | 匹配前面的子表达式零次或者多次               |
|      |                                              |
