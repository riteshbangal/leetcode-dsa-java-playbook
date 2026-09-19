class FindMinimumInRotatedSortedArray {
    public int findMin(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] > nums[right]) {
                // The rotation point is strictly to the right of mid.
                left = mid + 1;
            } else {
                // Mid may be the minimum, so keep it in the interval.
                right = mid;
            }
        }

        return nums[left];
    }
}
