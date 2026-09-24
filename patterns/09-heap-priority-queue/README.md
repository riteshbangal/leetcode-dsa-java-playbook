# Pattern 09 — Heap / Priority Queue

Workflow: **Candidate set -> Priority -> Heap invariant -> Bounded state**.

A heap is useful when the problem repeatedly asks for the current highest- or lowest-priority candidate while the candidate set changes. Java's `PriorityQueue` gives direct access to the root candidate without fully sorting every element.

For the problem-by-problem reasoning and traces, see [problem-walkthrough.md](problem-walkthrough.md).

## Chapter status

**Completed: preliminary core coverage with six covered problems.**

Find Median from Data Stream is intentionally deferred to revision as the advanced two-heap composition problem. Preliminary completion does not mean independent mastery.

| Problem | Main idea | Learning status |
| --- | --- | --- |
| Last Stone Weight | Repeatedly remove the two largest values | Guided implementation |
| Kth Largest Element in an Array | Keep only the largest `k` values | Guided implementation |
| Top K Frequent Elements | Hash map + bounded heap ordered by frequency | Guided implementation |
| K Closest Points to Origin | Keep the smallest `k` computed priorities | Guided implementation |
| Kth Largest Element in a Stream | Persistent bounded heap across calls | Independently implemented after concept discussion |
| Merge k Sorted Lists | One active candidate per sorted list | Reference solution after guided reasoning |
| Find Median from Data Stream | Two heaps and balancing | Deferred to revision |

## Core mental model

A heap guarantees the priority of its root. It does **not** guarantee that iteration produces sorted order.

```text
Min-heap:
    root = smallest priority

Max-heap:
    root = largest priority
```

For Java:

```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

PriorityQueue<Integer> maxHeap =
        new PriorityQueue<>(Collections.reverseOrder());
```

The common operations are:

```java
heap.offer(value);   // insert
heap.peek();         // inspect root without removing
heap.poll();         // remove and return root
```

For a heap with `h` elements:

| Operation | Complexity |
| --- | ---: |
| `peek()` | `O(1)` |
| `offer()` | `O(log h)` |
| `poll()` | `O(log h)` |

## Recognition clues

Consider a heap when the problem says or implies:

- repeatedly take the current smallest or largest item;
- keep only the best `k` candidates seen so far;
- data arrives incrementally and the answer must stay available;
- candidates are objects or pairs whose priority comes from one field or a computed score;
- several sorted sources each expose one current candidate;
- full sorting would maintain more order than the problem actually needs.

Do **not** use a heap automatically for a one-time minimum or maximum. A single scan finds one min/max in `O(n)` time and `O(1)` auxiliary space.

## Heap forms used in this chapter

| Goal | Heap | Root means | Eviction rule |
| --- | --- | --- | --- |
| Repeated current maximum | Max-heap | Largest available item | `poll()` largest |
| Keep largest `k` | Min-heap of size `k` | Smallest among current top `k` | Remove root when size exceeds `k` |
| Keep smallest `k` | Max-heap of size `k` | Largest among current bottom `k` | Remove root when size exceeds `k` |
| Merge sorted sources | Min-heap of active candidates | Smallest next output | Poll root, then expose successor |
| Streaming kth largest | Persistent min-heap of size `k` | Current kth largest | Update on every `add()` |

The counterintuitive rule is worth memorizing only after understanding it:

```text
Keep largest k  -> min-heap of size k
Keep smallest k -> max-heap of size k
```

The root is the weakest member of the candidates currently being kept, so it is the easiest one to evict.

## Java comparator rules

The queue may store integers, map entries, points, nodes, or custom objects. The comparator defines what "priority" means.

Frequency:

```java
(a, b) -> Integer.compare(a.getValue(), b.getValue())
```

Linked-list node value:

```java
(a, b) -> Integer.compare(a.val, b.val)
```

Squared distance for a max-heap:

```java
(a, b) -> Long.compare(distanceSquared(b), distanceSquared(a))
```

Prefer `Integer.compare` or `Long.compare` over subtraction such as:

```java
(a, b) -> b - a
```

because subtraction can overflow and produce incorrect ordering.

For squared coordinates, promote before multiplication:

```java
long distance =
        1L * x * x
        + 1L * y * y;
```

## Heap is not a sorted collection

A heap may internally look like:

```text
        2
       / \
      4   3
```

That is valid for a min-heap even though the level order is not globally sorted.

The only property required by the algorithms in this chapter is:

> `peek()` / `poll()` returns the current root candidate according to the comparator.

Therefore, never rely on iterating over `PriorityQueue` to produce ascending or descending order.

## Core invariants

### Last Stone Weight

> Before each smash, the max-heap contains exactly the stones still alive, and its root is the heaviest remaining stone.

### Kth Largest / bounded top `k`

> After processing each value, the min-heap contains at most the largest `k` values seen so far. When it contains `k` values, the root is the kth largest among those processed values.

