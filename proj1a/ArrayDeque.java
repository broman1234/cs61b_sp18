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

    /** helper function to the function addLast(T item).
     * @param item
     * @param addIndex
     */
    private void addLastItem(T item, int addIndex) {
        items[addIndex] = item;
        size = size + 1;
        removeNextLast = addIndex;
    }

    /** helper function to the funtion addFirst(T item).
     * @param item
     * @param addIndex
     */
    private void addFirstItem(T item, int addIndex) {
        items[addIndex] = item;
        size = size + 1;
        removeNextFirst = addIndex;
    }

    /** helper function to the function removeLast().
     * @param removeIndex
     */
    private void removeLastItem(int removeIndex) {
        items[removeIndex] = null;
        size = size - 1;
        addNextLast = removeIndex;
    }

    /** helper function to the function removeFirst().
     * @param removeIndex
     */
    private void removeFirstItem(int removeIndex) {
        items[removeIndex] = null;
        size = size - 1;
        addNextFirst = removeIndex;
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
            /*
            items[addNextLast] = item;
            size = size + 1;
            removeNextLast = addNextLast;
             */
            addLastItem(item, addNextLast);
            removeNextFirst = addNextLast;
            addNextLast = addNextLast + 1;
        } else {
            if (timeToIncreaseSize()) {
                resize(size * 2);
                /*
                items[addNextLast] = item;
                size = size + 1;
                removeNextLast = addNextLast;
                 */
                addLastItem(item, addNextLast);
                addNextLast = addNextLast + 1;
            } else {
                if (addNextLast == items.length - 1) {
                    /*
                    items[addNextLast] = item;
                    size = size + 1;
                    removeNextLast = addNextLast;
                     */
                    addLastItem(item, addNextLast);
                    addNextLast = 0;
                } else {
                    /*
                    items[addNextLast] = item;
                    size = size + 1;
                    removeNextLast = addNextLast;
                     */
                    addLastItem(item, addNextLast);
                    addNextLast = addNextLast + 1;
                }
            }
        }
    }

    public void addFirst(T item) {
        if (isEmpty()) {
            /*
            items[addNextFirst] = item;
            size = size + 1;
            removeNextFirst = addNextFirst;
             */
            addFirstItem(item, addNextFirst);
            removeNextLast = addNextFirst;
            addNextFirst = items.length - 1;
        } else {
            if (timeToIncreaseSize()) {
                resize(size * 2);
                /*
                items[addNextFirst] = item;
                size = size + 1;
                removeNextFirst = addNextFirst;
                 */
                addFirstItem(item, addNextFirst);
                addNextFirst = addNextFirst - 1;
            } else {
                if (addNextFirst == 0) {
                    /*
                    items[addNextFirst] = item;
                    size = size + 1;
                    removeNextFirst = addNextFirst;
                     */
                    addFirstItem(item, addNextFirst);
                    addNextFirst = items.length - 1;
                } else {
                    /*
                    items[addNextFirst] = item;
                    size = size + 1;
                    removeNextFirst = addNextFirst;
                     */
                    addFirstItem(item, addNextFirst);
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
            /*
            T removedItem = items[removeNextLast];
            addNextFirst = 0;
            addNextLast = 1;
            size = 0;
            return removedItem;
             */
            return removeToEmpty(removeNextLast);
        }
        T removedItem = items[removeNextLast];
        if (timeToReduceSize()) {
            resize((int) (items.length * 0.5));
             /*
            items[removeNextLast] = null;
            size = size - 1;
            addNextLast = removeNextLast;
             */
            removeLastItem(removeNextLast);
            removeNextLast = removeNextLast - 1;
        } else {
            if (removeNextLast == 0) {
                /*
                items[removeNextLast] = null;
                size = size - 1;
                addNextLast = removeNextLast;
                */
                removeLastItem(removeNextLast);
                removeNextLast = items.length - 1;
            } else {
                /*
                items[removeNextLast] = null;
                size = size - 1;
                addNextLast = removeNextLast;
                 */
                removeLastItem(removeNextLast);
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
            /*
            T removedItem = items[removeNextFirst];
            addNextFirst = 0;
            addNextLast = 1;
            size = 0;
            return removedItem;
             */
            return removeToEmpty(removeNextFirst);
        }
        T removedItem = items[removeNextFirst];
        if (timeToReduceSize()) {
            resize((int) (items.length * 0.5));
            /*
            items[removeNextFirst] = null;
            size = size - 1;
            addNextFirst = removeNextFirst;
             */
            removeFirstItem(removeNextFirst);
            removeNextFirst = removeNextFirst + 1;
        } else {
            if (removeNextFirst == items.length - 1) {
                /*
                items[removeNextFirst] = null;
                size = size - 1;
                addNextFirst = removeNextFirst;
                 */
                removeFirstItem(removeNextFirst);
                removeNextFirst = 0;
            } else {
                /*
                items[removeNextFirst] = null;
                size = size - 1;
                addNextFirst = removeNextFirst;
                 */
                removeFirstItem(removeNextFirst);
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
