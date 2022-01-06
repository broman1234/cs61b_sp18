package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;
    private int tableSize;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        this(DEFAULT_SIZE);
    }

    public MyHashMap(int m) {
        this.tableSize = m;
        buckets = new ArrayMap[m];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        int i = hash(key);
        return buckets[i].get(key);
    }

    private void resize(int capacity) {
        MyHashMap<K, V> temp = new MyHashMap<>(capacity);
        for (int i = 0; i < buckets.length; i += 1) {
            for (K key : buckets[i].keySet()) {
                temp.put(key, buckets[i].get(key));
            }
        }
        this.tableSize = temp.tableSize;
        this.size = temp.size;
        this.buckets = temp.buckets;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Null key not allowed.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Null values not allowed.");
        }
        if (loadFactor() >= MAX_LF) {
            resize(2 * buckets.length);
        }
        int i = hash(key);
        if (!buckets[i].containsKey(key)) {
            size += 1;
            buckets[i].put(key, value);
        } else {
            buckets[i].put(key, value);
        }
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keyset = new HashSet<>();
        for (int i = 0; i < buckets.length; i += 1) {
            for (K key : buckets[i].keySet()) {
                keyset.add(key);
            }
        }
        return keyset;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        V removedValue = null;
        for (int i = 0; i < buckets.length; i += 1) {
            if (buckets[i].containsKey(key)) {
                size -= 1;
                removedValue = buckets[i].remove(key);
            }
        }
        if (tableSize > DEFAULT_SIZE && loadFactor() < 0.25) {
            resize(tableSize / 2);
        }
        return removedValue;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        V removedValue = null;
        for (int i = 0; i < buckets.length; i += 1) {
            if (buckets[i].containsKey(key) && value == get(key)) {
                size -= 1;
                removedValue = buckets[i].remove(key, value);
            }
        }
        if (tableSize > DEFAULT_SIZE && loadFactor() < 0.25) {
            resize(tableSize / 2);
        }
        return removedValue;
    }

    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
