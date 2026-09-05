class MaximumNumberOfVowels {
    public int maxVowels(String s, int k) {
        int left = 0;
        int vowelCount = 0;
        int maxVowels = 0;

        for (int right = 0; right < s.length(); right++) {
            if (isVowel(s.charAt(right))) {
                vowelCount++;
            }

            if (right - left + 1 > k) {
                if (isVowel(s.charAt(left))) {
                    vowelCount--;
                }
                left++;
            }

            // vowelCount describes exactly the characters inside [left, right].
            if (right - left + 1 == k) {
                maxVowels = Math.max(maxVowels, vowelCount);
            }
        }

        return maxVowels;
    }

    private boolean isVowel(char c) {
        return "aeiou".indexOf(c) >= 0;
    }
}
