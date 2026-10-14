class Solution {
    public int minimumRecolors(String blocks, int k) {
        int minOps = Integer.MAX_VALUE;

        int left = 0;
        int count = 0;

        for (int right = 0; right < blocks.length(); right++) {
            char c = blocks.charAt(right);

            if (c == 'W') {
                count++;
            }

            if (right - left + 1 > k) {
                if (blocks.charAt(left) == 'W') {
                    count--;
                }
                left++;
            }

            if (right - left + 1 == k) {
                minOps = Math.min(minOps, count);
            }
        }

        return minOps;
    }
}
