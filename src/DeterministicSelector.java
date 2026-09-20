public class DeterministicSelector {
    public long comparisons;
    public int maxDepth;

    // returns the k-th smallest element (k starts at 0); reorders the array
    public int select(int[] a, int k) {
        if (k < 0 || k >= a.length) {
            throw new IllegalArgumentException("k is out of range");
        }
        comparisons = 0;
        maxDepth = 0;
        return select(a, 0, a.length - 1, k, 1);
    }

    private int select(int[] a, int lo, int hi, int k, int depth) {
        maxDepth = Math.max(maxDepth, depth);
        if (lo == hi) {
            return a[lo];
        }

        int pivot = medianOfMedians(a, lo, hi, depth);

        // 3-way partition around the pivot value
        int lt = lo;
        int gt = hi;
        int i = lo;
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

        // go only into the part that contains index k
        if (k < lt) {
            return select(a, lo, lt - 1, k, depth + 1);
        }
        if (k > gt) {
            return select(a, gt + 1, hi, k, depth + 1);
        }
        return pivot;
    }

    private int medianOfMedians(int[] a, int lo, int hi, int depth) {
        int n = hi - lo + 1;
        if (n <= 5) {
            insertionSort(a, lo, hi);
            return a[lo + n / 2];
        }

        // sort every group of 5 and move its median to the front of the range
        int groups = 0;
        for (int start = lo; start <= hi; start += 5) {
            int end = Math.min(start + 4, hi);
            insertionSort(a, start, end);
            swap(a, lo + groups, start + (end - start) / 2);
            groups++;
        }

        // the median of the medians is found recursively
        return select(a, lo, lo + groups - 1, lo + groups / 2, depth + 1);
    }

    private void insertionSort(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            int x = a[i];
            int j = i - 1;
            while (j >= lo) {
                comparisons++;
                if (a[j] <= x) break;
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = x;
        }
    }

    private void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}