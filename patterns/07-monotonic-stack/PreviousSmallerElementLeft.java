import java.util.ArrayDeque;
import java.util.Deque;

class PreviousSmallerElementLeft {
    // Returns indices. The remaining top answers the current position.
    // O(n) time and O(n) auxiliary space.
    public int[] previousSmallerIndices(int[] nums) {
        int[] answer = new int[nums.length];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < nums.length; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] >= nums[i]) {
                stack.pop();
            }
            answer[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }
        return answer;
    }
}
