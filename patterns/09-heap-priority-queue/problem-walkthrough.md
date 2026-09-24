# Heap / Priority Queue — Problem Walkthrough

Use this file for revision after first attempting the problems independently.

For traces below, heap contents may be displayed in sorted order for readability. A real heap is **not fully sorted**; only its root is guaranteed by the heap property.

## Implemented files

- [LastStoneWeight.java](LastStoneWeight.java)
- [KthLargestElementInAnArray.java](KthLargestElementInAnArray.java)
- [TopKFrequentElements.java](TopKFrequentElements.java)
- [KClosestPointsToOrigin.java](KClosestPointsToOrigin.java)
- [KthLargest.java](KthLargest.java)
- [MergeKSortedLists.java](MergeKSortedLists.java)
- [ListNode.java](ListNode.java)

---

## 1. Last Stone Weight — LeetCode 1046

**Learning status:** Guided implementation.

### Problem

Repeatedly smash the two heaviest stones. If they have equal weights, both disappear. Otherwise the heavier stone survives with weight equal to the difference. Return the last weight, or `0` when no stone remains.

```text
stones = [2, 7, 4, 1, 8, 1]
answer = 1
```

### Brute force

Sort, take the last two values, insert their difference when non-zero, then sort again.

The repeated requirement is not "find the maximum once." It is:

```text
take current maximum
take current maximum again
modify the candidate set
repeat
```

That is the heap clue.

### Heap choice

Use a max-heap because each round needs the two largest remaining stones.

```text
poll -> largest
poll -> second largest
offer(difference) when difference > 0
```

### Trace

```text
initial stones: 2, 7, 4, 1, 8, 1

poll 8, poll 7 -> offer 1
remaining values: 4, 2, 1, 1, 1

poll 4, poll 2 -> offer 2
remaining values: 2, 1, 1, 1

poll 2, poll 1 -> offer 1
remaining values: 1, 1, 1

poll 1, poll 1 -> equal, offer nothing
remaining: 1
```

### Invariant

> Before every iteration, the heap contains exactly the stones still alive and its root is the heaviest remaining stone.

### Complexity

- Time: `O(n log n)`.
- Auxiliary space: `O(n)`.

---

## 2. Kth Largest Element in an Array — LeetCode 215

**Learning status:** Guided implementation.

### Problem

Return the kth largest value in the array. Duplicates count as separate positions in sorted order.

```text
nums = [3, 2, 1, 5, 6, 4]
k = 2
answer = 5
```

Descending order would be:

```text
6, 5, 4, 3, 2, 1
```

but the heap solution avoids sorting all `n` values.

### Core idea

Keep only the largest `k` values seen so far in a min-heap.

Why a min-heap?

```text
largest k values: [5, 6]
smallest among them = 5 = kth largest
```

The root is both the answer threshold and the candidate that should be removed when a stronger value enters.

### Trace

For `k = 2`:

| Incoming | Heap after offer | Action | Kept candidates |
| ---: | --- | --- | --- |
| 3 | `[3]` | none | `[3]` |
| 2 | `[2,3]` | none | `[2,3]` |
| 1 | `[1,2,3]` | poll `1` | `[2,3]` |
| 5 | `[2,3,5]` | poll `2` | `[3,5]` |
| 6 | `[3,5,6]` | poll `3` | `[5,6]` |
| 4 | `[4,5,6]` | poll `4` | `[5,6]` |

Final root: `5`.

### Invariant

> After each value, the min-heap contains at most the largest `k` values seen so far. Once it has size `k`, the root is the kth largest among processed values.

### Complexity

- Time: `O(n log k)`.
- Auxiliary space: `O(k)`.

---

## 3. Top K Frequent Elements — LeetCode 347

**Learning status:** Guided implementation.

### Problem

Return the `k` values with the highest frequencies.

```text
nums = [1, 3, 1, 2, 3, 1, 2, 4]
k = 2
```

Frequencies:

```text
1 -> 3
2 -> 2
3 -> 2
4 -> 1
```

A valid answer contains `1` and either `2` or `3` when ties are allowed by the input/output contract.

### Two phases

Do not try to maintain final heap priorities while the frequency is still changing.

```text
Phase 1:
nums -> HashMap<number, frequency>

Phase 2:
completed map entries -> bounded min-heap ordered by frequency
```

Each heap entry is a map entry:

```text
(number, frequency)
```

Priority comes from the frequency, not from the numerical value.

### Example trace

Using:

```text
1 -> 3
3 -> 2
2 -> 4
4 -> 1
k = 2
```

| Candidate | Heap candidates after offer | If size > 2 |
| --- | --- | --- |
| `(1,3)` | `(1,3)` | — |
| `(3,2)` | `(3,2), (1,3)` | — |
| `(2,4)` | `(3,2), (1,3), (2,4)` | remove `(3,2)` |
| `(4,1)` | `(4,1), (1,3), (2,4)` | remove `(4,1)` |

The final heap stores the two highest-frequency candidates.

### Invariant

> After processing each completed map entry, the heap contains at most the `k` highest-frequency candidates seen so far, and the root is the lowest-frequency candidate currently kept.

### Complexity

Let `m` be the number of distinct values.

- Build frequency map: `O(n)`.
- Heap processing: `O(m log k)`.
- Total: `O(n + m log k)`.
- Auxiliary space: `O(m + k)`.

---

## 4. K Closest Points to Origin — LeetCode 973

**Learning status:** Guided implementation.

### Problem

Given points `[x, y]`, return the `k` points closest to `(0,0)`.

```text
points = [[3,3], [5,-1], [-2,4]]
k = 2
```

Squared distances:

```text
[3,3]  -> 18
[5,-1] -> 26
[-2,4] -> 20
```

