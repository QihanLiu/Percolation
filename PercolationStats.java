import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] results;
    private final int tot_trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N and T must be <= 0: " + n + " and " + trials);
        }

        tot_trials = trials;
        results = new double[trials];

        Percolation permap;
        int trial, row, col, open_sites, size = n * n;
        // independent trials
        for (trial = 0; trial < trials; trial++) { 
            results[trial] = 0;
            permap = new Percolation(n);
            // randomly generate sites to open
            while (!permap.percolates()) { 
                row = StdRandom.uniform(1, n + 1);
                col = StdRandom.uniform(1, n + 1);
                permap.open(row, col);
            }
            open_sites = permap.numberOfOpenSites();
            results[trial] = (double) open_sites / size;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(tot_trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(tot_trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = 20, trials = 10;
        n = Integer.parseInt(args[0]);
        trials = Integer.parseInt(args[1]);
        PercolationStats perstat = new PercolationStats(n, trials);

        String confidence = perstat.confidenceLo() + ", " + perstat.confidenceHi();
        StdOut.println("mean                    = " + perstat.mean());
        StdOut.println("stddev                  = " + perstat.stddev());
        StdOut.println("95% confidence interval = [" + confidence + "]");
    }
}