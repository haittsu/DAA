import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        // small demo of every algorithm
        int[] sample = {9, 4, 7, 1, 8, 2, 2, 5, 6, 3};
        System.out.println("Input:          " + Arrays.toString(sample));

        int[] a = sample.clone();
        new MergeSorter().sort(a);
        System.out.println("MergeSort:      " + Arrays.toString(a));

        a = sample.clone();
        new QuickSorter().sort(a);
        System.out.println("QuickSort:      " + Arrays.toString(a));

        a = sample.clone();
        System.out.println("Select (k=4):   " + new DeterministicSelector().select(a, 4)
                + "   (5th smallest element)");

        Point[] points = {new Point(0, 0), new Point(10, 10), new Point(3, 4), new Point(11, 9), new Point(50, 1)};
        System.out.println("Closest pair:   " + new ClosestPairSolver().findClosest(points));
        System.out.println();

        // full experiment, results go to results/results.csv
        System.out.println("Running experiments (this can take a minute)...");
        new Experiment().run("results/results.csv");
        System.out.println("Done. Results saved to results/results.csv");
    }
}