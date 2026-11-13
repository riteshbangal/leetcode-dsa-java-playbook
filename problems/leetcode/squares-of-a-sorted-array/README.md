# LeetCode 977 — Squares of a Sorted Array

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/squares-of-a-sorted-array/

## Problem

Given an integer array `nums` sorted in non-decreasing order, return an array of the squares of each number, also sorted in non-decreasing order.

### Example

```text
Input:  nums = [-4,-1,0,3,10]
Output: [0,1,9,16,100]
```

## Constraints

```text
1 <= nums.length <= 10^4
-10^4 <= nums[i] <= 10^4
nums is sorted in non-decreasing order
```

## Invariant

At every iteration, `res[count + 1 ... n - 1]` already contains the largest squared values in sorted order.

The next largest square must come from either `nums[left]` or `nums[right]`.

## Corner Cases

```text
nums = [-1]
=> [1]
```

```text
nums = [-5,-3,-1]
=> [1,9,25]
```

```text
nums = [1,2,3]
=> [1,4,9]
```

## Pseudocode

```text
res = new array of size n
left = 0
right = n - 1
count = n - 1

while left <= right:
    if abs(nums[left]) > abs(nums[right]):
        res[count] = nums[left] * nums[left]
        left++
    else:
        res[count] = nums[right] * nums[right]
        right--

    count--

return res
```

## Complexity

Time: O(n)  
Space: O(n)

Why: each element is processed exactly once. The result array stores `n` squared values; auxiliary space is O(1).

## Pattern

**Two Pointers**
