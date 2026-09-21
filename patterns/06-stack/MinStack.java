import java.util.ArrayDeque;
import java.util.Deque;

class MinStack {
    private static class Entry {
        private final int value;
        private final int minimumAtDepth;

        private Entry(int value, int minimumAtDepth) {
            this.value = value;
            this.minimumAtDepth = minimumAtDepth;
        }
    }

    private final Deque<Entry> stack = new ArrayDeque<>();

    public void push(int value) {
        int minimumAtDepth = stack.isEmpty()
                ? value
                : Math.min(value, stack.peek().minimumAtDepth);

        stack.push(new Entry(value, minimumAtDepth));
    }

    public void pop() {
        stack.pop();
    }

    public int top() {
        return stack.peek().value;
    }

    public int getMin() {
        return stack.peek().minimumAtDepth;
    }
}
