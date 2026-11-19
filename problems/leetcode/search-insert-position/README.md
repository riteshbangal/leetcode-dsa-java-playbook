# LeetCode 35 — Search Insert Position

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/search-insert-position/

## Problem

Given a sorted array of distinct integers `nums` and a target value `target`, return the index if the target is found.

If it is not found, return the index where it would be inserted so that the array remains sorted.

### Example

```text
Input: nums = [1,3,5,6], target = 5
Output: 2
```

```text
Input: nums = [1,3,5,6], target = 2
Output: 1
```

```text
Input: nums = [1,3,5,6], target = 7
Output: 4
```

## Constraints

```text
1 <= nums.length <= 10^4
-10^4 <= nums[i] <= 10^4
nums contains distinct values sorted in ascending order
-10^4 <= target <= 10^4
```

## Invariant

The active search interval is `[left, right]`.

- If `nums[mid] < target`, the target or insertion position must be to the right of `mid`.
- If `nums[mid] > target`, it must be to the left of `mid`.
- If the target is not found, `left` ends at the correct insertion index.

## Corner Cases

```text
nums = [1], target = 0
=> 0
```

```text
nums = [1], target = 2
=> 1
```

```text
nums = [1,3,5,6], target = 6
=> 3
```

## Pseudocode

```text
left = 0
right = nums.length - 1

while left <= right:
    mid = left + (right - left) / 2

    if nums[mid] < target:
        left = mid + 1
    else if nums[mid] > target:
        right = mid - 1
    else:
        return mid

return left
```

## Complexity

Time: O(log n)  
Space: O(1)

Why: binary search halves the remaining search interval each iteration.

## Pattern

**Binary Search**
