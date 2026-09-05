# Sliding Window Problem Revision Notes

> Revision flow used for every problem:
>
> ```text
> Problem statement
>       ↓
> Important clues
>       ↓
> Fixed or variable window
>       ↓
> Boundary meanings
>       ↓
> Window state
>       ↓
> Validity or qualifying condition
>       ↓
> Invariant
>       ↓
> Expand, shrink, evaluate
>       ↓
> Time + Space
> ```

---

## Implemented Java Files

- [MaxSumSubarrayOfSizeK.java](MaxSumSubarrayOfSizeK.java)
- [MaximumAverageSubarray.java](MaximumAverageSubarray.java)
- [MaximumNumberOfVowels.java](MaximumNumberOfVowels.java)
- [MinimumSizeSubarraySum.java](MinimumSizeSubarraySum.java)
- [LongestSubstringWithoutRepeatingCharacters.java](LongestSubstringWithoutRepeatingCharacters.java)
- [MaxConsecutiveOnesIII.java](MaxConsecutiveOnesIII.java)
- [FruitIntoBaskets.java](FruitIntoBaskets.java)
- [PermutationInString.java](PermutationInString.java)

---

# 1. Maximum Sum Subarray of Size K

## Problem statement

Return the largest sum among all contiguous subarrays containing exactly `k` elements.

Java file: [MaxSumSubarrayOfSizeK.java](MaxSumSubarrayOfSizeK.java)

```text
nums = [2, 1, 5, 1, 3, 2]
k = 3

best window = [5, 1, 3]
answer = 9
```

## Important clues

```text
contiguous subarray
exactly k elements
overlapping ranges
maximum aggregate
```

These clues suggest a **fixed-size Sliding Window**.

## Boundaries and state

```text
left      → first index in the current window
right     → last index/newly processed index
windowSum → sum of nums[left...right]
maxSum    → largest valid size-k sum processed so far
```

## Invariant

> At every evaluation point, `[left, right]` contains exactly `k` elements, `windowSum` is their sum, and `maxSum` is the largest sum among all evaluated size-`k` windows.

## Processing logic

```text
for every right:
    add nums[right]

    if size > k:
        subtract nums[left]
        left++

    if size == k:
        update maxSum
```

## State reuse

```text
new window sum
= old window sum
- outgoing value
+ incoming value
```

This avoids recalculating `k` elements for every overlapping window.

## Mistakes to remember

- `left` and `right` are indices, not values.
- For inclusive boundaries, size is `right - left + 1`.
- Do not evaluate the temporary size-`k + 1` window.
- Initialize `maxSum` safely when inputs can be negative.

## Time + Space

```text
Time:  O(n)
Space: O(1)
```

---

# 2. Maximum Average Subarray I

## Problem statement

Return the maximum average among all contiguous subarrays containing exactly `k` elements.

Java file: [MaximumAverageSubarray.java](MaximumAverageSubarray.java)

```text
nums = [1, 12, -5, -6, 50, 3]
k = 4

window sums = 2, 51, 42
answer = 51 / 4 = 12.75
```

## Important observation

Every valid window has the same positive denominator `k`:

```text
larger sum ⇔ larger average
```

Therefore, compare window sums and divide only the final maximum by `k`.

## Window state and invariant

```text
windowSum → sum of the current range
maxSum    → largest size-k sum processed so far
```

> At every evaluation point, `windowSum` is the sum of exactly `k` values. Since every candidate average divides by the same `k`, `maxSum` identifies the maximum-average window.

## Java numeric trap

```java
return (double) maxSum / k;
```

This is incorrect:

```java
return (double) (maxSum / k);
```

The second version performs integer division before the cast.

## Time + Space

```text
Time:  O(n)
Space: O(1)
```

---

# 3. Maximum Number of Vowels in a Substring of Given Length

## Problem statement

Return the maximum number of vowels in any substring of length `k`.

Java file: [MaximumNumberOfVowels.java](MaximumNumberOfVowels.java)

## Important clues

```text
substring
exactly length k
overlapping candidates
maximum count
```

This is a fixed-size window. The state is not the substring itself:

```text
vowelCount → number of vowels currently inside [left, right]
maxVowels  → largest valid vowelCount processed so far
```

## Expand and shrink

```text
incoming character is a vowel → vowelCount++
outgoing character is a vowel → vowelCount--
outgoing character always leaves → left++
```

`left++` is outside the vowel condition because every oversized window must lose one character, whether that character is a vowel or not.

## Invariant

> After expansion and any required shrinking, `vowelCount` equals the number of vowels in exactly the characters inside `[left, right]`.

## Java helper

```java
private boolean isVowel(char c) {
    return "aeiou".indexOf(c) >= 0;
}
```

