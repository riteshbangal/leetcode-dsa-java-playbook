class NumArray {
    private final int[] prefix;

    public NumArray(int[] nums) {
        prefix = new int[nums.length + 1];

        for (int i = 1; i <= nums.length; i++) {
            // After this iteration, prefix[i] is the sum of nums[0..i - 1].
            prefix[i] = prefix[i - 1] + nums[i - 1];
        }
    }

    public int sumRange(int left, int right) {
        return prefix[right + 1] - prefix[left];
    }
}
