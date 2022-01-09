package lab9;

import java.util.*;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Man Mo
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private int size;

        private Node(K k, V v, int size) {
            key = k;
            value = v;
            this.size = size;
        }
    }

    private Node root;  /* Root node of the tree. */
    //private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (p == null) {
            return null;
        }
        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            return getHelper(key, p.left);
        } else if (cmp > 0) {
            return getHelper(key, p.right);
        } else {
            return p.value;
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            return new Node(key, value, 1);
        }
        if (key.compareTo(p.key) < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (key.compareTo(p.key) > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            p.value = value;
        }
        p.size = 1 + size(p.left) + size(p.right);
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    private int size(Node p) {
        if (p == null) {
            return 0;
        } else {
            return p.size;
        }
    }
    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size(root);
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */

    private K select(int rank) {
        if (rank < 0 || rank >= size()) {
            throw new IllegalArgumentException();
        }
        return select(root, rank);
    }

    private K select(Node p, int rank) {
        if (p == null) {
            return null;
        }
        int leftSize = size(p.left);
        if (leftSize > rank) {
            return select(p.left, rank);
        } else if (leftSize < rank) {
            return select(p.right, rank - leftSize - 1);
        } else {
            return p.key;
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> kSet = new HashSet<>();
        for (int i = 0; i < size(); i += 1) {
            kSet.add(select(i));
        }
        return kSet;
    }

    private Node min(Node p) {
        if (p.left == null) {
            return p;
        }
        return min(p.left);
    }

    /* Return the tree which has the node with min key been removed */
    private Node deleteMin(Node p) {
        if (p.left == null) {
            return p.right;
        }
        p.left = deleteMin(p.left);
        p.size = size(p.left) + size(p.right) + 1;
        return p;
    }

    private Node remove(K key, Node p) {
        if (p == null) {
            return null;
        }
        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            p.left = remove(key, p.left);
        } else if (cmp > 0) {
            p.right = remove(key, p.right);
        } else {
            if (p.right == null) {
                return p.left;
            }
            if (p.left == null) {
                return p.right;
            }
            Node temp = p;
            p = min(temp.right);
            p.right = deleteMin(temp.right);
            p.left = temp.left;
        }
        p.size = size(p.left) + size(p.right) + 1;
        return p;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        V removedValue = get(key);
        root = remove(key, root);
        return removedValue;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (!containsKey(key)) {
            return null;
        }
        if (!get(key).equals(value)) {
            return null;
        }
        root = remove(key, root);
        return value;
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIter(root);
    }

    private class BSTMapIter implements Iterator<K> {
        private Stack<Node> stack = new Stack<>();

        private BSTMapIter(Node src) {
            while (src != null) {
                // push root node and all left nodes to the stack.
                stack.push(src);
                src = src.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public K next() {
            Node curr = stack.pop();

            if (curr.right != null) {
                Node temp = curr.right;
                while (temp != null) {
                    stack.push(temp);
                    temp = temp.left;
                }
            }
            return curr.key;
        }
    }
}
