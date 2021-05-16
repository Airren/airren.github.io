# imooc_React16 快速上手 实现TodoList

## react简介以及语法基础

react Fiber // React 16 之后的版本对应的框架  

`redux`

react 环境搭建

React 脚手架工具  create-react-app

```sh
npx create-react-app todolist  # create-react-app
cd todolist
yarn start  # npm run start
```



##### 什么是组件

component

#### 简单的jsx语法

## 项目代码

```
├── src
│   ├── TodoItem.js
│   ├── TodoList.js
│   ├── index.js
│   ├── serviceWorker.js
│   └── style.css
```



### Index.js 

Index.js 是项目的入口

```js
import React from 'react';
import ReactDOM from 'react-dom';
import './style.css';
// 组件，大写字母开头   s
import TodoList from './TodoList';


ReactDOM.render(<TodoList />, document.getElementById('root'));
```

> 所有的组件都是大写字母开头的， 引入`React`是为了识别大写字母开头的组件

### TodoList.js

组件, ES6语法规范下的一些新的写法

```js
import React, {Component, Fragment}from 'react';
import TodoItem from './TodoItem';

// 组件需要继承 React.Component, 才能具有组件的方法以及生命周期
class TodoList extends Component {

  // constructor(props){
  //   super(props);
  //   this.state={
  //     list:[
  //       'learn react',
  //       'learn english'
  //     ]
  //   }
  //this.handleDelete=this.handleDelete.bind(this) 
  // }

  state = {
    list: [],
    inputValue: ''
  }

  handleAdd=()=> {
    // this.state.list.push("hello world")   这样写是不工作的
    this.setState({
      list: [...this.state.list, this.state.inputValue],
      inputValue: ''
    })

    console.log("add new item: " + this.state.list)
  }

  handleInputChange(e) {
    this.setState({
      inputValue: e.target.value
    })
  }

  handleDelete(index) {
    // 如果要对state 中的数据进行操作，最好通过副本去操作
    const list = [...this.state.list]
    list.splice(index,1)
    // this.setState({
    //   list:list
    // })
    this.setState({list})
  }

  getItems(){
    return(
      this.state.list.map((item, index) => {
        return <TodoItem key={index} 
        handleDelete={this.handleDelete.bind(this)}
        content={item} 
        index={index}/>
      })
    )
  }

  render() {
    // jsx 语法
    // 父组件通过属性的方式向子组件传递参数
    // 子组件通过props的方式接受父组件传递的参数
    return (
      <Fragment>
        <div>
          <input value={this.state.inputValue} onChange={this.handleInputChange.bind(this)}></input>
          {/* <button style={{background:'red', color:'#fff'}} onClick={this.handleAdd.bind(this)} >add</button> */}
          <button className='red-btn' onClick={this.handleAdd} >add</button>
        </div>
        <ul>
          {
            // this.state.list.map((item, index) => {
            //   return <TodoItem key={index} 
            //   handleDelete={this.handleDelete.bind(this)}
            //   content={item} 
            //   index={index}/>
            //   // return <li onClick={this.handleDelete.bind(this, index)} key={index}>{item}</li>
            // })
            this.getItems()
          }
        </ul>
      </Fragment>
    );
  }
}

export default TodoList;

```



### TodoItem.js

```js
import React from 'react'

class TodoItem extends React.Component{
    // 子组件如果要和父组件通信要调用父组件传递过来的方法实现传值
    handleDelete(){
         // 子组件向父组件传值
        this.props.handleDelete(this.props.index)
        console.log(this.props.index)
    }

    render(){
        const {content} = this.props;
        return(
          <div onClick={this.handleDelete.bind(this)}>
              {/* {this.props.content} */}
              {content}
          </div>  
        )
    }
}

export default TodoItem; 
```

