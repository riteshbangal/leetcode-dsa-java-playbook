# Pattern 07 — Monotonic Stack

Workflow: **Clues -> Pattern -> Invariant -> Implementation**.

A monotonic stack keeps selected entries ordered by value. That order lets a new element resolve waiting answers or eliminate unsuitable boundary candidates using only the top.

## Chapter status

**Completed: preliminary coverage and reference-code consolidation.**
Independent recall and reimplementation remain revision work. Completion here does not mean every exercise was independently coded during the session.

| Group | Session coverage | Repository status |
| --- | --- | --- |
| Next Greater Element to the Right | Handwritten implementation reviewed; empty-stack guard corrected | Implemented |
| Daily Temperatures | Handwritten implementation reviewed | Implemented |
| Next Greater Element I | Handwritten implementation reviewed; map key/value corrected | Implemented |
| Next Greater Element II | Doubled-array reasoning accepted; learner implementation skipped | Virtual two-pass reference added during consolidation |
| Stock Span | Batch implementation attempted and corrected | Batch and persistent online reference implementations |
| Next/Previous Smaller Element | Both variants discussed; no handwritten implementation submitted | Both reference implementations |
| Largest Rectangle in Histogram | Guided diagrams, trace, pseudocode, and Java | Implemented |

Start with [problem-walkthrough.md](problem-walkthrough.md) for the session examples.

## Recognition clues

Look for a combination of:

- For every position, find the first/nearest greater or smaller element.
- Original order matters: next, previous, left, right, or circular.
- A candidate can wait for a future answer.
- A new value can permanently resolve several recent candidates.
- A smaller or greater boundary stops an interval from extending.

The words "maximum" or "greater" alone are insufficient. Finding the largest value anywhere to the right can use a running maximum. Finding the first greater value requires preserving proximity.

For histogram rectangles, fix a bar's height and ask where smaller bars stop expansion on each side. This turns an area problem into a boundary problem.

## What does one entry represent?

| Family | Meaning of an entry | What a pop accomplishes |
| --- | --- | --- |
| Next greater/smaller | An index whose answer is still unknown | The current element supplies that earlier position's answer |
| Previous greater/smaller | An earlier candidate boundary | Removes a candidate that cannot answer the current position or be needed later |
| Stock Span | A possible previous strictly higher boundary | Exposes the boundary that stops today's consecutive span |
| Histogram | An index awaiting a strictly shorter right boundary | Evaluates a rectangle at the popped bar's height |

The stack is not the answer array, a sorted copy of all input values, or a collection whose size is automatically the desired distance.

## Java representation

Use `Deque<Integer> stack = new ArrayDeque<>();` for indices.
Then `stack.peek()` is an index, and `nums[stack.peek()]` is a value.

- `push(i)` adds a pending index.
- `peek()` inspects without removing.
- `pop()` returns and removes the top.
- Guard top access with `!stack.isEmpty()` inside a repeated-pop loop.
- `Deque` takes one element type. A pair is an object or array stored as that one type.
- Java initializes an int array to zero. Use `Arrays.fill(answer, -1)` when the required missing-answer marker is -1.

For the online API, a persistent stack stores `Entry(price, dayIndex)`. There is no input array to look back into and no fixed-length answer array.

## Comparison rules: left-to-right scans

| Target | Pop while top value... | Order after pushing, bottom to top | Answer timing |
| --- | --- | --- | --- |
| Next strictly greater | < current | Non-increasing | Assign popped position inside while |
| Next strictly smaller | > current | Non-decreasing | Assign popped position inside while |
| Previous strictly greater | <= current | Strictly decreasing | Read remaining top after while |
| Previous strictly smaller | >= current | Strictly increasing | Read remaining top after while |
| Histogram reference | > current | Non-decreasing | Calculate candidate area on each pop |

Do not memorize a comparison without its scan direction and entry meaning.

## Two reusable algorithm shapes

### Resolve earlier positions using the current value

```java
while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
    int waitingIndex = stack.pop();
    answer[waitingIndex] = nums[i];
}
stack.push(i);
```

The assigned result could instead be an index or a distance. The problem statement decides which quantity to return.

### Answer the current position from a surviving boundary

```java
while (!stack.isEmpty() && nums[stack.peek()] >= nums[i]) {
    stack.pop();
}
answer[i] = stack.isEmpty() ? -1 : stack.peek();
stack.push(i);
```

## Implementations

