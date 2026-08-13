# Hashing

> Goal: revise **problem → clues → pattern → invariant → data structure → algorithm → complexity → Java** quickly.

---

## 0. Core Mental Model

```text
Problem statement
      ↓
Important clues
      ↓
Candidate pattern
      ↓
Invariant
      ↓
What information must be remembered?
      ↓
Choose data structure
      ↓
Algorithm
      ↓
Java implementation
      ↓
Time + Space complexity
```

### Pattern vs Data Structure vs Invariant

| Concept | Meaning | Example |
|---|---|---|
| **Pattern** | Reusable problem-solving strategy | Hashing, Two Pointers, Sliding Window |
| **Data structure** | How information is stored | HashSet, HashMap, Array, Heap |
| **Invariant** | Condition/meaning that remains true during the algorithm | “`seen` contains all previously processed distinct values” |
| **Algorithm** | Exact steps for this problem | Check current value, add it, continue |

### What is an invariant?

Think:

> **What does my current set/map/window represent at this moment?**

Example:

```text
Before processing index i,
seen contains all distinct values from indices 0 ... i-1.
```

Do not confuse:

```text
Invariant  ≠  Edge case
Invariant  ≠  Condition/check
Invariant  ≠  Goal
```

---

# 1. Hashing — Big Idea

## Why Hashing?

```text
Store information now
        ↓
retrieve/check it quickly later
        ↓
average O(1) lookup/update
```

Typical clues:

```text
duplicate
unique
frequency
count occurrence
previously seen
lookup
matching/complement
common elements
first/last occurrence
group by property
anagram
index tracking
consecutive-value existence
```

---

# 2. HashSet vs HashMap

## Fast decision rule

```text
Need only:
"Does it exist / have I seen it?"
            ↓
          HashSet
```

```text
Need extra information:
count / index / group / metadata
            ↓
          HashMap
```

### Typical structures

```java
Set<Integer> seen = new HashSet<>();
```

```java
Map<Integer, Integer> map = new HashMap<>();
```

---

# 3. Variation Map

| Problem need | Structure | Stored information |
|---|---|---|
| Duplicate / seen before | `HashSet` | values |
| Two Sum indices | `HashMap` | number → index |
| Frequency | `HashMap` | value → count |
| First/last occurrence | `HashMap` | value → index |
| Unique common elements | `HashSet` | values |
| Grouping | `HashMap` | signature → list of items |
| Longest consecutive sequence | `HashSet` | values |
| Nearby duplicate within `k` | `HashSet` + sliding window | recent values only |

---

# 4. Contains Duplicate

## Problem

```text
Given an unsorted integer array,
return true if any value appears at least twice.
```

### Recognition

```text
Clue:
duplicate + previously seen

Pattern:
Hashing

Data structure:
HashSet

Why:
only existence matters
```

### Invariant

> Before processing the current element, `seen` contains all distinct elements processed earlier.

### Trace

```text
nums = [3, 6, 8, 6]

before 3: {}
before 6: {3}
before 8: {3, 6}
before final 6: {3, 6, 8}

6 already exists → duplicate found
```

### Java

```java
public boolean containsDuplicate(int[] nums) {
    Set<Integer> seen = new HashSet<>();

    for (int num : nums) {
        if (seen.contains(num)) {
            return true;
        }

        seen.add(num);
    }

    return false;
}
```

### Why check before adding?

Wrong order:

```text
add current
check current
```

would always find the current value itself.

Correct:

```text
check current
then add current
```

### Complexity

```text
Time:  O(n) average
Space: O(n)
```

### Important correction learned

```text
Looping through n elements → explains TIME
Storing up to n elements   → explains SPACE
```

---

# 5. Two Sum — HashMap for Index Tracking

## Problem

```text
Given an unsorted array and target,
return indices of two different numbers whose sum = target.
```

### Key idea

For current number:

```text
complement = target - current
```

Need:

```text
Have I seen complement before?
AND
where did I see it?
```

So:

```text
HashSet  → insufficient for returning index
HashMap  → number → previous index
```

