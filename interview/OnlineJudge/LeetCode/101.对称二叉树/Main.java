import java.util.LinkedList;
import java.util.Queue;

public class Main {

    public static boolean isSymmetric(TreeNode root) {

        Queue<TreeNode> queue = new LinkedList<>();

        queue.offer(root);
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode t1 = queue.poll();
            TreeNode t2 = queue.poll();

            if (t1 == null && t2 == null) {
                continue;
            }
            if (t1 == null || t2 == null) {
                return false;
            }
            if (t1.val != t2.val) {
                return false;
            }

            queue.add(t1.left);
            queue.add(t2.right);
            queue.add(t1.right);
            queue.add(t2.left);

        }
        return true;
    }

}

class TreeNode {
    Integer val;
    TreeNode right;
    TreeNode left;

    public TreeNode(Integer val) {
        this.val = val;
    }
}