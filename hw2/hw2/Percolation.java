package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF uf;
    private int countOpen = 0;
    private final int n;
    private boolean[] siteopen;

    /*
     * Create two virtual sites: virtualTop and virtualBottom
     * Every site has two status stored in the connectTop and
     * connectBottom, indicates whether the site is linked to
     * the top or the bottom.
     * Percolates if virtualTop is connected to virtualBottom.
    */
    private boolean[] isconnectTop;
    private boolean[] isconnectBottom;
    private boolean percolatesFlag = false;

    //create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        check(N);
        this.uf = new WeightedQuickUnionUF(N * N);
        this.n = N;
        //the default site is blocked
        this.siteopen = new boolean[N * N];
        //two virtual sites
        this.isconnectBottom = new boolean[N * N];
        this.isconnectTop = new boolean[N * N];
    }

    //open the site (row, col) if it is not open already
    public void open(int row, int col) {
        check(row, col);
        int index = xyTo1D(row, col);

        //open the non-open site
        if (!siteopen[index]) {
            siteopen[index] = true;
            countOpen += 1;
        }
        boolean virtualTop = false;
        boolean virtualBottom = false;
        // if the site is on the first row, connect it to the top
        if (row == 0) {
            virtualTop = true;
        }
        // if the site is on the last row, connect it to the bottom
        if (row == n - 1) {
            virtualBottom = true;
        }
        // then check and connect the site with its neighbors
        // union the site with left neighbor
        if (col > 0 && siteopen[index - 1]) {
            //if the neighbor is full or site is full, validate the virtualTop
            if (!virtualTop && isconnectTop[uf.find(index - 1)] || isconnectTop[uf.find(index)]) {
                virtualTop = true;
            }
            // if the neighbor is connect bottom or the site is, validate the virtualBottom
            if (!virtualBottom && isconnectBottom[uf.find(index - 1)]
                    || isconnectBottom[uf.find(index)]) {
                virtualBottom = true;
            }
            uf.union(index, index - 1);
        }
        // union the site with right neighbor
        if (col < n - 1 && siteopen[index + 1]) {
            //if the neighbor is full or site is full, validate the virtualTop
            if (!virtualTop && isconnectTop[uf.find(index + 1)] || isconnectTop[uf.find(index)]) {
                virtualTop = true;
            }
            // if the neighbor is connect bottom or the site is, validate the virtualBottom
            if (!virtualBottom && isconnectBottom[uf.find(index + 1)]
                    || isconnectBottom[uf.find(index)]) {
                virtualBottom = true;
            }
            uf.union(index, index + 1);
        }
        // union the site with upper neighbor
        if (row > 0 && siteopen[index - n]) {
            //if the neighbor is full or site is full, validate the virtualTop
            if (!virtualTop && isconnectTop[uf.find(index - n)] || isconnectTop[uf.find(index)]) {
                virtualTop = true;
            }
            // if the neighbor is connect bottom or the site is, validate the virtualBottom
            if (!virtualBottom && isconnectBottom[uf.find(index - n)]
                    || isconnectBottom[uf.find(index)]) {
                virtualBottom = true;
            }
            uf.union(index, index - n);
        }
        // union the site with down neighbor
        if (row < n - 1 && siteopen[index + n]) {
            //if the neighbor is full or site is full, validate the virtualTop
            if (!virtualTop && isconnectTop[uf.find(index + n)] || isconnectTop[uf.find(index)]) {
                virtualTop = true;
            }
            // if the neighbor is connect bottom or the site is, validate the virtualBottom
            if (!virtualBottom && isconnectBottom[uf.find(index + n)]
                    || isconnectBottom[uf.find(index)]) {
                virtualBottom = true;
            }
            uf.union(index, index + n);
        }
        // set the status of the site
        isconnectTop[uf.find(index)] = virtualTop;
        isconnectBottom[uf.find(index)] = virtualBottom;
        // check whether the system is percolation
        if (!percolatesFlag && isconnectTop[uf.find(index)] && isconnectBottom[uf.find(index)]) {
            percolatesFlag = true;
        }
    }

    //Check whether the site (row, col) open
    public boolean isOpen(int row, int col) {
        check(row, col);
        return siteopen[xyTo1D(row, col)];
    }

    //Check whether the site (row, col) full
    public boolean isFull(int row, int col) {
        check(row, col);
        return isconnectTop[uf.find(xyTo1D(row, col))];
    }

    //return the number of open sites
    public int numberOfOpenSites() {
        return countOpen;
    }

    //return whether the system is percolate
    public boolean percolates() {
        return percolatesFlag;
    }

    //check the input N
    private void check(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException("N could not be less than 0");
        }
    }

    //check whether the site is out of bounds
    private void check(int x, int y) {
        if (x < 0 || x >= n || y < 0 || y >= n) {
            throw new IndexOutOfBoundsException("site must inside the grid");
        }
    }

    //transform xy to 1D
    private int xyTo1D(int x, int y) {
        check(x, y);
        return x * n + y;
    }

    // check main
    public static void main(String[] args) {
        PercolationFactory pf = new PercolationFactory();
        PercolationStats ps = new PercolationStats(20, 10, pf);

        System.out.println("meant = " + ps.mean());
        System.out.println("stddev = " + ps.stddev());
        System.out.println("95% confidcence interval = [" + ps.confidenceLow() + ", " + ps.confidenceHigh() + "]");
    }
}