### Invariant

> Before processing index `i`, the map contains previously processed numbers mapped to valid earlier indices.

### Java skeleton

```java
Map<Integer, Integer> map = new HashMap<>();

for (int i = 0; i < nums.length; i++) {
    int complement = target - nums[i];

    if (map.containsKey(complement)) {
        return new int[] { map.get(complement), i };
    }

    map.put(nums[i], i);
}
```

### Why insert after lookup?

Avoid matching an element with itself.

Example:

```text
nums = [3, 3], target = 6

first 3:
map empty
store 3 → 0

second 3:
complement 3 exists
return [0, 1]
```

### Complexity

```text
Time:  O(n) average
Space: O(n)
```

---

# 6. Frequency Counting

## Recognition

If the question asks:

```text
how many times?
exactly K times?
frequency?
occurrence count?
```

Use:

```text
HashMap<value, count>
```

### Template

```java
Map<Integer, Integer> frequency = new HashMap<>();

for (int value : nums) {
    frequency.put(
        value,
        frequency.getOrDefault(value, 0) + 1
    );
}
```

### Invariant

> After processing a prefix, the map stores the exact frequency of every value in that processed prefix.

---

# 7. Valid Anagram

## Problem idea

Two strings are anagrams if every character appears the same number of times.

### Important clue

```text
Same unique characters is NOT enough.
Frequency matters.
```

Example:

```text
"aab"
"abb"
```

Unique-character sets:

```text
{a, b}
{a, b}
```

but they are not anagrams.

### One-map approach

```text
1. If lengths differ → false
2. Count every character in s
3. Process t:
   decrement each count
4. If a count becomes negative → false
5. Otherwise → true
```

### Meaning of count

```text
positive → unmatched copies from s remain
zero     → perfectly matched so far
negative → t used too many copies
```

### Invariant

> The map tells how many copies of each character from `s` are still unmatched by the processed part of `t`.

More formal:

```text
map[c]
=
count of c in all of s
-
count of c seen so far in t
```

### Why can we stop on negative?

Once a count becomes negative:

```text
t has used that character too many times
```

Later characters cannot fix that.

### Why no final zero-check is needed?

If:

```text
s.length() == t.length()
```

and no count goes negative, then `t` has consumed exactly as many total characters as `s` supplied.

Therefore nothing can remain positive at the end.

---

# 8. Unique Common Elements

## Problem

```text
nums1 = [1, 2, 2, 4]
nums2 = [2, 2, 3, 4]

result = [2, 4]
```

### Approach

```text
set1 = unique values from nums1
resultSet = unique common values found from nums2
```

### Why HashSet?

Need:

```text
Does this value exist in nums1?
```

No frequency/index required.

### Invariant

> Before processing the current element of `nums2`, `set1` contains all unique values from `nums1`, and `resultSet` contains all unique intersection values found from the processed part of `nums2`.

### Complexity

If:

```text
n = nums1 length
m = nums2 length
```

then:

```text
Time:  O(n + m)
Space: O(n + k)
```

where `k` is number of unique intersection values.

Often simplified to `O(n)` auxiliary space if building the set from `nums1`.

---

# 9. First / Last Occurrence

Same map shape:

```text
value → index
```

Different update rule.

## First occurrence

```text
Store only if absent.
```

Example:

```text
"abca"

a → 0
b → 1
c → 2
```

Second `a` does not overwrite index `0`.

## Last occurrence

```text
Update every time.
```

Result:

```text
a → 3
b → 1
c → 2
```

---

# 10. First Unique Character

## Problem

Find first character whose total frequency is exactly `1`.

Example:

```text
"leetcode"
```

### Two-pass strategy

```text
Pass 1:
character → frequency

Pass 2:
scan ORIGINAL STRING in order
return first index whose frequency == 1
```

### Important insight

Do not iterate the `HashMap` to determine “first”.

Why?

```text
Problem cares about original string order.
```

### First-pass invariant

> After processing index `i`, the map contains correct frequencies for characters seen so far.

### Second-pass invariant

