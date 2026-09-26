# Pattern 10 — Tree DFS and BFS

Workflow:

```text
DFS: current node -> solve child subtrees -> combine / propagate result upward
BFS: queue snapshot -> process current level -> enqueue next level
```

Binary-tree problems become easier when each recursive call has one clear contract: **what does this call return about the subtree rooted at this node?** For BFS, the equivalent question is: **what does the queue represent at the start of this level?**

For problem-by-problem reasoning and traces, see [problem-walkthrough.md](problem-walkthrough.md).

## Chapter status

**Completed: preliminary core coverage with nine problems.**

This status records first-pass pattern coverage, not independent mastery. Recursion-stack tracing, DFS return contracts, BFS level boundaries, and the problems below should still be revisited independently during revision.

| Problem | Main idea | Learning status |
| --- | --- | --- |
| Maximum Depth of Binary Tree | Return subtree depth upward | Independently implemented after guided reasoning |
| Same Tree | Compare corresponding structure and values | Guided implementation |
| Invert Binary Tree | Mutate each node, then recurse into both subtrees | Guided implementation |
| Diameter of Binary Tree | Return depth locally while updating a global diameter | Guided implementation |
| Balanced Binary Tree | Return height or sentinel `-1` for imbalance | Guided implementation |
| Binary Tree Level Order Traversal | BFS queue with a fixed `levelSize` snapshot | Guided implementation |
| Binary Tree Right Side View | Capture the last node of each BFS level | Guided implementation |
| Lowest Common Ancestor of a Binary Tree | Propagate relevant nodes upward; split point is the LCA | Reference solution after guided reasoning |
| Subtree of Another Tree | DFS search + exact Same Tree comparison | Guided implementation |

## Scope of this chapter

This chapter is about **general binary trees**. It does not rely on Binary Search Tree ordering rules.

Pattern 11 — Binary Search Tree is a separate chapter. Ordinary Binary Search was already covered earlier as Pattern 05; a Binary Search Tree is a tree data structure with an ordering invariant, not the same pattern.

## Core tree mental model

A binary tree node naturally creates two smaller subproblems:

```text
        node
       /    \
    left    right
```

A recursive DFS usually follows this template:

```java
if (node == null) {
    return baseValue;
}

Result left = solve(node.left);
Result right = solve(node.right);

return combine(node, left, right);
```

The important question is not "where does recursion jump?" but:

> What does `solve(node)` promise to return for the subtree rooted at `node`?

## Recursion: pause, child call, resume

When Java evaluates:

```java
int leftDepth = depth(node.left);
int rightDepth = depth(node.right);
```

it must finish the entire `depth(node.left)` call before moving to the next line.

For:

```text
        1
       / \
      2   3
     / \
    4   5
```

one part of the call stack looks like:

```text
depth(1)   waiting for depth(2)
depth(2)   waiting for depth(4)
depth(4)   running
```

When `depth(4)` returns, the `depth(2)` call does **not restart**. It resumes at the exact statement after the completed child call.

Each call owns its own local variables:

```text
depth(1): node = 1
depth(2): node = 2
depth(4): node = 4
```

Returning from `depth(4)` removes only that stack frame. The paused `depth(2)` frame is still there.

## DFS forms used in this chapter

### 1. Return subtree information

Maximum Depth:

```text
depth(node)
= 1 + max(depth(node.left), depth(node.right))
```

The base case is:

```text
depth(null) = 0
```

### 2. Compare corresponding subtrees

Same Tree:

```text
both null              -> true
exactly one null       -> false
values different       -> false
otherwise              -> left same AND right same
```

### 3. Modify the tree

Invert Binary Tree:

```text
swap current left/right
invert left subtree
invert right subtree
return current root
```

A shallow branch finishing does not stop a deeper branch; each recursive call returns independently.

### 4. Return one value while updating another

Diameter of Binary Tree uses two different quantities:

```text
value returned to parent:
1 + max(leftDepth, rightDepth)

candidate diameter through current node:
leftDepth + rightDepth
```

The parent can extend only one downward branch, while a path through the current node can connect both branches.

### 5. Sentinel return value

Balanced Binary Tree uses:

```text
0, 1, 2, ... -> valid subtree height
-1           -> subtree is already unbalanced
```

`0` cannot be the failure signal because `0` is already the valid height of a null subtree.

