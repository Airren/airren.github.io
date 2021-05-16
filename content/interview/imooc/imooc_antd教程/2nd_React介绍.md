# 2. React介绍

## 2.1 React 基本介绍

- FaceBook 开源的JavaScript库

- React结合生态库构成一个MV*框架

React特点

- Declarative 声明式编码

- Component-Based 组件化编码

- 高效-高效的DOM Diff算法，最小化页面重绘

- 单项数据流

MV*框架代表- 只关注视图view层+数据层Model



生态介绍

- Vue生态： Vue+Vue-Router+Vuex+Axios+Babel + Webpack
- React生态： React+React-Router+Redux+Axios+Babel + WebPack

编程式实现

- 需要以具体代码表达在哪里(where)做什么(what)，如何实现（how）

声明式实现

- 只需要声明在哪里（where）做什么（what），而无需关系如何实现（how）

## 2.2 React脚手架、yarn

### 如何安装和使用React脚手架 

```sh
npm install -g create-react-app
creat-react-app my-app

cd my-app
npm start
```

什么是Yarn

- yarn 是新一代的包管理工具

为什么使用Yarn

- 速度快
- 安装版本统一、更安全
- 更简洁的输出
- 更好的语义化

如何使用Yarn

```sh
yarn init
yarn add   # npm install 
yarn remove  # npm uninstall
yarn/yarn install # npm install 或者 npm i
```

http:// reactjs.org/docs/add-react-to-a-new-app.html

### 项目初始化

```sh
npm install -g create-react-app
create-react-app imoocmanager # 路由是没有的 router redux没有


```

目录结构

```sh
node_modules #依赖
public # 静态文件
src  # 核心文件

App.js # 根组件
index.js # 入口
```



> yarn 使用说明 https://yarn.bootcss.com/
>
> 代码通过 **包（package）** (或者称为 **模块（module）**) 的方式来共享。 一个包里包含所有需要共享的代码，以及描述包信息的文件，称为 `package.json` 。

### Yarn 使用介绍

**初始化一个新项目**

```
yarn init
```

**添加依赖包**

```
yarn add [package]
yarn add [package]@[version]
yarn add [package]@[tag]
```

**将依赖项添加到不同依赖项类别中**

分别添加到 `devDependencies`、`peerDependencies` 和 `optionalDependencies` 类别中：

```
yarn add [package] --dev
yarn add [package] --peer
yarn add [package] --optional
```

**升级依赖包**

```
yarn upgrade [package]
yarn upgrade [package]@[version]
yarn upgrade [package]@[tag]
```

**移除依赖包**

```
yarn remove [package]
```

**安装项目的全部依赖**

```
yarn
```

或者

```
yarn install
```

#### 增加路由管理

```
yarn add react-router
```

Webpack 配置： 编译成静态文件



## 2.3 React生命周期介绍

#### React生命周期包含哪些 10 个API

- getDefaultProps

- getInitialState
- componentWillMount
- Render
- componentDidMount
- componentWillReceiveProps
- shouldComponentUpdate
- componentWillupdate
- componentDidUpdate
- componentWillUnmount

<img src="/Users/airren/Dropbox/JavaNote/imooc/imooc_antd教程/img/image-20191110000804621.png" alt="image-20191110000804621" style="zoom:50%;" />