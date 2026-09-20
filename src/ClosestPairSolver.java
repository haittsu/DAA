import java.util.Arrays;
import java.util.Comparator;

public class ClosestPairSolver {
    public long comparisons;   // number of distance calculations
    public int maxDepth;

    // returns the smallest distance between two points
    public double findClosest(Point[] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("need at least 2 points");
        }
        comparisons = 0;
        maxDepth = 0;
        Point[] byX = points.clone();
        Arrays.sort(byX, Comparator.comparingDouble(p -> p.x));
        return solve(byX, new Point[byX.length], 0, byX.length - 1, 1);
    }

    // Works on pts[lo..hi] (sorted by x). When it returns, that part is sorted by y.
    private double solve(Point[] pts, Point[] buf, int lo, int hi, int depth) {
        maxDepth = Math.max(maxDepth, depth);

        // small case: brute force
        if (hi - lo + 1 <= 3) {
            double best = Double.MAX_VALUE;
            for (int i = lo; i <= hi; i++) {
                for (int j = i + 1; j <= hi; j++) {
                    best = Math.min(best, dist(pts[i], pts[j]));
                }
            }
            Arrays.sort(pts, lo, hi + 1, Comparator.comparingDouble(p -> p.y));
            return best;
        }

        int mid = (lo + hi) / 2;
        double midX = pts[mid].x;   // dividing line (read before the halves get reordered)
        double d = Math.min(solve(pts, buf, lo, mid, depth + 1),
                solve(pts, buf, mid + 1, hi, depth + 1));

        // merge the two halves by y
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (j > hi || (i <= mid && pts[i].y <= pts[j].y)) {
                buf[k] = pts[i++];
            } else {
                buf[k] = pts[j++];
            }
        }
        System.arraycopy(buf, lo, pts, lo, hi - lo + 1);

        // strip: points closer than d to the dividing line (still sorted by y)
        int size = 0;
        for (int k = lo; k <= hi; k++) {
            if (Math.abs(pts[k].x - midX) < d) {
                buf[size++] = pts[k];
            }
        }

        // every strip point only needs to be checked against the next few points in y order
        for (int a = 0; a < size; a++) {
            for (int b = a + 1; b < size && buf[b].y - buf[a].y < d; b++) {
                d = Math.min(d, dist(buf[a], buf[b]));
            }
        }
        return d;
    }

    private double dist(Point p, Point q) {
        comparisons++;
        double dx = p.x - q.x;
        double dy = p.y - q.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // O(n^2) reference solution, used in tests and experiments
    public static double bruteForce(Point[] pts) {
        double best = Double.MAX_VALUE;
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                double dx = pts[i].x - pts[j].x;
                double dy = pts[i].y - pts[j].y;
                best = Math.min(best, Math.sqrt(dx * dx + dy * dy));
            }
        }
        return best;
    }
}