### 6. Return a relevant node upward

Lowest Common Ancestor:

```text
null                     -> found nothing
p or q                    -> return that node
left non-null, right null -> propagate left
left null, right non-null -> propagate right
both non-null             -> current node is the split point / LCA
```

### 7. Search plus exact matcher

Subtree of Another Tree separates two jobs:

```text
isSubtree -> search every possible starting node
isSameTree -> verify exact values + structure
```

A matching root value is only a candidate, not proof of a subtree match.

## DFS versus BFS

### DFS

DFS explores a branch deeply before returning.

Canonical forms:

```text
recursive DFS -> Java call stack remembers unfinished work
iterative DFS -> explicit stack / Deque remembers unfinished work
```

This chapter primarily uses recursive DFS for the general-tree reasoning problems.

### BFS

BFS processes nodes level by level and is naturally iterative with a queue:

```text
FIFO: First In, First Out
```

Java operations used here:

```java
queue.offer(node);  // enqueue
queue.poll();       // remove front
queue.size();       // number currently waiting
queue.isEmpty();
```

Recursive BFS is possible, but an iterative queue is the natural and standard implementation.

## The BFS `levelSize` invariant

For level-order problems, take a snapshot before processing a level:

```java
int levelSize = queue.size();
```

At that exact moment:

> Every node currently in the queue belongs to the current level.

Any children enqueued while those `levelSize` nodes are processed belong to the next level.

Example:

```text
        1
       / \
      2   3
     / \   \
    4   5   6
           / \
          7   8
```

| Round | Queue at start | `levelSize` | Process now | Queue after round |
| ---: | --- | ---: | --- | --- |
| 1 | `[1]` | 1 | `1` | `[2,3]` |
| 2 | `[2,3]` | 2 | `2,3` | `[4,5,6]` |
| 3 | `[4,5,6]` | 3 | `4,5,6` | `[7,8]` |
| 4 | `[7,8]` | 2 | `7,8` | `[]` |

The queue does not store explicit level numbers; the size snapshot creates the boundary.

## Core invariants

### Maximum Depth

> `maxDepth(node)` returns the number of nodes on the longest downward path starting at `node`.

### Same Tree

> Each recursive call compares nodes occupying the same structural position in both trees.

### Invert Binary Tree

> Every visited node swaps exactly its own left and right child; recursion applies the same local operation everywhere else.

### Diameter of Binary Tree

> `depth(node)` returns a one-branch depth to the caller, while `maxDiameter` stores the largest two-branch path seen anywhere so far.

### Balanced Binary Tree

> A non-negative helper return is a valid subtree height; `-1` means imbalance has already been detected and should propagate upward immediately.

### Level Order Traversal

> At the start of an outer BFS iteration, the queue contains exactly the nodes of the next level to process.

### Right Side View

> With left child enqueued before right child, the last node processed among the fixed `levelSize` nodes is the rightmost node of that level.

### Lowest Common Ancestor

> A non-null recursive return represents relevant evidence (`p`, `q`, or an already-resolved LCA). If both sides return non-null, the current node is the lowest split point.

### Subtree of Another Tree

> `isSubtree` searches candidate roots; `isSameTree` accepts a candidate only when both structure and values match completely.

## Implementations

All chapter implementations and supporting code live directly in this folder.

| Problem | Implementation |
| --- | --- |
| Maximum Depth of Binary Tree | [MaximumDepthOfBinaryTree.java](MaximumDepthOfBinaryTree.java) |
| Same Tree | [SameTree.java](SameTree.java) |
| Invert Binary Tree | [InvertBinaryTree.java](InvertBinaryTree.java) |
| Diameter of Binary Tree | [DiameterOfBinaryTree.java](DiameterOfBinaryTree.java) |
| Balanced Binary Tree | [BalancedBinaryTree.java](BalancedBinaryTree.java) |
| Binary Tree Level Order Traversal | [BinaryTreeLevelOrderTraversal.java](BinaryTreeLevelOrderTraversal.java) |
| Binary Tree Right Side View | [BinaryTreeRightSideView.java](BinaryTreeRightSideView.java) |
| Lowest Common Ancestor of a Binary Tree | [LowestCommonAncestorOfBinaryTree.java](LowestCommonAncestorOfBinaryTree.java) |
| Subtree of Another Tree | [SubtreeOfAnotherTree.java](SubtreeOfAnotherTree.java) |
| Local binary-tree node support | [TreeNode.java](TreeNode.java) |

