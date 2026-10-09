# LeetCode 392 — Is Subsequence

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/is-subsequence/

## Problem

Given two strings `s` and `t`, return `true` if `s` is a subsequence of `t`.

A subsequence keeps the original relative order, but characters do not need to be adjacent.

### Example

```text
s = "ace"
t = "abcde"
Output: true
```

```text
s = "aec"
t = "abcde"
Output: false
```

## Constraints

```text
0 <= s.length <= 100
0 <= t.length <= 10^4
s and t contain only lowercase English letters
```

## Invariant

`matchedCount` is the number of characters from the prefix of `s` already matched in order inside the processed part of `t`.

## Corner Cases

```text
s = ""
t = "abc"
=> true
```

```text
s = "abcd"
t = "abc"
=> false
```

## Pseudocode

```text
if s.length == 0:
    return true

matchedCount = 0

for each character c in t:
    if c == s[matchedCount]:
        matchedCount++

    if matchedCount == s.length:
        return true

return false
```

## Complexity

```text
Time:  O(|t|)
Space: O(1)
```

Why: `t` is scanned once, and only a counter is maintained.

## Pattern

**Two Pointers / Sequential Matching**
