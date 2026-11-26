# LeetCode 704 — Binary Search

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/binary-search/

## Problem

Given a sorted array of distinct integers `nums` and an integer `target`, return the index of the target if found, or `-1` otherwise. The algorithm must run in O(log n) time.

### Example

```text
nums = [-1,0,3,5,9,12], target = 9
Output: 4

nums = [-1,0,3,5,9,12], target = 2
Output: -1
```

## Constraints

- 1 <= nums.length <= 10^4
- -10^4 < nums[i], target < 10^4
- All values in nums are distinct and sorted in ascending order.

## Invariant

If the target exists and has not yet been found, its index lies within the inclusive interval `[left, right]`.

When `left > right`, the interval is empty and the target does not exist.

## Corner Cases

- Single element, matching target: return 0.
- Single element, missing target: return -1.
- Target at either boundary: return its index.
- Target outside the array's value range: return -1.

## Pseudocode

```text
left = 0
right = nums.length - 1

while left <= right:
    mid = left + (right - left) / 2
    if nums[mid] > target:
        right = mid - 1
    else if nums[mid] < target:
        left = mid + 1
    else:
        return mid

return -1
```

## Complexity

Time: O(log n)  
Space: O(1)

Why: each iteration eliminates at least half of the remaining candidates; only a constant number of variables is used.

## Pattern

**Binary Search**
