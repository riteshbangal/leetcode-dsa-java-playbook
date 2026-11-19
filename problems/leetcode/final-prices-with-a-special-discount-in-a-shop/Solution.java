import java.util.ArrayDeque;
import java.util.Deque;

class Solution {
    public int[] finalPrices(int[] prices) {
        int[] res = new int[prices.length];

        for (int i = 0; i < prices.length; i++) {
            res[i] = prices[i];
        }

        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < prices.length; i++) {
            int curr = prices[i];

            while (!stack.isEmpty() && prices[stack.peek()] >= curr) {
                int waiting = stack.pop();
                res[waiting] = prices[waiting] - curr;
            }

            stack.push(i);
        }

        return res;
    }
}
