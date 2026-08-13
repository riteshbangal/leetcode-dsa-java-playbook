import java.util.HashSet;
import java.util.Set;

class MatchingDifferenceByK {
    public boolean hasPairWithDifferenceK(int[] nums, int k) {
        if (k < 0) {
            return false;
        }

        Set<Long> seen = new HashSet<>();
        long difference = k;

        for (int num : nums) {
            long current = num;

            if (seen.contains(current - difference) || seen.contains(current + difference)) {
                return true;
            }

            seen.add(current);
        }

        return false;
    }
}
