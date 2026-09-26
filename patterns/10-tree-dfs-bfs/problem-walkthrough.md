# Tree DFS and BFS — Problem Walkthrough

Use this file for revision after first attempting the problems independently.

The chapter has two central execution models:

```text
DFS recursion:
go down -> child call finishes -> return value bubbles up -> parent resumes

BFS queue:
snapshot current level -> process exactly that many nodes -> children wait for next round
```

## Implemented files

- [MaximumDepthOfBinaryTree.java](MaximumDepthOfBinaryTree.java)
- [SameTree.java](SameTree.java)
- [InvertBinaryTree.java](InvertBinaryTree.java)
- [DiameterOfBinaryTree.java](DiameterOfBinaryTree.java)
- [BalancedBinaryTree.java](BalancedBinaryTree.java)
- [BinaryTreeLevelOrderTraversal.java](BinaryTreeLevelOrderTraversal.java)
- [BinaryTreeRightSideView.java](BinaryTreeRightSideView.java)
- [LowestCommonAncestorOfBinaryTree.java](LowestCommonAncestorOfBinaryTree.java)
- [SubtreeOfAnotherTree.java](SubtreeOfAnotherTree.java)
- [TreeNode.java](TreeNode.java)

---

## 1. Maximum Depth of Binary Tree — LeetCode 104

**Learning status:** Independently implemented after guided reasoning.

### Problem

Return the maximum number of nodes on a path from the root to a leaf.

```text
        3
       / \
      9   20
         /  \
        15   7

answer = 3
```

### Recursive contract

```text
maxDepth(node) = maximum depth of the subtree rooted at node
```

Base case:

```text
maxDepth(null) = 0
```

Recursive case:

```text
leftDepth  = maxDepth(node.left)
rightDepth = maxDepth(node.right)

return 1 + max(leftDepth, rightDepth)
```

### Bottom-up trace

```text
depth(9)  = 1
depth(15) = 1
depth(7)  = 1

depth(20) = 1 + max(1, 1) = 2
depth(3)  = 1 + max(1, 2) = 3
```

### Invariant

> The value returned by `maxDepth(node)` is the longest downward node-count path starting exactly at `node`.

### Complexity

- Time: `O(n)`.
- Auxiliary space: `O(h)` recursion.

---

## 2. Same Tree — LeetCode 100

**Learning status:** Guided implementation.

### Problem

Return `true` when two binary trees have identical structure and equal values at every corresponding position.

```text
p:          q:
    1           1
   / \         / \
  2   3       2   3

answer = true
```

Different shape must fail even when the same values appear:

```text
p:          q:
    1           1
   /             \
  2               2

answer = false
```

### Base cases

```text
p == null && q == null -> true
p == null || q == null -> false
p.val != q.val         -> false
```

Otherwise both corresponding subtrees must match:

```text
same(left, left) AND same(right, right)
```

### Invariant

> Every recursive pair represents the same structural position in the two candidate trees.

### Complexity

- Time: `O(n)` worst case.
- Auxiliary space: `O(h)` recursion.

---

## 3. Invert Binary Tree — LeetCode 226

**Learning status:** Guided implementation.

### Problem

Swap the left and right child of every node and return the root.

```text
before:             after:

        4                   4
       / \                 / \
      2   7               7   2
     / \ / \             / \ / \
    1  3 6  9           9  6 3  1
```

### Local responsibility

One node only needs to do:

```text
temp = node.left
node.left = node.right
node.right = temp
```

Then recursion ensures every descendant performs the same local operation.

### Uneven tree reminder

```text
        1
       / \
      2   3
     /
    4
```

The shallow `3` branch finishing does not stop the `2 -> 4` branch. `return` ends only the current recursive call.

### Invariant

> After `invertTree(node)` returns, the complete subtree rooted at `node` is inverted.

### Complexity

- Time: `O(n)`.
- Auxiliary space: `O(h)` recursion.

---

## 4. Diameter of Binary Tree — LeetCode 543

**Learning status:** Guided implementation.

### Problem

Return the number of **edges** in the longest path between any two nodes. The path does not need to pass through the root.

```text
        1
       / \
      2   3
     / \
    4   5

one longest path: 4 -> 2 -> 1 -> 3
answer = 3 edges
```

### Two quantities at every node

The helper returns depth upward:

```text
1 + max(leftDepth, rightDepth)
```

But it also creates a diameter candidate through the current node:

