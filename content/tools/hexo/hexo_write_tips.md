---
title: 「Hexo」Write Tips
subtitle: "写hexo的小技巧"
layout:     post
date: 2020-07-29 01:09:53
author:     "Airren"
catalog:    true
header-img: "post-bg-2015.jpg"
tags:
    - Hexo
---



#  Config
## md conf

```
title: Hello Hexo！  
layout:     post
subtitle:   "hello word, welcome"
date: 2020-07-26 01:09:53
author:     "Airren"
catalog:    true
header-img: "post-bg-js-module.jpg"
tags:
    - Life

```

## _config.yml

```yml
# Hexo Configuration
## Docs: https://hexo.io/docs/configuration.html
## Source: https://github.com/hexojs/hexo/

# Site
title: ByteGopher
subtitle: To Be A Lean Developer!
author: Airren 
language: en
timezone: Asia/Shanghai

# URL
## If your site is put in a subdirectory, set url as 'http://yoursite.com/child' and root as '/child/'
url: https://www.bytegopher.com
root: /
permalink: :year/:month/:day/:title/
permalink_defaults:

#Custom Setting Start

# Site settings
SEOTitle: ByteGopher | Airren
header-img: img/home-bg.jpg
email: renqiqiang@outlook.com
description: "ByteGopher"
keyword: "ByteGopher, Airren"


# SNS settings
# RSS: false
# weibo_username:     Demonbane
# zhihu_username:     Demonbane
github_username:    Airren
twitter_username:   Airrenz
facebook_username:  Airrenz
linkedin_username:  强-任-b8a3b4103

# Build settings
anchorjs: true                          # if you want to customize anchor. check out line:181 of `post.html`


# Disqus settings
disqus_username: bytegopher

# Duoshuo settings
# duoshuo_username: kaijun
# Share component is depend on Comment so we can NOT use share only.
# duoshuo_share: true                     # set to false if you want to use Comment without Sharing


# Analytics settings
# Baidu Analytics
ba_track_id: 4cc1f2d8f3067386cc5cdb626a202900
# Google Analytics
ga_track_id: 'UA-49627206-1'            # Format: UA-xxxxxx-xx
ga_domain: bytegopher.com


# Sidebar settings
sidebar: true                           # whether or not using Sidebar.
sidebar-about-description: "Hi, welcome!"
sidebar-avatar: ./img/avatar.jpg      # use absolute URL, seeing it's used in both `/` and `/about/`


# Featured Tags
featured-tags: true                     # whether or not using Feature-Tags
featured-condition-size: 1              # A tag will be featured if the size of it is more than this condition value


# Friends
friends: [
    {
        title: "Kaijun's Blog",
        href: "http://blog.kaijun.rocks"
    },{
        title: "Hux Blog",
        href: "http://huangxuan.me"
    },
]


#Custom Setting End



# Directory
source_dir: source
public_dir: public
tag_dir: tags
archive_dir: i_dont_wanna_use_default_archives
category_dir: categories
code_dir: downloads/code
i18n_dir: :lang
skip_render:

# Writing
new_post_name: :title.md # File name of new posts
default_layout: post
titlecase: false # Transform title into titlecase
external_link: true # Open external links in new tab
filename_case: 0
render_drafts: false
post_asset_folder: true
relative_link: false
future: true
highlight:
  enable: true
  line_number: true
  auto_detect: true
  tab_replace:

# Category & Tag
default_category: uncategorized
category_map:
tag_map:

# Date / Time format
## Hexo uses Moment.js to parse and display date
## You can customize the date format as defined in
## http://momentjs.com/docs/#/displaying/format/
date_format: YYYY-MM-DD
time_format: HH:mm:ss

# Pagination
## Set per_page to 0 to disable pagination
per_page: 10
pagination_dir: page

# Extensions
## Plugins: https://hexo.io/plugins/
## Themes: https://hexo.io/themes/
theme: huxblog

# Deployment
## Docs: https://hexo.io/docs/deployment.html
deploy:
  type: git
  repo: 
    github: https://github.com/Airren/airren.github.io
    alios: git@hongkong:/home/git/blog.git
  branch: gh-pages

index_generator:
  per_page: 3 ##首頁默认10篇文章标题 如果值为0不分页
archive_generator:
  per_page: 10 ##归档页面默认10篇文章标题
  yearly: true  ##生成年视图
  monthly: true ##生成月视图
tag_generator:
  per_page: 10 ##标签分类页面默认10篇文章
category_generator:
   per_page: 10 ###分类页面默认10篇文章

```


# Hexo Write
## Page
```
hexo new page --path about/me "About me"
```
```
INFO  Created: ~/Desktop/ByteGopher/airren_blog/source/about/me.md
```


## Post
```
hexo new post -p web/https_tips "HTTPS Tips"
```

```
INFO  Created: ~/Desktop/ByteGopher/airren_blog/source/_posts/web/https_tips.md
```


**reference：**

https://xdlrt.github.io/2016/02/18/2016-02-18/
https://www.lagou.com/lgeduarticle/40391.html
https://juejin.im/post/5ab47e48f265da23805991dc
https://blog.csdn.net/weixin_42646103/article/details/105181586?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param
https://zhuanlan.zhihu.com/p/158678677
https://oakland.github.io/2016/05/02/hexo-%E5%A6%82%E4%BD%95%E7%94%9F%E6%88%90%E4%B8%80%E7%AF%87%E6%96%B0%E7%9A%84post/
https://www.jianshu.com/p/e20deec143b1