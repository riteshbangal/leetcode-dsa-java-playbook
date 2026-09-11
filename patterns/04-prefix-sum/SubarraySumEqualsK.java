import java.util.HashMap;
import java.util.Map;

class SubarraySumEqualsK {
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> prefixFrequency = new HashMap<>();
        prefixFrequency.put(0, 1);

        int prefixSum = 0;
        int subarrayCount = 0;

        for (int value : nums) {
            prefixSum += value;

            int earlierPrefixNeeded = prefixSum - k;
            subarrayCount += prefixFrequency.getOrDefault(earlierPrefixNeeded, 0);

            // The map now includes every prefix through the current position.
            prefixFrequency.put(
                    prefixSum,
                    prefixFrequency.getOrDefault(prefixSum, 0) + 1
            );
        }

        return subarrayCount;
    }
}
