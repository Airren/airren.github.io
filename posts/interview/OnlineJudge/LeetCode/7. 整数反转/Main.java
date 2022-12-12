import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        System.out.println(reverse(n));
    }

    public static int reverse(int x) {
        int i = 1;
        if (x < 0) {
            i = -1;
            x = x * i;
        }
        long res = 0;
        while (x > 0) {
            res = res * 10 + x % 10;
            x /= 10;
        }
        if (res > Integer.MAX_VALUE) {
            return 0;
        } else {
            return (int) res;
        }
    }
}
