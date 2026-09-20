import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Random;

public class Experiment {
    private static final int[] SIZES = {1000, 10000, 100000, 1000000};   // small, medium, large
    private static final String[] TYPES = {"random", "sorted", "reverse", "duplicates"};
    private static final int RUNS = 5;                  // every test is repeated 5 times, the average is saved
    private static final int BRUTE_FORCE_LIMIT = 20000; // brute force is too slow for bigger inputs

    private final Random random = new Random(1);
    private final MergeSorter mergeSorter = new MergeSorter();
    private final QuickSorter quickSorter = new QuickSorter();
    private final DeterministicSelector selector = new DeterministicSelector();
    private final ClosestPairSolver closestPair = new ClosestPairSolver();

    public void run(String csvFile) throws IOException {
        new File(csvFile).getParentFile().mkdirs();
        PrintWriter out = new PrintWriter(csvFile);
        out.println("algorithm,input_type,n,time_ms,max_depth,comparisons");

        // sorting and selection on arrays
        for (String type : TYPES) {
            for (int n : SIZES) {
                int[] data = makeArray(type, n);
                testMergeSort(out, type, data);
                testQuickSort(out, type, data);
                testSelect(out, type, data);
            }
        }

        // closest pair on points
        String[] pointTypes = {"random", "duplicates"};
        for (String type : pointTypes) {
            for (int n : SIZES) {
                Point[] points = makePoints(type, n);
                testClosestPair(out, type, points);
                if (n <= BRUTE_FORCE_LIMIT) {
                    testBruteForce(out, type, points);
                }
            }
        }

        out.close();
    }

    private void testMergeSort(PrintWriter out, String type, int[] data) {
        mergeSorter.sort(data.clone());   // warm-up run, not measured (lets the JVM optimize the code)
        long total = 0;
        for (int i = 0; i < RUNS; i++) {
            int[] copy = data.clone();    // copy is made before the timer starts
            long start = System.nanoTime();
            mergeSorter.sort(copy);
            total += System.nanoTime() - start;
        }
        save(out, "MergeSort", type, data.length, total, mergeSorter.maxDepth, mergeSorter.comparisons);
    }

    private void testQuickSort(PrintWriter out, String type, int[] data) {
        quickSorter.sort(data.clone());
        long total = 0;
        for (int i = 0; i < RUNS; i++) {
            int[] copy = data.clone();
            long start = System.nanoTime();
            quickSorter.sort(copy);
            total += System.nanoTime() - start;
        }
        save(out, "QuickSort", type, data.length, total, quickSorter.maxDepth, quickSorter.comparisons);
    }

    private void testSelect(PrintWriter out, String type, int[] data) {
        int k = data.length / 2;          // we look for the median
        selector.select(data.clone(), k);
        long total = 0;
        for (int i = 0; i < RUNS; i++) {
            int[] copy = data.clone();
            long start = System.nanoTime();
            selector.select(copy, k);
            total += System.nanoTime() - start;
        }
        save(out, "Select", type, data.length, total, selector.maxDepth, selector.comparisons);
    }

    private void testClosestPair(PrintWriter out, String type, Point[] points) {
        closestPair.findClosest(points);
        long total = 0;
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            closestPair.findClosest(points);   // it works on its own copy of the array
            total += System.nanoTime() - start;
        }
        save(out, "ClosestPair", type, points.length, total, closestPair.maxDepth, closestPair.comparisons);
    }

    private void testBruteForce(PrintWriter out, String type, Point[] points) {
        ClosestPairSolver.bruteForce(points);
        long total = 0;
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            ClosestPairSolver.bruteForce(points);
            total += System.nanoTime() - start;
        }
        long n = points.length;
        save(out, "BruteForce", type, points.length, total, 0, n * (n - 1) / 2);
    }

    // writes one line to the CSV file and to the console
    private void save(PrintWriter out, String algorithm, String type, int n,
                      long totalNanos, int depth, long comparisons) {
        double ms = totalNanos / (double) RUNS / 1000000.0;
        out.printf(Locale.US, "%s,%s,%d,%.3f,%d,%d%n", algorithm, type, n, ms, depth, comparisons);
        System.out.printf(Locale.US, "%-11s %-10s n=%-8d %10.3f ms  depth=%d%n",
                algorithm, type, n, ms, depth);
    }

    private int[] makeArray(String type, int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            if (type.equals("sorted")) {
                a[i] = i;
            } else if (type.equals("reverse")) {
                a[i] = n - i;
            } else if (type.equals("duplicates")) {
                a[i] = random.nextInt(10);        // only 10 different values
            } else {
                a[i] = random.nextInt(10 * n);    // random
            }
        }
        return a;
    }

    private Point[] makePoints(String type, int n) {
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            if (type.equals("duplicates")) {
                points[i] = new Point(random.nextInt(100), random.nextInt(100));   // small 100x100 grid
            } else {
                points[i] = new Point(random.nextDouble() * 1000000, random.nextDouble() * 1000000);
            }
        }
        return points;
    }
}