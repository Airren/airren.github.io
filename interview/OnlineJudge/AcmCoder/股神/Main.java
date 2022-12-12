import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Integer> intList = new ArrayList<>();
        while (sc.hasNextInt()) {
            intList.add(sc.nextInt());
        }
        for (Integer v : intList) {
            System.out.println(getMoney(v));
        }
    }

    public static int getMoney(int n) {

        int i = 0;// i统计遇到了多少次下跌
        int j = 2;// 每次下跌之后上涨的天数，包含已经下跌的那天
        int k = n;
        while (k > j) {
            i += 2;
            k -= j;
            ++j;
        }
        return n - i;
    }
}