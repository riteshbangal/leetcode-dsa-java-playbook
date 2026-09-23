# Monotonic Stack — Problem Walkthrough

This chapter follows recognition, brute force, invariant, and implementation.
All stacks in trace tables are written bottom to top. Values and positions are
kept distinct throughout.

## 1. Next Greater Element to the Right

**Problem:** For each position, return the first strictly greater value to its
right, or -1 if none exists.

```text
Input:  [2, 1, 2, 4, 3]
Output: [4, 2, 4, -1, -1]
```

Brute force searches right from every index: O(n^2). Instead, keep indices
waiting for a greater value; their values are non-increasing.

| i | Current value | Resolved answers | Stack afterward |
| --- | --- | --- | --- |
| 0 | 2 | None | [0] |
| 1 | 1 | None | [0, 1] |
| 2 | 2 | answer[1] = 2; equal 2 at index 0 stays | [0, 2] |
| 3 | 4 | answer[2] = 4, then answer[0] = 4 | [3] |
| 4 | 3 | None | [3, 4] |

Initialize results with -1. Unresolved entries need no final cleanup.
Guard emptiness in the while condition, because repeated pops can empty the stack.

The original HashMap proposal had two issues: an entry set does not preserve
input order, and duplicate values may need different answers. For [2, 3, 2, 4],
the two occurrences of 2 need answers 3 and 4 respectively. Indices preserve
their identity.

**Implementation:** [NextGreaterElementRight.java](NextGreaterElementRight.java).
**Complexity:** O(n) time, O(n) auxiliary space.

## 2. Daily Temperatures

**Problem:** Return days until the first strictly warmer temperature. Use 0 if
none follows.

```text
Input:  [73, 74, 75, 71, 69, 72, 76, 73]
Output: [ 1,  1,  4,  2,  1,  1,  0,  0]
```

The invariant and comparison match next greater. Only the requested quantity
changes: when index j pops at current index i, write answer[j] = i - j.

| Day i | Temperature | Answers filled | Stack afterward |
| --- | --- | --- | --- |
| 0 | 73 | None | [0] |
| 1 | 74 | answer[0] = 1 | [1] |
| 2 | 75 | answer[1] = 1 | [2] |
| 3 | 71 | None | [2, 3] |
| 4 | 69 | None | [2, 3, 4] |
| 5 | 72 | answer[4] = 1; answer[3] = 2 | [2, 5] |
| 6 | 76 | answer[5] = 1; answer[2] = 4 | [6] |
| 7 | 73 | None | [6, 7] |

The learner's two peek() calls followed by pop() were valid; saving the popped
index in a variable is clearer. Java already initializes int arrays to zero.

**Implementation and notes:** [0739](../../problems/leetcode/0739-daily-temperatures/).
**Complexity:** O(n) time, O(n) auxiliary space.

## 3. Next Greater Element I

**Problem:** nums1 contains queries for distinct values in nums2. Find each
query's first greater value to its right in nums2, preserving nums1 order.

```text
nums1 = [4, 1, 2]
nums2 = [1, 3, 4, 2]
answer = [-1, 3, -1]
```

Compute next greater on nums2, storing value -> next greater value:
{1=3, 3=4, 4=-1, 2=-1}. Then look up nums1 in its own order.

The map update on a pop is:
`nextGreater.put(nums2[waitingIndex], nums2[i]);`

It does not store the current value as key and the waiting index as value.
Because every nums2 value has a default -1 entry and nums1 is a subset,
map.get(nums1[i]) is safe under the problem contract.

Distinct means unique within each array, not disjoint across the arrays.
If nums2 contained duplicates, a value alone would not identify an occurrence.
Queries would need an index or an occurrence rule. Duplicate queries in nums1
alone would be harmless when nums2 remains unique.

**Implementation and notes:** [0496](../../problems/leetcode/0496-next-greater-element-i/).
**Complexity:** expected O(n + m) time, O(n) auxiliary space, plus output.

## 4. Next Greater Element II

**Problem:** Search circularly for each position's next strictly greater value.

```text
Input:  [3, 1, 2]
Output: [-1, 2, 3]
```

The learner proposed duplicating [3, 1, 2] into [3, 1, 2, 3, 1, 2], applying
ordinary next greater, and retaining the first three answers. That is correct
and O(n). Implementation was intentionally skipped during the lesson.

The consolidated reference simulates that copy using i = step % n. Push
indices only during the first pass; the second pass resolves remaining queries.

| Step | Original index | Value | Action | Stack afterward |
| --- | --- | --- | --- | --- |
| 0 | 0 | 3 | Push 0 | [0] |
| 1 | 1 | 1 | Push 1 | [0, 1] |
| 2 | 2 | 2 | Resolve index 1 with value 2; push 2 | [0, 2] |
| 3 | 0 | 3 | Resolve index 2 with value 3 | [0] |
| 4 | 1 | 1 | No resolution; do not push again | [0] |
| 5 | 2 | 2 | No resolution; do not push again | [0] |

An equal value cannot resolve itself. If a greater value exists, its first
circular encounter is within the next n - 1 positions. Repeating values after
that cannot introduce a previously unseen greater answer.
Empty input performs zero iterations and never evaluates modulo zero.

**Implementation and notes:** [0503](../../problems/leetcode/0503-next-greater-element-ii/).
**Complexity:** O(n) time, O(n) auxiliary space.

## 5. Stock Span

**Problem:** Count consecutive days ending today whose prices are <= today's
price. Include today. The nearest earlier strictly greater price stops the span.

