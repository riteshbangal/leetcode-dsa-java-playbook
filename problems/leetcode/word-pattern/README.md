# LeetCode 290 — Word Pattern

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/word-pattern/

## Problem

Given a `pattern` and a string `s`, return `true` if `s` follows the same pattern.

Each character in `pattern` must map to exactly one word in `s`, and each word must map back to exactly one character.

### Example

```text
pattern = "abba"
s = "dog cat cat dog"
Output: true
```

```text
pattern = "abba"
s = "dog cat cat fish"
Output: false
```

## Constraints

```text
1 <= pattern.length <= 300
1 <= s.length <= 3000
pattern contains lowercase English letters
s contains lowercase English words separated by single spaces
```

## Invariant

After processing index `i`, all pattern characters and words seen so far satisfy a one-to-one mapping in both directions.

## Corner Cases

```text
pattern = "aaaa"
s = "dog cat cat dog"
=> false
```

```text
pattern = "abba"
s = "dog dog dog dog"
=> false
```

## Pseudocode

```text
words = s.split(" ")

if words.length != pattern.length:
    return false

charToWord = empty map
usedWords = empty set

for i from 0 to words.length - 1:
    c = pattern[i]
    word = words[i]

    if c exists in charToWord:
        if charToWord[c] != word:
            return false
    else:
        if word exists in usedWords:
            return false

        charToWord[c] = word
        add word to usedWords

return true
```

## Complexity

```text
Time:  O(n)
Space: O(n)
```

Why: each position is processed once, and the map/set store the mappings seen so far.

## Pattern

**Hashing — HashMap + HashSet**