## Complexity

Let `n` be the number of nodes in the relevant tree, `h` its height, and `w` its maximum width.

| Problem | Time | Auxiliary space |
| --- | ---: | ---: |
| Maximum Depth | `O(n)` | `O(h)` recursion |
| Same Tree | `O(n)` worst case | `O(h)` recursion |
| Invert Binary Tree | `O(n)` | `O(h)` recursion |
| Diameter of Binary Tree | `O(n)` | `O(h)` recursion |
| Balanced Binary Tree | `O(n)` | `O(h)` recursion |
| Level Order Traversal | `O(n)` | `O(w)` queue, excluding output |
| Right Side View | `O(n)` | `O(w)` queue |
| Lowest Common Ancestor | `O(n)` worst case | `O(h)` recursion |
| Subtree of Another Tree | worst case `O(n * m)` for `n` root nodes and `m` subRoot nodes | recursion up to `O(hRoot + hSub)` in nested comparison |

For recursive DFS:

```text
balanced tree -> h = O(log n)
skewed tree   -> h = O(n)
```

## Common mistakes from this chapter

1. Treating "leaf node" as the universal base case instead of using `node == null`.
2. Thinking a return from one child stops all recursion; it only returns from that current call.
3. Forgetting that a parent call is paused and resumes after its child returns.
4. Assuming the same local `node` variable changes globally; every recursive call has its own stack frame.
5. Returning `depth(root)` from Diameter instead of the separately accumulated `maxDiameter`.
6. Mixing up the diameter candidate `leftDepth + rightDepth` with the depth returned upward `1 + max(leftDepth, rightDepth)`.
7. Thinking a balanced tree requires either zero or two children. Balance is about subtree-height difference `<= 1`.
8. Reversing the balance check; imbalance is `Math.abs(leftHeight - rightHeight) > 1`.
9. Using `0` as the imbalance sentinel even though null subtrees legitimately have height `0`.
10. Polling from the BFS queue before capturing `levelSize`.
11. Re-reading `queue.size()` as the current-level boundary after children have already been enqueued.
12. Using `i == levelSize` for the last BFS node; the last valid index is `levelSize - 1`.
13. Enqueuing right-before-left while also taking the last node as the right-side view; choose a consistent ordering rule.
14. Comparing tree objects with `root == subRoot` when the problem requires equal structure and values.
15. Forgetting that LCA intentionally uses node identity (`root == p || root == q`) because `p` and `q` are node references.
16. Accepting matching root values as sufficient for Subtree; extra descendants must also cause the match to fail.

## Revision checklist

- Can I state exactly what one recursive call returns before writing code?
- Can I trace a call stack using "pause -> child finishes -> resume"?
- Can I explain why each recursive call has its own local `node`?
- Can I distinguish information returned upward from global/shared state updated during traversal?
- Can I derive Maximum Depth from `depth(null) = 0`?
- Can I write Same Tree base cases without mixing up one-null and both-null cases?
- Can I explain why Invert Tree processes uneven branches completely?
- Can I derive Diameter's two quantities without memorizing the code?
- Can I derive the `-1` sentinel solution for Balanced Binary Tree?
- Can I explain why balance allows one missing child when the height difference is only `1`?
- Can I derive BFS level boundaries from a queue-size snapshot?
- Can I implement Level Order Traversal from scratch?
- Can I derive Right Side View as "one selected node per level"?
- Can I explain why two non-null LCA child results make the current node the split point?
- Can I separate "search for candidate root" from "exact tree comparison" in Subtree?
- Can I explain recursive DFS vs iterative DFS vs BFS without treating recursion as synonymous with DFS?

## Interview articulation template

For recursive DFS:

> My helper solves the problem for the subtree rooted at the current node. The base case is ___. I recursively obtain ___ from the left and ___ from the right. The current node then combines those results by ___. The helper returns ___ upward. Each node is visited ___ times, giving ___ time and `O(h)` recursion space.

For BFS:

> I use a queue because the problem is level-oriented. At the beginning of each outer iteration, `queue.size()` is the number of nodes in the current level. I process exactly that many nodes and enqueue their children, which therefore form the next level. This visits every node once in `O(n)` time and uses up to `O(w)` queue space.