> Every index before the current index has already been proven non-unique.

### Complexity

```text
Time:  O(n)
Space: O(k)   where k = distinct characters
```

Generalized worst case:

```text
O(n) space
```

---

# 11. Longest Consecutive Sequence

## Problem

```text
[100, 4, 200, 1, 3, 2]

longest consecutive sequence:
1, 2, 3, 4

answer = 4
```

### Important distinction

```text
sequence length ≠ frequency
```

Frequency:

```text
[2,2,2] → frequency of 2 = 3
```

Consecutive length:

```text
[1,2,3,4] → length = 4
```

### Why HashSet?

Need fast existence checks:

```text
Does x - 1 exist?
Does x + 1 exist?
```

### Key trick

Only start counting from `x` if:

```text
x - 1 does NOT exist
```

That means `x` is the true start of the sequence.

Example:

```text
1,2,3,4

1:
0 absent → start

2:
1 exists → skip

3:
2 exists → skip

4:
3 exists → skip
```

### Invariant

> When counting from `x`, `x - 1` is absent, so `x` is the first value of that consecutive sequence. While counting, every value from `x` through the current value exists in the set.

### Complexity

HashSet approach:

```text
Time:  O(n) average
Space: O(n)
```

Sorting approach is valid but:

```text
O(n log n)
```

HashSet is preferable for the linear-time target.

---

# 12. Grouping by Property — Group Anagrams

## Problem

```text
["eat", "tea", "tan", "ate", "nat", "bat"]
```

Result conceptually:

```text
["eat", "tea", "ate"]
["tan", "nat"]
["bat"]
```

### Key idea

```text
item
 ↓
compute shared property/signature
 ↓
use signature as HashMap key
 ↓
append original item to that group
```

### Sorted signature

```text
"eat" → "aet"
"tea" → "aet"
"ate" → "aet"

"tan" → "ant"
"nat" → "ant"
```

### Map

```text
"aet" → ["eat", "tea", "ate"]
"ant" → ["tan", "nat"]
"abt" → ["bat"]
```

### Why HashMap instead of scanning groups?

Using a collection of groups:

```text
for each new word
    scan groups
    compare against representative
```

can approach:

```text
O(n² · k log k)
```

Using a HashMap:

```text
compute signature
lookup group directly
```

gives:

```text
O(n · k log k)
```

where:

```text
n = number of words
k = word length
```

### Java

```java
public List<List<String>> groupAnagrams(String[] input) {

    Map<String, List<String>> map = new HashMap<>();

    for (String word : input) {

        char[] chars = word.toCharArray();
        Arrays.sort(chars);

        String key = new String(chars);

        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<>());
        }

        map.get(key).add(word);
    }

    return new ArrayList<>(map.values());
}
```

### `computeIfAbsent`

Equivalent forms:

```java
if (!map.containsKey(key)) {
    map.put(key, new ArrayList<>());
}
map.get(key).add(word);
```

Short form:

```java
map.computeIfAbsent(
    key,
    k -> new ArrayList<>()
).add(word);
```

Meaning:

```text
If key is absent:
    create a new list

Then:
    return the list associated with key
```

### Important bug learned

Wrong:

```java
if (map.containsKey(key)) {
    map.put(key, new ArrayList<>());
}
```

This would overwrite an existing group.

Correct:

```java
if (!map.containsKey(key)) {
    map.put(key, new ArrayList<>());
}
```

### Why add `word`, not `key`?

Wrong:

```text
"aet" → ["aet", "aet", "aet"]
```

Correct:

```text
"aet" → ["eat", "tea", "ate"]
```

Rule:

```text
key   = group identity
value = original items in that group
```

### Invariant

> After processing some words, every map key represents one anagram signature, and its list contains all previously processed words having that signature.

### Complexity

```text
Sort one word:
O(k log k)

n words:
O(n · k log k)
```

Space:

```text
O(n · k)
```

---

# 13. Matching / Difference by `k`

## Problem

Determine whether two different elements have absolute difference `k`.

Example:

```text
nums = [3, 1, 7, 5]
k = 2
```

