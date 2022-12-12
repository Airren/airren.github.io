

# 3. 主页面架构设计

课程目标介绍

第二章 项目主页工程搭建

1. 基础插件安装，less文件加载配置
2. 项目主页结构开发
3. 菜单组件开发
4. 头部组件开发
5. 底部组件开发

## 3-1 基础插件安装（1）	

### 基础插件安装，less文件加载配置

安装所需的插件

- 安装React-Router, Axios
- 安装antD界面框架
- 暴露webpack配置文件
- 安装less-loader
- 修改less-loader

```sh
 yarn add react-router-dom axios less-loader # 3.0升级到 4.0


```

AntD 是基于`less`开发的

#### 暴露webpack文件使用less

```sh
yarn eject
```

> create-react-app添加less配置
>
> <font color=red> 修改完成配置后需要重启项目</font>，从后向前使用， less的配置放在css，scss之后
>
> 可能需要删除`node_module`文件夹重新`yarn install`
>
> `yarn add less`
>
> **需要修改的文件config/webpack.config.js**	
>
> ```js
> // style files regexes
> const cssRegex = /\.css$/;
> const cssModuleRegex = /\.module\.css$/;
> const lessRegex = /\.less$/;
> const lessModuleRegex = /\.module\.less$/;
> const sassRegex = /\.(scss|sass)$/;
> const sassModuleRegex = /\.module\.(scss|sass)$/;
> ```
```js
{
  test: lessRegex,
    exclude: lessModuleRegex,
      use: getStyleLoaders(
        {
          importLoaders: 2,
          sourceMap: isEnvProduction
          ? shouldUseSourceMap
          : isEnvDevelopment,
        },
        'less-loader'
      ),   
        sideEffects: true,
},

  {
    test: lessModuleRegex,
      use: getStyleLoaders(
        {
          importLoaders: 2,
          sourceMap: isEnvProduction
          ? shouldUseSourceMap
          : isEnvDevelopment,
          modules: true,
          getLocalIdent: getCSSModuleLocalIdent,
        },
        'less-loader'
      ),
  },
```

#### 使用antd的组件

```sh
yanr add antd
```

```tsx
import React from 'react'
import Child from './Child'
import './index.less'
import { Button } from 'antd'
import 'antd/dist/antd.css'
export default class Life extends React.Component {

    // constructor(props) {
    //     super(props);
    //     this.state = {
    //         count: 0
    //     };
    // }
    state = {
        count: 0
    }

    // ES 6 写法
    handleAdd = () => {
        this.setState({
            count: this.state.count + 1
        })
    }

    handleClick() {
        this.setState({
            count: this.state.count + 1
        })
    }


    render() {
        // let style = {
        //     padding:200
        // }
        // return 必须有根元素，不能同时出现两个div
        // return <div style={style}>
        return <div className="content">
            <p>React声明周期介绍</p>
            <Button onClick={this.handleAdd}>AntD点击一下</Button>
            <button onClick={this.handleAdd}>点击一下</button>
            <button onClick={this.handleClick.bind(this)}>点击一下</button>
            <p>{this.state.count}</p>
            <Child name={this.state.count}></Child>
        </div>
    }
}
```



#### 使用 babel-plugin-import

[babel-plugin-import](https://github.com/ant-design/babel-plugin-import) 是一个用于按需加载组件代码和样式的 babel 插件（[原理](https://ant.design/docs/react/getting-started-cn#按需加载)），现在我们尝试安装它并修改 `config-overrides.js` 文件。

```sh
yarn add babel-plugin-import
```



```sh
yarn add less@^2.7.3   # 制修订版本安装
```

> babel-plugin-import配置babel按需引入antd模块
>
> 将less版本降到3.0以下 比如安装 2.7.3版本。
>
> **需要修改的文件config/webpack.config.js**	
>
> ```js
> {
>   test: /\.(js|mjs|jsx|ts|tsx)$/,
>     include: paths.appSrc,
>       loader: require.resolve('babel-loader'),
>         options: {
>           customize: require.resolve(
>             'babel-preset-react-app/webpack-overrides'
>           ),
> 
>             plugins: [
>               [
>                 require.resolve('babel-plugin-named-asset-import'),
>                 {
>                   loaderMap: {
>                     svg: {
>                       ReactComponent: '@svgr/webpack?-prettier,-svgo![path]',
>                     },
>                   },
>                 },
>               ],
>               ['import',{libraryName:'antd',style:true}],
>             ],
>               // This is a feature of `babel-loader` for webpack (not Babel itself).
>               // It enables caching results in ./node_modules/.cache/babel-loader/
>               // directory for faster rebuilds.
>               cacheDirectory: true,
>                 // Don't waste time on Gzipping the cache
>                 cacheCompression: false,
>         },
>         },
> ```
>
> 

#### 更换主题

```js
options:{
  modules: false,
    modifyVars: {
      "@primary-color": "#f9c700" // 全局主色
    }
},
```

## 3-3 项目主页结构开发

主页结构定义

- 页面结构定义
- 目录结构的定义
- 栅格系统使用
- calc计算方法使用



> calc  动态计算长度值











3-2 基础插件安装（2）
3-3 （安装课程中Webpack版本的用户请忽略这一小节）Webpack4.19.1版本文件合并后配置按需加载
3-4 页面结构开发（1）
3-5 页面结构开发（2）
3-6 菜单组件开发（1）
3-7 菜单组件开发（2）
3-8 头部组件实现（1）
3-9 头部组件实现（2）
3-10 底部组件功能实现（1）
3-11 底部组件功能实现（2）