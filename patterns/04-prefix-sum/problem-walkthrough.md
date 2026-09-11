# Prefix Sum — Problem Walkthrough

Use this file for revision after attempting the problems independently.

```text
Recognition clues
    -> prefix meaning
    -> mathematical relationship
    -> required stored state
    -> initialization and update order
    -> invariant
    -> correctness
    -> complexity
    -> interview articulation
```

## Implemented Files

- [NumArray.java](./NumArray.java)
- [FindPivotIndex.java](./FindPivotIndex.java)
- [SubarraySumEqualsK.java](./SubarraySumEqualsK.java)
- [SubarraySumEqualsKPrefixArray.java](./SubarraySumEqualsKPrefixArray.java)
- [ContiguousArray.java](./ContiguousArray.java)
- [SubarraySumsDivisibleByK.java](./SubarraySumsDivisibleByK.java)

---

## 1. Range Sum Query — Immutable (LeetCode 303)

### Problem

Given an integer array that will not change, construct an object that can answer many queries of the form:

```text
sumRange(left, right)
```

Each query returns the sum of all elements from index `left` through index `right`, inclusive.

Example:

```text
nums = [-2, 0, 3, -5, 2, -1]

sumRange(0, 2) = 1
sumRange(2, 5) = -1
sumRange(0, 5) = -3
```

### Recognition

The strong clues are **immutable array**, **range sum**, and **many queries**. A brute-force query repeatedly adds values that earlier queries may already have added.

For `q` queries, direct summation can cost `O(nq)` in the worst case. Prefix preprocessing pays `O(n)` once and makes every query `O(1)`.

### Prefix definition

Use the leading-zero convention:

```text
prefix[p] = sum of the first p elements
          = sum of nums[0..p - 1]
```

For `nums = [2, -1, 3, 4]`:

| Prefix position `p` | Elements included | `prefix[p]` |
|---:|---|---:|
| 0 | none | 0 |
| 1 | `nums[0]` | 2 |
| 2 | `nums[0..1]` | 1 |
| 3 | `nums[0..2]` | 4 |
| 4 | `nums[0..3]` | 8 |

Notice that prefix position `3` ends at array index `2`.

### Deriving the query formula

Suppose `left = 1` and `right = 2`:

```text
prefix[3] = nums[0] + nums[1] + nums[2]
prefix[1] = nums[0]
```

Subtracting removes everything before `left`:

```text
prefix[3] - prefix[1] = nums[1] + nums[2]
```

In general:

```text
sum(left..right) = prefix[right + 1] - prefix[left]
```

For `left == 0`, `prefix[left]` is `prefix[0]`, the sum of no elements. Subtracting zero leaves the complete range from the beginning.

### Invariant

> After iteration `i` of prefix construction, `prefix[i]` equals the sum of `nums[0..i - 1]`.

### Complexity

- Constructor preprocessing: `O(n)` time.
- One `sumRange` query: `O(1)` time.
- `q` queries including preprocessing: `O(n + q)` time.
- Prefix array: `O(n)` auxiliary space.

### Interview-ready explanation

> The array is immutable and must support repeated range-sum queries, so I preprocess a leading-zero prefix array. `prefix[p]` stores the sum of the first `p` elements. For an inclusive query, `prefix[right + 1]` contains everything through `right`, while `prefix[left]` contains exactly the elements before `left`; subtracting isolates the requested range. Building costs `O(n)`, each query costs `O(1)`, and the auxiliary space is `O(n)`.

---

## 2. Find Pivot Index (LeetCode 724)

### Problem

Return the leftmost index whose sum of elements strictly to the left equals the sum of elements strictly to the right. Return `-1` if no such index exists.

```text
nums = [1, 7, 3, 6, 5, 6]
answer = 3

left of index 3:  1 + 7 + 3 = 11
right of index 3: 5 + 6     = 11
```

### State derivation

At pivot candidate `i`:

```text
totalSum = leftSum + nums[i] + rightSum
```

Therefore:

