# Pattern 08 — Linked List Pointers

Workflow: **References -> Reachability -> Invariant -> Rewiring**.

A linked-list variable holds a reference to a node. Moving a local reference is different from changing a node's `next` field. This chapter focuses on making that distinction explicit and safe.

## Chapter status

**Completed: core foundation with five problems.**

Reorder List is intentionally deferred to revision as the advanced composition problem.

| Problem | Main idea | Status |
| --- | --- | --- |
| Reverse Linked List | Preserve next, rewire, then advance | Completed |
| Middle of the Linked List | Slow/fast relative speed | Completed |
| Linked List Cycle | Floyd cycle detection and node identity | Completed |
| Merge Two Sorted Lists | Dummy + tail + structural reconnection | Completed |
| Remove Nth Node From End | Dummy + fixed pointer gap | Completed |
| Reorder List | Middle + reverse + alternating merge | Deferred to revision |

Start with [problem-walkthrough.md](problem-walkthrough.md) for the session reasoning and corrected misunderstandings.

## Core mental model

```text
Traversal changes which node a variable refers to.
Rewiring changes which node another node points to.
```

Example:

```java
current = current.next;      // move local reference
current.next = previous;     // mutate list structure
```

For every pointer assignment ask:

1. What node does each reference point to now?
2. Does this move only a local reference or mutate the list?
3. Which portion is processed?
4. Which portion remains unprocessed?
5. Can the unprocessed portion still be reached?

## Node identity vs value

Two different nodes may contain the same value. When identity matters, compare references:

```java
slow == fast
```

not values:

```java
slow.val == fast.val
```

## Reverse Linked List

Invariant:

> `previous` points to the head of the already-reversed prefix, while `current` points to the first unprocessed node.

Safe update order:

```java
ListNode next = current.next;
current.next = previous;
previous = current;
current = next;
```

The first assignment preserves reachability before the structural mutation.

## Slow/Fast pointers

For middle finding:

> `slow` moves one node for every two nodes moved by `fast`.

Safe loop condition:

```java
while (fast != null && fast.next != null)
```

`fast.next.next` is allowed to become `null`; we only need `fast` and `fast.next` to be safe to dereference.

For cycle detection, if a finite reachable list is acyclic, traversal reaches `null`. If it contains a cycle, slow and fast eventually refer to the same node.

## Dummy nodes

A dummy node gives a stable node before the real head:

```text
dummy -> real head -> ...
```

This makes operations involving the first real node look like ordinary middle-of-list operations.

### Merge Two Sorted Lists

- `dummy` is the stable sentinel.
- `tail` points to the last node of the merged portion.
- `list1` and `list2` point to the first unmerged nodes.

```java
tail.next = list1;    // structural mutation
tail = tail.next;     // move tail reference
list1 = list1.next;   // move traversal reference
```

Return `dummy.next`, not `dummy` and not `tail`.

### Remove Nth Node From End

Start `slow` and `fast` at `dummy`, move `fast` ahead by `n + 1`, then move both until `fast == null`.

Invariant:

> The fixed gap keeps `slow` one node before the removal target when `fast` reaches `null`.

Removal:

```java
slow.next = slow.next.next;
```

## Implementations

The Java implementations live directly in this pattern folder, matching the established pattern-folder convention used by chapters such as `05-binary-search`.

| Problem | Implementation |
| --- | --- |
| Reverse Linked List | [ReverseLinkedList.java](ReverseLinkedList.java) |
| Middle of the Linked List | [MiddleOfLinkedList.java](MiddleOfLinkedList.java) |
| Linked List Cycle | [LinkedListCycle.java](LinkedListCycle.java) |
| Merge Two Sorted Lists | [MergeTwoSortedLists.java](MergeTwoSortedLists.java) |
| Remove Nth Node From End | [RemoveNthNodeFromEnd.java](RemoveNthNodeFromEnd.java) |

All implementations share the simple [ListNode.java](ListNode.java) representation for local compilation and revision.

## Complexity

| Problem | Time | Auxiliary space |
| --- | --- | --- |
| Reverse Linked List | O(n) | O(1) |
| Middle of the Linked List | O(n) | O(1) |
| Linked List Cycle | O(n) | O(1) |
| Merge Two Sorted Lists | O(n + m) | O(1) |
| Remove Nth Node From End | O(n) | O(1) |

A fixed number of reference variables is O(1) space even though those references are reassigned many times.

## Common mistakes from this chapter

1. Treating a node reference as the node's value.
2. Saying a node is "gone" when the real problem is lost reachability.
3. Rewiring `current.next` before preserving the original next node.
4. Describing `previous` only as the last visited node instead of the head of the reversed prefix.
5. Returning the original head after reversal.
6. Claiming three pointers imply O(n) space; a constant number of references is O(1).
7. Checking `fast.next.next != null` instead of checking that `fast` and `fast.next` are safe to dereference.
8. Comparing node values instead of node identity for cycle detection.
9. Creating two dummy nodes conceptually; `tail` is a reference, not a second node.
10. Writing `tail = list1` when the goal is structural attachment via `tail.next = list1`.
11. Returning `dummy` instead of `dummy.next`.
12. Using a fake `dummy = head`; a sentinel must be a separate node when head deletion needs uniform handling.

## Revision prompts

- Explain reference movement versus structural mutation.
- State the reversal invariant without looking at code.
- Explain why saving `next` must happen before rewiring.
- Trace odd and even middle finding and explain the loop condition.
- Explain why `slow == fast` checks node identity.
- Explain why Floyd's algorithm needs no visited set.
- Explain the distinct roles of `dummy`, `tail`, `list1`, and `list2`.
- Explain why `dummy.next` is returned.
- Derive the `n + 1` gap for Remove Nth Node From End.
- Revisit Reorder List by composing middle finding, reversal, and alternating merge.

## Interview articulation template

> `___` points to ___. `___` points to ___. The invariant is ___. Before changing a link I preserve ___. This assignment mutates the list: ___. These assignments only move local references: ___. The algorithm visits each node ___ times, so time is ___ and auxiliary space is ___.
