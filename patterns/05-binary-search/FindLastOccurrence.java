class FindLastOccurrence {
    public int findLastOccurrence(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int lastIndex = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] < target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else {
                lastIndex = mid;
                left = mid + 1;
            }
        }

        return lastIndex;
    }
}