```text
rightSum = totalSum - leftSum - nums[i]
```

The entire prefix array is unnecessary. A running `leftSum` contains all required prefix information.

### Processing order

At each index:

1. Derive `rightSum` while the current value is still excluded from `leftSum`.
2. Compare `leftSum` and `rightSum`.
3. If they differ, add `nums[i]` to `leftSum` for the next iteration.

Updating `leftSum` before the comparison would incorrectly include the pivot itself on the left.

### Invariant

> Before processing index `i`, `leftSum` equals the sum of `nums[0..i - 1]`.

### Complexity

- Time: `O(n)`.
- Auxiliary space: `O(1)`.

### Interview-ready explanation

> I first calculate the total sum. While scanning, `leftSum` contains only elements before the candidate pivot. I derive the right sum as `totalSum - leftSum - nums[i]`. If the two sides match, I return the current index; otherwise, I add the current value to `leftSum`. This is `O(n)` time and `O(1)` auxiliary space.

---

## 3. Subarray Sum Equals K (LeetCode 560)

### Problem

Count all contiguous subarrays whose sum equals `k`. Subarrays with equal values but different index ranges count separately.

```text
nums = [1, 1, 1], k = 2
answer = 2

ranges: [0..1], [1..2]
```

### Why a sliding window is unsafe

The array may contain negative values. Expanding a window does not necessarily increase its sum, and shrinking it does not necessarily decrease its sum. There is no reliable direction in which to move a boundary.

### Relationship between two prefixes

At the current position, suppose the cumulative sum is `currentPrefix`. If an earlier boundary had cumulative sum `earlierPrefix`, the enclosed subarray has sum:

```text
currentPrefix - earlierPrefix
```

For that subarray to equal `k`:

```text
currentPrefix - earlierPrefix = k
earlierPrefix = currentPrefix - k
```

The map stores a **frequency**, because every occurrence of the needed earlier prefix creates a different valid left boundary.

### Why `{0 -> 1}` is necessary

The empty prefix occurs once before consuming any array element. It supplies the left boundary for a valid subarray starting at index `0`.

### Trace

For `nums = [1, -1, 1]` and `k = 1`:

| Index | Value | Current prefix | Earlier prefix needed | Matches added | Count | Frequency map after update |
|---:|---:|---:|---:|---:|---:|---|
| start | — | 0 | — | — | 0 | `{0=1}` |
| 0 | 1 | 1 | 0 | 1 | 1 | `{0=1, 1=1}` |
| 1 | -1 | 0 | -1 | 0 | 1 | `{0=2, 1=1}` |
| 2 | 1 | 1 | 0 | 2 | 3 | `{0=2, 1=2}` |

At index `2`, prefix value `0` has occurred at two different earlier boundaries, producing two different subarrays ending at index `2`.

### Update order

```text
1. Add the current value to the prefix.
2. Count earlier occurrences of currentPrefix - k.
3. Record the current prefix.
```

The lookup happens first because the map should represent strictly earlier boundaries during the lookup.

### Invariant

> Before inserting the current prefix, `prefixFrequency` stores how many times every prefix sum occurred at all earlier boundaries.

### Why these two updates are different

```java
subarrayCount += prefixFrequency.getOrDefault(earlierPrefixNeeded, 0);
```

adds newly discovered valid subarrays to the result.

```java
prefixFrequency.put(
        prefixSum,
        prefixFrequency.getOrDefault(prefixSum, 0) + 1
);
```

records one more occurrence of the current prefix for future iterations. It does not update the answer.

### Prefix-array alternative

[SubarraySumEqualsKPrefixArray.java](./SubarraySumEqualsKPrefixArray.java) uses the same constructor idea as Range Sum Query:

1. Build the prefix array in `O(n)`.
2. Enumerate every `(left, right)` pair.
3. Test each range in `O(1)`.

This improves each individual range calculation, but there are still `O(n^2)` candidate ranges. Total time therefore remains `O(n^2)`, with `O(n)` auxiliary space.

### Optimized complexity

