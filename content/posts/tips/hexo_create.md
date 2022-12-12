---
title: 「Hexo」 搭建部署
subtitle: 'Hexo搭建部署'
author: Airren
catalog: true
header-img: ''
tags:
  - Hexo
p: hexo/hexo_create
date: 2020-08-07 01:11:55
---

## Hexo 安装

https://hexo.io/zh-cn/docs/

1. 安装Node.js

2. 安装Git

3. 安装Hexo
   
   ```sh
   sudo npm install -g hexo-cli
   ```
   
   如果在mac中安装报`/usr/lib/node_modules/`的操作权限问题，执行以下命令。
   
   ```sh
   sudo chown -R `whoami` /usr/local/lib/node_modules
   ```

4. 初始化项目
   
   ```sh
   hexo init blog
   ```
   
   ![image-20200807012444173](hexo_create/image-20200807012444173.png)
   
   创建完成后，当前目录下会有一个`xx_blog`的文件夹，具体的文件夹查询官网hexo.io

## Hexo 部署到Nginx & Github.io

### 开发机

   在自己写Blog的Pc上安装插件

```sh
yarn add hexo-deployer-git
```

### 服务器

1. 在即将部署的服务器上执行以下操作
   
   ```sh
   yum install git 
   
   useradd -m git  # 创建一个git用户，用来运行git服务
                # 新建git用户并非必要，但是为了安全起见，还是用git用户单独来运行git服务
   
   passwd git
   ```

2. 设置PC到服务器的git用户免密登录
   
   ```sh
   # 生成ssh密钥
   ssh-keygen
   # 将公钥添加到server
   ssh-copy-id git@serverIp
   ```

3. 在服务器上初始化一个Git仓库
   
   ```sh
   mkdir -p /var/repo
   ca /var/repo
   git init --bare blog.git  # --bare 初始化一个裸仓库，裸仓库没有工作区，只为共享而存在
   chown -R git:git blog.git
   ```
   
   ​    配置Git hooks
   
   ```sh
   mkdir /var/repo/blog.git/hooks
   vi post-receive
   ```
   
   ​    写入以下内容
   
   ```sh
   #!/bin/sh
   git --work-tree=/var/www/blog --git-dir=/home/git/byte_gopher_blog.git checkout -f
   # /var/www/blog 是部署目录。 每次push完成之后 
   ```
   
   ​    增加可执行权限
   
   ```sh
   chmod +x /var/repo/blog.git/hooks/post-receive
   ```
   
   > 禁用git用户的shell登录权限
   > 
   > ```sh
   > vi /etc/passwd
   > # git:x:1001:1001:,,,:/home/git:/bin/bash
   >  git:x:1001:1001:,,,:/home/git:/usr/bin/git-shell
   > ```
   > 
   > 最后再禁用

4. 部署nginx
   
   创建需要的代理文件夹
   
   ```sh
   mkdir -p /home/www/hexo      #创建目录
   chown -R git:git /home/www/hexo   # 增加git用户权限
   ```
   
      修改nginx配置`/etc/nginx/nginx.conf`
   
   ```sh
       server {
           listen       80 default_server;
           listen       [::]:80 default_server;
           server_name  _;
           root         /home/www/hexo;
   
           include /etc/nginx/default.d/*.conf;
   
           location / {
           }
   
           error_page 404 /404.html;
            location = /40x.html {
           }
   
           error_page 500 502 503 504 /50x.html;
               location = /50x.html {
           }
       }
   ```
   
   5. 配置hexo `_config.yml`
      
      ```yaml
      # URL
      ## If your site is put in a subdirectory, set url as 'http://yoursite.com/child' and root as '/child/'
      url: https://www.bytegopher.com   # 为了避免不必要的麻烦此处设置根域名 & 根目录
      root: /
      
      # Deployment
      ```
      
        branch: gh-pages  # branch name, whaterver
      
      ```
      
      ```

5. 发布Blog
   
   写完博客之后直接发布就可以更新到Nginx服务器& Github.io
   
   ```
   hexo clean & hexo d -g
   ```
   
   ?
   
   
   
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
   
   **参考资料**
   
   https://www.jianshu.com/p/e1ccd49b4e5d
   
   ```
   
   ```
