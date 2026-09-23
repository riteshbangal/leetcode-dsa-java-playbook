# Pattern 07 — Monotonic Stack

Workflow: **Clues -> Pattern -> Invariant -> Implementation**.

A monotonic stack keeps selected entries ordered by value. That order lets a
new element resolve waiting answers or eliminate unsuitable boundary candidates
using only the top.

## Chapter status

**Completed: preliminary coverage and reference-code consolidation.**
Independent recall and reimplementation remain revision work. Completion here
does not mean every exercise was independently coded during the session.

| Group | Session coverage | Repository status |
| --- | --- | --- |
| Next Greater Element to the Right | Handwritten implementation reviewed; empty-stack guard corrected | Implemented |
| Daily Temperatures | Handwritten implementation reviewed | Implemented |
| Next Greater Element I | Handwritten implementation reviewed; map key/value corrected | Implemented |
| Next Greater Element II | Doubled-array reasoning accepted; learner implementation skipped | Virtual two-pass reference added during consolidation |
| Stock Span | Batch implementation attempted and corrected | Batch and persistent online reference implementations |
| Next/Previous Smaller Element | Both variants discussed; no handwritten implementation submitted | Both reference implementations |
| Largest Rectangle in Histogram | Guided diagrams, trace, pseudocode, and Java | Previously merged solution retained |

Start with [problem-walkthrough.md](problem-walkthrough.md) for the session examples.
The next pattern is **08 — Linked List Pointers**; see the [roadmap](../../ROADMAP.md).

## Recognition clues

Look for a combination of:

- For every position, find the first/nearest greater or smaller element.
- Original order matters: next, previous, left, right, or circular.
- A candidate can wait for a future answer.
- A new value can permanently resolve several recent candidates.
- A smaller or greater boundary stops an interval from extending.

The words "maximum" or "greater" alone are insufficient. Finding the largest
value anywhere to the right can use a running maximum. Finding the first greater
value requires preserving proximity.

For histogram rectangles, fix a bar's height and ask where smaller bars stop
expansion on each side. This turns an area problem into a boundary problem.

## What does one entry represent?

| Family | Meaning of an entry | What a pop accomplishes |
| --- | --- | --- |
| Next greater/smaller | An index whose answer is still unknown | The current element supplies that earlier position's answer |
| Previous greater/smaller | An earlier candidate boundary | Removes a candidate that cannot answer the current position or be needed ahead of the current one later |
| Stock Span | A possible previous strictly higher boundary | Exposes the boundary that stops today's consecutive span |
| Histogram | An index awaiting a strictly shorter right boundary | Evaluates a rectangle at the popped bar's height |

The stack is not the answer array, a sorted copy of all input values, or a
collection whose size is automatically the desired distance.

## Java representation

Use `Deque<Integer> stack = new ArrayDeque<>();` for indices.
Then `stack.peek()` is an index, and `nums[stack.peek()]` is a value.

- `push(i)` adds a pending index.
- `peek()` inspects without removing.
- `pop()` returns and removes the top.
- Guard top access with `!stack.isEmpty()` inside a repeated-pop loop.
- `Deque` takes one element type. A pair is an object or array stored as that
  one type, not `Deque<Integer, Integer>`.
- Java initializes an int array to zero. Use `Arrays.fill(answer, -1)` when the
  required missing-answer marker is -1.

For the online API, a persistent stack stores Entry(price, dayIndex). There is
no input array to look back into and no fixed-length answer array.

## Comparison rules: left-to-right scans

All comparisons below use the original values at stored indices.

| Target | Pop while top value... | Order after pushing, bottom to top | Answer timing |
| --- | --- | --- | --- |
| Next strictly greater | < current | Non-increasing | Assign popped position inside while |
| Next strictly smaller | > current | Non-decreasing | Assign popped position inside while |
| Previous strictly greater | <= current | Strictly decreasing | Read remaining top after while |
| Previous strictly smaller | >= current | Strictly increasing | Read remaining top after while |
| Histogram reference | > current | Non-decreasing | Calculate candidate area on each pop |

Do not memorize a comparison without its scan direction and entry meaning.
For a next-strictly-smaller query, equal values stay unresolved. For a
previous-strictly-smaller query, equal values are removed because they cannot
be the current position's answer and the current equal value is closer for
future queries.

## Two reusable algorithm shapes

### Resolve earlier positions using the current value

```java
while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
    int waitingIndex = stack.pop();
    answer[waitingIndex] = nums[i]; // value requested by this problem
}
stack.push(i);
```

The assigned result could instead be i (a position) or i - waitingIndex (a
distance). The problem statement decides which quantity to return.

### Answer the current position from a surviving boundary

```java
while (!stack.isEmpty() && nums[stack.peek()] >= nums[i]) {
    stack.pop();
}
answer[i] = stack.isEmpty() ? -1 : stack.peek(); // previous smaller index
stack.push(i);
```

The current index is pushed after calculating its answer.

## Implementations

Canonical LeetCode problems keep one implementation in their own problem
folder. Generic exercises and the batch Stock Span variant live here.

