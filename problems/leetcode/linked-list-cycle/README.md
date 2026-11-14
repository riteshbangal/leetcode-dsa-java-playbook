# LeetCode 141 — Linked List Cycle

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/linked-list-cycle/

## Problem

Given `head`, the head of a linked list, return `true` if the linked list contains a cycle; otherwise return `false`.

A cycle exists if following `next` pointers eventually reaches a previously visited node.

## Constraints

```text
0 <= number of nodes <= 10^4
-10^5 <= Node.val <= 10^5
```

## Invariant

`slow` moves one node at a time and `fast` moves two nodes at a time.

If the list contains a cycle, `slow` and `fast` must eventually meet inside the cycle.

If `fast == null` or `fast.next == null`, the list terminates and therefore has no cycle.

## Corner Cases

```text
head = null
=> false
```

```text
1
=> false
```

```text
1 ↺
=> true
```

## Pseudocode

```text
slow = head
fast = head

while fast != null and fast.next != null:
    slow = slow.next
    fast = fast.next.next

    if slow == fast:
        return true

return false
```

## Complexity

Time: O(n)  
Space: O(1)

Why: the pointers move through the list without storing visited nodes.

## Pattern

**Linked List Pointers / Floyd's Cycle Detection**
