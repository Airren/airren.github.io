---
title: 「Hugo」Hugo基本使用
---







hugo 增加 评论



编译&发布





## MarkDown 语法

公式解析

引入[MathJax](https://www.mathjax.org/)。MathJax 是一个Javascript库，通过官方提供的CDN集成到自己的页面非常简单，只需把一下内容添加到所有的页面，例如`foot.html`

```html
<script type="text/javascript" async
  src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-MML-AM_CHTML">
</script>
```





## 配置文件 config.toml


代码高亮设置

```toml
pygmentsUseClasses = true
[markup]
  [markup.highlight]
    codeFences = true
    guessSyntax = true
    hl_Lines = ""
    lineNoStart = 1   # display line number
    lineNos = true
    lineNumbersInTable = false
    noClasses = true
    style = "github"
    tabWidth = 4
```



行号已经可以显示了，但是复制的时候会与行号一起复制，修改自定义css

```css
.highlight .ln {
    width: 20px;
    display: block;
    float: left;
    text-align: right;
    user-select: none;   # 表示复制是不能被选中的
    padding-right: 8px;
    color: #ccc;
}
```

