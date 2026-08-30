class ValidPalindromeII {
    public boolean validPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;

        while (left < right) {
            // All mirrored pairs outside [left, right] are confirmed equal,
            // and the one permitted deletion has not been used.
            if (s.charAt(left) != s.charAt(right)) {
                return isPalindromeRange(s, left + 1, right)
                        || isPalindromeRange(s, left, right - 1);
            }

            left++;
            right--;
        }

        return true;
    }

    private boolean isPalindromeRange(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }

            left++;
            right--;
        }

        return true;
    }
}
