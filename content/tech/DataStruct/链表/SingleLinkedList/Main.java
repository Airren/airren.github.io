import java.util.*;
class Node{
    private final int value;
    private Node next;
    
    public Node(int value){
        this.value = value;
        this.next = null;
    }
    public int getValue(){
        return this.value;
    }
    public Node getNext(){
        return this.next;
    }
    public void setNext(Node next){
        this.next = next;
    }
    
    public static void printLinkedList(Node head){
        while(head != null){
            System.out.print(head.getValue());
            System.out.print(" ");
            head = head.next;
        }
        System.out.println("");
    }
    
}
class LinkedListCreator{
    public Node createLinkedList(List<Integer> data){
        if(data.isEmpty()){
            return null;
        }
        Node firstNode = new Node(data.get(0));
        firstNode.setNext(createLinkedList(data.subList(1,data.size())));
        return firstNode;                                          
    }
}

class LinkedListReverser{
    public Node reverseLinkedList(Node head){
        if(head==null || head.getNext()==null){
            return head;
        }
        Node newHead = reverseLinkedList(head.getNext());
        head.getNext().setNext(head);
        head.setNext(null);
        return newHead;
    }
}

public class Main {
    public static void main(String[] args) {
        LinkedListCreator creator = new LinkedListCreator();
        LinkedListReverser reverser = new LinkedListReverser();
        
        // Node.printLinkedList(creator.createLinkedList(Arrays.asList(1)));
        Node.printLinkedList(reverser.reverseLinkedList(creator.createLinkedList(Arrays.asList(1,2,3,4,5))));
        
        System.out.println("Hello World!");
    }
}