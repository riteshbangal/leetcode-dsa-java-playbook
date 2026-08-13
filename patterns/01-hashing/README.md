# Hashing

Hashing is useful when a problem needs fast lookup, counting, grouping, or remembering information from elements already processed.

## Recognition Clues

- Duplicate detection
- Frequency counting
- Previously seen values
- Complement lookup
- Grouping using a computed key

## HashSet vs HashMap

Use `HashSet` when only membership matters:

- Have I seen this value before?
- Does this value already exist?
- Can I detect a duplicate?

Use `HashMap` when each key needs associated information:

- Frequency count
- Index
- Last seen position
- Grouped values
- Computed metadata

## Core Invariant

The set or map represents information from elements that have already been processed. At each step, the current element is checked against that stored information before or after updating it, depending on the problem.

## Complexity

- Typical time: `O(n)`
- Typical space: `O(n)`

Hash-based operations are expected `O(1)` on average, but worst-case behavior can degrade when many keys collide.

## Common Mistakes

- Checking and inserting in the wrong order
- Accidentally using the same element twice
- Losing required index information
- Assuming iteration order
- Ignoring collision and worst-case considerations

## Java Tools

- `HashMap<K, V>`
- `HashSet<T>`
- `containsKey`
- `contains`
- `get`
- `put`
- `getOrDefault`
- `merge`

Example uses:

```java
count.put(value, count.getOrDefault(value, 0) + 1);
seen.add(value);
map.merge(key, 1, Integer::sum);
```

## Related Problems

| Number | Problem | Difficulty | Link | Notes |
| --- | --- | --- | --- | --- |

## Revision Checklist

- Can I identify whether I need membership, frequency, index, or grouping?
- Can I state what the set or map contains at each step?
- Am I checking before inserting when the same element must not be reused?
- Do I need `HashMap` instead of `HashSet` because extra information is required?
- Have I analyzed both average-case and space complexity?
