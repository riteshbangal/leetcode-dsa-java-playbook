# 0496 — Next Greater Element I

Primary pattern: [Monotonic Stack](../../../patterns/07-monotonic-stack/).
Session status: Handwritten map construction and result lookup reviewed and corrected.

## Problem and clues

nums2 contains distinct values. nums1 contains distinct queries drawn from
nums2. For each query, return its first strictly greater value to the right in
nums2, or -1. Preserve nums1 order.

```text
nums1 = [4, 1, 2]
nums2 = [1, 3, 4, 2]
answer = [-1, 3, -1]
```

## Brute force and pattern decision

Locate each query in nums2 and scan right: O(mn) for m queries and n source
values. Compute all next-greater answers once with a monotonic stack and store
a value-to-answer map for quick queries.

## Invariant

Pending nums2 indices have non-increasing values. The map contains -1 for each
seen unresolved value and its next greater value once resolved.

## Algorithm and Java

For each nums2 index i:
1. Initialize nextGreater[nums2[i]] to -1.
2. Pop smaller pending values and store nums2[poppedIndex] -> nums2[i].
3. Push i.

Then iterate nums1 and retrieve each answer into the same output position.

[Solution.java](Solution.java)

## Worked reasoning

For [1, 3, 4, 2], arriving 3 resolves 1; arriving 4 resolves 3. Values 4 and 2
never find a greater value. The map is {1=3, 3=4, 4=-1, 2=-1}.
Queries [4, 1, 2] therefore return [-1, 3, -1].

## Distinct values and map safety

Distinct means no repetition within nums2. It does not mean nums1 and nums2
are disjoint. This uniqueness is why one value can identify one answer.

For a generalized source [2, 3, 2, 4], the two 2s have different answers. A
value-keyed map cannot identify both. Such queries need positions or an explicit
occurrence rule. Repeated queries are harmless if the source remains unique.

## Edge cases and lessons

- One source value yields -1 for that query.
- Descending nums2 leaves every map answer at -1.
- Query order can differ from source order.
- Return the answer array, not the map's entry set.
- The key is the waiting value, not the arriving value or a waiting index.
- map.get is safe here because every source value was initialized and every
  query belongs to the source, under the problem contract.

## Complexity

Expected O(n + m) time with HashMap, O(n) auxiliary space, and O(m) output.

## Revision prompts

Why scan nums2? What exactly is the map key? Why does uniqueness matter?
Why should iteration over the map not determine answer order?

## Validation

Run the full chapter checks from the repository root:

```sh
python3 patterns/07-monotonic-stack/test_solutions.py
```

The runner compiles this problem separately and compares it with a brute-force
oracle on small inputs. Input arrays are checked for unintended mutation.
