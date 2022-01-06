import org.junit.Test;
import static org.junit.Assert.*;

public class UnionFind {

    // TODO - Add instance variables?
    private int[] parents;

    /* Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int n) {
        // TODO
        parents = new int[n];
        for (int i = 0; i < n; i++) {
            parents[i] = -1;
        }
    }

    /* Throws an exception if v1 is not a valid index. */
    private void validate(int vertex) {
        // TODO
        if (vertex < 0 || vertex >= parents.length) {
            throw new IllegalArgumentException();
        }
    }

    /* Returns the size of the set v1 belongs to. */
    public int sizeOf(int v1) {
        // TODO
        int r = find(v1);
        return -parents[r];
    }

    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */
    public int parent(int v1) {
        // TODO
        validate(v1);
        return parents[v1];
    }

    /* Returns true if nodes v1 and v2 are connected. */
    public boolean connected(int v1, int v2) {
        // TODO
        return find(v1) == find(v2);
    }

    /* Connects two elements v1 and v2 together. v1 and v2 can be any valid
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Unioning a
       vertex with itself or vertices that are already connected should not
       change the sets but may alter the internal structure of the data. */
    public void union(int v1, int v2) {
        // TODO
        if (v1 != v2 && !connected(v1, v2)) {
            int r1 = find(v1);
            int r2 = find(v2);
            if (sizeOf(v1) <= sizeOf(v2)) {
                parents[r2] = -(sizeOf(v1) + sizeOf(v2));
                parents[r1] = r2;
            } else {
                parents[r1] = -(sizeOf(v1) + sizeOf(v2));
                parents[r2] = r1;
            }
        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. */
    public int find(int vertex) {
        // TODO
        validate(vertex);
        int r = vertex;
        while (parent(r) >= 0) {
            r = parent(r);
        }
        // path compression
        int item = vertex;
        while (item != r) {
            parents[item] = r;
            item = parent(item);
        }
        return r;
    }

    @Test
    public static void main(String[] args) {
        UnionFind sets = new UnionFind(16);
        sets.union(2, 3);
        sets.union(1, 6);
        sets.union(5, 7);
        sets.union(8, 4);
        sets.union(7, 2);

        boolean expected1 = true;
        boolean actual1 = sets.connected(3, 5);
        assertEquals(expected1, actual1);

        int expected2 = -4;
        int actual2 = sets.parent(3);
        assertEquals(expected2, actual2);

        sets.union(6, 4);
        sets.union(6, 3);

        int expected3 = 3;
        int actual3 = sets.find(1);
        assertEquals(expected3, actual3);

        int expected4 = 3;
        int actual4 = sets.parent(1);
        assertEquals(expected4, actual4);
    }
}

