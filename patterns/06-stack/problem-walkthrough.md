# Stack — Problem Walkthrough

This walkthrough develops ordinary Stack reasoning through matching, cancellation, saved metadata, expression evaluation, canonical representation, nested decoding, and repeated collision resolution.

## 1. Valid Parentheses

**Problem:** Given a string containing `()[]{}`, return whether every opening bracket is closed by the same type in the correct nested order.

Example:

```text
input  = "([{}])"
output = true
```

| Current | Stack before | Decision | Stack after |
| --- | --- | --- | --- |
| `(` | empty | Push unresolved opening | `(` |
| `[` | `(` | Push unresolved opening | `( [` |
| `{` | `( [` | Push unresolved opening | `( [ {` |
| `}` | `( [ {` | Match `{`, then pop | `( [` |
| `]` | `( [` | Match `[`, then pop | `(` |
| `)` | `(` | Match `(`, then pop | empty |

Two invalid cases must be checked before popping:

1. The stack is empty, so no opening bracket exists.
2. The top opening bracket is the wrong type.

The final stack must be empty, but that is not sufficient by itself unless every earlier mismatch returned `false` immediately.

**Invariant:** The stack contains exactly the unmatched opening brackets from the processed prefix.

**Complexity:** `O(n)` time and `O(n)` auxiliary space.

**Strong articulation:** “A closing bracket must resolve the most recent unmatched opening bracket. If the top has the wrong type, searching deeper would violate nesting order.”

## 2. Remove All Adjacent Duplicates in String

**Problem:** Repeatedly remove adjacent equal-character pairs and return the final string.

Example:

```text
"abbaca" -> "aaca" -> "ca"
```

| Current | Stack before | Decision | Stack after |
| --- | --- | --- | --- |
| `a` | empty | Push | `a` |
| `b` | `a` | Different from top; push | `a b` |
| `b` | `a b` | Equal to top; pop | `a` |
| `a` | `a` | Equal to top; pop | empty |
| `c` | empty | Push | `c` |
| `a` | `c` | Different from top; push | `c a` |

The top is the most recent character that survived every removal so far. It is not necessarily the immediately previous character in the original input.

After using `push()`, the top is the end of the answer. Reconstruct from the deque's bottom with `removeLast()` to preserve left-to-right order.

**Invariant:** The stack is the fully reduced result of the processed prefix.

**Complexity:** `O(n)` time and `O(n)` auxiliary space.

## 3. Min Stack

**Problem:** Support `push`, `pop`, `top`, and `getMin` in constant time.

A single global minimum loses history:

```text
push(5) -> min 5
push(2) -> min 2
push(4) -> min 2
pop()   -> min 2
pop()   -> previous min must become 5
```

The top value is not necessarily the minimum, so setting the minimum to the new top is invalid.

Store a pair at every depth:

```text
(value, minimumAtDepth)
```

| Operation | Pair pushed or removed | Stack, bottom to top |
| --- | --- | --- |
| `push(5)` | `(5, 5)` | `(5, 5)` |
| `push(2)` | `(2, 2)` | `(5, 5), (2, 2)` |
| `push(4)` | `(4, 2)` | `(5, 5), (2, 2), (4, 2)` |
| `pop()` | Remove `(4, 2)` | `(5, 5), (2, 2)` |
| `pop()` | Remove `(2, 2)` | `(5, 5)` |

Duplicate minima work naturally because each depth stores its own minimum.

**Invariant:** Every pair stores the minimum from the bottom through its own depth.

**Complexity:** Every operation is `O(1)`; a stack containing `n` entries uses `O(n)` total space.

## 4. Evaluate Reverse Polish Notation

**Problem:** Evaluate postfix tokens containing integers and `+`, `-`, `*`, `/`.

Example:

```text
["10", "6", "3", "/", "-"]
```

| Token | Action | Stack, bottom to top |
| --- | --- | --- |
| `10` | Push number | `10` |
| `6` | Push number | `10, 6` |
| `3` | Push number | `10, 6, 3` |
| `/` | `6 / 3 = 2` | `10, 2` |
| `-` | `10 - 2 = 8` | `8` |

Operand order is determined by pop order:

```java
int rightOperand = stack.pop();
int leftOperand = stack.pop();
```

Store integers in `Deque<Integer>`. The input tokens are strings, but a stack entry represents a completed numeric value. Exact operator comparison also distinguishes `"-"` from the negative number `"-11"`.

