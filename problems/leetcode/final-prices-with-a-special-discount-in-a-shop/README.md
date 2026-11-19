# LeetCode 1475 — Final Prices With a Special Discount in a Shop

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/final-prices-with-a-special-discount-in-a-shop/

## Problem

Given an array `prices`, for each `prices[i]`, find the first element to its right `prices[j]` such that:

```text
j > i
prices[j] <= prices[i]
```

Use `prices[j]` as the discount. If no such element exists, keep the original price.

### Example

```text
Input:  prices = [8,4,6,2,3]
Output: [4,2,4,2,3]
```

## Constraints

```text
1 <= prices.length <= 500
1 <= prices[i] <= 1000
```

## Invariant

The stack contains indices whose discount has not been found yet.

When the current price is less than or equal to the price at the stack top, the current price is the first valid discount for that unresolved index.

## Corner Cases

```text
prices = [1]
=> [1]
```

```text
prices = [5,4,3,2,1]
=> [1,1,1,1,1]
```

```text
prices = [1,2,3,4,5]
=> [1,2,3,4,5]
```

## Pseudocode

```text
res = copy of prices
stack = empty stack of indices

for i from 0 to prices.length - 1:
    curr = prices[i]

    while stack not empty AND prices[stack.top] >= curr:
        j = stack.pop
        res[j] = prices[j] - curr

    stack.push(i)

return res
```

## Complexity

Time: O(n)  
Space: O(n)

Why: each index is pushed once and popped at most once.

## Pattern

**Monotonic Stack**