```text
leftDepth + rightDepth
```

Why different?

```text
parent can extend only one downward branch
current node can connect left branch + right branch
```

### Full recursion trace

For:

```text
        1
       / \
      2   3
     / \
    4   5
```

`maxDiameter` starts at `0`.

| Node finishing | `leftDepth` | `rightDepth` | Candidate | `maxDiameter` | Depth returned |
| --- | ---: | ---: | ---: | ---: | ---: |
| 4 | 0 | 0 | 0 | 0 | 1 |
| 5 | 0 | 0 | 0 | 0 | 1 |
| 2 | 1 | 1 | 2 | 2 | 2 |
| 3 | 0 | 0 | 0 | 2 | 1 |
| 1 | 2 | 1 | 3 | 3 | 3 |

The public method must return `maxDiameter`, not `depth(root)`.

### Call-stack visualization

When Java is evaluating node `2`:

```text
depth(1) -> paused, waiting for depth(2)
depth(2) -> paused, waiting for depth(4)
depth(4) -> running
```

After `depth(4)` returns `1`:

```text
depth(4) frame disappears

depth(2) resumes exactly where it paused
leftDepth = 1
next statement -> depth(5)
```

It does **not** restart `depth(2)` from the top and does not call `depth(4)` again.

### Shared vs local state

```text
leftDepth / rightDepth -> local to one stack frame
maxDiameter            -> one shared class field
```

So when node `2` updates `maxDiameter` to `2`, node `1` later sees that same shared value.

### Invariant

> `depth(node)` always returns the best one-branch depth from `node`, while `maxDiameter` stores the best two-branch path discovered anywhere so far.

### Complexity

- Time: `O(n)`.
- Auxiliary space: `O(h)` recursion.

---

## 5. Balanced Binary Tree — LeetCode 110

**Learning status:** Guided implementation.

### Problem

A binary tree is height-balanced when, at **every node**:

```text
|leftHeight - rightHeight| <= 1
```

This tree is balanced:

```text
    2
   /
  3
```

because:

```text
leftHeight = 1
rightHeight = 0
difference = 1
```

Balanced does not mean "zero children or two children". That is a different structural property.

### Optimized sentinel idea

A helper normally returns height:

```text
0, 1, 2, ...
```

Use `-1` to mean:

```text
this subtree is already unbalanced
```

Algorithm:

```text
height(null) -> 0

leftHeight = height(left)
if leftHeight == -1 -> return -1

rightHeight = height(right)
if rightHeight == -1 -> return -1

if abs(leftHeight - rightHeight) > 1 -> return -1

return 1 + max(leftHeight, rightHeight)
```

The public answer is:

```text
height(root) != -1
```

### Why not use `0` as failure?

Because `0` already means a valid empty-subtree height.

### Invariant

> Every non-negative helper return is a valid subtree height. `-1` means no caller needs to calculate a meaningful height anymore.

### Complexity

- Time: `O(n)` because each node is processed once.
- Auxiliary space: `O(h)` recursion.

---

## 6. Binary Tree Level Order Traversal — LeetCode 102

**Learning status:** Guided implementation.

### Problem

Return node values grouped by tree level.

```text
        3
       / \
      9   20
         /  \
        15   7
```

Answer:

```text
[
  [3],
  [9, 20],
  [15, 7]
]
```

### Why BFS?

DFS naturally goes deep. This problem explicitly asks for horizontal level order, so use a FIFO queue.

### `levelSize` boundary

At the beginning of each outer iteration:

```java
int levelSize = queue.size();
```

Everything currently in the queue belongs to the current level. Children added during the inner loop belong to the next level.

Trace:

```text
        1
       / \
      2   3
     / \   \
    4   5   6
           / \
          7   8
```

| Round | Queue before | `levelSize` | Values collected | Queue after |
| ---: | --- | ---: | --- | --- |
| 1 | `[1]` | 1 | `[1]` | `[2,3]` |
| 2 | `[2,3]` | 2 | `[2,3]` | `[4,5,6]` |
| 3 | `[4,5,6]` | 3 | `[4,5,6]` | `[7,8]` |
| 4 | `[7,8]` | 2 | `[7,8]` | `[]` |

### Invariant

> `levelSize` freezes the number of current-level nodes before next-level children are added.

### Complexity

- Time: `O(n)`.
- Auxiliary space: `O(w)` queue, excluding returned output.