Possible:

```text
3 - 1 = 2
7 - 5 = 2
```

### One-pass HashSet

For current `x`, check:

```text
x - k
x + k
```

among previously seen values.

### Invariant

> Before processing current `x`, `seen` contains all distinct values processed earlier.

### Important distinction

```text
"check x-k and x+k"
=
algorithm condition

NOT invariant
```

### Special case: `k = 0`

Then the problem asks whether there is a duplicate.

A whole-array HashSet loses duplicate-count information.

One-pass checking avoids matching the same position with itself.

### Complexity

```text
Time:  O(n) average
Space: O(n)
```

---

# 14. First Value Whose Duplicate Appears

Example:

```text
[7, 3, 5, 3, 9, 7]
```

Answer:

```text
3
```

### Best structure

`HashSet`, not `HashMap`.

Why?

Need only:

```text
Have I seen this value before?
```

Do not store frequency unless the problem requires it.

### Recognition rule

```text
at least twice / seen before
→ HashSet
```

But:

```text
exactly 3 times / how many times
→ HashMap<value, count>
```

---

# 15. Nearby Duplicate Within Distance `k`

## Problem

```text
nums = [1, 2, 3, 1]
k = 3
→ true
```

because indices `0` and `3` differ by `3`.

But:

```text
nums = [1, 2, 3, 1]
k = 2
→ false
```

### Pattern combination

```text
Hashing
+
Fixed-size Sliding Window
```

Hashing:

```text
fast existence lookup
```

Sliding window:

```text
keep only the recent k positions
```

### Invariant

> Before processing index `i`, the set contains the values from indices `max(0, i-k)` through `i-1`.

### Java

```java
public boolean containsNearbyDuplicate(int[] nums, int k) {
    Set<Integer> window = new HashSet<>();

    for (int i = 0; i < nums.length; i++) {

        if (window.contains(nums[i])) {
            return true;
        }

        window.add(nums[i]);

        if (window.size() > k) {
            window.remove(nums[i - k]);
        }
    }

    return false;
}
```

### Trace

```text
nums = [1,2,3,1]
k = 2

before i=0: {}
add 1

before i=1: {1}
add 2

before i=2: {1,2}
add 3
remove nums[0] = 1

before i=3: {2,3}
current 1 not found
→ false
```

### Complexity

```text
Time:  O(n) average
Space: O(k)
```

This is better than storing the entire history.

---

# 16. Hashing Templates

## A. Seen-before template

```java
Set<Integer> seen = new HashSet<>();

for (int value : nums) {
    if (seen.contains(value)) {
        // found previously seen value
    }

    seen.add(value);
}
```

---

## B. Frequency template

```java
Map<Integer, Integer> freq = new HashMap<>();

for (int value : nums) {
    freq.put(
        value,
        freq.getOrDefault(value, 0) + 1
    );
}
```

---

## C. Value → Index template

```java
Map<Integer, Integer> indexMap = new HashMap<>();

for (int i = 0; i < nums.length; i++) {
    indexMap.put(nums[i], i);
}
```

---

## D. Grouping template

```java
Map<String, List<String>> groups = new HashMap<>();

for (String word : words) {
    String key = createSignature(word);

    groups
        .computeIfAbsent(key, k -> new ArrayList<>())
        .add(word);
}
```

---

## E. Sliding HashSet template

```java
Set<Integer> window = new HashSet<>();

for (int i = 0; i < nums.length; i++) {

    // use window

    window.add(nums[i]);

    if (window.size() > k) {
        window.remove(nums[i - k]);
    }
}
```

---

# 17. Common Mistakes From This Session

## Mistake 1 — Confusing edge case with invariant

Wrong idea:

```text
"If array length is 0 or 1..."
```

That is an edge case.

Invariant example:

```text
Before processing current value,
the set contains all previously processed distinct values.
```

---

## Mistake 2 — Confusing condition with invariant

Condition:

```text
check x-k and x+k
```

Invariant:

```text
seen contains all previously processed distinct values
```

---

## Mistake 3 — Using HashMap when HashSet is enough

