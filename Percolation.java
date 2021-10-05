import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] stat;
    private int count;
    private final int n;
    // Virtual top is used to find a site is full
    private final int virtualtop;
    // Virtual bot is used to find if the map is percolates
    private final int virtualbot;
    // To find if the map is percolates
    private WeightedQuickUnionUF unionper;
    // To find a site is full. This one is to avoid blackslash: a grid is connect to the top via virual bot.
    private WeightedQuickUnionUF unionfull;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be > 0");
        }

        stat = new boolean[n][n];
        unionper = new WeightedQuickUnionUF((n * n) + 2); // including top and bottom
        unionfull = new WeightedQuickUnionUF((n * n) + 1); // including top
        virtualtop = n * n;
        virtualbot = n * n + 1;
        count = 0;
        this.n = n;
        int i, j;
        // Initial sites
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                stat[i][j] = false;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        int flatindex = (row - 1) * n + (col - 1);

        if (!stat[row - 1][col - 1]) { // if the site is not open
            stat[row - 1][col - 1] = true;
            // Connect it to top
            if (row == 1) {
                unionper.union(virtualtop, flatindex);
                unionfull.union(virtualtop, flatindex);
            }
            // Connect it to bot
            if (row == n) {
                unionper.union(virtualbot, flatindex);
            }
            // Connect it to adjacent open sites
            int i, rowt, colt;
            colt = col;
            for (i = -1; i < 2; i += 2) { // get top and bot adjacent sites.
                rowt = row + i;
                if (rowt >= 1 && rowt <= n) {
                    if (isOpen(rowt, colt)) { // check if the adjacent box is open.
                        unionper.union(flatindex, (rowt - 1) * n + (colt - 1)); // union it if it is open.
                        unionfull.union(flatindex, (rowt - 1) * n + (colt - 1));
                    }
                } else
                    continue;
            }
            rowt = row;
            for (i = -1; i < 2; i += 2) { // get left and right adjacent sites.
                colt = col + i;
                if (colt >= 1 && colt <= n) {
                    if (isOpen(rowt, colt)) { // check if the adjacent box is open.
                        unionper.union(flatindex, (rowt - 1) * n + (colt - 1)); // union it if it is open.
                        unionfull.union(flatindex, (rowt - 1) * n + (colt - 1)); // union it if it is open.
                    }
                } else
                    continue;
            }

            count++;// count open sites.
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return stat[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        int index, indtop;
        // get index of current site
        index = unionfull.find((row - 1) * n + (col - 1)); 
        indtop = unionfull.find(virtualtop);
        return index == indtop;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        int indtop, indbot;
        indtop = unionper.find(virtualtop);
        indbot = unionper.find(virtualbot);
        return indtop == indbot;
    }

    // validate that index of the map is valid
    private void validate(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException(
                    "map index row and col: " + row + " and " + col + " is not between 1 and " + n);
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        Percolation permap = new Percolation(n);

        int argCount = args.length;
        for (int i = 1; argCount >= 2; i += 2) {
            int row = Integer.parseInt(args[i]);
            int col = Integer.parseInt(args[i + 1]);
            StdOut.printf("Adding row: %d  col: %d %n", row, col);
            permap.open(row, col);
            if (permap.percolates()) {
                StdOut.printf("%nThe System percolates %n");
            }
            argCount -= 2;
        }
        if (!permap.percolates()) {
            StdOut.print("Does not percolate %n \n");
        }
    }
}
