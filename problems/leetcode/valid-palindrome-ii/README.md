# LeetCode 680 — Valid Palindrome II

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/valid-palindrome-ii/

## Problem

Given a string `s`, return `true` if it can become a palindrome after deleting at most one character.

### Example

```text
s = "aba"
Output: true
```

```text
s = "abca"
Output: true
```

```text
s = "abc"
Output: false
```

## Constraints

```text
1 <= s.length <= 10^5
s contains only lowercase English letters
```

## Invariant

Everything outside the current `[left, right]` range has already been matched correctly.

At the first mismatch, the only valid choices are to skip either the left character or the right character.

## Corner Cases

```text
s = "aba"
=> true
```

```text
s = "abca"
=> true
```

## Pseudocode

```text
left = 0
right = s.length - 1

while left < right:
    if s[left] != s[right]:
        return isPalindrome(s, left + 1, right)
            OR isPalindrome(s, left, right - 1)

    left++
    right--

return true


isPalindrome(s, left, right):
    while left < right:
        if s[left] != s[right]:
            return false

        left++
        right--

    return true
```

## Complexity

```text
Time:  O(n)
Space: O(1)
```

Why: the main scan is linear, and at most two remaining ranges are checked once.

## Pattern

**Two Pointers**
