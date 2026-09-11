class SubarraySumEqualsKPrefixArray {
    private static class RangeSums {
        private final int[] prefix;

        RangeSums(int[] nums) {
            prefix = new int[nums.length + 1];

            for (int i = 1; i <= nums.length; i++) {
                prefix[i] = prefix[i - 1] + nums[i - 1];
            }
        }

        int sumRange(int left, int right) {
            return prefix[right + 1] - prefix[left];
        }
    }

    public int subarraySum(int[] nums, int k) {
        RangeSums rangeSums = new RangeSums(nums);
        int subarrayCount = 0;

        for (int left = 0; left < nums.length; left++) {
            for (int right = left; right < nums.length; right++) {
                if (rangeSums.sumRange(left, right) == k) {
                    subarrayCount++;
                }
            }
        }

        return subarrayCount;
    }
}