- Time: `O(n)` average.
- Auxiliary space: `O(n)`.

### Interview-ready explanation

> Because the array may contain negative values and the target is an exact sum, a sliding window has no safe movement rule. I scan with a cumulative prefix sum. A subarray ending here sums to `k` when an earlier prefix equals `currentPrefix - k`. I store frequencies because duplicate earlier prefixes represent distinct left boundaries. The map starts with `{0 -> 1}` for subarrays beginning at index zero, and I count matches before recording the current prefix. The algorithm uses `O(n)` average time and `O(n)` auxiliary space.

---

## 4. Contiguous Array (LeetCode 525)

### Problem

Return the maximum length of a contiguous subarray containing the same number of zeroes and ones.

```text
nums = [0, 0, 1, 0, 1, 1]
answer = 6
```

The whole array has three zeroes and three ones.

### Transform the condition

Treat:

```text
0 as -1
1 as +1
```

Now a range with equal counts has transformed sum `0`. If the same cumulative balance occurs at two indices, everything between those positions has net balance zero.

### Why store the earliest index

The result asks for maximum length, not the number of ranges. For a current index `i`, the earliest previous occurrence of the same balance produces the longest possible distance:

```text
length = i - earliestIndex
```

Never overwrite an existing balance's index.

### Why `{0 -> -1}` is necessary

Balance zero exists before the array begins, at conceptual index `-1`. If the balance returns to zero at index `i`, the range `[0..i]` has length:

```text
i - (-1) = i + 1
```

### Trace

For `nums = [0, 0, 1, 1]`:

| Index | Value | Balance | Action | Maximum | Earliest-index map after step |
|---:|---:|---:|---|---:|---|
| start | — | 0 | initialize | 0 | `{0=-1}` |
| 0 | 0 | -1 | store first index | 0 | `{0=-1, -1=0}` |
| 1 | 0 | -2 | store first index | 0 | `{0=-1, -1=0, -2=1}` |
| 2 | 1 | -1 | length `2 - 0 = 2` | 2 | `{0=-1, -1=0, -2=1}` |
| 3 | 1 | 0 | length `3 - (-1) = 4` | 4 | `{0=-1, -1=0, -2=1}` |

### Invariant

> After processing index `i`, the map stores the earliest index at which each encountered balance occurred, and `maxLength` is the longest balanced subarray ending at or before `i`.

### Complexity

- Time: `O(n)` average.
- Auxiliary space: `O(n)`.

### Interview-ready explanation

> I transform zero into `-1` and one into `+1`, so equal counts become a zero-sum range. If the same running balance appears twice, the elements between those positions have net balance zero. Since the objective is maximum length, I store the earliest index for every balance. I initialize balance zero at index `-1` to support ranges beginning at zero. Each index is processed once, so the solution is `O(n)` average time and `O(n)` auxiliary space.

---

## 5. Subarray Sums Divisible by K (LeetCode 974)

### Problem

Count all non-empty contiguous subarrays whose sum is divisible by `k`.

```text
nums = [4, 5, 0, -2, -3, 1], k = 5
answer = 7
```

### Recognition

The problem asks for a count of contiguous ranges and gives a divisibility condition. This suggests comparing the modulo state of current and earlier prefixes.

### Derivation

Let two prefix sums be `current` and `earlier`. Their enclosed range is divisible by `k` when:

```text
(current - earlier) % k == 0
```

That is true when both prefix sums have the same normalized remainder modulo `k`.

The map stores remainder frequencies because every matching earlier remainder creates one distinct subarray ending at the current element.

### Java remainder normalization

Java preserves the sign of the dividend:

```text
-2 % 5 == -2
```

Normalize into `[0, k - 1]`:

```java
int remainder = sumSoFar % k;
if (remainder < 0) {
    remainder += k;
}
```

### Short trace

For `nums = [2, 3, 1, 4]` and `k = 5`:

