class FindPeakElement {
    public int findPeakElement(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] < nums[mid + 1]) {
                // Rising slope: a peak exists strictly to the right.
                left = mid + 1;
            } else {
                // Falling slope: a peak exists at mid or to the left.
                right = mid;
            }
        }

        return left;
    }
}
