import java.util.HashMap;
import java.util.Map;

class PermutationInString {
    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) {
            return false;
        }

        Map<Character, Integer> targetFrequency = new HashMap<>();
        Map<Character, Integer> windowFrequency = new HashMap<>();

        for (int i = 0; i < s1.length(); i++) {
            char current = s1.charAt(i);
            targetFrequency.put(current, targetFrequency.getOrDefault(current, 0) + 1);
        }

        int requiredSize = s1.length();
        int left = 0;

        for (int right = 0; right < s2.length(); right++) {
            char incoming = s2.charAt(right);
            windowFrequency.put(incoming, windowFrequency.getOrDefault(incoming, 0) + 1);

            if (right - left + 1 > requiredSize) {
                char outgoing = s2.charAt(left);
                int updatedFrequency = windowFrequency.get(outgoing) - 1;

                if (updatedFrequency == 0) {
                    windowFrequency.remove(outgoing);
                } else {
                    windowFrequency.put(outgoing, updatedFrequency);
                }

                left++;
            }

            // Equal frequency maps prove the fixed-size window is a permutation.
            if (right - left + 1 == requiredSize
                    && targetFrequency.equals(windowFrequency)) {
                return true;
            }
        }

        return false;
    }
}
