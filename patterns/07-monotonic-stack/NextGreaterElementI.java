import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

class NextGreaterElementI {
    // Assumes distinct values in nums2 and every nums1 value occurs in nums2.
    // Expected O(nums1.length + nums2.length) time; O(nums2.length) auxiliary space.
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> nextGreater = new HashMap<>();
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < nums2.length; i++) {
            nextGreater.put(nums2[i], -1);
            while (!stack.isEmpty() && nums2[i] > nums2[stack.peek()]) {
                int waitingIndex = stack.pop();
                nextGreater.put(nums2[waitingIndex], nums2[i]);
            }
            stack.push(i);
        }

        int[] answer = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            answer[i] = nextGreater.get(nums1[i]);
        }
        return answer;
    }
}
