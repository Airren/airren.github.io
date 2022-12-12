import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        int i = 4;
        double d = 4.0;
        String s = "HackerRank ";

        // Scanner scan = new Scanner(System.in);

        /* Declare second integer, double, and String variables. */
        int a;
        double b;
        String c;
        /* Read and save an integer, double, and String to your variables. */
        // Note: If you have trouble reading the entire String, please go back and
        // review the Tutorial closely.
        Scanner sc = new Scanner(System.in);
        /* Print the sum of both integer variables on a new line. */
        a = sc.nextInt();
        /* Print the sum of the double variables on a new line. */
        b = sc.nextDouble();
        sc.nextLine();
        /*
         * Concatenate and print the String variables on a new line; the 's' variable
         * above should be printed first.
         */
        c = sc.nextLine();
        System.out.println(i + a);
        System.out.println(d + b);
        System.out.println(s + c);
        sc.close();
    }
}