Ask:

```text
Do I need extra information?
```

If no:

```text
HashSet
```

If yes:

```text
HashMap
```

---

## Mistake 4 — Frequency vs sequence length

```text
frequency = repetitions of SAME value
sequence length = number of consecutive DIFFERENT values
```

---

## Mistake 5 — Complexity misses outer dimension

If:

```text
n words
each costs O(k log k)
```

then total is:

```text
O(n · k log k)
```

not:

```text
O(k log k)
```

---

## Mistake 6 — Time vs space reasoning

```text
n loop iterations → time

n stored elements → space
```

---

## Mistake 7 — HashMap overwrite in grouping

Wrong:

```java
if (map.containsKey(key)) {
    map.put(key, new ArrayList<>());
}
```

Correct:

```java
if (!map.containsKey(key)) {
    map.put(key, new ArrayList<>());
}
```

---

# 18. Fast Recognition Checklist

When reading a new problem, ask:

```text
1. Do I need fast lookup?
2. What exactly must I remember?
3. Only existence?
   → HashSet
4. Frequency?
   → HashMap<value, count>
5. Index?
   → HashMap<value, index>
6. Group members?
   → HashMap<signature, List<...>>
7. Do I need only recent values?
   → Hashing + Sliding Window
8. Is input sorted?
   → maybe Two Pointers / Binary Search instead
```

---

# 19. Pattern Cheat Sheet

## Pattern

**Hashing**

## When to use

```text
Fast lookup of previously stored information.
```

## Important clues

```text
duplicate
frequency
previously seen
lookup
complement
common
anagram
grouping
first/last occurrence
index tracking
```

## Core invariant examples

```text
seen contains all previously processed distinct values
```

```text
freq contains exact counts for the processed prefix
```

```text
map contains value → earlier index
```

```text
group map contains signature → all processed matching items
```

## Typical data structures

```text
HashSet
HashMap
```

## Common variations

```text
existence
frequency
index tracking
grouping
complement lookup
consecutive existence
bounded/recent lookup
```

## Common mistakes

```text
using HashMap when HashSet is enough
forgetting duplicate behavior
checking after insertion
wrong invariant
wrong map update
forgetting outer complexity dimension
assuming HashMap preserves original order
```

## Complexity

Typical:

```text
Time:  O(n) average
Space: O(n)
```

But depends on the exact problem.

Examples:

```text
Group Anagrams:
O(n · k log k)

Nearby Duplicate:
O(n) time
O(k) space
```

---

# 20. Current Learning Status

```text
Hashing:
Level 2 — can reproduce and explain solutions

Some Level 3 behavior:
- recognized HashSet + bounded-window idea independently
- selected HashSet for consecutive-value existence
- understood grouping signature after discussion
```

### Weak areas to revisit

```text
[ ] State invariant precisely
[ ] Distinguish invariant vs condition
[ ] Decide HashSet vs HashMap from required information
[ ] Complexity with multiple dimensions
[ ] Do not confuse frequency with length/count of sequence
```

### Revisit after 3–4 sessions

```text
[ ] Contains Duplicate
[ ] Two Sum
[ ] Valid Anagram
[ ] Group Anagrams
[ ] Longest Consecutive Sequence
[ ] Nearby Duplicate within k
```

---

# 21. One-Page Memory Map

```text
                    HASHING
                       |
        +--------------+--------------+
        |              |              |
     EXISTENCE       EXTRA INFO      GROUPING
        |              |              |
     HashSet          HashMap       HashMap
        |              |              |
   seen before?      count?        signature
   duplicate?        index?            ↓
   common?           frequency?     List/items
   neighbor exists?
        |
        +-----------------------------+
        |
   bounded history?
        ↓
 HashSet + Sliding Window
```

```text
Remember:

"WHAT information must I remember?"
            ↓
decides the data structure.
```

---

## Next Pattern

**Two Pointers**

Focus will be:

```text
clues
pointer movement
sorted input
invariant
same-direction vs opposite-direction pointers
when NOT to use hashing
```
