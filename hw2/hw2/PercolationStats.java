package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private final double mean;
    private final double stddev;
    private final double confidenceHi;
    private final double confidenceLow;
    private static final double CONFIDENCE_95 = 1.96;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("The N or T should not be less than 0");
        }

        double[] results = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation uf = pf.make(N * N);
            // use the constant numOfOpenSites to reduce the calls of methods
            int numOfOpenSites = 0;
            while (!uf.percolates()) {
                // generate random sites in until percolates
                int row = StdRandom.uniform(N); // Start from line 1
                int col = StdRandom.uniform(N);
                if (!uf.isOpen(row, col)) {
                    uf.open(row, col);
                    numOfOpenSites += 1;
                }
            }
            results[i] = (double) numOfOpenSites / (N * N * 1.0);
        }

        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);
        confidenceHi = mean + CONFIDENCE_95 * stddev / Math.sqrt(T);
        confidenceLow = mean - CONFIDENCE_95 * stddev / Math.sqrt(T);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return confidenceLow;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return confidenceHi;
    }

}
