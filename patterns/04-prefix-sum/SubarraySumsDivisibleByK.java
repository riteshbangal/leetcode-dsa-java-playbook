import java.util.HashMap;
import java.util.Map;

class SubarraySumsDivisibleByK {
    public int subarraysDivByK(int[] nums, int k) {
        Map<Integer, Integer> remainderFrequency = new HashMap<>();
        remainderFrequency.put(0, 1);

        int sumSoFar = 0;
        int subarrayCount = 0;

        for (int value : nums) {
            sumSoFar += value;
            int remainder = sumSoFar % k;

            if (remainder < 0) {
                remainder += k;
            }

            // Equal prefix remainders enclose a sum divisible by k.
            subarrayCount += remainderFrequency.getOrDefault(remainder, 0);
            remainderFrequency.put(
                    remainder,
                    remainderFrequency.getOrDefault(remainder, 0) + 1
            );
        }

        return subarrayCount;
    }
}
