# 0084 — Largest Rectangle in Histogram

Primary pattern: [Monotonic Stack](../../../patterns/07-monotonic-stack/).
Implementation: [Solution.java](Solution.java).
Executable checks: [SolutionTest.java](SolutionTest.java).

## Problem and important clues

Given nonnegative bar heights, each with width 1, return the largest rectangle
that fits under consecutive bars. The rectangle's height cannot exceed any bar
it covers. This implementation assumes the maximum area fits in a Java int.

Example: `[2, 1, 5, 6, 2, 3]` returns `10`: indices 2 and 3 support height 5
across width 2.

For a fixed bar height, a smaller bar stops expansion. This connects the problem
to previous/next smaller boundaries.

## Brute-force approach

Enumerate each starting position, extend its ending position, and maintain the
minimum height in that interval. Area is minimum height times interval length.
This takes O(n^2) time and O(1) auxiliary space.

## Pattern decision

For each candidate height, remember its index until a shorter bar arrives.
Several taller candidates may finish at the same incoming bar. Removing the
most recent candidate exposes the next candidate: a monotonic stack fits.

Container With Most Water is different: it chooses two walls, and only those
two heights limit the container. A histogram rectangle must fit under every bar
in its interval. For `[5, 1, 5]`, the container area is 10 but the largest
histogram rectangle is 5.

## Core invariant

After each real index is pushed, stack indices increase from bottom to top,
and their heights are non-decreasing. Each stored bar has not yet encountered
a strictly shorter bar to its right.

The stack stores pending candidates. The separate maxArea variable stores the
best calculated area. A candidate is pushed regardless of whether it improves
maxArea.

## Algorithm

1. Scan indices 0 through n, where n is a virtual endpoint with height 0.
2. While the stack is nonempty and its top bar is taller than the incoming bar:
   - Pop the candidate index and read its height.
   - Read the new stack top as left, or use -1 when the stack is empty.
   - The right boundary is the current index i.
   - Calculate width = i - left - 1 and update maxArea with height * width.
3. Push i if it is a real index (i < n).
4. Return maxArea.

The expression choosing the virtual height never reads heights[n]. Positive
heights remaining at the end are processed by the virtual zero. Zero-height
entries may remain; their area is zero and cannot improve the answer.

## Equal heights

The strict pop comparison `>` leaves equal heights on the stack. Therefore,
the new top after a pop can have equal height; it is not always a strictly
smaller boundary. The width computed for a later equal-height entry may be
narrower than the full plateau. When the leftmost equal-height entry is
eventually popped, it captures the plateau's full width.

For `[2, 2, 2]`, the final flush calculates widths 1, 2, and 3, producing
areas 2, 4, and 6. Do not describe every popped candidate's computed width as
its globally widest possible width when equal heights remain on the stack.

## Step-by-step trace

Input: `[2, 1, 5, 6, 2, 3]`. Stack entries below are indices, bottom to top.
Area is calculated only on pop rows.

| i | Incoming height | Action | Rectangle height | Width calculation | Area | Best | Stack afterward |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 0 | 2 | Push 0 | — | — | — | 0 | [0] |
| 1 | 1 | Pop 0 | 2 | 1 - (-1) - 1 = 1 | 2 | 2 | [] |
| 1 | 1 | Push 1 | — | — | — | 2 | [1] |
| 2 | 5 | Push 2 | — | — | — | 2 | [1, 2] |
| 3 | 6 | Push 3 | — | — | — | 2 | [1, 2, 3] |
| 4 | 2 | Pop 3 | 6 | 4 - 2 - 1 = 1 | 6 | 6 | [1, 2] |
| 4 | 2 | Pop 2 | 5 | 4 - 1 - 1 = 2 | 10 | 10 | [1] |
| 4 | 2 | Push 4 | — | — | — | 10 | [1, 4] |
| 5 | 3 | Push 5 | — | — | — | 10 | [1, 4, 5] |
| 6 | 0 (virtual) | Pop 5 | 3 | 6 - 4 - 1 = 1 | 3 | 10 | [1, 4] |
| 6 | 0 (virtual) | Pop 4 | 2 | 6 - 1 - 1 = 4 | 8 | 10 | [1] |
| 6 | 0 (virtual) | Pop 1 | 1 | 6 - (-1) - 1 = 6 | 6 | 10 | [] |

## Correctness reasoning

A popped candidate of height h covers indices left + 1 through i - 1. All
bars in that interval are at least h, so the calculated rectangle is valid.
The current bar is strictly shorter and cannot be included at height h.

Consider any optimal interval and its minimum height h. Expand through all
adjacent bars at least h. The leftmost bar of height h in that expanded block
eventually pops when the right shorter boundary (or the endpoint) is reached.
At that moment no equal-height bar remains to its left inside the block, so
the full block is evaluated. Thus at least one candidate attains the optimum.
All-zero histograms have optimum zero, already represented by maxArea.

## Edge cases

- Empty input returns 0.
- A single bar returns its height.
- Increasing heights require the endpoint flush.
- Decreasing heights cause pops during the scan.
- Equal heights must combine into wider rectangles.
- Zero-height bars separate positive-height regions.
- The input is not modified.

## Mistakes and lessons from this session

- Width is an original-index distance, never stack.size().
- Rectangle height comes from the popped bar, not the incoming bar.
- Read the left boundary after popping.
- Popping an index does not remove its bar from the original histogram.
- A rectangle over [2, 1, 5] has height at most 1.
- Push the current index after resolving all taller candidates.
- Always guard peek() with an emptiness check.
- Include the endpoint processing; otherwise increasing suffixes remain pending.

## Final complexity

Each real index is pushed once and popped at most once. Total time is O(n),
despite the inner while loop. Auxiliary stack space is O(n).

## Validation

From this problem directory, with a JDK installed:

```sh
javac Solution.java SolutionTest.java
java SolutionTest
```

Expected output: `Passed 21858 checks.`

The checks include 12 named cases, every array of lengths 0 through 7 with
heights from 0 through 3 against a brute-force oracle (21,845 inputs), and a
100,000-bar plateau. Every check also verifies that the input remains unchanged.
The suite uses explicit AssertionError checks; no -ea flag is required.

## Revision prompts

- What information does one stack entry represent?
- Why does a shorter arrival let us calculate areas?
- Why is width i - left - 1 rather than stack size?
- What happens to equal heights with the strict pop comparison?
- What does the virtual zero do?
