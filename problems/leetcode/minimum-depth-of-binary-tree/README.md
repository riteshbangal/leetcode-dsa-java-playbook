# LeetCode 111 — Minimum Depth of Binary Tree

**Difficulty:** Easy  
**LeetCode:** https://leetcode.com/problems/minimum-depth-of-binary-tree/

## Problem

Given the root of a binary tree, return its minimum depth.

The minimum depth is the number of nodes along the shortest path from the root down to the nearest leaf node.

A leaf is a node with no left or right child.

### Example

```text
        3
       / \
      9   20
         /  \
        15   7

Output: 2
```

## Constraints

```text
0 <= number of nodes <= 10^5
-1000 <= Node.val <= 1000
```

## Invariant

`minDepth(node)` returns the minimum valid number of nodes from `node` down to a leaf.

A missing child is not a valid root-to-leaf path.

## Corner Cases

```text
root = null
=> 0
```

```text
    1
     \
      2
       \
        3

=> 3
```

## Pseudocode

```text
minDepth(node):
    if node == null:
        return 0

    if node.left == null and node.right == null:
        return 1

    if node.left != null and node.right != null:
        return 1 + min(minDepth(node.left), minDepth(node.right))

    if node.left != null:
        return 1 + minDepth(node.left)

    return 1 + minDepth(node.right)
```

## Complexity

Time: O(n)  
Space: O(h)

Why: each node is visited at most once, and the recursion stack holds at most the tree height.

## Pattern

**Tree DFS / Recursion**