**Invariant:** The stack contains completed values that have not yet been consumed by an operator.

**Complexity:** `O(n)` time and `O(n)` auxiliary space.

## 5. Simplify Path

**Problem:** Convert an absolute Unix path to canonical form.

Rules:

- Ignore empty components and `"."`.
- `".."` removes the latest accepted directory when one exists.
- Every other component, including `"..."`, is a directory name.

Example:

```text
/a/./b/../../c/ -> /c
```

| Component | Action | Directories, root to current |
| --- | --- | --- |
| `a` | Push | `a` |
| `.` | Ignore | `a` |
| `b` | Push | `a, b` |
| `..` | Pop `b` | `a` |
| `..` | Pop `a` | empty/root |
| `c` | Push | `c` |

For `/../../x`, both parent operations occur at the root and have no effect. The result is `/x`.

`split("/")` removes slash separators. Repeated slashes produce empty components; a component is never the string `"/"`.

**Invariant:** The stack contains the simplified directory sequence for the processed prefix.

**Complexity:** `O(n)` time and `O(n)` auxiliary space, including the split components and deque.

## 6. Decode String

**Problem:** Decode nested expressions such as `k[encodedText]`.

This implementation follows the raw-token approach derived during the session. It pushes digits, `[`, letters, and decoded fragments. When `]` appears, it reduces the latest complete encoded section to one decoded fragment.

Example:

```text
2[x3[y]] -> xyyyxyyy
```

Inner reduction:

```text
3[y] -> yyy
```

After pushing that fragment back, the outer unresolved content is logically:

```text
2[x + yyy]
```

Outer reduction:

```text
2[xyyy] -> xyyyxyyy
```

Three ordering details matter:

1. Popping text fragments returns them from right to left, so reverse the fragment sequence before joining.
2. Popping the count from `12[...]` returns `2` and then `1`, so restore digit order before parsing.
3. The final stack may contain multiple fragments, as in `2[a]b`; join them from bottom to top.

Avoid repeated string prepending such as `text = fragment + text`, which can create quadratic copying. Use a temporary fragment deque and append whole fragments in order.

**Invariant:** Completed bracket expressions are replaced with decoded fragments; unfinished tokens remain available for their enclosing expression.

**Complexity:** Output-sensitive. With input length `n` and decoded output length `L`, the work is proportional to parsing plus the text materialized during decoding; the final output alone requires `O(L)` time and space.

## 7. Asteroid Collision

**Problem:** Simulate collisions between right-moving positive asteroids and left-moving negative asteroids.

A collision is possible only when:

```text
stack top > 0 and current < 0
```

Example:

```text
[10, 2, -5]
```

1. `2` meets `-5`; `2` is popped and `-5` remains alive.
2. The same `-5` meets the new top `10`.
3. `10` is larger, so `-5` is destroyed.
4. The final survivors are `[10]`.

The three collision branches are:

| Comparison | Top | Current | Continue? |
| --- | --- | --- | --- |
| `top < abs(current)` | Destroyed and popped | Alive | Yes |
| `top == abs(current)` | Destroyed and popped | Destroyed | No |
| `top > abs(current)` | Survives | Destroyed | No |

Use `while` because one current asteroid may destroy several earlier survivors.

**Invariant:** The stack contains all survivors from the processed prefix, with no possible collision remaining among them.

**Complexity:** `O(n)` time because every asteroid is pushed at most once and popped at most once; `O(n)` auxiliary space in the no-collision case.

## Cross-Problem Comparison

| Problem | Top interaction | Why pop is safe |
| --- | --- | --- |
| Valid Parentheses | Closing bracket matches latest opening | The pair is permanently resolved |
| Adjacent Duplicates | Current equals latest survivor | Both characters cancel |
| Min Stack | Pop removes value plus its depth minimum | New top already stores restored minimum |
| RPN | Operator consumes two completed values | Their combined result replaces them |
| Simplify Path | `..` removes latest directory | Navigation leaves that directory |
| Decode String | `]` completes latest bracket section | Decoded fragment replaces its tokens |
| Asteroid Collision | Current meets nearest left survivor | Destroyed asteroid cannot interact again |

## Final Revision Drill

For each problem, explain aloud:

1. The recognition clue.
2. The meaning of one stack entry.
3. The invariant after processing a prefix.
4. The exact pop condition.
5. Why only the top needs inspection.
6. The termination condition.
7. Time and auxiliary-space complexity.
