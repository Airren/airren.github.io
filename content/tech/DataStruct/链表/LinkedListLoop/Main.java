import java.util.*;

class Node {
    private Integer value;
    private Node next;

    public Node(Integer value) {
        this.value = value;
        this.next = null;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getNext() {
        return this.next;
    }

    public static void printLinkedList(Node head) {
        while (head != null) {
            System.out.print(head.getValue());
            System.out.print(" ");
            head = head.getNext();
        }
        System.out.println();
    }
}

class LinkedListCreator {
    public Node createLinkedList(List<Integer> data) {
        Node head = null;
        Node prev = null;
        for (Integer i : data) {
            Node node = new Node(i);
            if (prev != null) {
                prev.setNext(node);
            } else {
                head = node;
            }
            prev = node;
        }
        return head;

    }
}

class LinkedListReverser {
    public Node reverseLinkedList(Node head) {
        Node newHead = null;
        Node curHead = head;
        while (curHead != null) {
            Node node = curHead.getNext();
            curHead.setNext(newHead);
            newHead = curHead;
            curHead = node;
        }
        return newHead;
    }
}

class LinkedListDeleter {
    public Node deleteIfEqual(Node head, int value) {
        while (head != null && head.getValue() == value) {
            head = head.getNext();
        }
        Node curNode = head;
        while (curNode.getNext() != null) {
            if (curNode.getNext().getValue() == value) {
                curNode.setNext(curNode.getNext().getNext());
            } else {
                curNode = curNode.getNext();
            }
        }

        return head;

    }
}

public class Main {

    public static Node deleteNodeEquals(Node head, int value){
        while(head != null && head.getValue() == value){
            head = head.getNext();
        }
        Node curNode = head;
        while(curNode.getNext() != null){
            if(curNode.getNext().getValue() == value){
                curNode.setNext(curNode.getNext().getNext());
            }else{
                curNode = curNode.getNext();
            }
        }
        return head;
    }
    public static void main(String[] args) {
        LinkedListCreator creator = new LinkedListCreator();
        LinkedListReverser reverser = new LinkedListReverser();
        LinkedListDeleter deleter = new LinkedListDeleter();

        Node.printLinkedList(creator.createLinkedList(Arrays.asList(1, 2, 3, 4, 5, 6)));
        Node.printLinkedList(reverser.reverseLinkedList(creator.createLinkedList(Arrays.asList(1, 2, 3, 4, 5, 6))));
        Node.printLinkedList(deleter.deleteIfEqual(creator.createLinkedList(Arrays.asList(1, 2, 3, 4, 5, 6)), 6));


        Node.printLinkedList(deleteNodeEquals(creator.createLinkedList(Arrays.asList(1,2,3,4,5,6,7)), 5));

        System.out.println("Hello World!");
    }
}