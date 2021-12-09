public class LinkedListDeque<T> {
    private TNode sentinel;
    private int size;

    private class TNode {
        private TNode prev;
        private T item;
        private TNode next;

        public TNode(TNode p, T i, TNode n) {
            prev = p;
            item = i;
            next = n;
        }

        public T nodeGetRecursive(int index) {
            if (index == 0) {
                return item;
            }
            return next.nodeGetRecursive(index - 1);
        }
    }

    public LinkedListDeque() {
        size = 0;
        sentinel = new TNode(sentinel, null, sentinel);
    }

    public void addFirst(T item) {
        if (size == 0) {
            sentinel.prev = new TNode(sentinel, item, sentinel);
            sentinel.next = sentinel.prev;
            size = 1;
        } else {
            sentinel.next = new TNode(sentinel, item, sentinel.next);
            sentinel.next.next.prev = sentinel.next;
            size = size + 1;
        }
    }

    public void addLast(T item) {
        if (size == 0) {
            sentinel.prev = new TNode(sentinel, item, sentinel);
            sentinel.next = sentinel.prev;
            size = 1;
        } else {
            sentinel.prev.next = new TNode(sentinel.prev, item, sentinel);
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
        TNode p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            size = size - 1;
            T itemToBeReturned = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev.next = null;
            sentinel.next.prev.prev = null;
            sentinel.next.prev = sentinel;
            return itemToBeReturned;
        }
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            size = size - 1;
            T itemToBeReturned = sentinel.prev.item;
            sentinel.prev.next = null;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next.prev = null;
            sentinel.prev.next = sentinel;
            return itemToBeReturned;
        }
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }
        if (index == 0) {
            return sentinel.next.item;
        }
        TNode p = sentinel.next;
        while (index > 0) {
            p = p.next;
            index = index - 1;
        }
        return p.item;
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        if (index == 0) {
            return sentinel.next.item;
        }
        TNode p = sentinel.next;
        return p.next.nodeGetRecursive(index - 1);
    }
}
