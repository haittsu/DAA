import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SelectTest {
    @Test
    void matchesSortedArrayOn200RandomTests() {
        Random random = new Random(1);
        DeterministicSelector selector = new DeterministicSelector();

        for (int test = 0; test < 200; test++) {
            int n = 1 + random.nextInt(500);
            int[] a = random.ints(n, -50, 50).toArray();   // small range, so duplicates appear
            int k = random.nextInt(n);

            int[] sorted = a.clone();
            Arrays.sort(sorted);
            assertEquals(sorted[k], selector.select(a.clone(), k));
        }
    }

    @Test
    void singleElement() {
        assertEquals(7, new DeterministicSelector().select(new int[]{7}, 0));
    }

    @Test
    void allEqualElements() {
        int[] a = new int[1000];
        Arrays.fill(a, 3);
        assertEquals(3, new DeterministicSelector().select(a, 500));
    }

    @Test
    void invalidKThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new DeterministicSelector().select(new int[]{1, 2, 3}, 3));
    }
}
