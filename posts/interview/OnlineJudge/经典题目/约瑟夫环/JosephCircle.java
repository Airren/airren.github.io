import java.util.*;
 
public class JosephCircle{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int X = sc.nextInt();

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            list.add(i+1);
        }

        int count =0;
        int i = -1;
        while (list.size()!=0) {
            i++;
            if (i==list.size()) {
                i=0;
            }
            count++;
            if (count ==X) {
                System.out.println(list.get(i));
                 list.remove(i);
                count = 0;
                i--;
            }

        }
    }

}
    
