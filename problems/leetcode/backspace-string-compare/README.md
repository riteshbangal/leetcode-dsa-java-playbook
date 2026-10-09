# LeetCode 844 — Backspace String Compare

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/backspace-string-compare/

## Problem

Given two strings `s` and `t`, return `true` if they are equal after processing backspaces.

`#` means backspace and deletes the previous character if one exists.

### Example

```text
s = "ab#c"
t = "ad#c"
Output: true
```

```text
s = "a#c"
t = "b"
Output: false
```

## Constraints

```text
1 <= s.length, t.length <= 200
s and t contain lowercase English letters and '#'
```

## Invariant

After processing the first `i` characters, the stack contains exactly the characters that remain after applying all backspaces seen so far.

## Corner Cases

```text
s = "ab##"
t = "c#d#"
=> true
```

```text
s = "###a"
t = "a"
=> true
```

## Pseudocode

```text
main(s, t):
    processedS = processBackspace(s)
    processedT = processBackspace(t)

    return processedS == processedT


processBackspace(input):
    stack = empty stack

    for each character c in input:
        if c != '#':
            stack.push(c)
        else if stack is not empty:
            stack.pop()

    ans = empty string builder

    while stack is not empty:
        ans.append(stack.pop())

    return reverse(ans)
```

## Complexity

```text
Time:  O(n)
Space: O(n)
```

Why: each character is processed a constant number of times, and the stack/result can hold up to O(n) characters.

## Pattern

**Stack**
