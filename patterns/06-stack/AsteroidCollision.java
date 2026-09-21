import java.util.ArrayDeque;
import java.util.Deque;

class AsteroidCollision {
    public int[] asteroidCollision(int[] asteroids) {
        Deque<Integer> stack = new ArrayDeque<>();

        for (int current : asteroids) {
            boolean currentIsAlive = true;

            while (currentIsAlive
                    && current < 0
                    && !stack.isEmpty()
                    && stack.peek() > 0) {
                int top = stack.peek();
                int currentSize = Math.abs(current);

                if (top < currentSize) {
                    stack.pop();
                } else if (top == currentSize) {
                    stack.pop();
                    currentIsAlive = false;
                } else {
                    currentIsAlive = false;
                }
            }

            if (currentIsAlive) {
                stack.push(current);
            }
        }

        int[] survivors = new int[stack.size()];

        for (int i = 0; i < survivors.length; i++) {
            survivors[i] = stack.removeLast();
        }

        return survivors;
    }
}
