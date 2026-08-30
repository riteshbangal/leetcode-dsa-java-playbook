class ContainerWithMostWater {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;

        while (left < right) {
            // Any better unexplored pair must remain inside [left, right].
            int currentArea = calculateArea(height, left, right);
            maxArea = Math.max(maxArea, currentArea);

            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxArea;
    }

    private int calculateArea(int[] height, int left, int right) {
        int width = right - left;
        int usableHeight = Math.min(height[left], height[right]);
        return width * usableHeight;
    }
}
