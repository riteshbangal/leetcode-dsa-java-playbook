# LeetCode 283 — Move Zeroes

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/move-zeroes/

## Problem

Given an integer array `nums`, move all `0`s to the end while preserving the relative order of the non-zero elements.

Do this **in-place** without making a copy of the array.

### Example

```text
nums = [0,1,0,3,12]
Output: [1,3,12,0,0]
```

```text
nums = [0]
Output: [0]
```

## Constraints

```text
1 <= nums.length <= 10^4
-2^31 <= nums[i] <= 2^31 - 1
```

## Invariant

Before each `fast` iteration, indices `[0 .. slow-1]` contain all non-zero elements seen so far in their original order.

## Corner Cases

```text
nums = [0]
=> [0]
```

```text
nums = [1,2,3]
=> [1,2,3]
```

## Pseudocode

```text
slow = 0

for fast from 0 to nums.length - 1:
    if nums[fast] != 0:
        nums[slow] = nums[fast]

        if slow < fast:
            nums[fast] = 0

        slow++
```

## Complexity

```text
Time:  O(n)
Space: O(1)
```

Why: `fast` scans the array once, and only two index variables are used.

## Pattern

**Two Pointers — Slow / Fast**
