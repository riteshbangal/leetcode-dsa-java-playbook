import static java.lang.Math.min;
import static java.lang.Math.max;

class Solution {
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        int minPrice = prices[0];

        for (int todayPrice : prices) {
            minPrice = min(minPrice, todayPrice);
            int profitToday = todayPrice - minPrice;
            maxProfit = max(maxProfit, profitToday);
        }

        return maxProfit;
    }
}
