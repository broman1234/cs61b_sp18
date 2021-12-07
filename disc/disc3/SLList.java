import javax.print.attribute.standard.PrintQuality;

public class SLList {
    public class IntNode {
        public int item;
        public IntNode next;
        public IntNode(int item, IntNode next) {
            this.item = item;
            this.next = next;
        }
    }

    private IntNode sentinel;
    private int size;

    public SLList() {
        sentinel = new IntNode(63, null);
        size = 0;
    }

    public void addFirst(int x) {
        sentinel.next = new IntNode(x, sentinel.next);
        size = size + 1;
    }

    /* My solution */
    public void insert(int item, int position) {
        if (sentinel.next == null || position == 0) {
            addFirst(item);
            return;
        }
        if (position >= size) {
            IntNode p = sentinel;
            while(p.next != null) {
                p = p.next;
            }
            p.next = new IntNode(item, null);
        } else {
            IntNode p = sentinel;
            IntNode q = sentinel.next;
            while (position > 0) {
                p = p.next;
                q = q.next;
                position = position - 1;
            }
            p.next = new IntNode(10, q);
        }
        size = size + 1;
    }

    /* Solution, which is recommended */
    public void insertSol(int item, int position) {
        if (sentinel.next == null || position == 0) {
            addFirst(item);
            return;
        }
        IntNode p = sentinel.next;
        while (position > 1 && p.next != null) {
            position--;
            p = p.next;
        }
        IntNode newNode = new IntNode(item, p.next);
        p.next = newNode;
        size = size + 1;
    }

    /* My solution only changes items, not IntNodes, wouldn't recommend*/
    public void reverse() {
        sentinel.next = reverseHelper(sentinel.next, size);
    }

    private IntNode reverseHelper(IntNode p, int pSize) {
        if (pSize == 1) {
            return p;
        }
        if (pSize == 2) {
            int leftItem = p.item;
            p.item = p.next.item;
            p.next.item = leftItem;
            return p;
        }
        if (pSize % 2 == 0) {
            IntNode m = p;
            int position = pSize / 2 - 1;
            while (position > 0) {
                m = m.next;
                position = position - 1;
            }

            IntNode q = m.next;
            m.next = null;

            q = reverseHelper(q, pSize / 2);
            IntNode n = q;
            position = pSize / 2 - 1;
            while (position > 0) {
                n = n.next;
                position = position - 1;
            }
            p = reverseHelper(p, pSize / 2);
            n.next = p;
            return q;
        } else {
            IntNode m = p;
            int position = pSize / 2 - 1;
            while (position > 0) {
                m = m.next;
                position = position - 1;
            }

            IntNode midNode = m.next;
            IntNode q = m.next.next;
            m.next = null;
            midNode.next = null;

            q = reverseHelper(q, pSize / 2);
            IntNode n = q;
            position = pSize / 2 - 1;
            while (position > 0) {
                n = n.next;
                position = position - 1;
            }
            p = reverseHelper(p, pSize / 2);
            n.next = midNode;
            midNode.next = p;
            return q;
        }
    }

    private void exchangeItems(IntNode p, int position) {
        IntNode q = p;
        while (position > 0) {
            q = q.next;
            position = position - 1;
        }
        IntNode k = q;
        for (int i = 0; i < size / 2; i++) {
            position = size / 2 - 1 - i;
            while (position > 0) {
                k = k.next;
                position = position - 1;
            }
            int leftItem = p.item;
            p.item = k.item;
            k.item = leftItem;
            p = p.next;
            k = q;
        }
    }

    public void reverseIterative() {
        IntNode p = sentinel.next;
        if (size % 2 == 0) {
            int position = size / 2;
            exchangeItems(p, position);
        } else {
            int position = size / 2 + 1;
            exchangeItems(p, position);
        }
    }

    /* The solution exchanges IntNodes not items, which is better */
    public void reverseIterativaSol() {
        IntNode p = sentinel.next;
        IntNode resultInProgress = null;
        while (p != null) {
            IntNode remainingList = p.next;
            p.next = resultInProgress;
            resultInProgress = p;
            p = remainingList;
        }
        sentinel.next = resultInProgress;
    }

    public void reverseSol() {
        sentinel.next = reverseRecursiveHelper(sentinel.next);
    }

    private IntNode reverseRecursiveHelper(IntNode front) {
        if (front == null || front.next == null) {
            return front;
        } else {
            IntNode reversed = reverseRecursiveHelper(front.next);
            front.next.next = front;
            front.next = null;
            return reversed;
        }
    }

    /* My solution of exchanging IntNodes recursively */
    public void reverseMySol() {
        sentinel.next = reserveHelperSol(sentinel.next);
    }

    public IntNode reserveHelperSol(IntNode p) {
        if (p == null || p.next == null) {
            return p;
        }
        IntNode remainingList = p.next;
        p.next = null;
        IntNode Result = reserveHelperSol(remainingList);
        IntNode q = Result;
        while (q.next != null) {
            q = q.next;
        }
        q.next = p;
        return Result;
    }

    public void printList() {
        IntNode p = sentinel.next;
        while (p != null) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        SLList s = new SLList();
        s.addFirst(20);
        System.out.println(s.size);
        s.insert(10, 1);
        s.insertSol(1, 8);
        s.insert(2, 8);
        s.insert(3, 8);
        System.out.println(s.size);
        s.reverseIterativaSol(); // recommended
        s.printList();
        s.reverseSol(); // recommended
        s.printList();
        s.reverse();
        s.printList();
        s.reverseIterative();
        s.printList();
        s.reverseMySol();
        s.printList();
    }
}