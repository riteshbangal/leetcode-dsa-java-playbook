# Binary Search — Problem Walkthrough

At each step, ask what the comparison lets you discard. A peak search only needs to keep **one** peak; a boundary search can save a match outside the interval while it looks for a better one.

In the pseudocode, indices start at `0` and `//` means integer division rounded down.

## 1. Binary Search

**Problem:** Given an ascending sorted array and a target, return the target's index or `-1`.

Example:

```text
nums   = [-1, 0, 3, 5, 9, 12]
target = 9
answer = 4
```

Start with the inclusive interval `[0, 5]`.

| Iteration | `left` | `right` | `mid` | `nums[mid]` | Decision |
| --- | ---: | ---: | ---: | ---: | --- |
| 1 | 0 | 5 | 2 | 3 | `3 < 9`, so `left = 3` |
| 2 | 3 | 5 | 4 | 9 | Match; return `4` |

Why is the first elimination safe? Because the array is sorted. If `nums[mid] < target`, every value at or left of `mid` is also too small.

**Invariant:** If the target remains possible, its index is inside `[left, right]`.

**Pseudocode:**

```text
function search(nums, target):
    left, right = 0, length(nums) - 1
    while left <= right:
        mid = left + (right - left) // 2
        if nums[mid] < target:
            left = mid + 1
        else if nums[mid] > target:
            right = mid - 1
        else:
            return mid
    return -1
```

**Java implementation:** [BinarySearch.java](BinarySearch.java).

**Complexity:** `O(log n)` time and `O(1)` extra space.

**Strong articulation:** “I use a closed interval because both pointers refer to unchecked candidates. Equality returns the index. Otherwise sorted order proves which half cannot contain the target.”

## 2. Search Insert Position

**Problem:** Return the target's index if found; otherwise return the index where it should be inserted to preserve ascending order.

Example:

```text
nums   = [1, 3, 5, 6]
target = 2
answer = 1
```

| Iteration | `left` | `right` | `mid` | `nums[mid]` | Update |
| --- | ---: | ---: | ---: | ---: | --- |
| 1 | 0 | 3 | 1 | 3 | `right = 0` |
| 2 | 0 | 0 | 0 | 1 | `left = 1` |

The loop ends with `left = 1` and `right = 0`. Every index at or before `right` is too small, and every index at or after `left` is large enough. Therefore `left` is the insertion boundary.

For `target = 7`, the final `left` is `4`, which correctly represents insertion after the last element. No physical insertion or array shifting is required; the problem asks only for the index.

**Invariant:** All indices before `left` contain values less than the target; all indices after `right` contain values greater than the target.

**Pseudocode:**

```text
function searchInsert(nums, target):
    left, right = 0, length(nums) - 1
    while left <= right:
        mid = left + (right - left) // 2
        if nums[mid] < target:
            left = mid + 1
        else if nums[mid] > target:
            right = mid - 1
        else:
            return mid
    return left    # Insertion index; may equal length(nums).
```

**Java implementation:** [SearchInsertPosition.java](SearchInsertPosition.java).

**Complexity:** `O(log n)` time and `O(1)` extra space.

## 3. First Occurrence

**Problem:** In a sorted array containing duplicates, return the first index of the target.

Example:

```text
nums   = [1, 2, 2, 2, 3]
target = 2
answer = 1
```

An equality at index `2` proves that `2` is an answer, but not that it is the first answer. Preserve it, then search left:

```java
firstIndex = mid;
right = mid - 1;
```

The saved candidate matters. The remaining left interval might not contain another copy, so returning the final `mid` would be unreliable.

**Invariant:** `firstIndex` is the best matching index found so far, and any better answer must be inside the remaining interval.

**Pseudocode:**

```text
function findFirstOccurrence(nums, target):
    left, right = 0, length(nums) - 1
    firstIndex = -1
    while left <= right:
        mid = left + (right - left) // 2
        if nums[mid] < target:
            left = mid + 1
        else if nums[mid] > target:
            right = mid - 1
        else:
            firstIndex = mid
            right = mid - 1    # Only an earlier match can improve firstIndex.
    return firstIndex
```

