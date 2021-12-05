//import java.lang.reflect.Array;

public class ArrayDeque<Shooka> {

    public int size;
    public Shooka[] items;
    public int addNextFirst;
    public int addNextLast;
    public int removeNextFirst;
    public int removeNextLast;

    public ArrayDeque() {
        items = (Shooka[]) new Object[8];
        addNextFirst = 0;
        addNextLast = 1;
        size = 0;
    }

    private void resize(int capacity) {
        Shooka[] a = (Shooka[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, size);
        items = a;
    }

    public void addLast(Shooka item) {
        if (size == 0) {
            items[addNextLast] = item;
            size = size + 1;
            removeNextLast = addNextLast;
            removeNextFirst = addNextLast;
            addNextLast = addNextLast + 1;
        } else {
            if (addNextFirst == addNextLast) {
                items[addNextLast] = item;
                size = size + 1;
                removeNextFirst = 0;
                removeNextLast = items.length - 1;
                resize(size * 2);
                addNextFirst = items.length - 1;
                addNextLast = size;
            } else {
                if (addNextLast == items.length - 1) {
                    items[addNextLast] = item;
                    size = size + 1;
                    removeNextLast = addNextLast;
                    addNextLast = 0;
                } else {
                    items[addNextLast] = item;
                    size = size + 1;
                    removeNextLast = addNextLast;
                    addNextLast = addNextLast + 1;
                }
            }
        }
    }

    public void addFirst(Shooka item) {
        if (size == 0) {
            items[addNextFirst] = item;
            size = size + 1;
            removeNextLast = addNextFirst;
            removeNextFirst = addNextFirst;
            addNextFirst = items.length - 1;
        } else {
            if (addNextFirst == addNextLast) {
                items[addNextFirst] = item;
                size = size + 1;
                removeNextFirst = 0;
                removeNextLast = items.length - 1;
                resize(size * 2);
                addNextFirst = items.length - 1;
                addNextLast = size;
            } else {
                if (addNextFirst == 0) {
                    items[addNextFirst] = item;
                    size = size + 1;
                    removeNextFirst = addNextFirst;
                    addNextFirst = items.length - 1;
                } else {
                    items[addNextFirst] = item;
                    size = size + 1;
                    removeNextFirst = addNextFirst;
                    addNextFirst = addNextFirst - 1;
                }
            }
        }
    }

    public Shooka removeLast() {
        if (removeNextLast == removeNextFirst) {
            Shooka removedItem = items[removeNextLast];
            items = (Shooka[]) new Object[8];
            addNextFirst = 0;
            addNextLast = 1;
            size = 0;
            return removedItem;
        }
        Shooka removedItem = items[removeNextLast];
        items[removeNextLast] = null;
        size = size - 1;
        if (((size / (double) items.length) >= 0.25) || items.length <= 15) {
            if (removeNextLast == 0) {
                addNextLast = removeNextLast;
                removeNextLast = items.length - 1;
            } else {
                addNextLast = removeNextLast;
                removeNextLast = removeNextLast - 1;
            }
        } else {
            Shooka[] a = (Shooka[]) new Object[(int) (items.length / 2)];
            if (removeNextFirst > removeNextLast) {
                int copyNum1 = items.length - removeNextFirst;
                System.arraycopy(items, removeNextFirst, a, 0, copyNum1);
                int copyNum2 = removeNextLast + 1;
                System.arraycopy(items, 0, a, copyNum1, copyNum2);
                items = a;
            } else {
                System.arraycopy(items, removeNextFirst, a, 0, removeNextLast-removeNextFirst+1);
                items = a;
            }
            addNextFirst = items.length - 1;
            addNextLast = size;
            removeNextFirst = 0;
            removeNextLast = size - 1;
        }
        return removedItem;
    }

    public Shooka removeFirst() {
        if (removeNextLast == removeNextFirst) {
            Shooka removedItem = items[removeNextFirst];
            items = (Shooka[]) new Object[8];
            addNextFirst = 0;
            addNextLast = 1;
            size = 0;
            return removedItem;
        }
        Shooka removedItem = items[removeNextFirst];
        items[removeNextFirst] = null;
        size = size - 1;
        if (((size / (double) items.length) >= 0.25) || items.length <= 15) {
            if (removeNextFirst == items.length - 1) {
                addNextFirst = removeNextFirst;
                removeNextFirst = 0;
            } else {
                addNextFirst = removeNextFirst;
                removeNextFirst = removeNextFirst + 1;
            }
        } else {
            Shooka[] a = (Shooka[]) new Object[(int) (items.length / 2)];
            if (removeNextFirst < removeNextLast) {
                int copyNum1 = (items.length - 1) - removeNextFirst + 1;
                System.arraycopy(items, removeNextFirst, a, 0, copyNum1);
                int copyNum2 = removeNextLast + 1;
                System.arraycopy(items, 0, a, copyNum1, copyNum2);
                items = a;
            } else {
                System.arraycopy(items, removeNextFirst, a, 0, removeNextLast-removeNextFirst+1);
                items = a;
            }
            addNextFirst = items.length - 1;
            addNextLast = size;
            removeNextFirst = 0;
            removeNextLast = size - 1;
        }
        return removedItem;
    }

    public int size() {
        return size;
    }

    public Shooka get(int i) {
        return items[i];
    }

    public int len() {
        return items.length;
    }
}