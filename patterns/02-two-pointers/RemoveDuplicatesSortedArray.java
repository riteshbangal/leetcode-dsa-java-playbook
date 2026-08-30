class RemoveDuplicatesSortedArray {
    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }

        int slow = 1;

        for (int fast = 1; fast < nums.length; fast++) {
            // nums[0 ... slow-1] contains exactly the unique values
            // from the already-processed input, in sorted order.
            if (nums[fast] != nums[slow - 1]) {
                nums[slow] = nums[fast];
                slow++;
            }
        }

        return slow;
    }
}
