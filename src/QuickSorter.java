import java.util.Random;

public class QuickSorter {
    public long comparisons;
    public int maxDepth;
    private final Random random = new Random();

    public void sort(int[] a) {
        comparisons = 0;
        maxDepth = 0;
        sort(a, 0, a.length - 1, 1);
    }

    private void sort(int[] a, int lo, int hi, int depth) {
        maxDepth = Math.max(maxDepth, depth);

        while (lo < hi) {
            // random pivot, moved to the front
            swap(a, lo, lo + random.nextInt(hi - lo + 1));
            int pivot = a[lo];

            // 3-way partition: < pivot | == pivot | > pivot (handles duplicates well)
            int lt = lo;
            int gt = hi;
            int i = lo + 1;
            while (i <= gt) {
                comparisons++;
                if (a[i] < pivot) {
                    swap(a, lt++, i++);
                } else if (a[i] > pivot) {
                    swap(a, i, gt--);
                } else {
                    i++;
                }
            }

            // recurse into the smaller part, loop over the larger one
            if (lt - lo < hi - gt) {
                sort(a, lo, lt - 1, depth + 1);
                lo = gt + 1;
            } else {
                sort(a, gt + 1, hi, depth + 1);
                hi = lt - 1;
            }
        }
    }

    private void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}