A valid answer is:

```text
[[3,3], [-2,4]]
```

The square root is unnecessary because it preserves ordering.

### Heap choice

We want to keep the **smallest `k` distances**, so use a **max-heap of size `k`**.

The root is the farthest point among the points currently being kept. When a better point arrives and the heap grows to `k + 1`, the farthest candidate is evicted.

### Trace

For `k = 2`:

```text
[1,3]  distance² 10 -> keep
[-2,2] distance²  8 -> keep

[1,1] distance² 2 arrives
temporary distances: 10, 8, 2
poll max -> remove 10

kept distances: 8, 2
```

### Safe comparator

Heap entry:

```text
entire point [x,y]
```

Priority:

```text
x² + y²
```

Use `long` arithmetic and `Long.compare` rather than subtracting computed distances.

### Invariant

> After every point, the max-heap contains at most the `k` closest points seen so far, while its root is the farthest currently kept point.

### Complexity

- Time: `O(n log k)`.
- Auxiliary heap space: `O(k)`.

---

## 5. Kth Largest Element in a Stream — LeetCode 703

**Learning status:** Independently implemented after concept discussion.

### Problem

Construct an object with `k` and initial values. Every call to `add(val)` inserts one new value and returns the current kth largest value.

```text
k = 3
initial = [4,5,8,2]

add(3)  -> 4
add(5)  -> 5
add(10) -> 5
add(9)  -> 8
add(4)  -> 8
```

### New idea: persistent state

Do not rebuild a heap from all historical values after each call.

The object stores:

```text
k
+
a min-heap containing the largest k values seen so far
```

After every `add`:

```text
offer(val)
if size > k:
    poll()
return peek()
```

### Short trace

With `k = 3`, the heap may represent:

```text
[4,5,8]
root = 4
```

`add(10)`:

```text
temporary: [4,5,8,10]
poll 4
kept: [5,8,10]
peek -> 5
```

### Invariant

> The persistent min-heap contains the largest `k` values seen in the stream, and its root is the current kth largest.

### Complexity

- Constructor: `O(n log k)`.
- Each `add`: `O(log k)`.
- Persistent auxiliary space: `O(k)`.

---

## 6. Merge k Sorted Lists — LeetCode 23

**Learning status:** Reference solution after guided reasoning.

### Problem

Merge `k` individually sorted linked lists into one globally sorted linked list.

```text
A: 1 -> 4 -> 5
B: 1 -> 3 -> 4
C: 2 -> 6
```

Output:

```text
1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6
```

### Brute force

Collect all node values, sort them, then build a new list. For `N` total nodes this is `O(N log N)` and needs `O(N)` additional collection storage.

### Heap insight

Do not put all `N` nodes into the heap.

Because each input list is already sorted, only its first unmerged node can compete for the next output position.

Initially:

```text
A: [1] -> 4 -> 5
B: [1] -> 3 -> 4
C: [2] -> 6
     ^
one active candidate per non-empty list
```

Use a min-heap ordered by `ListNode.val`.

### Trace

```text
heap candidates: 1(A), 1(B), 2(C)

poll 1(A)
append it
offer 4(A)

heap candidates: 1(B), 2(C), 4(A)

poll 1(B)
append it
offer 3(B)

heap candidates: 2(C), 3(B), 4(A)

poll 2(C)
append it
offer 6(C)

...
```

The heap itself need not be sorted. Every `poll()` only needs to return the smallest active candidate.

### Result construction

Use a linked-list dummy and tail:

```text
dummy -> merged prefix
             ^
            tail
```

Every polled node is attached after `tail`. If that node has a successor, the successor becomes the new candidate from the same source list.

### Invariant

> The heap contains exactly the first unmerged node from each non-exhausted input list, and `tail` points to the last node already committed to the merged output.

### Complexity

With `N` total nodes and at most `k` active list heads:

- Time: `O(N log k)`.
- Auxiliary heap space: `O(k)`.
- The implementation reuses the existing list nodes.

---

## 7. Find Median from Data Stream — LeetCode 295

**Learning status:** Deferred to revision.

This is the advanced two-heap extension of the chapter and was intentionally not implemented during preliminary coverage.

The revision objective is to derive, rather than memorize, the two invariants:

```text
lower half -> max-heap
upper half -> min-heap
```

and a size-balance rule that makes the median available from one or both roots.

No Java reference implementation is stored in this chapter yet so that the problem can be attempted independently during revision.

---

## Consolidated decision table

| Problem | Heap entry | Heap type | Root represents |
| --- | --- | --- | --- |
| Last Stone Weight | stone weight | Max-heap | Heaviest alive stone |
| Kth Largest Array | value | Min-heap size `k` | kth largest threshold |
| Top K Frequent | `(value, frequency)` entry | Min-heap size `k` | Weakest kept frequency |
| K Closest Points | point `[x,y]` | Max-heap size `k` | Farthest kept point |
| Kth Largest Stream | value | Persistent min-heap size `k` | Current kth largest |
| Merge k Sorted Lists | `ListNode` | Min-heap size at most `k` | Smallest active list head |

## Revision prompts

- Why is Last Stone Weight not just a one-time maximum problem?
- Why does keeping the largest `k` use a min-heap?
- Why should Top K Frequent finish the frequency map before heap selection?
- What exactly does each heap entry represent in each problem?
- Why is K Closest the mirror image of Kth Largest?
- Why can comparator subtraction overflow?
- Why must stream state live on the object?
- Why does Merge k Sorted Lists need only one candidate from each list?
- Why can repeated `poll()` create sorted output even though the heap itself is not fully sorted?
- Can the two-heap median structure be derived independently during revision?