### Top K Frequent

> After each completed frequency-map candidate is processed, the heap contains at most the `k` highest-frequency candidates seen so far, and the root is the weakest kept frequency.

### K Closest Points

> After each point is processed, the max-heap contains at most the `k` closest points seen so far, and its root is the farthest point among those currently kept.

### Kth Largest in a Stream

> The object permanently stores the largest `k` values seen in the stream, so the min-heap root is the current kth largest whenever at least `k` values exist.

### Merge k Sorted Lists

> The min-heap contains the first unmerged node from each non-exhausted list. Its root is therefore the smallest value that can legally come next in the merged output.

## Heap vs. Monotonic Stack

| Heap / Priority Queue | Monotonic Stack |
| --- | --- |
| Selects the current highest- or lowest-priority candidate | Resolves order-based neighbor relationships |
| Original position is often irrelevant | Index/order is central |
| Root is globally best among active candidates | Top is the latest unresolved candidate |
| Insert/remove usually costs `O(log h)` | Each item is usually pushed/popped once for amortized `O(n)` |
| Examples: top `k`, scheduling, merging sorted sources | Examples: next greater, previous smaller, histogram boundaries |

## Implementations

All chapter implementations and supporting code live directly in this folder.

| Problem | Implementation |
| --- | --- |
| Last Stone Weight | [LastStoneWeight.java](LastStoneWeight.java) |
| Kth Largest Element in an Array | [KthLargestElementInAnArray.java](KthLargestElementInAnArray.java) |
| Top K Frequent Elements | [TopKFrequentElements.java](TopKFrequentElements.java) |
| K Closest Points to Origin | [KClosestPointsToOrigin.java](KClosestPointsToOrigin.java) |
| Kth Largest Element in a Stream | [KthLargest.java](KthLargest.java) |
| Merge k Sorted Lists | [MergeKSortedLists.java](MergeKSortedLists.java) |
| Local linked-list node support | [ListNode.java](ListNode.java) |

No implementation is added for Find Median from Data Stream because it was intentionally deferred.

## Complexity

Let:

- `n` = number of input values or points;
- `k` = requested candidate count / number of lists where applicable;
- `m` = number of distinct values;
- `N` = total nodes across all linked lists.

| Problem | Time | Auxiliary space |
| --- | ---: | ---: |
| Last Stone Weight | `O(n log n)` | `O(n)` |
| Kth Largest Element in an Array | `O(n log k)` | `O(k)` |
| Top K Frequent Elements | `O(n + m log k)` | `O(m + k)` |
| K Closest Points to Origin | `O(n log k)` | `O(k)` plus returned output |
| Kth Largest stream constructor | `O(n log k)` | `O(k)` |
| One `KthLargest.add()` | `O(log k)` | persistent `O(k)` state |
| Merge k Sorted Lists | `O(N log k)` | `O(k)` heap |

## Common mistakes from this chapter

1. Using a heap just to find a one-time min or max.
2. Assuming a `PriorityQueue` is fully sorted.
3. Iterating over a heap and expecting priority order.
4. Using a max-heap for "keep largest `k`" instead of a bounded min-heap.
5. Using a min-heap for "keep smallest `k`" instead of a bounded max-heap.
6. Forgetting that the root of a bounded heap is the eviction threshold.
7. Mixing the stored object with the property used for priority.
8. Updating heap priorities while a frequency is still changing instead of first completing the frequency map.
9. Writing subtraction comparators that can overflow.
10. Squaring coordinates with `int` when a wider type may be needed.
11. Rebuilding stream state on every call instead of keeping the heap as object state.
12. Putting every node of every sorted list into the heap instead of one active node per list.
13. Using `peek()` when the selected candidate must be removed; merging requires `poll()`.

## Revision checklist

- Can I explain what a heap guarantees and what it does not?
- Can I state the difference between `offer`, `peek`, and `poll`?
- Can I explain why `peek()` is `O(1)` but insertion/removal are logarithmic?
- Can I choose min-heap versus max-heap from the eviction rule?
- Can I derive why largest `k` uses a min-heap?
- Can I derive why smallest `k` uses a max-heap?
- Can I compare complex heap entries safely with `Integer.compare` / `Long.compare`?
- Can I explain why Top K Frequent is naturally a two-phase map-then-heap problem?
- Can I explain why the stream heap must persist between calls?
- Can I explain why Merge k Sorted Lists needs only one active candidate per list?
- Can I distinguish a priority candidate problem from a monotonic-stack relationship problem?
- During revision, can I derive and implement the two-heap median invariant independently?

## Interview articulation template

> I do not need full sorted order; I repeatedly need the current ___ candidate. I use a ___-heap because the root should represent ___. Each heap entry represents ___. I keep the heap at size ___, so when it grows too large I remove ___. The invariant is ___. The heap size is at most ___, making each insertion/removal `O(log ___)` and the total complexity ___.
