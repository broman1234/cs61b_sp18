package hw2;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    double mean;
    double stddev;
    double confidenceLow;
    double confidenceHigh;

    private double eachExperiment(int N, PercolationFactory pf) {
        Percolation sample = pf.make(N);
        int row, col;
        while (!sample.percolates()) {
            row = StdRandom.uniform(0, N);
            col = StdRandom.uniform(0, N);
            if (sample.isOpen(row, col)) {
                continue;
            }
            sample.open(row, col);
        }
        double threshold = sample.numberOfOpenSites() / (double) (N * N);
        return threshold;
    }

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        double[] experiments = new double[T];
        for (int i = 0; i < T; i += 1) {
            experiments[i] = eachExperiment(N, pf);
        }
        mean = StdStats.mean(experiments);
        stddev = StdStats.stddev(experiments);
        confidenceLow = mean - 1.96 * stddev / Math.sqrt(T);
        confidenceHigh = mean + 1.96 * stddev / Math.sqrt(T);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLow() {
        return confidenceLow;
    }

    public double confidenceHigh() {
        return confidenceHigh;
    }

    public static void main(String[] args) {
        PercolationFactory pf = new PercolationFactory();

        Stopwatch timer1 = new Stopwatch();
        PercolationStats stats1 = new PercolationStats(100, 30, pf);
        double threshold1 = stats1.mean();
        stats1.stddev();
        stats1.confidenceLow();
        stats1.confidenceHigh();
        double time1 = timer1.elapsedTime();
        StdOut.printf("%e (%.2f seconds)\n", threshold1, time1);

        Stopwatch timer2 = new Stopwatch();
        PercolationStats stats2 = new PercolationStats(200, 30, pf);
        double threshold2 = stats2.mean();
        stats2.stddev();
        stats2.confidenceLow();
        stats2.confidenceHigh();
        double time2 = timer2.elapsedTime();
        StdOut.printf("%e (%.2f seconds)\n", threshold2, time2);

        Stopwatch timer3 = new Stopwatch();
        PercolationStats stats3 = new PercolationStats(100, 60, pf);
        double threshold3 = stats3.mean();
        stats3.stddev();
        stats3.confidenceLow();
        stats3.confidenceHigh();
        double time3 = timer3.elapsedTime();
        StdOut.printf("%e (%.2f seconds)\n", threshold3, time3);
    }
}
