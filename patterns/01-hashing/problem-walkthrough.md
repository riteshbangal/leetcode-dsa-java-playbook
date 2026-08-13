# Hashing Problem Revision Notes

> Revision flow used for every problem:
>
> ```text
> Problem statement
>       ↓
> Important clues
>       ↓
> Candidate pattern
>       ↓
> Invariant
>       ↓
> What information must be remembered?
>       ↓
> Choose data structure
>       ↓
> Algorithm
>       ↓
> Key Java
>       ↓
> Time + Space
> ```

---

## Implemented Java Files

- [ContainsDuplicate.java](ContainsDuplicate.java)
- [TwoSum.java](TwoSum.java)
- [ValidAnagram.java](ValidAnagram.java)
- [UniqueCommonElements.java](UniqueCommonElements.java)
- [FirstLastOccurrenceTracking.java](FirstLastOccurrenceTracking.java)
- [FirstUniqueCharacter.java](FirstUniqueCharacter.java)
- [LongestConsecutiveSequence.java](LongestConsecutiveSequence.java)
- [GroupAnagrams.java](GroupAnagrams.java)
- [MatchingDifferenceByK.java](MatchingDifferenceByK.java)
- [FirstDuplicateValue.java](FirstDuplicateValue.java)
- [FirstThirdOccurrence.java](FirstThirdOccurrence.java)
- [ContainsNearbyDuplicate.java](ContainsNearbyDuplicate.java)

---

# 1. Contains Duplicate

## Problem statement

Given an unsorted integer array, return `true` if any value appears at least twice.

Java file: [ContainsDuplicate.java](ContainsDuplicate.java)

Example:

```text
[3, 6, 8, 6] → true
[3, 6, 8]    → false
```

## Important clues

```text
duplicate
appears at least twice
unsorted
existence only
```

We only need to answer:

```text
Have I seen this value before?
```

## Candidate pattern

**Hashing**

Why:

```text
Need fast lookup of previously processed values
→ Hashing is a strong candidate
```

Without hashing, comparing every pair can become `O(n²)`.

## Invariant

> Before processing the current element, the set contains all distinct values processed earlier.

Example:

```text
nums = [3, 6, 8, 6]

before 3 → {}
before 6 → {3}
before 8 → {3, 6}
before 6 → {3, 6, 8}
```

## What information must be remembered?

```text
Only whether a value was seen before
```

## Choose data structure

```text
HashSet<Integer>
```

## Algorithm

```text
create empty set

for each number:
    if number already exists:
        return true

    add number

return false
```

## Key Java

```java
if (seen.contains(num)) {
    return true;
}
seen.add(num);
```

Remember:

```text
CHECK before ADD
```

## Time + Space

```text
Time:  O(n) average
Space: O(n)
```

---

# 2. Two Sum — Unsorted Array, Return Indices

## Problem statement

Given an unsorted array and a target, return the indices of two different numbers whose sum equals the target.

Java file: [TwoSum.java](TwoSum.java)

Example:

```text
nums = [2, 7, 11, 15]
target = 9

answer = [0, 1]
```

## Important clues

```text
two numbers
sum = target
unsorted
return indices
```

Need:

```text
partner value + its earlier index
```

## Candidate pattern

**Hashing / Complement lookup**

For current value `x`:

```text
needed = target - x
```

Ask:

```text
Have I already seen needed?
```

## Invariant

> Before processing index `i`, the map contains previously processed numbers mapped to valid earlier indices.

## What information must be remembered?

```text
number + index where it was seen
```

## Choose data structure

```text
HashMap<Integer, Integer>
number → index
```

A `HashSet` is insufficient because it cannot return the earlier index.

## Algorithm

```text
create empty map

for each index i:
    complement = target - nums[i]

    if complement exists in map:
        return [stored index, i]

    store nums[i] → i
```

## Key Java

```java
int complement = target - nums[i];

if (map.containsKey(complement)) {
    return new int[] {map.get(complement), i};
}

map.put(nums[i], i);
```

Remember:

```text
LOOKUP before INSERT
```

## Time + Space

```text
Time:  O(n) average
Space: O(n)
```

---

# 3. Valid Anagram

## Problem statement

Given two strings `s` and `t`, return `true` if `t` is an anagram of `s`.

Java file: [ValidAnagram.java](ValidAnagram.java)

Example:

```text
"anagram"
"nagaram"
→ true
```

```text
"aab"
"abb"
→ false
```

## Important clues

```text
same characters
same number of occurrences
order does not matter
```

Important:

```text
Same UNIQUE characters is not enough.
Frequency must match.
```