| Group | Output | Implementation | Notes |
| --- | --- | --- | --- |
| Next Greater Element to the Right | Next greater values, or -1 | [NextGreaterElementRight.java](NextGreaterElementRight.java) | [Walkthrough](problem-walkthrough.md#1-next-greater-element-to-the-right) |
| Daily Temperatures | Days until warmer, or 0 | [Solution.java](../../problems/leetcode/0739-daily-temperatures/Solution.java) | [Problem notes](../../problems/leetcode/0739-daily-temperatures/README.md) |
| Next Greater Element I | Answers in nums1 order | [Solution.java](../../problems/leetcode/0496-next-greater-element-i/Solution.java) | [Problem notes](../../problems/leetcode/0496-next-greater-element-i/README.md) |
| Next Greater Element II | Circular next greater values | [Solution.java](../../problems/leetcode/0503-next-greater-element-ii/Solution.java) | [Problem notes](../../problems/leetcode/0503-next-greater-element-ii/README.md) |
| Batch Stock Span | Consecutive spans | [StockSpan.java](StockSpan.java) | [Walkthrough](problem-walkthrough.md#5-stock-span) |
| Online Stock Span | One span per next(price) call | [StockSpanner.java](../../problems/leetcode/0901-online-stock-span/StockSpanner.java) | [Problem notes](../../problems/leetcode/0901-online-stock-span/README.md) |
| Next Smaller Element | Indices to the right, or -1 | [NextSmallerElementRight.java](NextSmallerElementRight.java) | [Walkthrough](problem-walkthrough.md#6-next-and-previous-smaller-elements) |
| Previous Smaller Element | Indices to the left, or -1 | [PreviousSmallerElementLeft.java](PreviousSmallerElementLeft.java) | [Walkthrough](problem-walkthrough.md#6-next-and-previous-smaller-elements) |
| Largest Rectangle in Histogram | Maximum area | [Solution.java](../../problems/leetcode/0084-largest-rectangle-in-histogram/Solution.java) | [Full trace and notes](../../problems/leetcode/0084-largest-rectangle-in-histogram/README.md) |

## Core invariants and permanent removal

For next greater, pending values are non-increasing. If the current value cannot
beat the top, it cannot beat any value beneath it. If it can, scanning in original
order ensures this is the first greater value for every index it resolves.

For previous smaller, candidates are strictly increasing after pushing. A popped
value is at least the current value, so it cannot be the current answer. For a
future position, the current value is closer and no larger, making the removed
candidate unnecessary.

For Stock Span, candidate prices are strictly decreasing. Equal prices are
included in the span, so only a strictly higher price stops it.

For histogram, heights remain non-decreasing. On a pop, all bars strictly between
the new top index and the current index are at least as tall as the popped bar.
That makes the candidate rectangle valid. Equal heights can remain; the
leftmost equal-height entry eventually accounts for the full plateau width.

## Width and distance calculations

| Requirement | Calculation |
| --- | --- |
| Days from earlier index j to current index i | i - j |
| Stock span after previous higher boundary j | i - j |
| Stock span with no previous higher boundary | i + 1 |
| Rectangle between excluded boundaries L and R | R - L - 1 |
| Histogram height on a pop | heights[poppedIndex] |

Do not use stack.size() as a width. Removed entries still represent positions in
the original array. Read the left boundary after the pop, not before it.

## Complexity

Each index is pushed at most once and popped at most once per pass. Total stack
work is O(n), even if a single arrival causes many pops.

- Generic next/previous queries, Daily Temperatures, batch Stock Span, and
  histogram: O(n) time and O(n) auxiliary space.
- Circular next greater: two linear scans, still O(n) time and O(n) space.
- Next Greater Element I: expected O(n + m) time for nums2 length n and nums1
  length m; O(n) auxiliary space, plus O(m) output.
- Online Stock Span: amortized O(1) per call, O(n) total over n calls, and O(n)
  worst-case space. One individual call can take O(n).

## Common mistakes from this chapter

1. Checking only the immediately next value instead of continuing to the first
   qualifying value farther right.
2. Treating "first greater" as "largest anywhere to the right."
3. Using value -> answer when duplicates can have different answers.
4. Confusing value, index, distance, span, and area.
5. Checking emptiness once before while; popping can empty the stack inside it.
6. Pushing the current index inside while or forgetting to push after zero pops.
7. Comparing an incoming price with a stored index instead of its price.
8. Calculating stock span from a popped index rather than the surviving boundary.
9. Forgetting equality when counting stock spans.
10. Using stack size as histogram width or the incoming height as rectangle height.
11. Omitting the histogram endpoint flush.
12. Claiming nested loops are quadratic without counting total pushes and pops.

## Validation

From the repository root:

```sh
python3 patterns/07-monotonic-stack/test_solutions.py
```

Requires Python 3 and JDK 9 or newer supporting Java 8 compilation. The script compiles
each standalone problem into a separate temporary directory to avoid collisions
between their Solution classes. It executes fixed examples and brute-force
comparisons for small inputs, verifies inputs remain unchanged, and runs the
existing histogram suite. No generated class files are committed.

Validated on 2026-09-23: all nine implementations compiled with Java 8 targeting
and -Xlint:all; **31,278 checks passed**. This includes exhaustive small-array
comparisons, query-order permutations, interleaved independent online streams,
and the existing histogram regressions.

## Revision prompts

- Explain the entry meaning before naming the push/pop operations.
- Derive the pop condition and equality rule for each direction.
- Explain why remaining candidates can be skipped below the top.
- Identify whether the answer belongs to the popped index or current index.
- Explain when a value-keyed HashMap is valid.
- Explain circular traversal without duplicating the array.
- Reimplement the persistent online StockSpanner independently.
- Derive i - left - 1 from the two excluded histogram boundaries.
- Trace equal heights and the virtual endpoint.
- Explain the amortized bound without saying "the stack has fewer than n items."

## Interview articulation

> The stack stores ___. Its values remain ___ from bottom to top. When ___
> happens, the top is permanently resolved or dominated, so I pop it. The answer
> uses ___ from the original indices. Each index is pushed and popped at most
> once per pass, giving ___ time and ___ auxiliary space.
