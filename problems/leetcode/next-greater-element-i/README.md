# LeetCode 496 — Next Greater Element I

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/next-greater-element-i/

## Problem

Given two arrays `nums1` and `nums2`:

- all values are unique,
- every value in `nums1` appears in `nums2`.

For each value in `nums1`, find its first greater value to the right in `nums2`.

If no greater value exists, return `-1`.

### Example

```text
nums1 = [4,1,2]
nums2 = [1,3,4,2]

Output = [-1,3,-1]
```

## Constraints

```text
1 <= nums1.length <= nums2.length <= 1000
0 <= nums1[i], nums2[i] <= 10^4
All values are unique
Every nums1[i] exists in nums2
```

## Invariant

The stack contains indices from `nums2` whose next greater element has not been found yet.

When `nums2[i]` is greater than the value at the stack top, `nums2[i]` is the first greater element for that unresolved index.

## Corner Cases

```text
nums1 = [4]
nums2 = [4]
=> [-1]
```

```text
nums1 = [2,4]
nums2 = [1,2,3,4]
=> [3,-1]
```

## Pseudocode

```text
map = HashMap
stack = empty stack of indices

for i from 0 to nums2.length - 1:
    curr = nums2[i]

    while stack not empty AND curr > nums2[stack.top]:
        j = stack.pop
        map[nums2[j]] = curr

    stack.push(i)

res = new array of nums1.length

for i from 0 to nums1.length - 1:
    res[i] = map.getOrDefault(nums1[i], -1)

return res
```

## Complexity

Time: O(n + m)  
Space: O(n)

Why: each index from `nums2` is pushed and popped at most once, and each `nums1` lookup is expected O(1).

## Pattern

**Monotonic Stack + HashMap**
