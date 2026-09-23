import java.util.ArrayDeque;
import java.util.Deque;

class StockSpan {
    // Batch version: span includes today and consecutive earlier prices <= today.
    // O(n) time and O(n) auxiliary space.
    public int[] calculateSpans(int[] prices) {
        int[] span = new int[prices.length];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < prices.length; i++) {
            while (!stack.isEmpty() && prices[stack.peek()] <= prices[i]) {
                stack.pop();
            }
            span[i] = stack.isEmpty() ? i + 1 : i - stack.peek();
            stack.push(i);
        }
        return span;
    }
}
