# LeetCode Problem Index

This index tracks recorded LeetCode problems. The status distinguishes reviewed implementations from guided or consolidation references; reference code does not imply independent mastery.

Each problem should have one primary pattern. Secondary patterns can be mentioned inside the problem notes when they help explain the solution.

## Folder Naming

```text
problems/leetcode/0001-two-sum/
├── README.md
└── Solution.java
```

Conventions:

- Use four-digit LeetCode problem numbers.
- Use lowercase kebab-case for problem titles.
- Use the folder format `0001-two-sum`.
- Keep one canonical solution folder per problem.
- Use `class Solution` for standard LeetCode-compatible Java solutions; design problems use their required class name, such as `StockSpanner`.

## Problems

| Number | Problem | Difficulty | Primary Pattern | Status | Solution | Last Reviewed |
| --- | --- | --- | --- | --- | --- | --- |
| 217 | Contains Duplicate | Easy | Hashing | Solved | [Java](../../patterns/01-hashing/ContainsDuplicate.java) |  |
| 84 | Largest Rectangle in Histogram | Hard | Monotonic Stack | Guided solution; revision pending | [Java](0084-largest-rectangle-in-histogram/Solution.java) · [Notes](0084-largest-rectangle-in-histogram/README.md) | 2026-09-23 |
| 496 | Next Greater Element I | Easy | Monotonic Stack | Reviewed implementation; revision pending | [Java](0496-next-greater-element-i/Solution.java) · [Notes](0496-next-greater-element-i/README.md) | 2026-09-23 |
| 503 | Next Greater Element II | Medium | Monotonic Stack | Reasoning completed; reference added | [Java](0503-next-greater-element-ii/Solution.java) · [Notes](0503-next-greater-element-ii/README.md) | 2026-09-23 |
| 739 | Daily Temperatures | Medium | Monotonic Stack | Reviewed implementation; revision pending | [Java](0739-daily-temperatures/Solution.java) · [Notes](0739-daily-temperatures/README.md) | 2026-09-23 |
| 901 | Online Stock Span | Medium | Monotonic Stack | Batch reasoning covered; online reference added | [Java](0901-online-stock-span/StockSpanner.java) · [Notes](0901-online-stock-span/README.md) | 2026-09-23 |
