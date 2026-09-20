import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import org.junit.jupiter.api.Test;

class ClosestPairTest {
    @Test
    void matchesBruteForceOnRandomPoints() {
        Random random = new Random(3);
        ClosestPairSolver solver = new ClosestPairSolver();

        for (int test = 0; test < 100; test++) {
            int n = 2 + random.nextInt(1999);   // 2 .. 2000 points
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                points[i] = new Point(random.nextDouble() * 1000, random.nextDouble() * 1000);
            }
            assertEquals(ClosestPairSolver.bruteForce(points), solver.findClosest(points), 1e-9);
        }
    }

    @Test
    void matchesBruteForceWithManyDuplicateCoordinates() {
        Random random = new Random(4);
        Point[] points = new Point[1000];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(random.nextInt(300), random.nextInt(300));
        }
        assertEquals(ClosestPairSolver.bruteForce(points), new ClosestPairSolver().findClosest(points), 1e-9);
    }

    @Test
    void identicalPointsHaveDistanceZero() {
        Point[] points = {new Point(1, 1), new Point(5, 5), new Point(1, 1)};
        assertEquals(0.0, new ClosestPairSolver().findClosest(points), 1e-9);
    }

    @Test
    void twoPoints() {
        Point[] points = {new Point(0, 0), new Point(3, 4)};
        assertEquals(5.0, new ClosestPairSolver().findClosest(points), 1e-9);
    }

    @Test
    void lessThanTwoPointsThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new ClosestPairSolver().findClosest(new Point[]{new Point(0, 0)}));
    }
}