Pattern 07 is self-contained: the Java implementations used while learning this pattern live directly in this folder.

| Group | Output | Implementation |
| --- | --- | --- |
| Next Greater Element to the Right | Next greater values, or -1 | [NextGreaterElementRight.java](NextGreaterElementRight.java) |
| Daily Temperatures | Days until warmer, or 0 | [DailyTemperatures.java](DailyTemperatures.java) |
| Next Greater Element I | Answers in nums1 order | [NextGreaterElementI.java](NextGreaterElementI.java) |
| Next Greater Element II | Circular next greater values | [NextGreaterElementII.java](NextGreaterElementII.java) |
| Batch Stock Span | Consecutive spans | [StockSpan.java](StockSpan.java) |
| Online Stock Span | One span per `next(price)` call | [StockSpanner.java](StockSpanner.java) |
| Next Smaller Element | Indices to the right, or -1 | [NextSmallerElementRight.java](NextSmallerElementRight.java) |
| Previous Smaller Element | Indices to the left, or -1 | [PreviousSmallerElementLeft.java](PreviousSmallerElementLeft.java) |
| Largest Rectangle in Histogram | Maximum area | [LargestRectangleInHistogram.java](LargestRectangleInHistogram.java) |

## Core invariants and permanent removal

For next greater, pending values are non-increasing. If the current value cannot beat the top, it cannot beat any value beneath it. If it can, scanning in original order ensures this is the first greater value for every index it resolves.

For previous smaller, candidates are strictly increasing after pushing. A popped value is at least the current value, so it cannot be the current answer. For a future position, the current value is closer and no larger, making the removed candidate unnecessary.

For Stock Span, candidate prices are strictly decreasing. Equal prices are included in the span, so only a strictly higher price stops it.

For histogram, heights remain non-decreasing. On a pop, all bars strictly between the new top index and the current index are at least as tall as the popped bar. That makes the candidate rectangle valid.

## Width and distance calculations

| Requirement | Calculation |
| --- | --- |
| Days from earlier index j to current index i | i - j |
| Stock span after previous higher boundary j | i - j |
| Stock span with no previous higher boundary | i + 1 |
| Rectangle between excluded boundaries L and R | R - L - 1 |
| Histogram height on a pop | heights[poppedIndex] |

Do not use `stack.size()` as a width. Removed entries still represent positions in the original array.

## Complexity

Each index is pushed at most once and popped at most once per pass. Total stack work is O(n), even if a single arrival causes many pops.

- Generic next/previous queries, Daily Temperatures, batch Stock Span, and histogram: O(n) time and O(n) auxiliary space.
- Circular next greater: two linear scans, still O(n) time and O(n) space.
- Next Greater Element I: expected O(n + m) time for nums2 length n and nums1 length m; O(n) auxiliary space, plus O(m) output.
- Online Stock Span: amortized O(1) per call, O(n) total over n calls, and O(n) worst-case space. One individual call can take O(n).

## Common mistakes from this chapter

1. Checking only the immediately next value instead of continuing to the first qualifying value farther right.
2. Treating "first greater" as "largest anywhere to the right."
3. Using value -> answer when duplicates can have different answers.
4. Confusing value, index, distance, span, and area.
5. Checking emptiness once before `while`; popping can empty the stack inside it.
6. Pushing the current index inside `while` or forgetting to push after zero pops.
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

The script compiles each Pattern 07 implementation into a separate temporary directory to avoid class collisions, executes fixed examples and brute-force comparisons for small inputs, verifies inputs remain unchanged, and runs the histogram regression suite. No generated class files are committed.

## Revision prompts

- Explain the entry meaning before naming the push/pop operations.
- Derive the pop condition and equality rule for each direction.
- Explain why remaining candidates can be skipped below the top.
- Identify whether the answer belongs to the popped index or current index.
- Explain when a value-keyed HashMap is valid.
- Explain circular traversal without duplicating the array.
- Reimplement the persistent online `StockSpanner` independently.
- Derive `i - left - 1` from the two excluded histogram boundaries.
- Trace equal heights and the virtual endpoint.
- Explain the amortized bound without saying "the stack has fewer than n items."

## Interview articulation

> The stack stores ___. Its values remain ___ from bottom to top. When ___ happens, the top is permanently resolved or dominated, so I pop it. The answer uses ___ from the original indices. Each index is pushed and popped at most once per pass, giving ___ time and ___ auxiliary space.
