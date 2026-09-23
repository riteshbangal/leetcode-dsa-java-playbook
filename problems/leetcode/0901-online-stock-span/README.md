# 0901 — Online Stock Span

Primary pattern: [Monotonic Stack](../../../patterns/07-monotonic-stack/).
Session status: Batch reasoning and handwritten corrections covered; persistent API reference added during consolidation.

## Problem and clues

Implement StockSpanner with next(int price). Each call represents the next day
and immediately returns the number of consecutive days ending today whose
prices are <= today's price. Include today and stop at a strictly higher price.

```text
Calls:   next(100), next(80), next(60), next(70), next(60), next(75), next(85)
Returns: 1,         1,        1,        2,        1,        4,        6
```

## Brute force and pattern decision

Keep all prior prices and scan backward for every call: O(n^2) total in the
worst case. A monotonic stack removes candidates that cannot be today's
strictly greater boundary.

## Invariant

Entries contain (price, dayIndex). Day indices increase and prices strictly
decrease from bottom to top. State belongs to one StockSpanner instance and
persists across next calls.

## Algorithm and Java

Increment the day counter. Pop while top.price <= price. The surviving top's
day is the nearest earlier strictly higher boundary, or -1 if empty.
Return day - boundary after pushing the new (price, day) entry.

[StockSpanner.java](StockSpanner.java) uses the required design-problem class
name instead of Solution. The batch counterpart is
[StockSpan.java](../../../patterns/07-monotonic-stack/StockSpan.java).

## Worked reasoning

Before price 75, candidate prices and days are (100,0), (80,1), (70,3), (60,4).
Pop (60,4) and (70,3). Boundary (80,1) remains, so today's span is 5 - 1 = 4.
The earlier 60 at day 2 has already been popped but is included by that distance.

The online version stores each candidate price with its day because it has no
supplied array from which an index alone could retrieve the old price.
Its constructor starts a fresh stream. Independent instances must not share
stacks or day counters.

## Edge cases and lessons

- First call always returns 1.
- [50, 50, 50] produces [1, 2, 3]; equality must be popped.
- Decreasing prices produce span 1 each time.
- A new maximum clears all candidates and returns day + 1.
- Recreating the stack inside next would lose history.
- Deque has one element type; use a named Entry for the pair.
- Span comes from the surviving boundary after all pops, not the last popped
  index or the number of popped entries.
- This code is a consolidation reference, not evidence that the online API was
  independently implemented during the lesson.

## Complexity

Across n calls: O(n) total time and O(n) worst-case space.
Each call is amortized O(1); one call can pop O(n) entries.

## Revision prompts

What state must persist between calls? Why do equal prices extend the span?
Why is a pair useful online? Why does the surviving boundary give the full span?

## Validation

Run the full chapter checks from the repository root:

```sh
python3 patterns/07-monotonic-stack/test_solutions.py
```

The runner compiles this problem separately and compares it with a brute-force
oracle on small inputs. Input arrays are checked for unintended mutation.
