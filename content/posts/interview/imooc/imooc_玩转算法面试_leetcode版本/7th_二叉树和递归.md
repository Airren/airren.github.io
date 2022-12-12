# 二叉树和递归

## 二叉树天然的递归结构

<img src="/Users/airren/Dropbox/JavaNote/imooc/imooc_玩转算法面试/img/image-20191030011100767.png" alt="image-20191030011100767" style="zoom:50%;" />

满二叉树

#### 二叉树的前序遍历 递归实现

```java
void preOrder(TreeNode node){
  if(node==null){
    return;  // 递归终止条件
  }
   System.out.print(node.val);
   preOrder(node.left);    // 递归过程
   preOrder(node.right);
}
```

二叉树的定义：空是一棵二叉树

#### 二叉树总是否包含某个key

```Java
boolean contain(TreeNode node, Key key){
  if(node == null){
    return false;
  }
  if(key == node.key){
    return true;
  }
  if(contain(node.left,key)|| contain(node.right, key)){
    return true;
  }
  return false;
}
```



```java
// c++ 释放二叉树的内存
void destory(TreeNode node){
  if(node == null){
    return;
  }
  destory(node.left);
  destory(node.right);
  delete node;
  count--
}

```

#### LeetCode 104： Maximum Depth of Binary Tree

求一棵二叉树的最高深度，从根节点到叶子节点的最长路径长度

###### 递归实现

```java
public static int maxDepth(TreeNode root){
  if(root == NULL){
    return 0;
  }
  return Math.max(maxDepth(root.left), maxDepth(root.right))+1;
}
```

###### 非递归实现

```java
class Solution {
  public int maxDepth(TreeNode root) {
    Queue<Pair<TreeNode, Integer>> stack = new LinkedList<>();
    if (root != null) {
      stack.add(new Pair(root, 1));
    }

    int depth = 0;
    while (!stack.isEmpty()) {
      Pair<TreeNode, Integer> current = stack.poll();
      root = current.getKey();
      int current_depth = current.getValue();
      if (root != null) {
        depth = Math.max(depth, current_depth);
        stack.add(new Pair(root.left, current_depth + 1));
        stack.add(new Pair(root.right, current_depth + 1));
      }
    }
    return depth;
  }
};
```



复习二叉树相关的所有操作

#### LeetCode  111：Minimum Depth of Binary Tree

求一棵二叉树的最低深度，从根节点到叶子节点的最短路径长度

```Java
class Solution {
  public int minDepth(TreeNode root) {
    if (root == null) {
      return 0;
    }
    
    if ((root.left == null) && (root.right == null)) {
      return 1;
    }

    int min_depth = Integer.MAX_VALUE;
    if (root.left != null) {
      min_depth = Math.min(minDepth(root.left), min_depth);
    }
    if (root.right != null) {
      min_depth = Math.min(minDepth(root.right), min_depth);
    }

    return min_depth + 1;
  }
}
```



## 一个简单的二叉树问题引发的血案

#### 226： Invert Binary Tree

反转一棵二叉树

Max Howell 因为不会做这道题目而被Google拒绝

Google：90%of our engineer use the software you wrote(Homebrew), but you can't invert a binary tree on a whiteboard so fuck off.

```java
public TreeNode invertTree(TreeNode root) {
    if (root == null) {
        return null;
    }
    TreeNode right = invertTree(root.right);
    TreeNode left = invertTree(root.left);
    root.left = right;
    root.right = left;
    return root;
}
```



#### 100: Same Tree

给出两棵二叉树，判断这两棵二叉树是否完全一样

```Java
  public boolean isSameTree(TreeNode p, TreeNode q) {
    // p and q are both null
    if (p == null && q == null) return true;
    // one of p and q is null
    if (q == null || p == null) return false;
    if (p.val != q.val) return false;
    return isSameTree(p.right, q.right) &&
            isSameTree(p.left, q.left);
  }
```



#### 101: Sysmmetric Tree

给出一棵二叉树，判断其是否是左右对称的

```Java
public boolean isSymmetric(TreeNode root) {
    return isMirror(root, root);
}

public boolean isMirror(TreeNode t1, TreeNode t2) {
    if (t1 == null && t2 == null) return true;
    if (t1 == null || t2 == null) return false;
    return (t1.val == t2.val)
        && isMirror(t1.right, t2.left)
        && isMirror(t1.left, t2.right);
}
```

#### 222: Count Complete Tree Node

给定一个棵完全二叉树，求完全二叉树节点的个数

完全二叉树: 除了最后一层，所有层的节点数达到最大，与此同时，最后一层的所有节点都在最左侧。 堆使用完全二叉树。

