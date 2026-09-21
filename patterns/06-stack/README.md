# Pattern 06 — Stack

A stack is more than a container with “last in, first out” behavior. Its deeper purpose is:

> Preserve unresolved work, completed values, previous state, or survivors when the most recently stored item must be handled before older items.

The top is the next correct item because newer work blocks, depends on, cancels, or completes older work beneath it. For worked examples and traces, see [problem-walkthrough.md](problem-walkthrough.md).

This chapter covers ordinary Stack problems. Monotonic Stack is Pattern 07 and is intentionally separate.

## Recognition Clues

Consider a stack when a problem involves:

- Nested structures.
- Matching opening and closing symbols.
- Undoing or reversing recent operations.
- Processing the most recent unfinished item first.
- Completed expression values waiting for an operator.
- Restoring an earlier execution or parsing state.
- Cancelling adjacent items.
- Simulating collisions where a new item may remove recent survivors.
- Repeatedly resolving the current item against the latest surviving item.

Do not choose a stack merely because the input is processed from left to right. Ask the stronger question:

> Why must the most recently stored unresolved item be processed before every older item?

## Core Questions

Before coding, answer:

1. What does one stack entry represent?
2. What does the entire stack represent after processing the first `i` inputs?
3. When do we push?
4. When do we inspect with `peek()`?
5. When is it safe to pop?
6. Can the stack be empty at this point?
7. Does popping reverse the order needed for the final answer?

These questions define the invariant and prevent the stack from becoming a memorized implementation detail.

## Java Stack Operations

Prefer `Deque<T>` with `ArrayDeque<>`:

```java
Deque<Integer> stack = new ArrayDeque<>();

stack.push(value);        // Add at the top.
int top = stack.peek();   // Inspect the top without removing it.
int removed = stack.pop(); // Remove and return the top.
boolean empty = stack.isEmpty();
```

`push()`, `peek()`, and `pop()` operate on the same end of the deque.

Important empty-stack behavior:

- `pop()` throws `NoSuchElementException` when empty.
- `peek()` returns `null` when empty.
- Assigning an empty `peek()` result to primitive `int` or `char` triggers a `NullPointerException` during unboxing.

Check `isEmpty()` before using the top whenever emptiness is possible.

Java's legacy `Stack` class extends `Vector` and carries older synchronized collection behavior. `Deque` is the modern interface recommended for stack operations.

## What Should the Stack Store?

The correct entry type follows from the invariant:

| Problem need | Useful entry |
| --- | --- |
| Match a closing symbol | Opening character |
| Remove recent equal characters | Surviving character |
| Restore a minimum after pop | Pair of value and minimum at that depth |
| Evaluate an expression | Completed integer value |
| Navigate directories | Directory name |
| Restore nested parsing | Saved state or unresolved token/decoded fragment |
| Resolve collisions | Surviving asteroid value |

Do not store an index when only a value is required, or store only a value when previous state must also be restored.

## Reusable Shapes

### Match or cancel against the top

```java
for (Item current : input) {
    if (!stack.isEmpty() && resolves(stack.peek(), current)) {
        stack.pop();
    } else {
        stack.push(current);
    }
}
```

The meaning of `resolves` depends on the problem: matching brackets, equal adjacent characters, or another local cancellation rule.

### Repeated resolution

```java
boolean currentIsAlive = true;

while (currentIsAlive
        && !stack.isEmpty()
        && canInteract(stack.peek(), current)) {
    // Remove the top and continue, or destroy the current item and stop.
}

if (currentIsAlive) {
    stack.push(current);
}
```

Use `while`, rather than `if`, when one current item may resolve multiple earlier entries.

### Save state before entering a nested level

```text
On opening delimiter:
    save previous state
    begin a new inner state

On closing delimiter:
    finish inner state
    restore previous state
    combine them
```

Nested structures resolve in reverse opening order, which is why a stack fits naturally.

## Implemented Problems

| Problem | What the stack represents | Solution |
| --- | --- | --- |
| Valid Parentheses | Unmatched opening brackets | [ValidParentheses.java](ValidParentheses.java) |
| Remove All Adjacent Duplicates | Fully reduced result of the processed prefix | [RemoveAllAdjacentDuplicatesInString.java](RemoveAllAdjacentDuplicatesInString.java) |
| Min Stack | Each value paired with the minimum at its depth | [MinStack.java](MinStack.java) |
| Evaluate Reverse Polish Notation | Completed values waiting for an operator | [EvaluateReversePolishNotation.java](EvaluateReversePolishNotation.java) |
| Simplify Path | Accepted directories in the current path | [SimplifyPath.java](SimplifyPath.java) |
| Decode String | Raw unresolved tokens and completed decoded fragments | [DecodeString.java](DecodeString.java) |
| Asteroid Collision | Survivors from the processed prefix | [AsteroidCollision.java](AsteroidCollision.java) |

## Core Invariants

### Valid Parentheses

After processing the first `i` characters, the stack contains exactly the unmatched opening brackets, in nesting order. The most recent unmatched opening bracket is on top.

### Remove All Adjacent Duplicates

After processing the first `i` characters, the stack contains the fully reduced result for that prefix. No removable adjacent equal pair remains inside it.

### Min Stack

For every entry, `minimumAtDepth` is the minimum of all values from the bottom through that entry. Therefore the top entry stores the minimum of the current stack.

### Reverse Polish Notation

