package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int numOfOpenSites = 0;
    private final int size;
    private final int visualTop;
    private final int visualBottom;
    private final boolean[][] sites;
    private WeightedQuickUnionUF disjointSites;

    private int xyTo1d(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            throw new IndexOutOfBoundsException();
        }
        return size * x + y;
    }

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        size = N;
        visualTop = size * size + 1;
        visualBottom = size * size;
        sites = new boolean[size][size];
        disjointSites = new WeightedQuickUnionUF(size * size + 2);
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                sites[i][j] = false;
            }
        }
        for (int i = 0, j = 0; j < N; j += 1) {
            disjointSites.union(xyTo1d(i, j), visualTop);
        }
        for (int i = N - 1, j = 0; j < N; j += 1) {
            disjointSites.union(xyTo1d(i, j), visualBottom);
        }
    }

    private boolean inBoundary(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    private void unionNeighbor(int row, int col, int nRow, int nCol) {
        if (inBoundary(nRow, nCol)) {
            if (isOpen(nRow, nCol)) {
                disjointSites.union(xyTo1d(row, col), xyTo1d(nRow, nCol));
            }
        }
    }

    public void open(int row, int col) {
        if (!inBoundary(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            sites[row][col] = true;
            int upRow= row - 1;
            int upCol = col;
            unionNeighbor(row, col, upRow, upCol);

            int downRow = row + 1;
            int downCol = col;
            unionNeighbor(row, col, downRow, downCol);

            int leftRow = row;
            int leftCol = col - 1;
            unionNeighbor(row, col, leftRow, leftCol);

            int rightRow = row;
            int rightCol = col + 1;
            unionNeighbor(row, col, rightRow, rightCol);

            numOfOpenSites += 1;
        }
    }

    public boolean isOpen(int row, int col) {
        if (!inBoundary(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        return sites[row][col];
    }

    public boolean isFull(int row, int col) {
        if (!inBoundary(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        return isOpen(row, col) && disjointSites.connected(xyTo1d(row, col), visualTop);
    }

    public int numberOfOpenSites() {
        return numOfOpenSites;
    }

    public boolean percolates() {
        if (size == 1) {
            if (!isOpen(0, 0)) {
                return false;
            }
        }
        return disjointSites.connected(visualTop, visualBottom);
    }

    public static void main(String[] args) {
        Percolation sample = new Percolation(5);
        sample.open(3, 4);
        sample.open(2, 4);
        System.out.println(sample.disjointSites.connected(sample.xyTo1d(3, 4), sample.xyTo1d(2, 4)));

        sample.open(2, 2);
        sample.open(2, 3);
        System.out.println(sample.disjointSites.connected(12, 19));

        sample.open(0, 2);
        sample.open(1, 2);
        System.out.println(sample.isFull(2, 2));

        sample.open(4, 4);
        System.out.println(sample.percolates());
        System.out.println(sample.numberOfOpenSites());
    }
}