**Java implementation:** [FindFirstOccurrence.java](FindFirstOccurrence.java).

**Complexity:** `O(log n)` time and `O(1)` extra space.

**Strong articulation:** “On equality, I save `mid` and eliminate `mid` plus everything to its right, because only a smaller index could improve the answer.”

## 4. Last Occurrence

**Problem:** Return the final index of the target in a sorted array containing duplicates.

This mirrors first occurrence. On equality:

```java
lastIndex = mid;
left = mid + 1;
```

Everything to the left, including `mid`, cannot improve a last-occurrence answer. A later copy may still exist to the right.

**Invariant:** `lastIndex` is the best matching index found so far, and any better answer must be inside the remaining interval.

**Pseudocode:**

```text
function findLastOccurrence(nums, target):
    left, right = 0, length(nums) - 1
    lastIndex = -1
    while left <= right:
        mid = left + (right - left) // 2
        if nums[mid] < target:
            left = mid + 1
        else if nums[mid] > target:
            right = mid - 1
        else:
            lastIndex = mid
            left = mid + 1    # Only a later match can improve lastIndex.
    return lastIndex
```

**Java implementation:** [FindLastOccurrence.java](FindLastOccurrence.java).

**Complexity:** `O(log n)` time and `O(1)` extra space.

## 5. Find First and Last Position

**Problem:** Return both boundaries of the target, or `[-1, -1]` if absent.

Example:

```text
nums   = [5, 7, 7, 8, 8, 10]
target = 8
answer = [3, 4]
```

Run two independent binary searches:

1. One biased toward smaller matching indices.
2. One biased toward larger matching indices.

Each helper is `O(log n)`, so together they remain `O(log n)`:

```text
O(log n) + O(log n) = O(log n)
```

**Invariant:** Each helper preserves its best boundary and searches only where a better boundary could exist.

**Pseudocode:**

```text
function searchRange(nums, target):
    first = boundary(nums, target, "first")
    last = boundary(nums, target, "last")
    return [first, last]

# Java implements the two directions as findFirst and findLast.
function boundary(nums, target, direction):
    left, right = 0, length(nums) - 1
    best = -1
    while left <= right:
        mid = left + (right - left) // 2
        if nums[mid] < target:
            left = mid + 1
        else if nums[mid] > target:
            right = mid - 1
        else:
            best = mid
            if direction == "first":
                right = mid - 1
            else:
                left = mid + 1
    return best
```

**Java implementation:** [FindFirstAndLastPosition.java](FindFirstAndLastPosition.java).

**Complexity:** `O(log n)` time and `O(1)` extra space.

## 6. Find Minimum in a Rotated Sorted Array

**Problem:** Find the minimum value in an ascending sorted array that has been rotated. Values are distinct.

Example:

```text
nums = [4, 5, 6, 7, 0, 1, 2]
answer = 0
```

Use a converging interval and compare `nums[mid]` with `nums[right]`:

- If `nums[mid] > nums[right]`, the rotation point must be strictly right of `mid`, so `left = mid + 1`.
- Otherwise, `mid` may be the minimum, so preserve it with `right = mid`.

| Iteration | `left` | `right` | `mid` | Comparison | Update |
| --- | ---: | ---: | ---: | --- | --- |
| 1 | 0 | 6 | 3 | `7 > 2` | `left = 4` |
| 2 | 4 | 6 | 5 | `1 <= 2` | `right = 5` |
| 3 | 4 | 5 | 4 | `0 <= 1` | `right = 4` |

Now `left == right == 4`, so return `nums[left]`, which is `0`.

Why not use `left <= right`? When `left == right`, `mid` equals `right`, and `right = mid` would not shrink the interval. The `left < right` template stops as soon as one candidate remains.

**Invariant:** The minimum's index always remains inside `[left, right]`.

**Pseudocode:**

```text
function findMin(nums):
    left, right = 0, length(nums) - 1
    while left < right:
        mid = left + (right - left) // 2
        if nums[mid] > nums[right]:
            left = mid + 1
        else:
            right = mid    # mid could be the minimum; keep it.
    return nums[left]      # Minimum value, not its index.
```

