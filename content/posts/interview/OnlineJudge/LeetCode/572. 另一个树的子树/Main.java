public class Main {

    public static boolean isSubtree(TreeNode s, TreeNode t) {
        if (s == null && t == null) {
            return true;
        }
        if (s == null || t == null) {
            return false;
        }
        if (s.val == t.val) {
            return isEqual(s, t) || isSubtree(s.left, t) || isSubtree(s.right, t);
        }
        return isSubtree(s.right, t) || isSubtree(s.left, t);
    }

    public static boolean isEqual(TreeNode l, TreeNode r) {
        if (l == null && r == null) {
            return true;
        }
        if (l == null || r == null) {
            return false;
        }
        if (l.val == r.val) {
            return isEqual(l.lefr, r.left) && isEqual(l.right, r.right);
        }
        return false;
    }

}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}