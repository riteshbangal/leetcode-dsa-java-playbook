import static java.lang.Math.abs;

class Solution {
    public int[] sortedSquares(int[] nums) {
        int res[] = new int[nums.length];

        int count = nums.length - 1;
        int left = 0;
        int right = nums.length - 1;

        while (right >= left) {
            if (abs(nums[left]) > abs(nums[right])) {
                res[count] = nums[left] * nums[left];
                left++;
            } else {
                res[count] = nums[right] * nums[right];
                right--;
            }
            count--;
        }

        return res;
    }
}
