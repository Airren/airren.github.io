import java.util.*;

public class Solutions {

    public class TreeNode {
            int val;
            TreeNode left;
            TreeNode right;
            TreeNode(int x) { val = x; }
         }
    public TreeNode Solutions(int [] pre,int [] in) {
        if(pre == null || in == null || pre.length == 0){
            return null;
        }
        int rootValue = pre[0];
        TreeNode node = new TreeNode(rootValue);
        for(int i = 0; i < in.length; i++){
            if(in[i] == rootValue ){
                if(pre.length > 1){
                    node.left = Solutions(Arrays.copyOfRange(pre, 1, i+1),
                                                 Arrays.copyOfRange(in, 0,i));
                    node.right = Solutions(Arrays.copyOfRange(pre,i+1,pre.length),
                                                  Arrays.copyOfRange(in,i+1,in.length));
                }else{
                    node.left = null;
                    node.right = null;
                }
                
            }
        }
      
        return node;
    }


    public static void main(String[] args) {
        int[] a = new int[]{1,2,3,4,5,6,7};
        int[] b = new int[]{3,2,4,1,6,5,7};

        Solutions solution = new Solutions();
        TreeNode node = solution.Solutions(a,b);
        System.out.println("result");

        int i = 0;

        //int s = i++ + i;
        int tmp = i;
        i = i +1;
        i = tmp +i;
        
        System.out.println(  (byte)129);
    }

   

    
}