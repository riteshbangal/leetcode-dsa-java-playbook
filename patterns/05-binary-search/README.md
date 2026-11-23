# Pattern 05 — Binary Search

Before writing the loop, finish this sentence: **“After I compare at `mid`, I can discard ___ because ___.”** In a sorted array, the proof comes from value order. For a peak, it comes from two neighbors. For Koko or shipping, it comes from knowing that once a speed or capacity works, every larger one works too.

[Jump to the 10 problems and their Java solutions](#implemented-problems) · [Worked traces and pseudocode](problem-walkthrough.md) · [All patterns](../README.md)

## Choose the loop by what the bounds mean

Both templates here use **inclusive** bounds. The difference is what you know when only one candidate remains.

| What `[left, right]` means | Loop guard | When `left == right` |
| --- | --- | --- |
| Indices still to **test** for a match or a better match | `left <= right` | One index is unchecked. It could still hold the match you need. |
| Candidate **indices** containing a minimum or peak index, or candidate **values** containing the minimum feasible value | `left < right` | The survivor identifies the answer. Return the index, its array value, or the feasible number as requested. |

For the nonnegative bounds in this chapter, `mid = left + (right - left) / 2` takes the lower midpoint without computing `left + right`, which can overflow in Java. With `left < right`, that gives `left <= mid < right`: both `right = mid` and `left = mid + 1` shrink the interval. With `left <= right`, `right = mid` can get stuck on a singleton.

## Test the midpoint: exact search and boundaries

In a sorted array, if `nums[mid] < target`, even the **largest** value in `[left, mid]` is too small. That proves the whole interval can go, not just the element at `mid`.

```java
static int search(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] < target) {
            left = mid + 1;
        } else if (nums[mid] > target) {
            right = mid - 1;
        } else {
            return mid;
        }
    }
    return -1;
}
```

**Same trace, different return.** Search `[1, 3, 5, 6]` for `2`:

| Candidates | Midpoint observation | Next bounds |
| --- | --- | --- |
| `[0, 3]` | Index `1` holds `3`: too large | `right = 0` |
| `[0, 0]` | Index `0` holds `1`: too small | `left = 1` |

The bounds cross at `left = 1, right = 0`. **Exact search** returns `-1`. **Search Insert Position** returns `left`: everything before it is too small, so `2` belongs at index `1`. An insertion index can equal `nums.length`; return it without indexing the array. This problem uses distinct values. Returning immediately on a match would not guarantee the first copy among duplicates.

**First or last occurrence:** Equality proves a match, not a boundary. Save `best = mid`, then set `right = mid - 1` for the first copy or `left = mid + 1` for the last. Return `best` (initially `-1`), not the last midpoint tested.

**Rotated target search (distinct values):** Test `nums[mid] == target` first. If it misses, identify the sorted half and use its range:

| Which half is sorted? | Is the target in that half’s value range? | If yes | If no |
| --- | --- | --- | --- |
| Left: `nums[left] <= nums[mid]` | `nums[left] <= target && target < nums[mid]` | `right = mid - 1` | `left = mid + 1` |
| Right: `nums[left] > nums[mid]` | `nums[mid] < target && target <= nums[right]` | `left = mid + 1` | `right = mid - 1` |

Being in range tells you **where to look**, not that the value exists. Return `-1` only after the interval is empty.

## Keep one candidate: rotated minimum and peak

For a **nonempty rotated sorted array with distinct values**, compare to the **right endpoint**, not a target. If `nums[mid] > nums[right]`, the drop to the minimum must be after `mid`. Otherwise `mid` itself could be the minimum.

```java
static int findMin(int[] nums) {
    int left = 0, right = nums.length - 1;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] > nums[right]) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return nums[left];
}
```

On `[9, 4]`, the last `mid` is `0`, the surviving **index** is `1`, and the required **value** is `4`. Those are three different things.

**Find Peak Element** assumes a nonempty array, unequal neighbors, and virtual values of negative infinity beyond the ends. Keep a rise into `left` and a fall after `right`. When `nums[mid] < nums[mid + 1]`, set `left = mid + 1`: that rising edge becomes the new left boundary. Otherwise set `right = mid`: the falling edge becomes the new right boundary. Reading `mid + 1` is safe because `mid < right`. When the bounds meet, both neighbors are smaller. Return the **index** `left`; you only had to keep **one** peak, not every peak.

## Search the answer: minimum feasible speed or capacity

Here the bounds are **candidate values**, not array indices. You never need to build an array of speeds or capacities. Start with an upper bound known to work. A feasible midpoint might be the minimum; an infeasible midpoint rules out every smaller value too.

```text
firstFeasible(low, high, works):
    while low < high:
        mid = low + floor((high - low) / 2)
        if works(mid):
            high = mid
        else:
            low = mid + 1
    return low
```

| Problem | Inclusive bounds | What makes a candidate work? |
| --- | --- | --- |
| Koko Eating Bananas | `1` to `max(piles)` | Sum the whole hours for **each** pile. The total must be `<= h`. At the upper bound, each pile takes one hour; the problem guarantees `h >= piles.length`. |
| Ship Packages Within D Days | `max(weights)` (heaviest package) to `sum(weights)` (all fit in one day) | Load packages **in order**. Start a new day only if the next package would exceed capacity. Filling each day this way uses the fewest days for that capacity; check `daysNeeded <= days`. |

For a positive pile and speed, Java’s `(pile - 1L) / speed + 1` rounds up without floating point. At speed `4`, a pile of `8` needs `2` hours, not `3`. Accumulate hours in a `long`. For shipping, if you generalize beyond the problem’s bounds, use `long` for the sum, candidate capacities, and load as well.

## Implemented problems

Each problem name goes directly to its trace or explanation and fenced pseudocode.

| Walkthrough | What to return | Java implementation |
| --- | --- | --- |
| [1. Binary Search](problem-walkthrough.md#1-binary-search) | Matching index or `-1` | [BinarySearch.java](BinarySearch.java) |
| [2. Search Insert Position](problem-walkthrough.md#2-search-insert-position) | Matching index or insertion index in `[0, n]` | [SearchInsertPosition.java](SearchInsertPosition.java) |
| [3. First Occurrence](problem-walkthrough.md#3-first-occurrence) | Earliest matching index or `-1` | [FindFirstOccurrence.java](FindFirstOccurrence.java) |
| [4. Last Occurrence](problem-walkthrough.md#4-last-occurrence) | Latest matching index or `-1` | [FindLastOccurrence.java](FindLastOccurrence.java) |
| [5. Find First and Last Position](problem-walkthrough.md#5-find-first-and-last-position) | Both indices or `[-1, -1]` | [FindFirstAndLastPosition.java](FindFirstAndLastPosition.java) |
| [6. Find Minimum in a Rotated Sorted Array](problem-walkthrough.md#6-find-minimum-in-a-rotated-sorted-array) | Minimum **value** | [FindMinimumInRotatedSortedArray.java](FindMinimumInRotatedSortedArray.java) |
| [7. Search in a Rotated Sorted Array](problem-walkthrough.md#7-search-in-a-rotated-sorted-array) | Matching index or `-1` | [SearchInRotatedSortedArray.java](SearchInRotatedSortedArray.java) |
| [8. Find Peak Element](problem-walkthrough.md#8-find-peak-element) | Index of **any** peak | [FindPeakElement.java](FindPeakElement.java) |
| [9. Koko Eating Bananas](problem-walkthrough.md#9-koko-eating-bananas) | Minimum feasible eating speed | [KokoEatingBananas.java](KokoEatingBananas.java) |
| [10. Capacity to Ship Packages Within D Days](problem-walkthrough.md#10-capacity-to-ship-packages-within-d-days) | Minimum feasible ship capacity | [CapacityToShipPackagesWithinDDays.java](CapacityToShipPackagesWithinDDays.java) |

## Complexity

- **Index searches:** `O(log n)` time and `O(1)` extra space. Finding both boundaries takes two logarithmic searches, not a scan of the duplicate run.
- **Answer searches:** Let `W = high - low + 1` be the number of candidate values at the start. Computing the bounds takes `O(n)`; each of at most `ceil(log2 W)` checks scans up to `n` items. Total: `O(n · (1 + log W))` time and `O(1)` extra space. When `W = 1`, the bounds scan is all you need.

## Interview check

Say the **index**, the **observed value**, and the **reason for the update** separately:

> “`mid` is index 3. Its value is 12. The target is 16, so in this sorted interval every index through 3 is too small. I move `left` to 4.”

Before submitting, trace a singleton, a miss beyond the last element, and a run of duplicates where allowed. For a feasibility search, test a value that works **exactly** at the limit: equality is feasible, but it does not prove minimality.

Optional follow-up for a later revision: in *Split Array Largest Sum*, what would the shipping capacity and the number of days represent?
