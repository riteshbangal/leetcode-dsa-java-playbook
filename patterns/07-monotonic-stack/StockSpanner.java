import java.util.ArrayDeque;
import java.util.Deque;

class StockSpanner {
    private final Deque<Entry> stack = new ArrayDeque<>();
    private int day = -1;

    // State belongs to this instance and persists between next() calls.
    public StockSpanner() {
    }

    // Amortized O(1) per call; O(n) worst-case space after n calls.
    public int next(int price) {
        day++;
        while (!stack.isEmpty() && stack.peek().price <= price) {
            stack.pop();
        }
        int left = stack.isEmpty() ? -1 : stack.peek().day;
        int span = day - left;
        stack.push(new Entry(price, day));
        return span;
    }

    private static final class Entry {
        private final int price;
        private final int day;

        private Entry(int price, int day) {
            this.price = price;
            this.day = day;
        }
    }
}
