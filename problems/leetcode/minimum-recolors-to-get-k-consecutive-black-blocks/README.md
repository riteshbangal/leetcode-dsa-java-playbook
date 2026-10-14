# LeetCode 2379 — Minimum Recolors to Get K Consecutive Black Blocks

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/minimum-recolors-to-get-k-consecutive-black-blocks/

## Problem

Given a string `blocks` containing only `'B'` and `'W'`, and an integer `k`, return the minimum number of recolors needed so that at least one group of `k` consecutive blocks is all `'B'`.

In one operation, you may recolor a `'W'` block to `'B'`.

### Example

```text
blocks = "WBBWWBBWBW"
k = 7
Output: 3
```

```text
blocks = "WBWBBBW"
k = 2
Output: 0
```

## Constraints

```text
1 <= k <= blocks.length <= 100
blocks[i] is either 'W' or 'B'
```

## Invariant

For every window of size `k`, `count` stores the number of `'W'` blocks inside that window.

## Corner Cases

```text
blocks = "BBBB"
k = 2
=> 0
```

```text
blocks = "WWWW"
k = 4
=> 4
```

## Pseudocode

```text
minOps = infinity
left = 0
count = 0

for right from 0 to blocks.length - 1:
    if blocks[right] == 'W':
        count++

    if right - left + 1 > k:
        if blocks[left] == 'W':
            count--
        left++

    if right - left + 1 == k:
        minOps = min(minOps, count)

return minOps
```

## Complexity

```text
Time:  O(n)
Space: O(1)
```

Why: each character enters and leaves the fixed-size window at most once.

## Pattern

**Sliding Window — Fixed Size**
