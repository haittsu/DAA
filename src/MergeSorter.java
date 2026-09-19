public class MergeSorter {
    private static final int CUTOFF = 16;   // below this size insertion sort is faster

    public long comparisons;
    public int maxDepth;
    private int[] buffer;                   // one buffer, reused by every merge

    public void sort(int[] a) {
        comparisons = 0;
        maxDepth = 0;
        buffer = new int[a.length];
        sort(a, 0, a.length - 1, 1);
    }

    private void sort(int[] a, int lo, int hi, int depth) {
        maxDepth = Math.max(maxDepth, depth);
        if (hi - lo + 1 <= CUTOFF) {
            insertionSort(a, lo, hi);
            return;
        }
        int mid = (lo + hi) / 2;
        sort(a, lo, mid, depth + 1);
        sort(a, mid + 1, hi, depth + 1);
        merge(a, lo, mid, hi);
    }

    // merges the sorted halves a[lo..mid] and a[mid+1..hi]
    private void merge(int[] a, int lo, int mid, int hi) {
        System.arraycopy(a, lo, buffer, lo, hi - lo + 1);
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = buffer[j++];
            } else if (j > hi) {
                a[k] = buffer[i++];
            } else {
                comparisons++;
                if (buffer[j] < buffer[i]) {
                    a[k] = buffer[j++];
                } else {
                    a[k] = buffer[i++];
                }
            }
        }
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
}