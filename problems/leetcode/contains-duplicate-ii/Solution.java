import java.util.HashSet;
import java.util.Set;

class Solution {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        Set<Integer> window = new HashSet<>();

        for (int right = 0; right < nums.length; right++) {
            if (right > k) {
                window.remove(nums[right - k - 1]);
            }

            if (window.contains(nums[right])) {
                return true;
            }

            window.add(nums[right]);
        }

        return false;
    }
}
