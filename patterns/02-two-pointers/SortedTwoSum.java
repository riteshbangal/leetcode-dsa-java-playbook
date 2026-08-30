class SortedTwoSum {
    public boolean hasPairWithSum(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            // Any valid pair that remains must be inside [left, right].
            long sum = (long) nums[left] + nums[right];

            if (sum < target) {
                left++;
            } else if (sum > target) {
                right--;
            } else {
                return true;
            }
        }

        return false;
    }
}
