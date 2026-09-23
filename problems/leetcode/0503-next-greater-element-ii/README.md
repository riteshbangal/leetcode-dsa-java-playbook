# 0503 — Next Greater Element II

Primary pattern: [Monotonic Stack](../../../patterns/07-monotonic-stack/).
Session status: Circular reasoning completed; learner skipped coding; reference added during consolidation.

## Problem and clues

For each position, find its first strictly greater value when moving right
circularly. After the last index, continue at index 0. Return -1 if no greater
value exists. Duplicates are allowed.

```text
[3, 1, 2] -> [-1, 2, 3]
[1, 2, 1] -> [2, -1, 2]
```

## Brute force and the learner's approach

A direct circular search checks up to n - 1 later positions for each index:
O(n^2). The learner proposed doubling the array, running ordinary next greater,
and retaining the first n answers. That proposal is correct and O(n) time with
O(n) extra space.

The reference uses the same idea without allocating the copy.

## Invariant

Each pending original index is pushed once. Its answer is still unknown.
Pending values are non-increasing. After the first pass, every remaining index
has no strictly greater value in its ordinary suffix; the second pass exposes
the beginning of the array.

## Algorithm and Java

Initialize answers to -1. Scan steps 0 through 2n - 1, using i = step % n.
Pop pending indices with values smaller than nums[i] and assign nums[i].
Push i only if step < n.

[Solution.java](Solution.java)

## Worked reasoning

For [3, 1, 2], the first pass resolves 1 with 2 and leaves indices [0, 2].
The next virtual arrival is 3 at original index 0, resolving index 2 with 3.
Index 0 stays unresolved, so the answer is [-1, 2, 3].

The repeated copy of an element is equal to itself and cannot be its strictly
greater answer. If a greater value exists, it appears within the next n - 1
circular positions.

## Edge cases and lessons

- Empty input executes no loop, avoiding modulo zero.
- One value returns [-1].
- All equal values remain -1.
- A decreasing array requires wrapping for every position except the maximum.
- Physical doubling is linear, but the virtual scan avoids the extra copy.
- Store answers by original index; duplicate values can have different answers.
- The second pass does not need to push another copy of each index.

## Complexity

O(n) time: 2n visits, at most n pushes and n pops. O(n) auxiliary stack space.

## Revision prompts

Why are two passes enough? How does modulo repeat indices? Why do we push only
during the first pass? Why is a repeated equal value not a valid answer?

## Validation

Run the full chapter checks from the repository root:

```sh
python3 patterns/07-monotonic-stack/test_solutions.py
```

The runner compiles this problem separately and compares it with a brute-force
oracle on small inputs. Input arrays are checked for unintended mutation.
