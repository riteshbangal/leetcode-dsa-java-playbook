import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Arrays;

class NextGreaterElementII {
    // Simulates two copies without allocating a doubled array.
    // O(n) time and O(n) auxiliary space.
    public int[] nextGreaterElements(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];
        Arrays.fill(answer, -1);
        Deque<Integer> stack = new ArrayDeque<>();

        for (int step = 0; step < 2 * n; step++) {
            int i = step % n;
            while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
                answer[stack.pop()] = nums[i];
            }
            // Second pass resolves pending entries without adding them again.
            if (step < n) {
                stack.push(i);
            }
        }
        return answer;
    }
}
