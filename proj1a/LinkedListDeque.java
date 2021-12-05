public class LinkedListDeque<Shooka>{
    private ShookaNode sentinel;
    private int size;

    public class ShookaNode {
        public ShookaNode prev;
        public Shooka item;
        public ShookaNode next;

        public ShookaNode(ShookaNode p, Shooka i, ShookaNode n) {
            prev = p;
            item = i;
            next = n;
        }

        public Shooka nodeGetRecursive(int index) {
            if (index == 0) {
                return item;
            }
            return next.nodeGetRecursive(index - 1);
        }
    }

    public LinkedListDeque() {
        size = 0;
        sentinel = new ShookaNode(sentinel, null, sentinel);
    }

    public void LinkedListDeque(Shooka item) {
        sentinel.prev = new ShookaNode(sentinel, item, sentinel);
        sentinel.next = sentinel.prev;
        size = 1;
    }

    public void addFirst(Shooka item) {
        if (size == 0) {
            LinkedListDeque(item);
        } else {
            sentinel.next = new ShookaNode(sentinel, item, sentinel.next);
            sentinel.next.next.prev = sentinel.next;
            size = size + 1;
        }
    }

    public void addLast(Shooka item) {
        if (size == 0) {
            LinkedListDeque(item);
        } else {
            sentinel.prev.next = new ShookaNode(sentinel.prev, item, sentinel);
            sentinel.prev = sentinel.prev.next;
            size = size + 1;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        ShookaNode p = sentinel.next;
        while (p!= sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
    }

    public Shooka removeFirst() {
        if (size == 0) {
            return null;
        } else {
            size = size - 1;
            Shooka itemToBeReturned = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev.next = null;
            sentinel.next.prev.prev = null;
            sentinel.next.prev = sentinel;
            return itemToBeReturned;
        }
    }

    public Shooka removeLast() {
        if (size == 0) {
            return null;
        } else {
            size = size - 1;
            Shooka itemToBeReturned = sentinel.prev.item;
            sentinel.prev.next = null;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next.prev = null;
            sentinel.prev.next = sentinel;
            return itemToBeReturned;
        }
    }

    public Shooka get(int index) {
        if (index >= size) {
            return null;
        }
        if (index == 0) {
            return sentinel.next.item;
        }
        ShookaNode p = sentinel.next;
        while (index > 0) {
            p = p.next;
            index = index - 1;
        }
        return p.item;
    }

    public Shooka getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        if (index == 0) {
            return sentinel.next.item;
        }
        ShookaNode p = sentinel.next;
        return p.next.nodeGetRecursive(index - 1);
    }
}