---

## 7. Binary Tree Right Side View — LeetCode 199

**Learning status:** Guided implementation.

### Problem

Return the rightmost visible node at every level.

```text
        1
       / \
      2   3
       \   \
        5   4

answer = [1, 3, 4]
```

Do not simply follow `right` pointers. A level may have no direct right child but still have a visible node.

### Reuse level-order BFS

With children enqueued left-to-right, process one fixed level:

```text
i = 0 ... levelSize - 1
```

The rightmost node is processed when:

```text
i == levelSize - 1
```

Add only that node's value to the answer.

### Ordering consistency

Two valid designs exist:

```text
left first, right second -> take last node of level
right first, left second -> take first node of level
```

Do not mix the two rules.

### Invariant

> Under left-to-right enqueue order, the last node among the original `levelSize` nodes is the rightmost existing node of the level.

### Complexity

- Time: `O(n)`.
- Auxiliary space: `O(w)`.

---

## 8. Lowest Common Ancestor of a Binary Tree — LeetCode 236

**Learning status:** Reference solution after guided reasoning.

### Problem

Return the lowest node that has both target nodes `p` and `q` in its subtree. A node may be an ancestor of itself.

```text
        3
       / \
      5   1
     / \ / \
    6  2 0  8
      / \
     7   4
```

Examples:

```text
p = 5, q = 1 -> LCA = 3
p = 5, q = 4 -> LCA = 5
```

### Recursive meaning

A call may return:

```text
null -> found neither target
p    -> found p / p is current node
q    -> found q / q is current node
LCA  -> lower subtree already resolved the answer
```

Base cases:

```text
root == null       -> null
root == p || root == q -> root
```

After searching both sides:

```text
left != null && right != null -> return root
only left non-null            -> return left
only right non-null           -> return right
both null                     -> return null
```

### Why return immediately when `root == p || root == q`?

The standard problem guarantees both target nodes exist in the tree. If the other target is below the current target node, the current target is already the lowest common ancestor; if the other target is elsewhere, an ancestor will later receive non-null evidence from both sides.

### Invariant

> A non-null return is the most relevant node discovered in that subtree. Two non-null child returns mean the current node is the first split point.

### Complexity

- Time: `O(n)` worst case.
- Auxiliary space: `O(h)` recursion.

---

## 9. Subtree of Another Tree — LeetCode 572

**Learning status:** Guided implementation.

### Problem

Return `true` when `subRoot` appears somewhere inside `root` as an exact subtree, matching both structure and values.

```text
root:
        3
       / \
      4   5
     / \
    1   2

subRoot:
      4
     / \
    1   2

answer = true
```

An extra descendant makes a candidate fail:

```text
root candidate:       subRoot:
      4                   4
     / \                 / \
    1   2               1   2
       /
      0

answer for this candidate = false
```

### Two-method decomposition

Search method:

```text
isSubtree(root, subRoot)
```

asks:

```text
Does the match start here?
OR does it start somewhere in the left subtree?
OR does it start somewhere in the right subtree?
```

Exact matcher:

```text
isSameTree(candidate, subRoot)
```

requires:

```text
both null -> true
one null  -> false
values differ -> false
otherwise left AND right must both match
```

### Important Java distinction

```java
root == subRoot
```

checks whether both variables reference the exact same object. It does **not** mean two independently built trees have equal structure and values.

Use `isSameTree` for structural equality.

### Invariant

> `isSubtree` explores every possible candidate root until one exact `isSameTree` comparison succeeds.

### Complexity

If the main tree has `n` nodes and `subRoot` has `m` nodes:

- Worst-case time: `O(n * m)`.
- Recursive auxiliary space: up to `O(hRoot + hSub)` across the nested search/comparison calls.

---

# Chapter-level revision prompts

1. For a recursive tree problem, can you write the helper's return contract in one sentence before coding?
2. Can you draw the active call stack and identify which caller is paused?
3. Can you explain why returning from a child does not restart its parent?
4. Can you distinguish local stack-frame variables from shared class state?
5. Can you recognize when a parent needs one child result versus both child results?
6. Can you explain when a sentinel such as `-1` is cleaner than a separate boolean?
7. Can you derive the BFS `levelSize` technique without memorizing it?
8. Can you choose DFS when information must flow from subtrees and BFS when processing is level-oriented?
9. Can you explain why Binary Search Tree rules are not required anywhere in this chapter?
