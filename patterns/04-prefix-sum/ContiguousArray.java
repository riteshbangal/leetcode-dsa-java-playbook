import java.util.HashMap;
import java.util.Map;

class ContiguousArray {
    public int findMaxLength(int[] nums) {
        Map<Integer, Integer> earliestIndexByBalance = new HashMap<>();
        earliestIndexByBalance.put(0, -1);

        int balance = 0;
        int maxLength = 0;

        for (int i = 0; i < nums.length; i++) {
            balance += nums[i] == 0 ? -1 : 1;

            if (earliestIndexByBalance.containsKey(balance)) {
                int earliestIndex = earliestIndexByBalance.get(balance);
                maxLength = Math.max(maxLength, i - earliestIndex);
            } else {
                // Keep only the earliest index to maximize every future distance.
                earliestIndexByBalance.put(balance, i);
            }
        }

        return maxLength;
    }
}