| Index | Value | Sum so far | Remainder | Earlier matches | Count | Remainder frequencies after update |
|---:|---:|---:|---:|---:|---:|---|
| start | — | 0 | 0 | — | 0 | `{0=1}` |
| 0 | 2 | 2 | 2 | 0 | 0 | `{0=1, 2=1}` |
| 1 | 3 | 5 | 0 | 1 | 1 | `{0=2, 2=1}` |
| 2 | 1 | 6 | 1 | 0 | 1 | `{0=2, 1=1, 2=1}` |
| 3 | 4 | 10 | 0 | 2 | 3 | `{0=3, 1=1, 2=1}` |

The three valid ranges are `[0..1]`, `[0..3]`, and `[2..3]`.

### Invariant

> Before recording the current remainder, the map stores the frequencies of all normalized prefix remainders at earlier boundaries.

### Complexity

- Time: `O(n)` average.
- Auxiliary space: `O(min(n, k))`, commonly stated as `O(n)` under general input bounds.

### Interview-ready explanation

> A range sum is divisible by `k` when its two boundary prefix sums have the same normalized remainder modulo `k`. I scan while maintaining the cumulative sum and a frequency map of earlier remainders. The map begins with remainder zero occurring once for the empty prefix. At each element, I normalize Java's possibly negative remainder, add its earlier frequency to the result, and then increment its frequency. This takes `O(n)` average time and `O(n)` auxiliary space in the general case.

---

## Consolidated Decision Table

| Problem | Cumulative state | Earlier state needed | Map meaning | Initial state | Result update |
|---|---|---|---|---|---|
| Range Sum Query | Full prefix array | Boundary at `left` | No map | `prefix[0] = 0` | Subtract two prefix positions |
| Pivot Index | `leftSum` plus total | None | No map | `leftSum = 0` | Return matching index |
| Subarray Sum Equals K | Prefix sum | `current - k` | Frequency | `{0=1}` | Add matching frequency |
| Contiguous Array | `ones - zeroes` balance | Same balance | Earliest index | `{0=-1}` | Maximize distance |
| Sums Divisible by K | Normalized remainder | Same remainder | Frequency | `{0=1}` | Add matching frequency |

## Recurring Mistake Log

### Prefix position versus array index

`prefix[p]` contains `p` elements, so its last included array index is `p - 1`.

### Preprocessing versus a query

Building the prefix array is `O(n)`. Once built, one range query is `O(1)`. Do not combine those into `O(n)` for every query.

### Frequency versus earliest index

- Counting all ranges requires every earlier occurrence: store a frequency.
- Maximizing a range requires the farthest earlier boundary: store the earliest index.

### Result update versus state update

```text
count += number of earlier matches
frequency[currentState] += 1
```

These lines serve different purposes and are not interchangeable.

### Pattern recognition language

Do not say a problem is not Sliding Window merely because it has several possible subarrays. Say why the condition lacks a reliable grow/shrink rule, or why the solution must compare the current cumulative state with many historical boundaries.

### Invariant language

An invariant states what is guaranteed and when. Prefer:

> Before processing index `i`, `leftSum` equals the sum of all elements strictly before `i`.

Avoid:

> We keep calculating the left side and find the answer.

## 45–60 Second Articulation Drill

For any new Prefix Sum problem, speak in this order:

1. **Recognition:** What words or constraints suggest cumulative state?
2. **Definition:** What exactly does the prefix or running state represent?
3. **Derivation:** What cancels between the current and earlier boundary?
4. **Storage:** Do you need a prefix array, frequency, or earliest index?
5. **Initialization:** Does the empty prefix require `0`, `{0=1}`, or `{0=-1}`?
6. **Invariant:** What is guaranteed before or after each iteration?
7. **Correctness:** Why does every update correspond to exactly the required range or result?
8. **Complexity:** Separate preprocessing, query time, total time, and auxiliary space.

Compact template:

> The problem asks for ____, so I track ____. At the current position, a valid range requires an earlier state of ____. I store ____ because the objective is ____. I initialize ____ to represent ____. Before each update, ____. This examines each element once, so the time is ____ and the auxiliary space is ____.
