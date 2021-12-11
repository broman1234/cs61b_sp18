public class ArrayDeque<T> {

    private int size;
    private T[] items;
    private int addNextFirst; /* the front of the array where the item will be added to */
    private int addNextLast; /* the back of the array where the item will be added to */
    private int removeNextFirst; /* the index of the item at the beginning of the array. */
    private int removeNextLast; /* the index of the item at the end of the array */

    public ArrayDeque() {
        items = (T[]) new Object[8];
        addNextFirst = 0;
        addNextLast = 1;
        size = 0;
    }

    private boolean timeToIncreaseSize() {
        return addNextFirst == addNextLast;
    }

    private boolean timeToReduceSize() {
        return (((size - 1) / (double) (items.length)) < 0.25) && items.length > 15;
    }

    private void resize(int capacity) {
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

    /** helper function for adding an item to an array. */
    private void addItem(T item, int addIndex) {
        items[addIndex] = item;
        size = size + 1;
    }

    /** helper function for removing an item from an array. */
    private void removeItem(int removeIndex) {
        items[removeIndex] = null;
        size = size - 1;
    }

    private boolean isOneItemLeft() {
        return removeNextLast == removeNextFirst;
    }

    private T removeToEmpty(int removeIndex) {
        T removedItem = items[removeIndex];
        addNextFirst = 0;
        addNextLast = 1;
        size = 0;
        return removedItem;
    }

    public void addLast(T item) {
        if (isEmpty()) {
            addItem(item, addNextLast);
            removeNextLast = addNextLast;
            removeNextFirst = addNextLast;
            addNextLast = addNextLast + 1;
        } else {
            if (timeToIncreaseSize()) {
                resize(size * 2);
                addItem(item, addNextLast);
                removeNextLast = addNextLast;
                addNextLast = addNextLast + 1;
            } else {
                addItem(item, addNextLast);
                removeNextLast = addNextLast;
                if (addNextLast == items.length - 1) {
                    addNextLast = 0;
                } else {
                    addNextLast = addNextLast + 1;
                }
            }
        }
    }

    public void addFirst(T item) {
        if (isEmpty()) {
            addItem(item, addNextFirst);
            removeNextFirst = addNextFirst;
            removeNextLast = addNextFirst;
            addNextFirst = items.length - 1;
        } else {
            if (timeToIncreaseSize()) {
                resize(size * 2);
                addItem(item, addNextFirst);
                removeNextFirst = addNextFirst;
                addNextFirst = addNextFirst - 1;
            } else {
                addItem(item, addNextFirst);
                removeNextFirst = addNextFirst;
                if (addNextFirst == 0) {
                    addNextFirst = items.length - 1;
                } else {
                    addNextFirst = addNextFirst - 1;
                }
            }
        }
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (isOneItemLeft()) {
            return removeToEmpty(removeNextLast);
        }
        T removedItem = items[removeNextLast];
        if (timeToReduceSize()) {
            resize((int) (items.length * 0.5));
            removeItem(removeNextLast);
            addNextLast = removeNextLast;
            removeNextLast = removeNextLast - 1;
        } else {
            removeItem(removeNextLast);
            addNextLast = removeNextLast;
            if (removeNextLast == 0) {
                removeNextLast = items.length - 1;
            } else {
                removeNextLast = removeNextLast - 1;
            }
        }
        return removedItem;
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (isOneItemLeft()) {
            return removeToEmpty(removeNextFirst);
        }
        T removedItem = items[removeNextFirst];
        if (timeToReduceSize()) {
            resize((int) (items.length * 0.5));
            removeItem(removeNextFirst);
            addNextFirst = removeNextFirst;
            removeNextFirst = removeNextFirst + 1;
        } else {
            removeItem(removeNextFirst);
            addNextFirst = removeNextFirst;
            if (removeNextFirst == items.length - 1) {
                removeNextFirst = 0;
            } else {
                removeNextFirst = removeNextFirst + 1;
            }
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
}
