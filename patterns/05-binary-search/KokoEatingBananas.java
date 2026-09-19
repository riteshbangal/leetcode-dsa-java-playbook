class KokoEatingBananas {
    public int minEatingSpeed(int[] piles, int h) {
        int left = 1;
        int right = getMaxPile(piles);

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (canFinish(piles, h, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    private boolean canFinish(int[] piles, int h, int speed) {
        long hoursNeeded = 0;

        for (int pile : piles) {
            hoursNeeded += (pile - 1L) / speed + 1;

            if (hoursNeeded > h) {
                return false;
            }
        }

        return true;
    }

    private int getMaxPile(int[] piles) {
        int maxPile = 0;

        for (int pile : piles) {
            maxPile = Math.max(maxPile, pile);
        }

        return maxPile;
    }
}
