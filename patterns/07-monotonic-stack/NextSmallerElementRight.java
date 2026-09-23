import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Arrays;

class NextSmallerElementRight {
    // Returns indices, not values. Equal values do not resolve each other.
    // O(n) time and O(n) auxiliary space.
    public int[] nextSmallerIndices(int[] nums) {
        int[] answer = new int[nums.length];
        Arrays.fill(answer, -1);
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < nums.length; i++) {
            while (!stack.isEmpty() && nums[i] < nums[stack.peek()]) {
                answer[stack.pop()] = i;
            }
            stack.push(i);
        }
        return answer;
    }
}
