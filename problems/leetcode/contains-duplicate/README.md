# LeetCode 217 — Contains Duplicate

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/contains-duplicate/

## Problem

Given an integer array `nums`, return `true` if any value appears at least twice.

Return `false` if every element is distinct.

### Example

```text
nums = [1,2,3,1]
Output: true
```

```text
nums = [1,2,3,4]
Output: false
```

## Constraints

```text
1 <= nums.length <= 10^5
-10^9 <= nums[i] <= 10^9
```

## Invariant

After processing each number, the set contains all distinct values seen so far.

## Corner Cases

```text
nums = [1]
=> false
```

```text
nums = [1,1]
=> true
```

## Pseudocode

```text
seen = empty set

for each num in nums:
    if num exists in seen:
        return true

    add num to seen

return false
```

## Complexity

```text
Time:  O(n)
Space: O(n)
```

Why: each element is processed once, and the set can hold up to `n` distinct values.

## Pattern

**Hashing / HashSet**
