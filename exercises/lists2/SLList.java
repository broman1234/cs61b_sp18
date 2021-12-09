public class SLList {
    private class IntNode {
        public int item;
        public IntNode next;
        public IntNode(int i, IntNode n) {
            item = i;
            next = n;
        }
    }

    private IntNode sentinel; 
    private int size;

    public SLList() {
        sentinel = new IntNode(63, null);
        size = 0;
    }

    public SLList(int x) {
        sentinel = new IntNode(63, null);
        sentinel.next = new IntNode(x, null);
        size = 1;
    }


    /** Adds an item to the front of the list. */
    public void addFirst(int x) {
        sentinel.next = new IntNode(x, sentinel.next);
        size += 1;
    }    

    /** Retrieves the front item from the list. */
    public int getFirst() {
        return sentinel.next.item;
    }

    /** Returns the number of items in the list. */
    public int size() {
        return size;
    } 

    /** Adds an item to the end of the list. */
    public void addLast(int x) {
        size = size + 1;
        IntNode p = sentinel;

        /* Advance p to the end of the list. */
        while (p.next != null) {
            p = p.next;
        }
        p.next = new IntNode(x, null);
    }

    public void insert(int item, int position) {
        if (sentinel.next == null || position == 0) {
            addFirst(item);
            return;
        }

        IntNode currentNode = sentinel.next.next;
        while (position > 1 && currentNode.next != null) {
            position -= 1;
            currentNode = currentNode.next;
        }

        IntNode newNode = new IntNode(item, currentNode.next);
        currentNode.next = newNode;
    }

    /** Crashes when you call addLast on an empty SLList. Fix it. */
    public static void main(String[] args) {
        SLList x = new SLList();
        x.addFirst(10);
        x.addLast(20);
        System.out.println(x.size);
    }
}