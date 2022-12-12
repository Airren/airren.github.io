import java.util.*;

class Solution {
    public int lengthOfLongestSubstring(String s) {
        int n = s.length(), ans = 0;
        int i = 0, j = 0;

        Set<Character> set = new HashSet<>();
        while (i < n && j < n) {
            if (!set.contains(s.charAt(j))) {
                set.add(s.charAt(j));
                j++;
                ans = Math.max(ans, j - i);
            } else {
                set.remove(s.charAt(i));
                i++;
            }
        }

        return ans;
    }
}

public class Huadong {
    public static void main(String[] args) {
        Solution solution = new Solution();

        System.out.println(solution.lengthOfLongestSubstring("hell"));

        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        System.out.println();
    }
}