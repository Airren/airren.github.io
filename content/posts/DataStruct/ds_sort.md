---
title: 「算法与数据结构」排序算法
---

# 排序算法

## 术语说明

- **稳定**：如果a原本在b前面，而a=b，排序之后a仍然在b的前面；

- **不稳定**：如果a原本在b的前面，而a=b，排序之后a可能会出现在b的后面；

- **内部排序**：待排序的数据可以全部放入内存中；

- **外部排序**：待排序数据的数量很大，以致于内存不能一次容纳全部记录，所以在排序过程中**需要对外存(磁盘)进行访问**；

- **时间复杂度：** 一个算法执行所耗费的时间。

- **空间复杂度**：运行完一个程序所需内存的大小。

##  排序算法总结

| 排序算法 | 平均时间复杂度 |  最好情况   |  最坏情况   | 空间复杂度 | 排序方式  | 稳定性 |
| :------: | :------------: | :---------: | :---------: | :--------: | :-------: | :----: |
| 冒泡排序 |    O(n^2^)     |    O(n)     |   O(n^2^)   |    O(1)    | In-place  |  稳定  |
| 选择排序 |    O(n^2^)     |   O(n^2^)   |   O(n^2^)   |    O(1)    | In-place  | 不稳定 |
| 插入排序 |    O(n^2^)     |    O(n)     |   O(n^2^)   |    O(1)    | In-place  |  稳定  |
| 希尔排序 |   O(n logn)    | O(nlog^2^n) | O(nlog^2^n) |    O(1)    | In-place  | 不稳定 |
| 归并排序 |   O(n logn)    |  O(n logn)  |  O(n logn)  |    O(n)    | Out-place |  稳定  |
| 快速排序 |   O(n logn)    |  O(n logn)  |   O(n^2^)   | O(n logn)  | In-place  | 不稳定 |
|  堆排序  |   O(n logn)    |  O(n logn)  |  O(n logn)  |    O(1)    | In-place  | 不稳定 |
| |||||||
| 计数排序 |     O(n+k)     |   O(n+k)    |   O(n+k)    |    O(k)    | Out-place |  稳定  |
|  桶排序  |     O(n+k)     |   O(n+k)    |   O(n^2^)   |   O(n+k)   | Out-place |  稳定  |
| 基数排序 |     O(n*k)     |   O(n*k)    |   O(n*k)    |   O(n+k)   | Out-place |  稳定  |

n:数据规模；k:"桶"的个数；n-place:占用常数内存，不占用额外内存；Out-place:占用额外内存

#### 算法分类

时间分类

- 非线性时间比较类排序：通过比较来决定元素间的相对次序，由于其时间复杂度不能突破O(nlogn)，因此称为非线性时间比较类排序。
- 线性时间非比较类排序：不通过比较来决定元素间的相对次序，它可以突破基于比较排序的时间下界，以线性时间运行，因此称为线性时间非比较类排序。

比较分类

- 比较：快速排序、归并排序、堆排序、冒泡排序
  在排序的最终结果里，元素之间的次序依赖于它们之间的比较。每个数都必须和其他数进行比较，才能确定自己的位置。
  
- 非比较：计数排序、基数排序、桶排序
  非比较排序是通过确定每个元素之前，应该有多少个元素来排序。针对数组arr，计算arr之前有多少个元素，则唯一确定了arr在排序后数组中的位置。

> 非线性时间比较类排序
>
> - 交换排序
>   - 冒泡排序
>   - 快速排
> - 插入排序
>
>   - 简单插入排序
>   - 希尔排序
> - 选择排序
>
>   - 简单选择排序
>   - 堆排序
> - 归并排序
>   - 二路归并排序
>
>   - 多路归并排序
>
> 线性时间非比较类排序
>
> - 计数排序
> - 桶排序
> - 基数排序



## 冒泡排序 (Bubble Sort)

冒泡排序是一种简单的排序算法。它重复地走访过要排序的数列，一次比较两个元素，如果它们的顺序错误就把它们交换过来。走访数列的工作是重复地进行直到没有再需要交换，也就是说该数列已经排序完成。这个算法的名字由来是因为越小的元素会经由交换慢慢“浮”到数列的顶端。 

#### 算法描述

- 比较相邻的元素。如果第一个比第二个大，就交换它们两个；
- 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对，这样在最后的元素应该会是最大的数；
- 针对所有的元素重复以上的步骤，除了最后一个；
- 重复步骤1~3，直到排序完成。

####  动图演示

![img](ds_sort/849589-20171015223238449-2146169197.gif)

#### 代码实现

