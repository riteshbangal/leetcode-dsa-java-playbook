# Pattern 07 — Monotonic Stack

Workflow: Clues -> Pattern -> Invariant -> Implementation.

## Status

This chapter has been studied through the histogram problem. This commit records
the histogram implementation and walkthrough. Full chapter code consolidation
and independent revision are still pending; the chapter is not marked completed.

## Recognition clues

- Find the next or previous greater/smaller element.
- Find a boundary where a candidate stops extending.
- A new value can permanently resolve or eliminate several recent candidates.

Store indices when positions, distances, or widths matter. Values can be read
from the original array through those indices. Stack entries must have a clear
meaning: unresolved answers or candidate boundaries, depending on the problem.

## Core invariants and Java tools

Use `Deque<Integer> stack = new ArrayDeque<>();` for a stack of indices.
Check `!stack.isEmpty()` before `peek()`. Define the required value order from
bottom to top, and decide explicitly whether equality should cause a pop.

Next greater to the right keeps unresolved indices with non-increasing values.
Next smaller to the right keeps unresolved indices with non-decreasing values.
Previous smaller removes greater-or-equal candidates and then reads the remaining
top as the current position's answer. Histogram uses non-decreasing pending
heights and calculates candidate areas when a shorter bar arrives.

## Related problem recorded

- [0084 — Largest Rectangle in Histogram](../../problems/leetcode/0084-largest-rectangle-in-histogram/):
  full algorithm, Java solution, trace table, equality handling, and checks.

## Session coverage for later consolidation

1. Next Greater Element to the Right.
2. Daily Temperatures: answer with an index distance.
3. Next Greater Element I: map unique values to their next greater values.
4. Next Greater Element II: doubled-array reasoning; implementation skipped.
5. Stock Span: previous greater boundary; batch version discussed and corrected.
   The persistent online API still needs a separate implementation.
6. Next/Previous Smaller Element: two distinct boundary exercises.
7. Largest Rectangle in Histogram: pending candidates, index-based widths,
   and a virtual endpoint.

## Typical complexity

Each index is pushed once and popped at most once per pass: O(n) total time
and O(n) worst-case auxiliary space. One iteration may pop many entries; the
total number of pops across the scan is still bounded by n.

## Common mistakes

- Treating stack size as a distance or rectangle width.
- Confusing the index on the stack with the value at that index.
- Returning a value when an index was requested.
- Using a value-keyed map when duplicate occurrences need different answers.
- Forgetting equal-value behavior or the end-of-input processing.
- Assuming the top after popping is strictly smaller when equal heights are kept.
