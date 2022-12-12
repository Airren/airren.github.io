import java.util.Scanner;

/**
 * 9. 回文数
 */
/*
 * Input: 121 OutPut: true
 * 
 * Input: -121 Output: false
 * 
 */
public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        System.out.println(isPalindrome1(x));
    }

    /**
     * 转成字符串对比
     * 
     * @param x
     * @return
     */
    public static boolean isPalindrome1(int x) {
        String num = "" + x;
        int n = num.length();
        for (int i = 0, j = n - 1; i < n; i++, j--) {
            if (num.charAt(i) != num.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 反转一半数据对比
     * 
     * @param x
     * @return
     */
    public static boolean isPalindrome2(int x) {
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }

        int reverseNum = 0;
        while (x > reverseNum) {
            reverseNum = reverseNum * 10 + x % 10;
            x /= 10;
        }

        return x == reverseNum || x == reverseNum / 10;
    }
}
