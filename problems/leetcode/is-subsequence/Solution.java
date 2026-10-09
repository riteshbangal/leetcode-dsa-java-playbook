class Solution {
    public boolean isSubsequence(String s, String t) {
        if (s.length() == 0) {
            return true;
        }

        int matchedCount = 0;

        for (int i = 0; i < t.length(); i++) {
            if (t.charAt(i) == s.charAt(matchedCount)) {
                matchedCount++;
            }

            if (matchedCount == s.length()) {
                return true;
            }
        }

        return false;
    }
}
