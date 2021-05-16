# 图

## 图的表示

#### 邻接矩阵（Adjacent Matrix）



<img src="img/image-20191014164712503.png" alt="image-20191014164712503" style="zoom: 33%;" align="left"/>

#### 邻接表（Adjacent Matrix）



<img src="img/image-20191014164759194.png" alt="image-20191014164759194" style="zoom:33%;" align="left" />

邻接表适合表示稀疏图（Sparse Graph）

邻接矩阵适合表示稠密图（Dense Graph）

#### 稀疏图

<img src="img/image-20191014165406186.png" alt="image-20191014165406186" style="zoom:33%;" />

#### 稠密图和完全图

<img src="img/image-20191014165515525.png" alt="image-20191014165515525" style="zoom:33%;" />

## 寻路

获得两点之间的一条路径

<img src="img/image-20191015103539741.png" alt="image-20191015103539741" style="zoom:33%;" />

图的深度优先遍历-时间复杂度

稀疏图（邻接表）：O(V+E)

稠密图（邻接矩阵）：O(v^2^)

深度优先遍历算法-有向图

- dfs 查看是否有环- 有向图

## 广度优先遍历和最短路径

借助队列实现

<img src="img/image-20191015105139728.png" alt="image-20191015105139728" style="zoom:33%;" />

广度优先遍历求出了无权图的最短路径

图的广度优先遍历-时间复杂度

稀疏图（邻接表）：O(V+E)

稠密图（邻接矩阵）：O(v^2^)



## 无权图的应用- 迷宫生成，PS抠图

#### flood fill

魔棒抠图 连通分量 

<img src="img/image-20191015110344923.png" alt="image-20191015110344923" style="zoom:33%;" />

<img src="img/image-20191015110427920.png" alt="image-20191015110427920" style="zoom:33%;" />

#### 扫雷

<img src="img/image-20191015110611365.png" alt="image-20191015110611365" style="zoom:33%;" />

#### 走迷宫、 迷宫生成

迷宫的本质是一棵树 本质是一个生成树的过程

<img src="img/image-20191015110646483.png" alt="image-20191015110646483" style="zoom:33%;" />

不能只用一种方式遍历， 随机队列遍历

<img src="img/image-20191015111359237.png" alt="image-20191015111359237" style="zoom:33%;" />

#### 欧拉路径 哈密尔顿路径

<img src="img/image-20191015111453393.png" alt="image-20191015111453393" style="zoom:33%;" />

#### 二分图

<img src="img/image-20191015111532614.png" alt="image-20191015111532614" style="zoom:33%;" />

同学选课

#### 地图着色