**Java implementation:** [FindMinimumInRotatedSortedArray.java](FindMinimumInRotatedSortedArray.java).

**Complexity:** `O(log n)` time and `O(1)` extra space.

## 7. Search in a Rotated Sorted Array

**Problem:** Return the index of a target in a rotated sorted array with distinct values.

Example:

```text
nums   = [4, 5, 6, 7, 0, 1, 2]
target = 0
answer = 4
```

The whole current interval may not be sorted, but at least one half around `mid` is sorted.

First check equality. Then:

```java
if (nums[left] <= nums[mid]) {
    // Left half is sorted.
} else {
    // Right half is sorted.
}
```

If the left half is sorted, the target lies there exactly when:

```java
nums[left] <= target && target < nums[mid]
```

If the right half is sorted, the target lies there exactly when:

```java
nums[mid] < target && target <= nums[right]
```

The inequalities exclude `mid` because equality was already checked.

**Invariant:** If the target exists, it remains inside `[left, right]`; each range check eliminates only a half proven not to contain it.

**Common trap:** Do not return `-1` merely because the target is outside one sorted half. That proves the search must continue in the other half, not that the target is absent.

**Pseudocode:**

```text
function search(nums, target):
    left, right = 0, length(nums) - 1
    while left <= right:
        mid = left + (right - left) // 2
        if nums[mid] == target:
            return mid

        if nums[left] <= nums[mid]:    # Left half is sorted.
            if nums[left] <= target and target < nums[mid]:
                right = mid - 1
            else:
                left = mid + 1
        else:                          # Right half is sorted.
            if nums[mid] < target and target <= nums[right]:
                left = mid + 1
            else:
                right = mid - 1
    return -1
```

**Java implementation:** [SearchInRotatedSortedArray.java](SearchInRotatedSortedArray.java).

**Complexity:** `O(log n)` time and `O(1)` extra space.

## 8. Find Peak Element

**Problem:** Return an index whose value is greater than its neighbors. Adjacent elements differ, and values outside the array are treated as negative infinity.

Example:

```text
nums = [1, 2, 3, 1]
answer = 2
```

Compare `nums[mid]` and `nums[mid + 1]`:

- Rising slope: `nums[mid] < nums[mid + 1]`. A peak exists to the right, so `left = mid + 1`.
- Falling slope: `nums[mid] > nums[mid + 1]`. `mid` could already be a peak, so `right = mid`.

Using `left < right` guarantees `mid < right`, making `mid + 1` a valid index.

At termination, `left == right`. Return that index, not the value and not the last computed `mid`.

**Invariant:** At least one peak remains inside `[left, right]`.

**Pseudocode:**

```text
function findPeakElement(nums):
    left, right = 0, length(nums) - 1
    while left < right:
        mid = left + (right - left) // 2
        if nums[mid] < nums[mid + 1]:
            left = mid + 1    # Rising slope: mid cannot be a peak.
        else:
            right = mid      # Falling slope: mid could be a peak.
    return left              # Peak index, not its value.
```

**Java implementation:** [FindPeakElement.java](FindPeakElement.java).

**Complexity:** `O(log n)` time and `O(1)` extra space.

## 9. Koko Eating Bananas

**Problem:** Each pile must be processed separately. At speed `k`, Koko eats up to `k` bananas from one pile per hour. Find the minimum integer speed that finishes all piles within `h` hours.

Example:

```text
piles = [3, 6, 7, 11]
h = 8
answer = 4
```

There is no speed array. Build the answer space:

- Slowest possible positive speed: `1`.
- Sufficient upper bound: the largest pile.

For each candidate speed, total hours are:

```text
ceil(3 / speed) + ceil(6 / speed)
+ ceil(7 / speed) + ceil(11 / speed)
```

At speed `4`:

```text
1 + 2 + 2 + 3 = 8 hours
```

At speed `3`:

```text
1 + 2 + 3 + 4 = 10 hours
```

So `4` is the first feasible speed.

