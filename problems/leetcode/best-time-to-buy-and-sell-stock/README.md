# LeetCode 121 — Best Time to Buy and Sell Stock

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/best-time-to-buy-and-sell-stock/

## Problem

Given an array `prices` where `prices[i]` is the price of a stock on day `i`, choose one day to buy and a later day to sell.

Return the maximum profit you can achieve. If no profit is possible, return `0`.

### Example

```text
Input:  prices = [7,1,5,3,6,4]
Output: 5
```

Buy at `1` and sell later at `6`.

## Constraints

```text
1 <= prices.length <= 100000
0 <= prices[i] <= 10000
```

## Invariant

Before processing each day, `minPrice` is the minimum price seen so far.

`maxProfit` stores the best valid buy-before-sell profit found so far.

## Corner Cases

```text
prices = [7,6,4,3,1]
=> 0
```

```text
prices = [5]
=> 0
```

## Pseudocode

```text
minPrice = prices[0]
maxProfit = 0

for each todayPrice in prices:
    minPrice = min(minPrice, todayPrice)
    profitToday = todayPrice - minPrice
    maxProfit = max(maxProfit, profitToday)

return maxProfit
```

## Complexity

Time: O(n)  
Space: O(1)

Why: the array is scanned once and only constant extra state is maintained.

## Pattern

**Single Pass / Running Minimum**
