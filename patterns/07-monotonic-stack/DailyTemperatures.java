import java.util.ArrayDeque;
import java.util.Deque;

class DailyTemperatures {
    // Each index waits for its first strictly warmer day.
    // O(n) time and O(n) auxiliary space.
    public int[] dailyTemperatures(int[] temperatures) {
        int[] answer = new int[temperatures.length];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < temperatures.length; i++) {
            while (!stack.isEmpty()
                    && temperatures[i] > temperatures[stack.peek()]) {
                int previousDay = stack.pop();
                answer[previousDay] = i - previousDay;
            }
            stack.push(i);
        }
        return answer;
    }
}
