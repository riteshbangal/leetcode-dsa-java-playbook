import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Arrays;

class NextGreaterElementRight {
    // Returns values; -1 means absent. Each stack entry is an unresolved index.
    // O(n) time and O(n) auxiliary space.
    public int[] nextGreaterElements(int[] nums) {
        int[] answer = new int[nums.length];
        Arrays.fill(answer, -1);
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < nums.length; i++) {
            while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
                answer[stack.pop()] = nums[i];
            }
            stack.push(i);
        }
        return answer;
    }
}
