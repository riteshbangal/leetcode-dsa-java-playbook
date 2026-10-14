# LeetCode 744 — Find Smallest Letter Greater Than Target

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/find-smallest-letter-greater-than-target/

## Problem

Given a sorted array of lowercase letters `letters` and a lowercase letter `target`, return the smallest letter strictly greater than `target`.

If no such letter exists, return the first letter in the array.

### Example

```text
letters = ['c','f','j']
target = 'c'
Output: 'f'
```

```text
letters = ['x','x','y','y']
target = 'z'
Output: 'x'
```

## Constraints

```text
2 <= letters.length <= 10^4
letters contains only lowercase English letters
letters is sorted in non-decreasing order
```

## Invariant

During binary search, the current `[left, right]` range keeps the smallest possible letter that can still be strictly greater than `target`.

## Corner Cases

```text
letters = ['c','f','j']
target = 'j'
=> 'c'
```

```text
letters = ['c','f','j']
target = 'a'
=> 'c'
```

## Pseudocode

```text
left = 0
right = letters.length - 1

while left < right:
    mid = left + (right - left) / 2

    if letters[mid] > target:
        right = mid
    else:
        left = mid + 1

if letters[left] > target:
    return letters[left]

return letters[0]
```

## Complexity

```text
Time:  O(log n)
Space: O(1)
```

Why: each iteration halves the search range, and only a few index variables are used.

## Pattern

**Binary Search — Boundary Search**
