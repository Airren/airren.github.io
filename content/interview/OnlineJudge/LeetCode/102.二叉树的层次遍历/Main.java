import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class TreeNode {
    Integer val;
    TreeNode right;
    TreeNode left;

    public TreeNode(Integer val) {
        this.val = val;
    }
}

public class Main {

    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> levels = new ArrayList<>();
        if (root == null) {
            return levels;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int level = 0;
        while (!queue.isEmpty()) {
            levels.add(new ArrayList<Integer>());

            int level_length = queue.size();
            for (int i = 0; i < level_length; i++) {
                TreeNode node = queue.poll();
                levels.get(level).add(node.val);

                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            level++;
        }

        return levels;
    }

    public static void main(String[] args) {
        System.out.println("test");
    }
}