`String.contains` cannot directly accept a primitive `char`. For the five lowercase vowels, `indexOf` is concise and clear.

## Time + Space

```text
Time:  O(n)
Space: O(1)
```

---

# 4. Minimum Size Subarray Sum

## Problem statement

Given an array of positive integers and a positive target, return the minimum length of a contiguous subarray whose sum is at least the target. Return `0` if none exists.

Java file: [MinimumSizeSubarraySum.java](MinimumSizeSubarraySum.java)

```text
target = 7
nums = [2, 3, 1, 2, 4, 3]

best window = [4, 3]
answer = 2
```

## Important clues

```text
contiguous subarray
minimum length
sum at least target
positive numbers
```

This is a **variable-size minimum qualifying window**.

## Boundaries and state

```text
right     → expands by adding a new value
left      → shrinks a qualifying window
windowSum → sum of nums[left...right]
minLength → smallest qualifying length processed so far
```

## Why `while`, not `if`?

After one incoming value is added, several outgoing values may be removable while the sum still meets the target. Every qualifying size must be considered:

```text
while windowSum >= target:
    record current length
    subtract nums[left]
    left++
```

## Answer timing

Record the length **before** removing the left value. At that moment, the window is known to qualify.

## Loop-exit invariant

> When the shrinking loop finishes, `windowSum < target`. The last valid lengths were recorded in `minLength` before their left values were removed.

## Why positivity matters

Positive values make the sum monotonic:

```text
expand → sum increases
shrink → sum decreases
```

With negative numbers, this greedy rule is not generally safe.

## Initialization

```java
int minLength = Integer.MAX_VALUE;
```

Return `0` only if the sentinel was never replaced. Initializing to `0` would make every real length look worse.

## Time + Space

```text
Time:  O(n)
Space: O(1)
```

---

# 5. Longest Substring Without Repeating Characters

## Problem statement

Return the length of the longest substring containing no repeated character.

Java file: [LongestSubstringWithoutRepeatingCharacters.java](LongestSubstringWithoutRepeatingCharacters.java)

```text
s = "pwwkew"
answer = 3
one best window = "wke"
```

## Important clues

```text
substring
longest
without repeating characters
overlapping ranges
```

This is a **variable-size longest valid window**.

## State

```text
HashSet<Character> windowCharacters
```

The set represents exactly the characters currently inside the valid window. A length alone cannot tell whether the incoming character is already present.

## Processing logic

```text
incoming = s.charAt(right)

while set contains incoming:
    remove s.charAt(left)
    left++

add incoming
update maxLength
```

The incoming character is added only after the old duplicate has been removed.

## Example trace: `pwwkew`

| `right` | Incoming | Removed while duplicate | Valid window | `maxLength` |
| ---: | --- | --- | --- | ---: |
| 0 | `p` | none | `p` | 1 |
| 1 | `w` | none | `pw` | 2 |
| 2 | `w` | `p`, old `w` | `w` | 2 |
| 3 | `k` | none | `wk` | 2 |
| 4 | `e` | none | `wke` | 3 |
| 5 | `w` | old `w` | `kew` | 3 |

At index `2`, removing only `p` is insufficient because the old `w` is still inside the range. This is why shrinking uses `while`.

## Invariant

> After the shrinking loop and insertion of the incoming character, every character in `[left, right]` is unique, and the set contains exactly those characters.

## Time + Space

```text
Time:  O(n) average
Space: O(min(n, alphabet size))
```

---

# 6. Max Consecutive Ones III

## Problem statement

Return the longest contiguous binary subarray that can become all ones after flipping at most `k` zeroes.

Java file: [MaxConsecutiveOnesIII.java](MaxConsecutiveOnesIII.java)

## Reframe the problem

Flipping at most `k` zeroes is equivalent to finding:

```text
the longest window containing at most k zeroes
```

`k` is a budget, not the exact number of zeroes required.

## Window state and validity

```text
zeroCount → number of zeroes inside [left, right]
valid      → zeroCount <= k
invalid    → zeroCount > k
```

## Processing logic

```text
if nums[right] == 0:
    zeroCount++

while zeroCount > k:
    if nums[left] == 0:
        zeroCount--
    left++

update maxLength
```

`left` moves on every shrinking iteration. `zeroCount` decreases only when the outgoing value is zero.

## Invariant

> After shrinking, `[left, right]` contains at most `k` zeroes, `zeroCount` exactly describes that window, and `maxLength` is the largest valid length evaluated so far.

## Mistakes to remember

- Invalidity is `zeroCount > k`, not `zeroCount >= k`.
- Ones still contribute to the total length even though they do not change `zeroCount`.
- Update `maxLength` only after validity has been restored.

