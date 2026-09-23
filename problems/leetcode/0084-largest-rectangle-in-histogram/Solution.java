import java.util.ArrayDeque;
import java.util.Deque;

class Solution {
    // Assumes nonnegative heights and an area that fits in int.
    // Time: O(n). Auxiliary space: O(n). Does not modify heights.
    public int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int maxArea = 0;
        int n = heights.length;

        // i == n supplies a virtual zero; heights[n] is never accessed.
        for (int i = 0; i <= n; i++) {
            int currentHeight = (i == n) ? 0 : heights[i];

            // Pending heights are non-decreasing from bottom to top.
            while (!stack.isEmpty()
                    && heights[stack.peek()] > currentHeight) {
                int height = heights[stack.pop()];
                int left = stack.isEmpty() ? -1 : stack.peek();
                int width = i - left - 1;
                maxArea = Math.max(maxArea, height * width);
            }

            // Every real bar becomes a candidate, regardless of maxArea.
            if (i < n) {
                stack.push(i);
            }
        }

        return maxArea;
    }
}