满二叉树： 所有层的节点数达到最大。

```Java
    public int countNodes(TreeNode root) {
        if(root == null){
           return 0;
        } 
        int left = countLevel(root.left);
        int right = countLevel(root.right);
        if(left == right){
            return countNodes(root.right) + (1<<left);
        }else{
            return countNodes(root.left) + (1<<right);
        }
    }
    private int countLevel(TreeNode root){
        int level = 0;
        while(root != null){
            level++;
            root = root.left;
        }
        return level;
    }
```



#### 110 : Balanced Binary Tree

判断一棵树是否为平衡二叉树

平衡二叉树： 每一个节点的左右子树的高度差不超过1

```java
public boolean isBalanced(TreeNode root) {
    //它是一棵空树
    if (root == null) {
        return true;
    }
    //它的左右两个子树的高度差的绝对值不超过1
    int leftDepth = getTreeDepth(root.left);
    int rightDepth = getTreeDepth(root.right);
    if (Math.abs(leftDepth - rightDepth) > 1) {
        return false;
    }
    //左右两个子树都是一棵平衡二叉树
    return isBalanced(root.left) && isBalanced(root.right);

}

private int getTreeDepth(TreeNode root) {
    if (root == null) {
        return 0;
    }
    int leftDepth = getTreeDepth(root.left);
    int rightDepth = getTreeDepth(root.right);
    return Math.max(leftDepth, rightDepth) + 1;
}
```



## 注意递归的终止条件

#### 112： Path sum

给出一棵二叉树以及一个数字sum, 判断这棵二叉树上是否存在一条从根到<font color=red>叶子</font>的路径。其路径上所有节点和为sum。



易错情况

<img src="/Users/airren/Dropbox/JavaNote/imooc/imooc_玩转算法面试/img/image-20191031011425387.png" alt="image-20191031011425387" style="zoom:50%;" />

```Java
public static boolean hasPathSum(TreeNode root, int sum){
  if( root == null){  // 递归终止条件有问题
		return sum == 0;
  }
  if(hasPathSum(root.left, sum -root.val)){
    return true;
  }
   if(hasPathSum(root.right, sum -root.val)){
    return true;
  }
  return false;
}
```



```Java
public static boolean hasPathSum(TreeNode root, int sum){
  if(root == null){
    return false;
  }
  if( root.left == null && root.right = null){  // 递归终止条件有问题
		return sum == root.val;
  }
  if(hasPathSum(root.left, sum -root.val)){
    return true;
  }
   if(hasPathSum(root.right, sum -root.val)){
    return true;
  }
  return false;
  // return hasPathSum(root.left, sum -root.val)||hasPathSum(root.right, sum -root.val);
}
```



#### 404：Sum of Left Leaves

求出一棵二叉树所有左叶子的和

```Java
public int sumOfLeftLeaves(TreeNode root) {
    if (root == null) return 0;
    if (root.left == null) return  sumOfLeftLeaves(root.right); //如果左子树为空，那么只需返回右子树的左叶子和
    if (root.left.left == null && root.left.right == null) // 如果左子树为叶子节点，那么需返回右子树的左叶子和 + 左孩子的值
        return sumOfLeftLeaves(root.right) + root.left.val;
    return sumOfLeftLeaves(root.left) + sumOfLeftLeaves(root.right); // 其他情况需返回左右子树的左叶子和之和
}
```



257: Binary Tree Path

给定一棵二叉树，返回所有表示从根节点到叶子节点路径的字符串

```Java
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> list = new ArrayList<>();
        if(root == null){
            return list;
        }
        if(root.left == null && root.right == null){
           list.add(""+root.val);
        }
        List<String> left = binaryTreePaths(root.left);
        for(String str: left){
            list.add(""+root.val+"->"+str);
        }
        List<String> right = binaryTreePaths(root.right);
         for(String str: right){
             list.add(""+root.val+"->"+str);
        }
        return list;
    }
```



113：Path Sum II

给定一棵二叉树，返回所有从根节点到叶子节点的路径，其和为sum

对于右侧二叉树，sum = 22，结果为[[5,4,11,2],[5,4,8,5]]



129：Sum root to Leaf Numbers

给定一棵二叉树，每个节点只包含数字0-9，从根节点到叶子节点的每条路径可以表示成一个数，求这些数的和。



## 更复杂的递归逻辑

437： Path Sum II

给出一棵二叉树以及一个数字sum，判断在这棵二叉树上存在多少条路径，其路径上所有节点和为sum。

- 其中路径不一定哟啊起始于根节点；终止于叶子节点
- 路径可以从任意节点开始，但是只能是向下走的。