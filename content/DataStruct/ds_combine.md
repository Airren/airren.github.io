---
title: 「算法与数据结构」排列组合
---

## 组合

```java
import java.util.*;

public class Mains{
    public static List<List<Integer>> resultss = new ArrayList<>();

    public void combinations(List<Integer> selected, List<Integer> data, int num){
        if(num == 0){
            resultss.add(new ArrayList<Integer>(selected));
            return;
        }
        if(data.size() == 0 ){
            System.out.print("");
            return;
        }
        selected.add(data.get(0));
        combinations(selected, data.subList(1, data.size()), num -1);
        selected.remove(data.get(0));
        combinations(selected, data.subList(1, data.size()), num );
    }
    public static void main(String[] args) {
        Mains combin = new Mains();
        int[] nums = new int[]{1,2,3,4,5};
        combin.combinations(new ArrayList<Integer>(),Arrays.asList(1,2,3,4),3);
        for (List<Integer> res : Mains.resultss) {
            System.out.println(res.toString());
        }
    }
}

/*
Output

[1, 2, 3]
[1, 2, 4]
[1, 3, 4]
[2, 3, 4]

*/

```