## Time + Space

```text
Time:  O(n)
Space: O(1)
```

---

# 7. Fruit Into Baskets

## Problem statement

Return the longest contiguous subarray containing at most two distinct fruit types.

Java file: [FruitIntoBaskets.java](FruitIntoBaskets.java)

## Important clues

```text
contiguous range
longest
at most two distinct values
frequency changes as values enter and leave
```

This is a variable-size longest valid window.

## Why a frequency map instead of a set?

A set knows only whether a type exists. When one occurrence leaves, the same fruit type may still appear elsewhere inside the window.

```text
key   → fruit type
value → number of occurrences inside [left, right]
```

Only remove a key when its frequency reaches zero.

## Processing logic

```text
increment frequency of fruits[right]

while map size > 2:
    decrement frequency of fruits[left]
    if frequency becomes zero:
        remove that key
    left++

update maxLength
```

## Invariant

> After shrinking, the map contains exactly the frequencies of the at-most-two fruit types inside `[left, right]`.

## Complexity detail

```text
Time:  O(n) average
Space: O(1)
```

The map never retains more than a small constant number of fruit types after validity is restored.

---

# 8. Permutation in String

## Problem statement

Return `true` if `s2` contains a substring that is a permutation of `s1`.

Java file: [PermutationInString.java](PermutationInString.java)

```text
s1 = "ab"
s2 = "eidbaooo"

window "ba" has the same frequencies as "ab"
answer = true
```

## Important clues

```text
substring length must equal s1.length()
ordering does not matter
character frequencies must match
```

This is a fixed-size window. A `HashSet` is insufficient because repeated characters matter.

## Two separate states

```text
targetFrequency → counts from s1; never changes
windowFrequency → counts from the current window in s2
requiredSize    → s1.length()
```

Example:

```text
s1 = "aab"
targetFrequency = {a=2, b=1}
```

## Processing logic

```text
for every right in s2:
    increment window frequency of incoming character

    if window size > requiredSize:
        decrement window frequency of outgoing character
        remove its key if the count becomes zero
        left++

    if window size == requiredSize
       and targetFrequency equals windowFrequency:
        return true

return false after all windows are exhausted
```

## Invariant

> At every evaluation point, the window contains exactly `s1.length()` characters, and `windowFrequency` contains exactly their counts. The target map remains unchanged.

## Mistakes to remember

- Shrink the window map, not the target map.
- Remove zero-count keys before using `Map.equals`.
- Do not return `false` after the first mismatch; later windows may match.
- `left` is the outgoing index and `right` is the incoming index.

## Map versus array

A map clearly demonstrates the frequency model. If the input is restricted to lowercase English letters, two `int[26]` arrays provide the same idea with constant-size storage and often lower overhead.

## Time + Space

With lowercase English letters or another fixed alphabet:

```text
Time:  O(n)
Space: O(1)
```

With a general character set represented by maps, describe the space as proportional to the number of distinct characters stored.

---

# Consolidated Pattern Comparison

| Problem | Form | Shrink condition | Evaluation timing | State |
| --- | --- | --- | --- | --- |
| Max Sum Size K | Fixed | `size > k` | `size == k` | Sum |
| Max Average Size K | Fixed | `size > k` | `size == k` | Sum |
| Max Vowels Size K | Fixed | `size > k` | `size == k` | Vowel count |
| Minimum Size Sum | Variable minimum | `sum >= target` | Inside `while`, before removal | Sum |
| No Repeated Characters | Variable longest | Incoming character already present | After restoring uniqueness | Set |
| Max Consecutive Ones III | Variable longest | `zeroCount > k` | After restoring budget | Zero count |
| Fruit Into Baskets | Variable longest | `map.size() > 2` | After restoring validity | Frequency map |
| Permutation in String | Fixed | `size > s1.length()` | Exact-size frequency match | Two frequency maps |

# Consolidated Articulation Drill

Before speaking, write only these keywords:

```text
clues
form
state
invalid/qualifying condition
answer timing
invariant
complexity
```

Then use short deterministic sentences:

> This is a **[fixed/variable] Sliding Window** because **[specific clues]**. `left` is the outgoing boundary and `right` is the incoming boundary. I maintain **[state]**, which equals **[precise meaning]**. I shrink when **[exact condition]**. I update **[answer]** **[exact timing]**. After the updates, **[invariant]**. Each pointer moves only forward, so the time complexity is `O(n)`.

During voice-to-text practice, self-corrections are normal. Evaluate the final corrected idea separately from transcription noise, but still retry once when a condition such as `>`, `>=`, or `==` was spoken incorrectly.
