class RemoveElement {
    public int removeElement(int[] nums, int val) {
        int slow = 0;

        for (int fast = 0; fast < nums.length; fast++) {
            // nums[0 ... slow-1] contains exactly the retained values
            // from the already-processed input, in their original order.
            if (nums[fast] != val) {
                nums[slow] = nums[fast];
                slow++;
            }
        }

        return slow;
    }
}
