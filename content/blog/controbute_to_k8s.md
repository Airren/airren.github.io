---
title: 怎么成为K8s的Contributor
---



Important Doc

1. [Contributor Cheat Sheet](https://github.com/kubernetes/community/blob/master/contributors/guide/contributor-cheatsheet/README-zh.md)









### 加入SIG

SIG(Special Interest Groups) 是Kubernetes社区中关注特定模块的永久组织。

![](https://github.com/kubernetes/community/blob/master/SIG-diagram.png?raw=true)



作为刚参与社区的开发者，可以从sig/app, sig/node, sig/scheduling 这几个SIG开始。



### KEP

KEP(Kubernetes Enhancement Proposal)。对于功能和API的修改都需要先在kubernetes/enhancements仓库对应SIG的目录下提交Proposal才能实施。所有的Proposal都必须经过讨论，通过社区SIG Leader的批准。

很多不熟悉Kubernetes工作流的开发者会在社区中直接提交一个包含API改动的commit，但是如果这类commit没有对应的KEP 是不会被社区合入的。



### 项目设计

所有进入开发状态的功能都会有一个非常详细的KEP。KEP有一个详细的模板，设计是非常规范的。我们再日常的开发中也可以使用类似的格式写设计文档或者技术文档。

最近几年新实现的功能都会有着比较详细的KEP文档



### 分布式协作

Kubernetes 中所有的社区会议都会使用Zoom录制，并上传到Youtube。大多数的讨论和交流也对会再对应的issue和PR中。

每个提交的PR都要通过2K以上的成员的review以及几千个单元测试、集成测试、端到端测试以及扩展性测试，这些测定共同保证了项目的稳定性。



kubernetes/test-infra 项目中包含了Kubernetes的测试基础配置。 很多端到端的测试都会启动Kubernetes集群，并在真实的环境中测试代码的逻辑，这些测试可能一次会执行几十分钟，并且有一定概率出现Flaky。



### 影响力

提高个人和公司在开源社区的影响力，也是参与社区的重要目的。

对个人来说，参与开源项目可以快速理解项目的实现原理以及工作流程。如果想要在未来从事相关的工作，参与开源项目一定是加分项。

对公司来说，参与开源项目可以提高公司在开源社区的话语权，提高公司的技术影响力。足够的话语权也会让开源社区在关键需求上有较快的支持，减少与社区代码的分叉，降低维护成本，并满足公司内部的需求。







### 操作指南

参与开源项目并不一定要从非常复杂的功能或者Proposal开始。任何一个对代码库有利的PR都是值得提交的。修复代码中的typo 或者静态检查错误，作为最开始的工作是没有任何问题的。这能够帮我们快速热身。不过在熟悉了kubernetes的提交流程之后就没有必要做类似的提交了，以为所有的提交都需要Reviewer和Approver的批准，我们应该尽可能的做有意义的变动，减少他们的工作量。



### 从阅读源码开始

我们可以从自己熟悉的模块入手，了解该模块的实现原理，在阅读代码的过程中，我们很容易发现源代码中的一些typo和缺陷，这个时候就可以提交PR修复这些问题。

### 从静态检查开始

.golint_failures 文件中忽略了几百个Package中的静态检查，你可以在其中选择合适的Package作为成为Kubernetes贡献者的而第一步。



### 从项目管理开始

也可以选择Kubernetes的项目管理， sig/release.





### 选取第一个kubernetes问题



issue 列表

使用tag过滤问题  “good first issue” “help wanted” 这些标签表明了对新手非常友好。有时候问题也会被打上错误标签，也许是技术难度被低估了。

TODO 搜索代码库里的TODO。



Go 语言的开发规范、分布式社区的治理方式



代码需要每天都看，留出专门的时间来查看Kubernetes的源码，对它的核心组件的实现看看，构建一下, 测试一下，修一下Bug，typo等。

一般半年之后，你就可以熟悉基础项目，并且开始贡献代码。

当你已经和团队协作的很熟悉，就会自然承接一些子任务，然后团队就会赋予你写的权限，这样就可以顺理成章的成为Kubernetes的committer。这个时候你就是这个子项目的maintainer了



## TODO

- [ ] 加入 Kubenetes Slack Channle
- [ ] 加入邮件列表
- [ ] 加入SIG
- [ ] 参加社区会议
- [ ] 提issue



如何提交PR

https://segmentfault.com/a/1190000040437510



**Reference**

https://www.zhihu.com/question/372403348

https://draveness.me/kubernetes-contributor/

https://cloud.tencent.com/developer/article/1539308

https://aws.amazon.com/cn/blogs/opensource/newbies-guide-to-kubernetes/
