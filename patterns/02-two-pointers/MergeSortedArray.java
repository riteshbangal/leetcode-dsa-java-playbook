class MergeSortedArray {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int first = m - 1;
        int second = n - 1;
        int write = m + n - 1;

        while (first >= 0 && second >= 0) {
            // Positions after write contain the largest merged values
            // in their correct final positions.
            if (nums1[first] > nums2[second]) {
                nums1[write] = nums1[first];
                first--;
            } else {
                nums1[write] = nums2[second];
                second--;
            }

            write--;
        }

        while (second >= 0) {
            nums1[write] = nums2[second];
            second--;
            write--;
        }
    }
}