## Candidate pattern

**Hashing / Frequency counting**

Need:

```text
character → how many times it appears
```

## Invariant

Using the one-map approach:

> While processing `t`, the map stores how many copies of each character from `s` are still unmatched.

More formally:

```text
map[c]
=
count of c in all of s
-
count of c processed so far in t
```

Meaning:

```text
positive → copies still available
zero     → exactly matched so far
negative → t used too many copies
```

## What information must be remembered?

```text
frequency per character
```

## Choose data structure

```text
HashMap<Character, Integer>
```

## Algorithm

```text
if lengths differ:
    return false

count all characters of s

for each character of t:
    decrement its count

    if count becomes negative:
        return false

return true
```

## Key Java

```java
freq.put(c, freq.getOrDefault(c, 0) + 1);
```

and:

```java
freq.put(c, freq.getOrDefault(c, 0) - 1);

if (freq.get(c) < 0) {
    return false;
}
```

## Time + Space

```text
Time:  O(n)
Space: O(k)
```

`k` = number of distinct characters.

---

# 4. Unique Common Elements of Two Arrays

## Problem statement

Return values that appear in both arrays, with each result value appearing only once.

Java file: [UniqueCommonElements.java](UniqueCommonElements.java)

Example:

```text
nums1 = [1, 2, 2, 4]
nums2 = [2, 2, 3, 4]

result = [2, 4]
```

## Important clues

```text
common elements
appear in both arrays
result must be unique
```

Need only:

```text
Does this value exist in the other array?
```

## Candidate pattern

**Hashing / Existence lookup**

## Invariant

> `set1` contains all unique values from `nums1`, and `resultSet` contains all unique common values found from the already-processed part of `nums2`.

## What information must be remembered?

```text
existence only
```

## Choose data structure

```text
HashSet for nums1
HashSet for result
```

## Algorithm

```text
put nums1 into set1

for each value in nums2:
    if value exists in set1:
        add it to resultSet

return resultSet
```

## Key Java

```java
if (set1.contains(num)) {
    resultSet.add(num);
}
```

## Time + Space

If:

```text
n = nums1.length
m = nums2.length
```

then:

```text
Time:  O(n + m)
Space: O(n + k)
```

`k` = number of unique common values.

---

# 5. First / Last Occurrence Tracking

## Problem statement

Track where a value or character first or last appeared.

Java file: [FirstLastOccurrenceTracking.java](FirstLastOccurrenceTracking.java)

Example:

```text
s = "abca"
```

First occurrence:

```text
a → 0
b → 1
c → 2
```

Last occurrence:

```text
a → 3
b → 1
c → 2
```

## Important clues

```text
first occurrence
last occurrence
where did I see it?
index
```

## Candidate pattern

**Hashing / Index tracking**

## Invariant

For first occurrence:

> The map stores the earliest index seen so far for every processed value.

For last occurrence:

> The map stores the most recent index seen so far for every processed value.

## What information must be remembered?

```text
value → index
```

## Choose data structure

```text
HashMap<Value, Integer>
```

## Algorithm

First occurrence:

```text
store only if key is absent
```

Last occurrence:

```text
overwrite every time
```

## Key Java

```java
map.putIfAbsent(value, i);
```

```java
map.put(value, i);
```

## Time + Space

```text
Time:  O(n)
Space: O(n)
```

---

# 6. First Unique Character in a String

## Problem statement

Return the index of the first character whose total frequency is exactly `1`.

Java file: [FirstUniqueCharacter.java](FirstUniqueCharacter.java)

Example:

```text
"leetcode" → 0
```

## Important clues

```text
first
unique
frequency exactly 1
original order matters
```

This combines:

```text
frequency
+
order
```

## Candidate pattern

**Hashing / Frequency counting + second scan**

## Invariant

First pass:

> After processing a prefix, the map contains correct frequencies for all characters seen in that prefix.

Second pass:

> Every index before the current one has already been proven non-unique.

## What information must be remembered?

```text
character frequency
```

## Choose data structure

```text
HashMap<Character, Integer>
```

## Algorithm

```text
pass 1:
    count every character

pass 2:
    scan original string from left to right

    first character with count 1:
        return its index
```

Important:

```text
Do NOT iterate the HashMap to determine "first".
```

## Key Java

```java
if (freq.get(s.charAt(i)) == 1) {
    return i;
}
```

## Time + Space

```text
Time:  O(n)
Space: O(k)
```

---

# 7. Longest Consecutive Sequence

## Problem statement

Given an unsorted integer array, return the length of the longest consecutive sequence of values.

