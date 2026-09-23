# Pattern 08 — Linked List Pointers: Problem Walkthrough

This walkthrough records the five completed problems and the reasoning corrections that mattered during the session.

## 1. Reverse Linked List

Input:

```text
1 -> 2 -> 3 -> null
```

Output:

```text
3 -> 2 -> 1 -> null
```

### Key realization

Moving `current` does not reverse anything. Reversal requires changing `current.next`.

Before a rewire:

```text
previous       current       next
    |             |            |
    v             v            v
   null           1 ----------> 2 -> 3 -> null
```

Save first, then rewire:

```java
ListNode next = current.next;
current.next = previous;
previous = current;
current = next;
```

### Corrected misunderstanding

Initially, moving `previous = current` and `current = current.next` first left the current node structurally unchanged and risked losing the suffix. The corrected order is:

> save future path -> reverse current link -> advance references.

### Invariant

`previous` is the head of the reversed prefix; `current` is the first unprocessed node.

---

## 2. Middle of the Linked List

Odd:

```text
1 -> 2 -> 3 -> 4 -> 5
          ^
        middle
```

Even:

```text
1 -> 2 -> 3 -> 4 -> 5 -> 6
               ^
          second middle
```

### Straightforward approach

Count length, then traverse to position `(n / 2) + 1` using one-based positions. This is still O(n), but uses two passes.

### One-pass reasoning

`slow` moves one link for every two links moved by `fast`. Therefore slow covers half the distance.

```java
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
}
```

### Corrected misunderstanding

The condition is not `fast.next.next != null`. `fast.next.next` is allowed to become null. We only require `fast` and `fast.next` to exist before evaluating the two-step move.

---

## 3. Linked List Cycle

Cycle example:

```text
1 -> 2 -> 3 -> 4 -> 5
          ^         |
          |_________|
```

### Straightforward approach

Store visited node references in a `HashSet`. Re-seeing the same node reference means a cycle.

### Floyd's cycle detection

Use slow/fast pointers without a set. In a finite acyclic list, fast reaches null. In a cycle, both pointers eventually enter the cycle and the faster pointer catches the slower one.

```java
if (slow == fast) {
    return true;
}
```

Compare references, not values.

### Infinite-list clarification

The standard DSA model assumes finitely many allocated nodes. A finite reachable structure that never reaches null must repeat a node and therefore contain a cycle. A truly infinite acyclic generated stream is outside this problem model.

---

## 4. Merge Two Sorted Lists

Input:

```text
list1: 1 -> 3 -> 5
list2: 2 -> 4 -> 6
```

Output:

```text
1 -> 2 -> 3 -> 4 -> 5 -> 6
```

### Dummy and tail

Only one new sentinel node is created:

```java
ListNode dummy = new ListNode(-1);
ListNode tail = dummy;
```

`tail` is another reference to the same sentinel initially, not a second node.

### Invariant

`tail` points to the last node of the merged portion; `list1` and `list2` point to the first unmerged nodes.

### Important distinction

```java
tail.next = list1;   // changes structure
tail = tail.next;    // moves tail reference
list1 = list1.next;  // moves traversal reference
```

### Corrected misunderstandings

- Compare current values, not `next` values.
- Attach nodes, not copied values.
- `tail = list1` does not connect the remainder; `tail.next = list1` does.
- Return `dummy.next`, because `dummy` is not part of the result.

---

## 5. Remove Nth Node From End

Example:

```text
1 -> 2 -> 3 -> 4 -> 5
n = 2
```

Remove node 4:

```text
1 -> 2 -> 3 -> 5
```

### Why a dummy helps

A sentinel before head makes head deletion identical to deleting an internal node.

```text
dummy -> 1 -> 2 -> ...
```

### Fixed-gap strategy

Both pointers start at dummy. Move fast `n + 1` steps ahead, then move both one step at a time until fast reaches null.

At termination, slow is immediately before the target:

```java
slow.next = slow.next.next;
```

### Corrected misunderstanding

A counter plus one moving pointer did not maintain a structural gap. The one-pass solution needs two references whose distance stays fixed.

---

## Deferred revision — Reorder List

Reorder List was intentionally not completed in this session. It should be used during revision to compose:

1. middle finding,
2. splitting,
3. reversing the second half,
4. alternating merge,
5. cycle-safe termination.

The repository should not mark this problem as completed until it is independently worked through.