The predicate is monotonic: if speed `k` works, every larger speed also works. This produces a `false ... true` boundary suitable for binary search.

Use exact integer ceiling division:

```java
(pile - 1L) / speed + 1
```

**Invariant:** The minimum feasible speed remains inside `[left, right]`.

**Pseudocode:**

```text
function minEatingSpeed(piles, h):
    left = 1
    right = max(piles)    # One hour per pile; h >= length(piles).
    while left < right:
        speed = left + (right - left) // 2
        if canFinish(piles, h, speed):
            right = speed
        else:
            left = speed + 1
    return left

function canFinish(piles, h, speed):
    hoursNeeded = 0    # Use a 64-bit total in Java.
    for pile in piles:
        hoursForPile = 1 + (pile - 1) // speed
        hoursNeeded += hoursForPile
        if hoursNeeded > h:
            return false
    return true
```

**Java implementation:** [KokoEatingBananas.java](KokoEatingBananas.java).

**Complexity:** `O(n log M)` time, where `M` is the largest pile, and `O(1)` extra space.

## 10. Capacity to Ship Packages Within D Days

**Problem:** Packages must be shipped in their original order. Find the minimum ship capacity that completes all packages within the given number of days.

Example:

```text
weights = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
days = 5
answer = 15
```

Choose answer-space bounds:

- `left = max(weights)`: the ship must carry the heaviest individual package.
- `right = sum(weights)`: this capacity ships everything in one day.

For a candidate capacity, scan in order. Add weights to the current day until the next package would exceed capacity, then start a new day. This greedy helper returns whether the required days are within the limit.

If a capacity works, do not return immediately. A smaller feasible capacity may exist, so preserve `mid` with `right = mid`. If it fails, every smaller capacity also fails, so set `left = mid + 1`.

**Invariant:** The minimum feasible capacity remains inside `[left, right]`.

**Pseudocode:**

```text
function shipWithinDays(weights, days):
    left, right = max(weights), sum(weights)
    while left < right:
        capacity = left + (right - left) // 2
        if canShip(weights, days, capacity):
            right = capacity
        else:
            left = capacity + 1
    return left

function canShip(weights, allowedDays, capacity):
    daysNeeded = 1
    load = 0
    for weight in weights:    # Keep the original package order.
        if load + weight > capacity:
            daysNeeded += 1
            load = 0
        load += weight        # This package travels on the current day.
        if daysNeeded > allowedDays:
            return false
    return true
```

**Java implementation:** [CapacityToShipPackagesWithinDDays.java](CapacityToShipPackagesWithinDDays.java).

**Complexity:** `O(n log S)` time, where `S` is the sum of all weights, and `O(1)` extra space.

## Pattern Comparison

| Problem family | Loop | Successful midpoint | Pointer behavior | Final return |
| --- | --- | --- | --- | --- |
| Exact target | `left <= right` | Return immediately | Exclude `mid` with `±1` | `-1` if absent |
| First/last occurrence | `left <= right` | Save and continue | Exclude saved `mid` | Saved index |
| Minimum/peak | `left < right` | Not an immediate return | Sometimes preserve `mid` | Meeting pointer/value |
| Minimum feasible answer | `left < right` | Preserve feasible `mid` | `right = mid` or `left = mid + 1` | Meeting value |

## Debugging Checklist

When a binary search fails, inspect these questions in order:

1. Are `left` and `right` indices, values, or answer candidates?
2. Is the interval closed, half-open, or converging?
3. When equality occurs, should the algorithm return or seek a better boundary?
4. Can `mid` still be the answer after the comparison?
5. Does every branch strictly shrink the interval?
6. Is the returned variable still valid at termination?
7. For answer search, does feasibility change monotonically?
8. Are calculations using `long` where sums may overflow?

## Articulation Drill

For each new problem, practice saying:

> “My candidate space is ___. My invariant is ___. At `mid`, the comparison tells me ___. Therefore candidates from ___ through ___ are impossible, and I update ___. The loop terminates when ___, so I return ___.”

This makes pointer movements consequences of a proof rather than memorized lines.
