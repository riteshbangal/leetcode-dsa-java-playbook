# LeetCode DSA Java Playbook

Learn the pattern. Understand the invariant. Build the solution.

This repository is a pattern-first learning playbook for Data Structures and Algorithms practice using Java. It is not meant to be a collection of copied LeetCode answers. Each note is written to explain how to recognize the problem, choose the right pattern, reason about the invariant, and implement the solution independently.

Repository description: Pattern-first LeetCode solutions in Java, with DSA explanations, invariants, complexity analysis, mistakes, and revision notes.

## Philosophy

The goal is to learn reusable problem-solving patterns before moving into broader LeetCode practice. A solution is useful only when the reasoning behind it is clear enough to repeat later without memorizing the exact code.

The learning workflow is:

```text
Clues -> Pattern -> Invariant -> Implementation
```

For each problem, the focus is to:

- Read the problem carefully.
- Identify important clues.
- Recognize the applicable DSA pattern.
- Explain why the pattern applies.
- State the invariant or condition that remains true.
- Develop the brute-force approach.
- Implement the optimized Java solution.
- Analyze time and space complexity.
- Record mistakes and revision insights.

## Current Status

- Completed patterns: [Hashing](patterns/01-hashing/), [Two Pointers](patterns/02-two-pointers/), [Sliding Window](patterns/03-sliding-window/), [Prefix Sum](patterns/04-prefix-sum/), [Binary Search](patterns/05-binary-search/), [Stack](patterns/06-stack/), [Monotonic Stack](patterns/07-monotonic-stack/), [Linked List Pointers](patterns/08-linked-list-pointers/), and [Heap / Priority Queue](patterns/09-heap-priority-queue/)
- Pattern 07 completion means preliminary coverage and reference-code consolidation; independent revision remains pending as recorded in its chapter README.
- Pattern 08 core coverage includes five completed problems; Reorder List is intentionally deferred to revision.
- Pattern 09 preliminary core coverage includes six covered problems; Find Median from Data Stream is intentionally deferred to revision, and the chapter README records guided/reference/independent status per problem.
- Next pattern: **10 — Tree DFS and BFS**.
- Problem index: [LeetCode problems](problems/leetcode/)
- Full learning plan: [ROADMAP.md](ROADMAP.md)

## Repository Structure

```text
.
├── patterns/              # Pattern notes, walkthroughs, and chapter solutions
├── problems/leetcode/     # Standalone/canonical LeetCode problem notes where used
├── ROADMAP.md             # Planned learning order
└── README.md
```

## Naming Conventions

- Use lowercase kebab-case for directories.
- Use two-digit numeric prefixes for pattern folders, such as `01-hashing`.
- Keep pattern-learning chapter solutions, walkthroughs, and supporting code inside the corresponding `patterns/<nn-pattern>/` folder.
- Use four-digit LeetCode problem numbers for standalone problem folders under `problems/leetcode/`.
- Use the problem folder format `0001-two-sum` when adding a standalone problem folder.
- Standard Java solutions may use a descriptive class name inside pattern chapters; design problems use the required API class, such as `StockSpanner` or `KthLargest`.
- Keep one canonical implementation in the location chosen for that learning chapter or standalone problem; avoid unnecessary duplication.

## Problem Notes

Each problem note follows a consistent structure:

- Important clues
- Brute-force approach
- Pattern decision
- Core invariant
- Step-by-step reasoning
- Edge cases
- Mistakes and lessons
- Final complexity
- Revision prompts

Use an existing chapter walkthrough or standalone problem note as a structural reference when adding new material.

## Contributing

Corrections, clearer explanations, alternative approaches, missing edge cases, and better revision prompts are welcome. Please keep contributions simple, readable, and focused on understanding the pattern rather than collecting many variations of the same solution.

## Disclaimer

This project is unofficial and is not affiliated with, endorsed by, or sponsored by LeetCode.

## License

This repository is licensed under the [MIT License](LICENSE).