Java file: [LongestConsecutiveSequence.java](LongestConsecutiveSequence.java)

Example:

```text
[100, 4, 200, 1, 3, 2]

1, 2, 3, 4
→ length 4
```

## Important clues

```text
consecutive values
unsorted input
longest sequence
positions do not matter
```

Important:

```text
sequence length ≠ frequency
```

## Candidate pattern

**Hashing / Fast existence lookup**

Need repeated checks:

```text
Does x - 1 exist?
Does x + 1 exist?
```

## Invariant

When counting from `x`:

> `x - 1` does not exist, so `x` is the true beginning of that consecutive sequence.

While counting:

> Every integer from `x` up to the current value exists in the set.

## What information must be remembered?

```text
whether each value exists
```

## Choose data structure

```text
HashSet<Integer>
```

## Algorithm

```text
put all values into a set

for each value x:
    if x - 1 exists:
        skip

    otherwise:
        start counting from x

        while x+1, x+2, ... exist:
            continue

        update maximum length
```

## Key Java

```java
if (!set.contains(num - 1)) {
    int current = num;
    int length = 1;

    while (set.contains(current + 1)) {
        current++;
        length++;
    }
}
```

## Time + Space

```text
Time:  O(n) average
Space: O(n)
```

Sorting is valid but costs:

```text
O(n log n)
```

---

# 8. Group Anagrams — Grouping by Property

## Problem statement

Group strings that are anagrams of each other.

Java file: [GroupAnagrams.java](GroupAnagrams.java)

Example:

```text
["eat", "tea", "tan", "ate", "nat", "bat"]
```

Groups:

```text
["eat", "tea", "ate"]
["tan", "nat"]
["bat"]
```

## Important clues

```text
group together
same property
anagrams
```

Key question:

```text
What common signature can identify the group?
```

Example:

```text
"eat" → "aet"
"tea" → "aet"
"ate" → "aet"
```

## Candidate pattern

**Hashing / Grouping by property**

General idea:

```text
item
↓
compute signature
↓
use signature as HashMap key
↓
append original item to that group
```

## Invariant

> After processing some words, every map key represents one anagram signature, and its list contains all processed words having that signature.

## What information must be remembered?

```text
signature → original words in that group
```

## Choose data structure

```text
HashMap<String, List<String>>
```

## Algorithm

```text
for each word:
    convert word to char[]
    sort chars
    convert back to String → key

    if key absent:
        create new list

    add ORIGINAL word to the list

return all map values
```

## Key Java

```java
char[] chars = word.toCharArray();
Arrays.sort(chars);
String key = new String(chars);
```

```java
map.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
```

```java
return new ArrayList<>(map.values());
```

Remember:

```text
key = group identity
word = original item
```

## Time + Space

If:

```text
n = number of words
k = word length
```

then:

```text
Time:  O(n · k log k)
Space: O(n · k)
```

HashMap avoids scanning all existing groups.

---

# 9. Pair With Absolute Difference `k`

## Problem statement

Determine whether two different array elements have absolute difference exactly `k`.

Java file: [MatchingDifferenceByK.java](MatchingDifferenceByK.java)

Example:

```text
nums = [3, 1, 7, 5]
k = 2
→ true
```

## Important clues

```text
two different elements
difference exactly k
existence only
```

Possible partners for current `x`:

```text
x - k
x + k
```

## Candidate pattern

**Hashing / Matching-complement lookup**

## Invariant

> Before processing the current number, `seen` contains all distinct values processed earlier.

Important:

```text
check x-k / x+k
```

is a condition, not the invariant.

## What information must be remembered?

```text
only whether a possible partner was seen
```

## Choose data structure

```text
HashSet<Integer>
```

## Algorithm

```text
create empty seen set

for each x:
    if x-k exists OR x+k exists:
        return true

    add x

return false
```

## Key Java

```java
if (seen.contains(x - k) || seen.contains(x + k)) {
    return true;
}

seen.add(x);
```

## Time + Space

```text
Time:  O(n) average
Space: O(n)
```

---

# 10. First Value Whose Second Occurrence Is Encountered

## Problem statement

Return the first value that becomes a duplicate while scanning left to right.

Java file: [FirstDuplicateValue.java](FirstDuplicateValue.java)

Example:

```text
[7, 3, 5, 3, 9, 7]

answer = 3
```

## Important clues

```text
first value that appears twice
left-to-right scan
second occurrence
existence only
```

We do **not** need the exact count.

## Candidate pattern

**Hashing / Seen-before detection**

## Invariant

> Before processing the current value, the set contains all distinct values encountered earlier.

## What information must be remembered?

