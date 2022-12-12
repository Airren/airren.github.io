import java.util.Scanner;

import jdk.nashorn.internal.runtime.FindProperty;

/*
在一个二维数组中（每个一维数组的长度相同），每一行都按照从左到右递增的顺序排序，
每一列都按照从上到下递增的顺序排序。
请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。*/

public class Solution {
    public static boolean Find(int target, int [][] array) {
        int row = 0;
        int col = array[0].length - 1;
        while(row <= array[0].length -1 && col >=0){
            if(target == array[row][col]){
                return true;
            }else if(target < array[row][col]){
                col --;
            }else if(target > array[row][col]){
                row ++;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // Scanner sc = new Scanner(System.in);
        // String str = sc.nextLine();

        int target = 7;
        int[][] array = {{1,2,8,9},
                         {2,4,9,12},
                         {4,7,10,13},
                         {6,8,11,15}};

        System.out.println(Find(target, array));
    }
}


// 输入样例
/*
7,[[1,2,8,9],[2,4,9,12],[4,7,10,13],[6,8,11,15]]

*/