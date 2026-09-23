import java.util.Arrays;

class LargestRectangleInHistogramTest {
    private static final LargestRectangleInHistogram SOLUTION = new LargestRectangleInHistogram();
    private static int checks;

    public static void main(String[] args) {
        check(new int[] {}, 0);
        check(new int[] {0}, 0);
        check(new int[] {7}, 7);
        check(new int[] {2, 4}, 4);
        check(new int[] {2, 1}, 2);
        check(new int[] {1, 2, 3, 4}, 6);
        check(new int[] {4, 3, 2, 1}, 6);
        check(new int[] {2, 2, 2}, 6);
        check(new int[] {2, 0, 2}, 2);
        check(new int[] {5, 1, 5}, 5);
        check(new int[] {2, 1, 5, 6, 2, 3}, 10);
        check(new int[] {0, 2, 2, 0}, 4);

        for (int length = 0; length <= 7; length++) {
            enumerate(new int[length], 0);
        }

        int[] plateau = new int[100_000];
        Arrays.fill(plateau, 10_000);
        check(plateau, 1_000_000_000);

        System.out.println("Passed " + checks + " checks.");
    }

    private static void enumerate(int[] heights, int index) {
        if (index == heights.length) {
            check(heights, bruteForce(heights));
            return;
        }
        for (int height = 0; height <= 3; height++) {
            heights[index] = height;
            enumerate(heights, index + 1);
        }
    }

    private static int bruteForce(int[] heights) {
        int best = 0;
        for (int left = 0; left < heights.length; left++) {
            int minimum = Integer.MAX_VALUE;
            for (int right = left; right < heights.length; right++) {
                minimum = Math.min(minimum, heights[right]);
                best = Math.max(best, minimum * (right - left + 1));
            }
        }
        return best;
    }

    private static void check(int[] heights, int expected) {
        int[] before = heights.clone();
        int actual = SOLUTION.largestRectangleArea(heights);
        if (actual != expected) {
            throw new AssertionError(Arrays.toString(heights)
                    + ": expected " + expected + ", got " + actual);
        }
        if (!Arrays.equals(before, heights)) {
            throw new AssertionError("Input array was modified");
        }
        checks++;
    }
}
