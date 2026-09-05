class MaxSumSubarrayOfSizeK {
    public int maxSumSubarrayOfSizeK(int[] nums, int k) {
        int left = 0;
        int windowSum = 0;
        int maxSum = Integer.MIN_VALUE;

        for (int right = 0; right < nums.length; right++) {
            windowSum += nums[right];

            if (right - left + 1 > k) {
                windowSum -= nums[left];
                left++;
            }

            // At evaluation, windowSum is the sum of exactly k elements.
            if (right - left + 1 == k) {
                maxSum = Math.max(maxSum, windowSum);
            }
        }

        return maxSum;
    }
}
