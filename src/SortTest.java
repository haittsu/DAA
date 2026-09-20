import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class SortTest {
    private final Random random = new Random(7);

    private int[][] testArrays() {
        int n = 5000;
        return new int[][]{
                random.ints(n, -1000, 1000).toArray(),      // random
                IntStream.range(0, n).toArray(),            // sorted
                IntStream.range(0, n).map(i -> n - i).toArray(),   // reverse-sorted
                random.ints(n, 0, 5).toArray(),             // many duplicates
                {},                                         // empty
                {42},                                       // single element
                {2, 1},
                {3, 3, 3, 3}
        };
    }

    @Test
    void mergeSortMatchesArraysSort() {
        for (int[] original : testArrays()) {
            int[] expected = original.clone();
            Arrays.sort(expected);
            int[] actual = original.clone();
            new MergeSorter().sort(actual);
            assertArrayEquals(expected, actual);
        }
    }

    @Test
    void quickSortMatchesArraysSort() {
        for (int[] original : testArrays()) {
            int[] expected = original.clone();
            Arrays.sort(expected);
            int[] actual = original.clone();
            new QuickSorter().sort(actual);
            assertArrayEquals(expected, actual);
        }
    }
}