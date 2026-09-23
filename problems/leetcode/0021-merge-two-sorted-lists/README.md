# Merge Two Sorted Lists

Pattern: [Linked List Pointers](../../../patterns/08-linked-list-pointers/)

## Important clue

Reuse existing nodes and build one sorted chain with a dummy sentinel and tail.

## Core invariant

> tail points to the last merged node; list1/list2 point to first unmerged nodes.

## Implementation notes

- Work with node references, not copied values unless the problem explicitly asks for values.
- Distinguish local-reference movement from assignments to a node's `next` field.
- Preserve reachability before overwriting a link when necessary.

See the pattern [problem walkthrough](../../../patterns/08-linked-list-pointers/problem-walkthrough.md) for the session reasoning and diagrams.

## Complexity

- Time: **O(n + m)**
- Auxiliary space: **O(1)**

## Revision prompt

Explain what every pointer represents before reading the implementation, then state which assignments mutate the list and which only move local references.
