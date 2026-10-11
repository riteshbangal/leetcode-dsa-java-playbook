# LeetCode 219 — Contains Duplicate II

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/contains-duplicate-ii/

## Problem

Given an integer array `nums` and an integer `k`, return `true` if there are two distinct indices `i` and `j` such that:

```text
nums[i] == nums[j]
and
|i - j| <= k
```

Otherwise return `false`.

### Example

```text
nums = [1,2,3,1]
k = 3
Output: true
```

```text
nums = [1,2,3,1,2,3]
k = 2
Output: false
```

## Constraints

```text
1 <= nums.length <= 10^5
-10^9 <= nums[i] <= 10^9
0 <= k <= 10^5
```

## Invariant

Before checking `nums[right]`, the set contains values from valid previous indices within distance `k` of `right`.

## Corner Cases

```text
nums = [1]
k = 0
=> false
```

```text
nums = [1,1]
k = 1
=> true
```

## Pseudocode

```text
window = empty set

for right from 0 to nums.length - 1:

    if right > k:
        remove nums[right - k - 1] from window

    if nums[right] exists in window:
        return true

    add nums[right] to window

return false
```

## Complexity

```text
Time:  O(n)
Space: O(k)
```

Why: each element is added and removed from the set at most once, and the set keeps only the recent window.

## Pattern

**Sliding Window + HashSet**
