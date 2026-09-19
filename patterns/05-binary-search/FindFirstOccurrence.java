class FindFirstOccurrence {
    public int findFirstOccurrence(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int firstIndex = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] < target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else {
                firstIndex = mid;
                right = mid - 1;
            }
        }

        return firstIndex;
    }
}
