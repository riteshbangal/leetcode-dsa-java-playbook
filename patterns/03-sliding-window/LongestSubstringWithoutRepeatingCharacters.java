import java.util.HashSet;
import java.util.Set;

class LongestSubstringWithoutRepeatingCharacters {
    public int lengthOfLongestSubstring(String s) {
        Set<Character> windowCharacters = new HashSet<>();
        int left = 0;
        int maxLength = 0;

        for (int right = 0; right < s.length(); right++) {
            char incoming = s.charAt(right);

            while (windowCharacters.contains(incoming)) {
                windowCharacters.remove(s.charAt(left));
                left++;
            }

            windowCharacters.add(incoming);

            // After shrinking, every character in [left, right] is unique.
            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }
}
