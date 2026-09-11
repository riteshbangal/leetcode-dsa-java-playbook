class FindPivotIndex {
    public int pivotIndex(int[] nums) {
        int totalSum = 0;

        for (int value : nums) {
            totalSum += value;
        }

        int leftSum = 0;

        for (int i = 0; i < nums.length; i++) {
            // Before processing i, leftSum is the sum of nums[0..i - 1].
            int rightSum = totalSum - leftSum - nums[i];

            if (leftSum == rightSum) {
                return i;
            }

            leftSum += nums[i];
        }

        return -1;
    }
}
