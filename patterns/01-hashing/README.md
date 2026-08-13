# Hashing

Hashing is useful when a problem needs fast lookup, counting, grouping, or remembering information from elements already processed.

Detailed problem-by-problem walkthrough notes are kept separately in [problem-walkthrough.md](problem-walkthrough.md).

## Core Mental Model

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

## Pattern vs Data Structure vs Invariant

| Concept | Meaning | Example |
| --- | --- | --- |
| Pattern | Reusable problem-solving strategy | Hashing, Two Pointers, Sliding Window |
| Data structure | How information is stored | `HashSet`, `HashMap`, Array, Heap |
| Invariant | Meaning that remains true during the algorithm | `seen` contains previously processed values |
| Algorithm | Exact steps for a specific problem | Check current value, then add it |

## When Hashing Helps

Use hashing when the problem asks for fast lookup or remembered information:

- Duplicate detection
- Frequency counting
- Previously seen values
- Complement lookup
- Common elements
- First or last occurrence
- Grouping using a computed key
- Consecutive-value existence
- Bounded recent lookup

## HashSet vs HashMap

Use `HashSet` when only membership matters:

```text
Does this value exist?
Have I seen this before?
Is there a duplicate?
```

Use `HashMap` when each key needs extra information:

```text
count
index
last seen position
grouped values
computed metadata
```

## Common Invariant

For one-pass hashing, the common invariant is:

> Before processing the current element, the set or map contains information from elements already processed.

The exact meaning depends on the problem:

- `seen` contains previous distinct values.
- `frequency` contains counts from the processed prefix.
- `indexByValue` maps previous values to valid earlier indices.
- `groups` maps each computed signature to matching items seen so far.

## Variation Map

| Problem need | Structure | Stored information |
| --- | --- | --- |
| Duplicate / seen before | `HashSet` | values |
| Complement lookup with indices | `HashMap` | value -> index |
| Frequency counting | `HashMap` | value -> count |
| First occurrence | `HashMap` | value -> first index |
| Last occurrence | `HashMap` | value -> latest index |
| Unique common elements | `HashSet` | values |
| Grouping | `HashMap` | signature -> list of items |
| Longest consecutive sequence | `HashSet` | values |
| Nearby duplicate within `k` | `HashSet` + sliding window | recent values only |

## Java Tools

- `HashSet<T>`
- `HashMap<K, V>`
- `contains`
- `containsKey`
- `add`
- `put`
- `get`
- `getOrDefault`
- `merge`
- `computeIfAbsent`

## Complexity

Typical hashing solutions:

```text
Time:  O(n) average
Space: O(n)
```

Hash-based operations are expected `O(1)` on average, but worst-case behavior can degrade when many keys collide.

For grouped string problems, remember to include key construction cost. For example, sorting each word for an anagram signature costs `O(k log k)` per word.

## Common Mistakes

- Checking and inserting in the wrong order
- Accidentally using the same element twice
- Losing required index information
- Using `HashSet` when count, index, or grouping data is required
- Using `HashMap` when membership alone is enough
- Assuming hash iteration order matches input order
- Confusing frequency with consecutive-sequence length
- Forgetting the outer dimension in complexity analysis
- Overwriting a group list instead of appending to it

## Revision Checklist

- Can I identify whether I need membership, frequency, index, or grouping?
- Can I state what the set or map represents before each iteration?
- Am I checking before inserting when the same element must not be reused?
- Do I need `HashMap` instead of `HashSet` because extra information is required?
- Have I included key construction cost in time complexity?
- Have I analyzed both average-case and space complexity?