```Java
public class BubbleSort {
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        int tmp;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) { // 相邻元素两两对比
                    tmp = arr[j];          // 元素交换
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = { 6, 1, 2, 7, 9, 3, 4, 5, 10, 8 };
        bubbleSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}

/* Output
1 2 3 4 5 6 7 8 9 10
*/
```



## 选择排序（Selection Sort）

选择排序(Selection-sort)是一种简单直观的排序算法。它的工作原理：首先在未排序序列中找到最小（大）元素，存放到排序序列的起始位置，然后，再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。以此类推，直到所有元素均排序完毕。 

#### 算法描述

n个记录的直接选择排序可经过n-1趟直接选择排序得到有序结果。具体算法描述如下：

- 初始状态：无序区为R[1..n]，有序区为空；
- 第i趟排序(i=1,2,3…n-1)开始时，当前有序区和无序区分别为R[1..i-1]和R(i..n）。该趟排序从当前无序区中-选出关键字最小的记录 R[k]，将它与无序区的第1个记录R交换，使R[1..i]和R[i+1..n)分别变为记录个数增加1个的新有序区和记录个数减少1个的新无序区；
- n-1趟结束，数组有序化了。

#### 动图演示

![img](ds_sort/849589-20171015224719590-1433219824.gif)

#### 代码实现

```Java
public class SelectionSort {
    public static void selectionSort(int[] arr) {
        int n = arr.length;
        int tmp, minIndex;
        for (int i = 0; i < n - 1; i++) {
            minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[minIndex] > arr[j]) {
                    minIndex = j;
                }
            }
            tmp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = tmp;
        }
    }

    public static void main(String[] args) {
        int[] arr = { 6, 1, 2, 7, 9, 3, 4, 5, 10, 8 };
        selectionSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ", ");
        }
    }
}
```

##  插入排序（Insertion Sort）

插入排序（Insertion-Sort）的算法描述是一种简单直观的排序算法。它的工作原理是通过构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。

####  算法描述

一般来说，插入排序都采用in-place在数组上实现。具体算法描述如下：

- 从第一个元素开始，该元素可以认为已经被排序；
- 取出下一个元素，在已经排序的元素序列中从后向前扫描；
- 如果该元素（已排序）大于新元素，将该元素移到下一位置；
- 重复步骤3，直到找到已排序的元素小于或者等于新元素的位置；
- 将新元素插入到该位置后；
- 重复步骤2~5。
#### 动图演示

![img](ds_sort/849589-20171015225645277-1151100000.gif)

#### 代码实现

```Java
public class InsertionSort {
    public static void insertionSort(int[] arr) {
        int n = arr.length;
        int preIndex, current;
        for (int i = 1; i < n; i++) {
            preIndex = i - 1;
            current = arr[i];
            while (preIndex >= 0 && current < arr[preIndex]) {
                arr[preIndex + 1] = arr[preIndex];
                preIndex--;
            }
            arr[preIndex + 1] = current;
        }
    }

    public static void main(String[] args) {
        int[] arr = { 6, 1, 2, 7, 9, 3, 4, 5, 10, 8 };
        insertionSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}
```

#### 算法分析

插入排序在实现上，通常采用in-place排序（即只需用到O(1)的额外空间的排序），因而在从后向前扫描过程中，需要反复把已排序元素逐步向后挪位，为最新元素提供插入空间。

## 希尔排序（Shell Sort）

1959年Shell发明，第一个突破O(n2)的排序算法，是简单插入排序的改进版。它与插入排序的不同之处在于，它会优先比较距离较远的元素。希尔排序又叫**缩小增量排序**。

#### 算法描述

先将整个待排序的记录序列分割成为若干子序列分别进行直接插入排序，具体算法描述：

- 选择一个增量序列t1，t2，…，tk，其中ti>tj，tk=1；
- 按增量序列个数k，对序列进行k 趟排序；
- 每趟排序，根据对应的增量ti，将待排序列分割成若干长度为m 的子序列，分别对各子表进行直接插入排序。仅增量因子为1 时，整个序列作为一个表来处理，表长度即为整个序列的长度。

####  动图演示

![img](ds_sort/849589-20180331170017421-364506073.gif)

![img](ds_sort/140156o7nq6qd76zd66nzn.jpg)

#### 代码实现

```Java
public class ShellSort {
    public static void shellSort(int[] arr) {
        int n = arr.length;
        for (int gap = Math.floorDiv(n, 2); gap > 0; gap = Math.floorDiv(gap, 2)) {
            // 注意：这里和动图演示的不一样，动图是分组执行，实际操作是多个分组交替执行
            for (int i = gap; i < n; i++) {
                int j = i;
                int current = arr[i];
                while (j - gap >= 0 && current < arr[j - gap]) {
                    arr[j] = arr[j - gap];
                    j = j - gap;
                }
                arr[j] = current;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = { 6, 1, 2, 7, 9, 3, 4, 5, 10, 8 };
        shellSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}
/* Output
1 2 3 4 5 6 7 8 9 10 
*/

```

#### 算法分析

希尔排序的核心在于间隔序列的设定。既可以提前设定好间隔序列，也可以动态的定义间隔序列。动态定义间隔序列的算法是《算法（第4版）》的合著者Robert Sedgewick提出的。　

##  归并排序（Merge Sort）

归并排序是建立在归并操作上的一种有效的排序算法。该算法是采用分治法（Divide and Conquer）的一个非常典型的应用。将已有序的子序列合并，得到完全有序的序列；即先使每个子序列有序，再使子序列段间有序。若将两个有序表合并成一个有序表，称为2-路归并。 

#### 算法描述

- 把长度为n的输入序列分成两个长度为n/2的子序列；
- 对这两个子序列分别采用归并排序；
- 将两个排序好的子序列合并成一个最终的排序序列。

#### 动图演示

![img](ds_sort/849589-20171015230557043-37375010.gif)

#### 代码实现

```Java
import java.util.Arrays;

public class MergeSort {
    public static void main(String[] args) {
        int[] arr = { 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void sort(int []arr){
        int[] temp = new int[arr.length];//在排序前，先建好一个长度等于原数组长度的临时数组，避免递归中频繁开辟空间
        sort(arr,0,arr.length-1,temp);
    }

    private static void sort(int[] arr, int left, int right, int[] temp) {
        if (left < right) {
            int mid = (left + right) / 2;
            sort(arr, left, mid, temp);// 左边归并排序，使得左子序列有序
            sort(arr, mid + 1, right, temp);// 右边归并排序，使得右子序列有序
            merge(arr, left, mid, right, temp);// 将两个有序子数组合并操作
        }
    }

    private static void merge(int[] arr, int left, int mid, int right, int[] temp) {
        int i = left;// 左序列指针
        int j = mid + 1;// 右序列指针
        int t = 0;// 临时数组指针
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[t++] = arr[i++];
            } else {
                temp[t++] = arr[j++];
            }
        }
        while (i <= mid) {// 将左边剩余元素填充进temp中
            temp[t++] = arr[i++];
        }
        while (j <= right) {// 将右序列剩余元素填充进temp中
            temp[t++] = arr[j++];
        }
        t = 0;
        // 将temp中的元素全部拷贝到原数组中
        while (left <= right) {
            arr[left++] = temp[t++];
        }
    }
}

```

#### 算法分析

归并排序是一种稳定的排序方法。和选择排序一样，归并排序的性能不受输入数据的影响，但表现比选择排序好的多，因为始终都是O(nlogn）的时间复杂度。代价是需要额外的内存空间。

## 快速排序（Quick Sort）

快速排序的基本思想：通过一趟排序将待排记录分隔成独立的两部分，其中一部分记录的关键字均比另一部分的关键字小，则可分别对这两部分记录继续进行排序，以达到整个序列有序。

#### 算法描述

快速排序使用分治法来把一个串（list）分为两个子串（sub-lists）。具体算法描述如下：

- 从数列中挑出一个元素，称为 “基准”（pivot）；
- 重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作；
- 递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。

#### 动图演示

![img](ds_sort/849589-20171015230936371-1413523412.gif)

#### 代码实现

```Java
public class QuickSort {
    public static void quickSort(int[] arr, int low, int high) {
        int i, j, temp, t;
        if (low > high) {
            return;
        }
        i = low;
        j = high;
        // temp就是基准位
        temp = arr[low];
        while (i < j) {
            // 先看右边，依次往左递减
            while (temp <= arr[j] && i < j) {
                j--;
            }
            // 再看左边，依次往右递增
            while (temp >= arr[i] && i < j) {
                i++;
            }
            // 如果满足条件则交换
            if (i < j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }

        }
        // 最后将基准为与i和j相等位置的数字交换
        arr[low] = arr[i];
        arr[i] = temp;
        // 递归调用左半数组
        quickSort(arr, low, j - 1);
        // 递归调用右半数组
        quickSort(arr, j + 1, high);
    }

    public static void main(String[] args) {
        int[] arr = { 6, 1, 2, 7, 9, 3, 4, 5, 10, 8 };
        quickSort(arr, 0, arr.length - 1);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}
```

## 堆排序（Heap Sort）

堆排序（Heapsort）是指利用堆这种数据结构所设计的一种排序算法。堆积是一个近似完全二叉树的结构，并同时满足堆积的性质：即子结点的键值或索引总是小于（或者大于）它的父节点。

####  算法描述

- 将初始待排序关键字序列(R1,R2….Rn)构建成大顶堆，此堆为初始的无序区；
- 将堆顶元素R[1]与最后一个元素R[n]交换，此时得到新的无序区(R1,R2,……Rn-1)和新的有序区(Rn),且满足R[1,2…n-1]<=R[n]；
- 由于交换后新的堆顶R[1]可能违反堆的性质，因此需要对当前无序区(R1,R2,……Rn-1)调整为新堆，然后再次将R[1]与无序区最后一个元素交换，得到新的无序区(R1,R2….Rn-2)和新的有序区(Rn-1,Rn)。不断重复此过程直到有序区的元素个数为n-1，则整个排序过程完成。

#### 动图演示

![img](ds_sort/849589-20171015231308699-356134237.gif)

#### 代码实现



## 计数排序（Counting Sort）

计数排序不是基于比较的排序算法，其核心在于将输入的数据值转化为键存储在额外开辟的数组空间中。 作为一种线性时间复杂度的排序，计数排序要求输入的数据必须是有确定范围的整数。

####  算法描述

- 找出待排序的数组中最大和最小的元素；
- 统计数组中每个值为i的元素出现的次数，存入数组C的第i项；
- 对所有的计数累加（从C中的第一个元素开始，每一项和前一项相加）；
- 反向填充目标数组：将每个元素i放在新数组的第C(i)项，每放一个元素就将C(i)减去1。

####  动图演示

![img](ds_sort/849589-20171015231740840-6968181.gif)

#### 算法分析

计数排序是一个稳定的排序算法。当输入的元素是 n 个 0到 k 之间的整数时，时间复杂度是O(n+k)，空间复杂度也是O(n+k)，其排序速度快于任何比较排序算法。当k不是很大并且序列比较集中时，计数排序是一个很有效的排序算法。

##桶排序（Bucket Sort）

桶排序是计数排序的升级版。它利用了函数的映射关系，高效与否的关键就在于这个映射函数的确定。桶排序 (Bucket sort)的工作的原理：假设输入数据服从均匀分布，将数据分到有限数量的桶里，每个桶再分别排序（有可能再使用别的排序算法或是以递归方式继续使用桶排序进行排）。

####  算法描述?

- 设置一个定量的数组当作空桶；
- 遍历输入数据，并且把数据一个一个放到对应的桶里去；
- 对每个不是空的桶进行排序；
- 从不是空的桶里把排好序的数据拼接起来。 

#### 图片演示

![img](ds_sort/849589-20171015232107090-1920702011.png)

#### 代码实现



#### 算法分析

桶排序最好情况下使用线性时间O(n)，桶排序的时间复杂度，取决与对各个桶之间数据进行排序的时间复杂度，因为其它部分的时间复杂度都为O(n)。很显然，桶划分的越小，各个桶之间的数据越少，排序所用的时间也会越少。但相应的空间消耗就会增大。 

## 基数排序（Radix Sort）

基数排序是按照低位先排序，然后收集；再按照高位排序，然后再收集；依次类推，直到最高位。有时候有些属性是有优先级顺序的，先按低优先级排序，再按高优先级排序。最后的次序就是高优先级高的在前，高优先级相同的低优先级高的在前。

####  算法描述

- 取得数组中的最大数，并取得位数；
- arr为原始数组，从最低位开始取每个位组成radix数组；
- 对radix进行计数排序（利用计数排序适用于小范围数的特点）；

####  动图演示

![img](https://images2017.cnblogs.com/blog/849589/201710/849589-20171015232453668-1397662527.gif)

#### 算法分析

基数排序基于分别排序，分别收集，所以是稳定的。但基数排序的性能比桶排序要略差，每一次关键字的桶分配都需要O(n)的时间复杂度，而且分配之后得到新的关键字序列又需要O(n)的时间复杂度。假如待排数据可以分为d个关键字，则基数排序的时间复杂度将是O(d*2n) ，当然d要远远小于n，因此基本上还是线性级别的。

基数排序的空间复杂度为O(n+k)，其中k为桶的数量。一般来说n>>k，因此额外空间需要大概n个左右。



**基数排序 vs 计数排序 vs 桶排序**

这三种排序算法都利用了桶的概念，但对桶的使用方法上有明显差异：

- 基数排序：根据键值的每位数字来分配桶
- 计数排序：每个桶只存储单一键值
- 桶排序：每个桶存储一定范围的数值



 

#### 参考链接

https://www.cnblogs.com/onepixel/articles/7674659.html

https://www.cnblogs.com/guoyaohua/p/8600214.html

https://blog.csdn.net/zhangshk_/article/details/82911093

https://blog.csdn.net/meibenxiang/article/details/92796909

https://www.cnblogs.com/itsharehome/p/11058010.html