After processing the first `i` tokens, the stack contains completed operands or subexpression results that have not yet been consumed by an operator.

### Simplify Path

After processing each path component, the stack contains exactly the directory names in the simplified path so far.

### Decode String

After processing each character, every completed encoded section has been replaced by its decoded string, while unfinished tokens remain in their original logical order.

### Asteroid Collision

After processing the first `i` asteroids, the stack contains every survivor from that prefix in original order, with no possible collision remaining among them.

## Complexity and Amortized Analysis

Most problems in this chapter take `O(n)` time and `O(n)` auxiliary space.

A nested `while` loop does not automatically imply `O(n²)`. In Asteroid Collision, for example, each asteroid is pushed at most once and popped at most once. Across the entire algorithm there are at most `n` pushes and `n` pops, so the total stack work is `O(n)`.

Space is measured by the maximum memory held at one time. Constant-size work per iteration does not make the whole algorithm `O(1)` space when the persistent stack may grow to `n` entries.

Decode String is output-sensitive. If the encoded input length is `n` and the decoded output length is `L`, the algorithm must spend at least `O(L)` time and space to materialize the output. The raw-token implementation avoids repeated front insertion by temporarily reversing whole fragments before appending them.

## Common Mistakes and Corrected Articulations

### Stack versus queue

- Stack: LIFO — last in, first out.
- Queue: FIFO — first in, first out.

LIFO is the mechanical rule. The reasoning is that the newest unresolved item must be processed first.

### `peek()` versus `pop()`

- `peek()` inspects without removing.
- `pop()` returns and removes.

Do not say “peek or pop” when the algorithm first validates the top and only then removes it.

### Stack entry versus operation

“I push opening brackets” describes an operation. “Each entry represents an unmatched opening bracket” defines the representation and supports the invariant.

### Empty result versus empty internal state

Valid Parentheses returns a boolean, not an empty stack. An empty stack is one required final condition after every character has been processed without a mismatch.

### Matching type matters

A closing bracket cannot match any opening bracket. It must match the exact type at the top. Searching deeper would violate nesting order.

### One minimum variable loses history

A single `currentMin` cannot restore an older minimum after the current minimum is popped. Pairing each value with its minimum at that depth preserves the history.

The new top after a pop is not necessarily the minimum because an ordinary stack has no ordering guarantee.

### RPN operand order

For a noncommutative operator:

```java
int rightOperand = stack.pop();
int leftOperand = stack.pop();
```

Then calculate `leftOperand operator rightOperand`. Operators are `+`, `-`, `*`, and `/`; numbers and completed results are operands.

### Path components and reconstruction

After `split("/")`, slash characters are separators and are no longer components. Ignore `""` and `"."`. Let `".."` pop only when the stack is nonempty. Names such as `"..."` are ordinary directories.

Popping a stack reverses order. Reconstruct from the deque's bottom when the answer must preserve root-to-directory or left-to-right order.

### Decode String ordering

Popping returns fragments in reverse order. Reverse the fragment sequence without reversing the characters inside an already-decoded fragment. Multi-digit counts must also be reconstructed in their original digit order.

The final stack may contain multiple fragments, such as for `"2[a]b"`; concatenate all fragments from bottom to top.

### Asteroid Collision branches

When a positive top meets a negative current asteroid:

- Smaller top: pop the top and continue with the same current asteroid.
- Equal sizes: pop the top and destroy the current asteroid.
- Larger top: keep the top and destroy the current asteroid.

Only the first case continues the collision loop.

## Ordinary Stack versus Monotonic Stack

An ordinary stack stores unresolved work, completed values, saved state, or survivors. It does not deliberately preserve numerical order.

A monotonic stack maintains increasing or decreasing order to answer nearest greater or nearest smaller questions. Problems such as Daily Temperatures and Next Greater Element belong to Pattern 07.

## Debugging Checklist

1. Can `peek()` or `pop()` run while the stack is empty?
2. Does the stack store the right representation: value, index, string, operator, or full state?
3. Is the current item compared with the top, or is a deeper item incorrectly searched?
4. Does a pop mean the top is permanently resolved?
5. Is `if` sufficient, or can one current item cause repeated pops?
6. Are left and right operands assigned in the correct pop order?
7. Does answer reconstruction accidentally reverse the required order?
8. Does a persistent stack make worst-case space `O(n)`?
9. Is the problem ordinary Stack or does it require a monotonic ordering invariant?

## Revision Prompts

Practice answering without reading the implementation:

1. Why does a closing bracket need only the top opening bracket?
2. What exactly remains in the stack after processing a prefix of an adjacent-duplicate problem?
3. Why can a single minimum variable not support `pop()` in constant time?
4. Why is the first popped RPN value the right operand?
5. Which path components are ignored, popped, or pushed?
6. How does the raw-token Decode String approach handle nested fragments and multi-digit counts?
7. What is the only asteroid direction combination that can collide?
8. Why is Asteroid Collision `O(n)` despite a nested loop?
9. Give an input that makes a stack solution use `O(n)` auxiliary space.
10. Explain the difference between ordinary Stack and Monotonic Stack.

## Interview Articulation Template

> “The stack stores ___. After processing the first `i` items, it contains ___. I push when ___. I pop when ___ because the top is permanently resolved. The top is sufficient because ___. Each element is pushed and popped at most ___ times, giving ___ time and ___ auxiliary space.”
