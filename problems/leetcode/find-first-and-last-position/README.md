# LeetCode 34 — Find First and Last Position of Element in Sorted Array

**Difficulty:** Medium  
**LeetCode:** https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/

## Problem

Given a sorted integer array `nums` and an integer `target`, return the first and last index of `target`.

If `target` is not present, return `[-1, -1]`.

### Example

```text
nums = [5,7,7,8,8,10]
target = 8
Output: [3,4]
```

```text
nums = [5,7,7,8,8,10]
target = 6
Output: [-1,-1]
```

## Constraints

```text
0 <= nums.length <= 10^5
-10^9 <= nums[i], target <= 10^9
nums is sorted in non-decreasing order
```

## Invariant

For the first occurrence, keep the leftmost possible target inside `[left, right]`.

For the last occurrence, keep the rightmost possible target inside `[left, right]`.

## Corner Cases

```text
nums = []
target = 8
=> [-1,-1]
```

```text
nums = [8,8]
target = 8
=> [0,1]
```

## Pseudocode

```text
if nums.length == 0:
    return [-1, -1]

# Find first occurrence
left = 0
right = nums.length - 1

while left < right:
    mid = left + (right - left) / 2

    if nums[mid] > target:
        right = mid - 1
    else if nums[mid] < target:
        left = mid + 1
    else:
        right = mid

first = -1
if nums[left] == target:
    first = left


# Find last occurrence
left = 0
right = nums.length - 1

while left < right:
    mid = left + (right - left + 1) / 2

    if nums[mid] > target:
        right = mid - 1
    else if nums[mid] < target:
        left = mid + 1
    else:
        left = mid

last = -1
if nums[right] == target:
    last = right

return [first, last]
```

## Complexity

```text
Time:  O(log n)
Space: O(1)
```

Why: two binary searches are performed, and each search halves the remaining range.

## Pattern

**Binary Search — Boundary Search**

## Boundary Note

For the first occurrence, use the lower midpoint:

```text
mid = left + (right - left) / 2
```

This works with:

```text
right = mid
```

For the last occurrence, use the upper midpoint:

```text
mid = left + (right - left + 1) / 2
```

This works with:

```text
left = mid
```

Why: when only two elements remain, lower-mid + `left = mid` can get stuck on the same index forever. Using the upper midpoint guarantees that the search interval still shrinks.