```text
Prices: [100, 80, 60, 70, 60, 75, 85]
Spans:  [  1,  1,  1,  2,  1,  4,  6]
```

Pop earlier prices <= current. Calculate today's answer after all pops:
i - topIndex, or i + 1 if empty. Push today afterward.

| i | Price | Popped indices | Remaining boundary | Span | Stack afterward |
| --- | --- | --- | --- | --- | --- |
| 0 | 100 | None | -1 | 1 | [0] |
| 1 | 80 | None | 0 | 1 | [0, 1] |
| 2 | 60 | None | 1 | 1 | [0, 1, 2] |
| 3 | 70 | 2 | 1 | 2 | [0, 1, 3] |
| 4 | 60 | None | 3 | 1 | [0, 1, 3, 4] |
| 5 | 75 | 4, 3 | 1 | 4 | [0, 1, 5] |
| 6 | 85 | 5, 1 | 0 | 6 | [0, 6] |

At 75, index 2 is no longer on the stack but its day still counts.
The boundary at index 1 gives 5 - 1 = 4, covering [60, 70, 60, 75].
Updating with i - poppedIndex + 1 inside while would end at 5 - 3 + 1 = 3,
missing the earlier 60. Width comes from original positions, not stored entries.

Equality matters: [50, 50, 50] gives [1, 2, 3].
The extra-60 variant discussed aloud,
[100, 80, 60, 70, 60, 60, 75, 85], gives [1, 1, 1, 2, 1, 2, 5, 7].

The batch version stores indices into a supplied prices array.
The online reference preserves a deque of (price, dayIndex) and a day counter
between next(price) calls. It returns today's span immediately. This API was
added during consolidation, not independently implemented during the session.

**Batch implementation:** [StockSpan.java](StockSpan.java).
**Online implementation and notes:** [0901](../../problems/leetcode/0901-online-stock-span/).
**Complexity:** O(n) total time and O(n) space; online calls are amortized O(1).

## 6. Next and Previous Smaller Elements

These are two separate exercises. Each uses one stack. If both result arrays
are required, run the two algorithms separately, clearing and reusing the stack.

```text
Values:                 [4,  2,  2, 5,  1]
Next smaller indices:   [1,  4,  4, 4, -1]
Previous smaller indices: [-1, -1, -1, 2, -1]
```

The requested output is an index. For the 2 at index 1, the next smaller
value is 1, located at index 4; the answer is 4. No counting or deduplication
is involved.

| i | Value | Next smaller index | Previous smaller index |
| --- | --- | --- | --- |
| 0 | 4 | 1 | -1 |
| 1 | 2 | 4 | -1 |
| 2 | 2 | 4 | -1 |
| 3 | 5 | 4 | 2 |
| 4 | 1 | -1 | -1 |

Next smaller:
- Pop when current < top value.
- Set answer[poppedIndex] = currentIndex.
- Equal values keep waiting.

Previous smaller:
- Pop when top value >= current.
- After popping, set answer[currentIndex] = remaining top or -1.
- Equal values cannot serve as strictly smaller boundaries.

**Implementations:** [NextSmallerElementRight.java](NextSmallerElementRight.java)
and [PreviousSmallerElementLeft.java](PreviousSmallerElementLeft.java).
**Complexity:** each is O(n) time and O(n) auxiliary space.

## 7. Largest Rectangle in Histogram

**Problem:** Every bar has width 1. Find the largest rectangle that fits below
all bars in a consecutive block.

```text
Input:  [2, 1, 5, 6, 2, 3]
Output: 10
```

For every pending bar, a shorter incoming bar establishes its right boundary.
Pop its index, get the height from that popped index, then read the new top as
the left boundary for this candidate.

```text
height = heights[poppedIndex]
left   = stack empty ? -1 : stack.peek()
width  = currentIndex - left - 1
area   = height * width
```

At incoming height 2, index 4:
- Pop index 3 (height 6): left = 2, width = 1, area = 6.
- Pop index 2 (height 5): left = 1, width = 2, area = 10.
- Keep index 1 (height 1), then push index 4.

An endpoint of virtual height 0 finishes positive-height candidates.
At the end, height 1 spans all six original bars even though only one index
remains just before its pop. Equal heights remain with the strict > comparison;
the earliest equal-height candidate eventually captures the full plateau.

The learner's proposed 2 * 3 rectangle across [2, 1, 5] was invalid because
the middle height is 1. A popped index is removed only from the working stack;
the original bar still constrains every rectangle covering it.

Container With Most Water uses the shorter of two selected walls and width
right - left. Histogram uses the minimum of every covered bar and the number
of bars. Fixing a height converts the histogram task to a smaller-boundary
search; it does not inherit the container's two-pointer movement proof.

**Full algorithm, twelve-row trace, equality proof, tests, and Java:**
[0084 — Largest Rectangle in Histogram](../../problems/leetcode/0084-largest-rectangle-in-histogram/).

## Final revision drill

For each group, explain:
1. What quantity is returned: value, index, distance, span, or area?
2. What does one entry represent?
3. Which comparison includes or excludes equality, and why?
4. Does a pop answer an earlier position or expose a boundary for today?
5. Why is the removed entry no longer needed?
6. What happens when the stack empties or input ends?
7. Why is total work linear?

Revisit circular traversal, the persistent online Stock Span API, and histogram
width derivation through an independent implementation. Repository reference
code is available; independent mastery has not been asserted.
