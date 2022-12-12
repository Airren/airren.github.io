public class Main {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode preHead = new ListNode(-1);

        ListNode pre = preHead;

        while(l1 != null && l2 != null){
            if(l1.val < l2.val){
                pre.next = l1.val;
            }

        }

        return null;
    }
}
 class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}