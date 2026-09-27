import java.util.HashMap;
import java.util.Map;

class SubarraySumEqualsK {
    public int subarraySum(int[] nums, int k) {

        //SumRanges sr = new SumRanges(nums);

        int count = 0;

        Map<Integer, Integer> map = new HashMap<>();
        

        map.put(0,1);

        
        int prefixSum = 0;
        for (int num : nums) {
            //prefixSum = sr.sumRange(0, index)
            prefixSum += num;
            int requiredVal = prefixSum - k;
            
            if (map.containsKey(requiredVal))
              count += map.get(requiredVal);

            map.put(prefixSum, map.getOrDefault(prefixSum, 0)+1);
        }

        return count;
    }
}