```text
existence only
```

## Choose data structure

```text
HashSet<Integer>
```

## Algorithm

```text
scan left to right

if current already in set:
    return current

otherwise:
    add current
```

## Key Java

```java
if (seen.contains(num)) {
    return num;
}
seen.add(num);
```

## Time + Space

```text
Time:  O(n)
Space: O(n)
```

---

# 11. First Value Whose Third Occurrence Is Encountered

## Problem statement

Return the first value whose third occurrence is reached while scanning from left to right.

Java file: [FirstThirdOccurrence.java](FirstThirdOccurrence.java)

Example:

```text
[4, 7, 4, 4, 7]

4 reaches count 3 first
→ answer = 4
```

## Important clues

```text
third occurrence
exact count matters
how many times?
```

A `HashSet` cannot distinguish:

```text
seen once
seen twice
seen three times
```

## Candidate pattern

**Hashing / Frequency counting**

## Invariant

> After processing the current prefix, the map stores the exact occurrence count of every value in that prefix.

## What information must be remembered?

```text
value → occurrence count
```

## Choose data structure

```text
HashMap<Integer, Integer>
```

## Algorithm

```text
for each number:
    increment its count

    if new count == 3:
        return number
```

## Key Java

```java
int count = freq.getOrDefault(num, 0) + 1;
freq.put(num, count);

if (count == 3) {
    return num;
}
```

## Time + Space

```text
Time:  O(n)
Space: O(n)
```

---

# 12. Contains Nearby Duplicate — Distance ≤ `k`

## Problem statement

Return `true` if the same value appears twice within index distance `k`.

Java file: [ContainsNearbyDuplicate.java](ContainsNearbyDuplicate.java)

Example:

```text
nums = [1, 2, 3, 1]
k = 3
→ true
```

But:

```text
nums = [1, 2, 3, 1]
k = 2
→ false
```

## Important clues

```text
duplicate
within distance k
only recent elements matter
```

Critical observation:

```text
We do NOT need the full history.
```

Anything farther than `k` positions behind cannot form a valid pair with the current index.

## Candidate pattern

**Hashing + Fixed-Size Sliding Window**

Why:

```text
Hashing:
fast existence lookup

Sliding Window:
keep only the last k relevant positions
```

## Invariant

> Before processing index `i`, the set contains values from indices `max(0, i-k)` through `i-1`.

Example with `k = 2`:

```text
before i=3:
only indices 1 and 2 are relevant
```

## What information must be remembered?

```text
values from only the previous k positions
```

## Choose data structure

```text
HashSet<Integer>
```

## Algorithm

```text
create empty window set

for each index i:
    if nums[i] already exists in window:
        return true

    add nums[i]

    if window grew beyond k:
        remove the value that became too old

return false
```

## Key Java

```java
if (window.contains(nums[i])) {
    return true;
}

window.add(nums[i]);

if (window.size() > k) {
    window.remove(nums[i - k]);
}
```

## Time + Space

```text
Time:  O(n) average
Space: O(k)
```

---

# Final Recognition Map

```text
                         HASHING
                            |
        +-------------------+-------------------+
        |                   |                   |
     EXISTENCE           EXTRA INFO          GROUPING
        |                   |                   |
     HashSet              HashMap             HashMap
        |                   |                   |
 seen before?            count?            signature?
 duplicate?              index?                |
 common?                 occurrence?           ↓
 neighbor exists?                          List/items
        |
        +--------------------+
        |
  only recent values matter?
        |
        ↓
 HashSet + Sliding Window
```

# Fast Decision Rules

```text
Need only "Does it exist?"
→ HashSet
```

```text
Need "How many times?"
→ HashMap<value, count>
```

```text
Need "Where did I see it?"
→ HashMap<value, index>
```

```text
Need "Which group does it belong to?"
→ HashMap<signature, List<items>>
```

```text
Need "Only values from recent k positions?"
→ HashSet + Sliding Window
```

# Complexity Memory Rules

```text
Loop through n items
→ usually O(n) time
```

```text
Store up to n values
→ O(n) space
```

```text
n words
each word costs O(k log k)
→ O(n · k log k)
```

```text
Two input arrays:
length n and m
→ often O(n + m)
```

# Mistakes to Revisit

```text
[ ] Invariant vs edge case
[ ] Invariant vs condition
[ ] HashSet vs HashMap
[ ] Frequency vs sequence length
[ ] Time reasoning vs space reasoning
[ ] Multiple complexity dimensions: n, m, k
[ ] Do not overwrite an existing group accidentally
[ ] Preserve original order when problem asks for "first"
```
