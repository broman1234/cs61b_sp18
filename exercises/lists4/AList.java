/** Array based list.
 *  @author Josh Hug
 */

/* Invariants;
    addLast: The next item we want to add, will go into position size
    getLast: The item we want to return is in position size - 1
    size: The number of items in the list should be size.
 */

public class AList<Item> {
    /** Creates an empty list. */
    private Item[] items;
    private int size;

    public AList() {
        items = (Item[]) new Object[100];
        size = 0;
    }

    /** Resizes the underlying array to the target capacity. */
    private void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, size);
        items = a;
    }

    /** Inserts X into the back of the list. */
    public void addLast(Item x) {
        if (size == items.length) {
            resize(size + 1);
        }

        items[size] = x;
        size = size + 1;
    }

    /** Returns the item from the back of the list. */
    public Item getLast() {
        return items[size - 1];
    }
    /** Gets the ith item in the list (0 is the front). */
    public Item get(int i) {
        return items[i];
    }

    /** Returns the number of items in the list. */
    public int size() {
        return size;
    }

    /** Deletes item from back of the list and
     * returns deleted item. */
    public Item removeLast() {
        Item removedItem = getLast();
        items[size - 1] = null; // we need to do this for generic types
        size = size - 1;
        return removedItem;
    }
}