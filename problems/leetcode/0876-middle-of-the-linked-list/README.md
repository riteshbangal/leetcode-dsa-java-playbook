# Middle of the Linked List

Pattern: [Linked List Pointers](../../../patterns/08-linked-list-pointers/)

## Important clue

Use slow and fast references moving at different speeds to find the middle in one pass.

## Core invariant

> slow moves one link for every two links moved by fast.

## Implementation notes

- Work with node references, not copied values unless the problem explicitly asks for values.
- Distinguish local-reference movement from assignments to a node's `next` field.
- Preserve reachability before overwriting a link when necessary.

See the pattern [problem walkthrough](../../../patterns/08-linked-list-pointers/problem-walkthrough.md) for the session reasoning and diagrams.

## Complexity

- Time: **O(n)**
- Auxiliary space: **O(1)**

## Revision prompt

Explain what every pointer represents before reading the implementation, then state which assignments mutate the list and which only move local references.
