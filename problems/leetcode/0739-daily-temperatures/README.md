# 0739 — Daily Temperatures

Primary pattern: [Monotonic Stack](../../../patterns/07-monotonic-stack/).
Session status: Handwritten solution reviewed; reference consolidated; independent revision remains useful.

## Problem and clues

For each day, return the number of days until the first strictly warmer
temperature. Use 0 if none follows. Equal temperatures are not warmer.

```text
Input:  [73, 74, 75, 71, 69, 72, 76, 73]
Output: [ 1,  1,  4,  2,  1,  1,  0,  0]
```

## Brute force and pattern decision

Scan ahead from every day: O(n^2). The first-greater-to-the-right requirement
suggests a monotonic stack. A new warmer day can resolve several earlier days.

## Invariant

The stack holds unresolved day indices with non-increasing temperatures.
Indices increase from bottom to top. Every resolved day has its first warmer
day recorded.

## Algorithm and Java

Initialize a zero-filled answer array and an empty index stack.
For each day i, pop while temperatures[i] > temperatures[top]. For each popped
day j, assign answer[j] = i - j. Then push i.

The current index gives the right endpoint; the popped index gives the day
whose answer is being filled. Do not store the temperature difference.

[Solution.java](Solution.java)

## Worked reasoning

Day 2 has temperature 75. Days 3, 4, and 5 have 71, 69, and 72, so it remains
pending. Day 6 has 76 and resolves it: 6 - 2 = 4 days.
The same arrival resolves day 5 first with a distance of 1.

See the [complete chapter trace](../../../patterns/07-monotonic-stack/problem-walkthrough.md#2-daily-temperatures).

## Edge cases and lessons

- [70, 70] returns [0, 0].
- [70, 70, 71] returns [2, 1, 0].
- Decreasing temperatures leave all answers zero.
- One day has answer zero.
- Recheck stack emptiness during each while condition.
- Two peek() calls before a pop see the same index; storing the popped index
  once makes the answer calculation clearer.

## Complexity

O(n) time and O(n) auxiliary space. Each index enters and leaves at most once.

## Revision prompts

Why do we store indices? Why must equality not pop? Why is a nested while still
linear? Which day owns the answer being written?

## Validation

Run the full chapter checks from the repository root:

```sh
python3 patterns/07-monotonic-stack/test_solutions.py
```

The runner compiles this problem separately and compares it with a brute-force
oracle on small inputs. Input arrays are checked for unintended mutation.
