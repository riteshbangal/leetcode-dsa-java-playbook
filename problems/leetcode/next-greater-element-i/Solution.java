import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Deque<Integer> stack = new ArrayDeque<>();
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums2.length; i++) {
            int curr = nums2[i];

            while (!stack.isEmpty() && curr > nums2[stack.peek()]) {
                int j = stack.pop();
                map.put(nums2[j], curr);
            }

            stack.push(i);
        }

        int[] res = new int[nums1.length];

        for (int i = 0; i < nums1.length; i++) {
            res[i] = map.getOrDefault(nums1[i], -1);
        }

        return res;
    }
}
