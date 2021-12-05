//import java.lang.reflect.Array;

public class ArrayDeque<T> {

    private int size;
    private T[] items;
    private int addNextFirst;
    private int addNextLast;
    private int removeNextFirst;
    private int removeNextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        addNextFirst = 0;
        addNextLast = 1;
        size = 0;
    }

    private void resize(int capacity) {
        /*
        T[] a = (T[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, size);
        items = a;
         */
        T[] a = (T[]) new Object[capacity];
        if (removeNextFirst > removeNextLast) {
            int copyNum1 = items.length - removeNextFirst;
            System.arraycopy(items, removeNextFirst, a, 0, copyNum1);
            int copyNum2 = removeNextLast + 1;
            System.arraycopy(items, 0, a, copyNum1, copyNum2);
            items = a;
        } else {
            System.arraycopy(items, removeNextFirst, a, 0, removeNextLast - removeNextFirst + 1);
            items = a;
        }
        addNextFirst = items.length - 1;
        addNextLast = size;
        removeNextFirst = 0;
        removeNextLast = size - 1;
    }

    public void addLast(T item) {
        if (isEmpty()) {
            items[addNextLast] = item;
            size = size + 1;
            removeNextLast = addNextLast;
            removeNextFirst = addNextLast;
            addNextLast = addNextLast + 1;
        } else {
            if (addNextFirst == addNextLast) {
                /*
                items[addNextLast] = item;
                size = size + 1;
                removeNextFirst = 0;
                removeNextLast = items.length - 1;
                resize(size * 2);
                addNextFirst = items.length - 1;
                addNextLast = size;
                 */
                resize(size * 2);
                items[addNextLast] = item;
                size = size + 1;
                removeNextLast = addNextLast;
                addNextLast = addNextLast + 1;
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

    public void addFirst(T item) {
        if (isEmpty()) {
            items[addNextFirst] = item;
            size = size + 1;
            removeNextLast = addNextFirst;
            removeNextFirst = addNextFirst;
            addNextFirst = items.length - 1;
        } else {
            if (addNextFirst == addNextLast) {
                /*
                items[addNextFirst] = item;
                size = size + 1;
                removeNextFirst = 0;
                removeNextLast = items.length - 1;
                resize(size * 2);
                addNextFirst = items.length - 1;
                addNextLast = size;
                 */
                resize(size * 2);
                items[addNextFirst] = item;
                size = size + 1;
                removeNextFirst = addNextFirst;
                addNextFirst = addNextFirst - 1;
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

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (removeNextLast == removeNextFirst) {
            T removedItem = items[removeNextLast];
            // items = (T[]) new Object[8];
            addNextFirst = 0;
            addNextLast = 1;
            size = 0;
            return removedItem;
        }
        T removedItem = items[removeNextLast];

        if ((((size - 1)/ (double) (items.length)) >= 0.25) || items.length <= 15) {
            if (removeNextLast == 0) {
                items[removeNextLast] = null;
                size = size - 1;
                addNextLast = removeNextLast;
                removeNextLast = items.length - 1;
            } else {
                items[removeNextLast] = null;
                size = size - 1;
                addNextLast = removeNextLast;
                removeNextLast = removeNextLast - 1;
            }
        } else {
            /*
            T[] a = (T[]) new Object[(int) (items.length / 2)];
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
             */
            resize((int) (items.length * 0.5));
            items[removeNextLast] = null;
            size = size - 1;
        }
        return removedItem;
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (removeNextLast == removeNextFirst) {
            T removedItem = items[removeNextFirst];
            // items = (T[]) new Object[8];
            addNextFirst = 0;
            addNextLast = 1;
            size = 0;
            return removedItem;
        }
        T removedItem = items[removeNextFirst];

        if ((((size - 1) / (double) (items.length)) >= 0.25) || items.length <= 15) {
            if (removeNextFirst == items.length - 1) {
                items[removeNextFirst] = null;
                size = size - 1;
                addNextFirst = removeNextFirst;
                removeNextFirst = 0;
            } else {
                items[removeNextFirst] = null;
                size = size - 1;
                addNextFirst = removeNextFirst;
                removeNextFirst = removeNextFirst + 1;
            }
        } else {
            /*
            T[] a = (T[]) new Object[(int) (items.length / 2)];
            if (removeNextFirst < removeNextLast) {
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
             */
            resize((int) (items.length * 0.5));
            items[removeNextFirst] = null;
            size = size - 1;
        }
        return removedItem;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public T get(int i) {
        if (removeNextFirst > removeNextLast) {
            if (i < items.length - removeNextFirst) {
                return items[removeNextFirst + i];
            } else {
                return items[i - (items.length - removeNextFirst)];
            }
        } else {
            return items[removeNextFirst + i];
        }
    }

    public void printDeque() {
        int i = 0;
        while (i < size) {
            System.out.print(get(i) + " ");
            i = i + 1;
        }
        System.out.println();
    }
    /*
    public int len() {
        return items.length;
    }
     */
}