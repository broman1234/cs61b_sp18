public class IntList {
    public int first;
    public IntList rest;
    public IntList(int f, IntList r) {
        this.first = f;
        this.rest = r;
    }

    public void skippify() {
        IntList p = this;
        int n = 1;
        while (p != null) {
            IntList next = p.rest;
            for (int i = 0; i < n; i++) {
                if (next == null) {
                    break;
                }
                next = next.rest;
            }
            p.rest = next;
            p = next;
            n++;
        }
    }

    public static void removeDuplicates(IntList p) {
        if (p == null) {
            return;
        }
        IntList current = p.rest;
        IntList previous = p;
        while (current.rest != null) {
            if (previous.first == current.first) {
                previous.rest = current.rest;
            } else {
                previous = previous.rest;
            }
            current = current.rest;
        }
    }

    public static void printLinkedList(IntList lst) {
        IntList p = lst;
        while (p != null) {
            System.out.print(p.first + " ");
            p = p.rest;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        IntList a = new IntList(1, null);
        a = new IntList(2, a);
        a = new IntList(3, a);
        a = new IntList(4, a);
        a = new IntList(5, a);
        a = new IntList(6, a);
        a = new IntList(7, a);
        a = new IntList(8, a);
        a = new IntList(9, a);

        a.skippify();
        printLinkedList(a);

        IntList b = new IntList(3, null);
        b = new IntList(2, b);
        b = new IntList(2, b);
        b = new IntList(2, b);
        b = new IntList(1, b);

        removeDuplicates(b);
        printLinkedList(b);
    }
}