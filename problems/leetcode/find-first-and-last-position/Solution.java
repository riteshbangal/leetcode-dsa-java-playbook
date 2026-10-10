class Solution {
    public int[] searchRange(int[] nums, int target) {
        if (nums.length == 0) {
            return new int[]{-1, -1};
        }

        // find the first index
        int left = 0;
        int right = nums.length - 1;
        int first = -1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] > target) {
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        if (nums[left] == target) {
            first = left;
        }

        // find the last index
        left = 0;
        right = nums.length - 1;
        int last = -1;

        while (left < right) {
            int mid = left + (right - left + 1) / 2;

            if (nums[mid] > target) {
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                left = mid;
            }
        }

        if (nums[right] == target) {
            last = right;
        }

        return new int[]{first, last};
    }
}
