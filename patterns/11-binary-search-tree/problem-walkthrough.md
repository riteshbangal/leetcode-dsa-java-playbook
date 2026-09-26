# Binary Search Tree — Problem Walkthrough

Use this file for revision after first attempting the problems independently.

The chapter has two recurring ideas:

```text
ordered navigation -> compare at current node and eliminate a subtree
sorted inorder     -> Left -> Node -> Right yields ascending values
```

## Implemented files

- [SearchInBinarySearchTree.java](SearchInBinarySearchTree.java)
- [ValidateBinarySearchTree.java](ValidateBinarySearchTree.java)
- [KthSmallestElementInBST.java](KthSmallestElementInBST.java)
- [LowestCommonAncestorOfBST.java](LowestCommonAncestorOfBST.java)
- [InsertIntoBinarySearchTree.java](InsertIntoBinarySearchTree.java)
- [MinimumAbsoluteDifferenceInBST.java](MinimumAbsoluteDifferenceInBST.java)
- [TreeNode.java](TreeNode.java)

---

## 1. Search in a Binary Search Tree — LeetCode 700

**Learning status:** Independently implemented.

### Recognition

A target value is being searched in a BST. Ordering means only one subtree can still contain the target.

```text
target < node.val -> left
target > node.val -> right
equal             -> return node
```

### Invariant

> If the target exists, after each comparison it can exist only in the subtree we choose next.

### Complexity

- Time: `O(h)`.
- Auxiliary space: `O(1)` iterative.
- Balanced: `O(log n)`; skewed: `O(n)`.

---

## 2. Validate Binary Search Tree — LeetCode 98

**Learning status:** Guided implementation.

### Key mistake to avoid

Immediate-child checks are insufficient.

```text
        10
       /  \
      5    15
          /  \
         6    20
```

Although `6 < 15`, node `6` violates the ancestor constraint from `10`.

### Recursive state

Each node receives an allowed interval:

```text
lower < node.val < upper
```

Then:

```text
left  -> (lower, node.val)
right -> (node.val, upper)
```

Use `long` sentinels so valid integer extremes are not rejected.

### Invariant

> Every recursive call validates the current node against all ancestor constraints summarized by its lower and upper bounds.

### Complexity

- Time: `O(n)`.
- Auxiliary space: `O(h)`.

---

## 3. Kth Smallest Element in a BST — LeetCode 230

**Learning status:** Guided implementation.

### Recognition

Inorder traversal of a valid BST produces ascending values:

```text
Left -> Node -> Right
```

Therefore the kth node processed in inorder is the kth smallest value.

### State

```text
count = number of nodes processed in inorder
when count == k -> current value is the answer
```

Increment the counter when processing the current node, not while descending left.

### Invariant

> After processing a node in inorder, `count` equals the number of smallest BST values visited so far.

### Complexity

- Time: `O(n)` worst case.
- Auxiliary space: `O(h)`.

---

## 4. Lowest Common Ancestor of a BST — LeetCode 235

**Learning status:** Guided implementation.

At current node:

```text
p and q both smaller -> LCA must be left
p and q both larger  -> LCA must be right
otherwise            -> current is the first split / one target equals current
```

Do not compare targets against the current node's children; compare `p.val` and `q.val` directly with `root.val`.

### Invariant

> While both targets lie strictly on the same side, the LCA must lie on that side. The first node where they no longer share a side is their LCA.

### Complexity

- Time: `O(h)`.
- Auxiliary space: `O(h)` for the recursive implementation.

---

## 5. Insert into a Binary Search Tree — LeetCode 701

**Learning status:** Independently reasoned / Guided implementation.

Insertion follows the same navigation as search:

```text
Search: reach null -> target is absent
Insert: reach null -> new node belongs there
```

For the iterative solution, preserve the parent before moving:

```text
parent = current
current = current.left / current.right
```

When `current == null`, attach the new node to `parent.left` or `parent.right`.

Empty-tree edge case:

```text
root == null -> new TreeNode(val) becomes the root
```

### Invariant

> At each comparison, BST ordering determines the only subtree where the new value may be inserted without violating the invariant.

### Complexity

- Time: `O(h)`.
- Auxiliary space: `O(1)`.

---

## 6. Minimum Absolute Difference in BST — LeetCode 530

**Learning status:** Guided implementation.

### Key transformation

Do not compare every pair and do not compare only physical parent/child pairs.

BST inorder gives a sorted sequence:

```text
1 -> 3 -> 4 -> 6 -> 7 -> 8 -> 10
```

In a sorted sequence, the minimum difference must occur between adjacent values. Therefore remember only:

```text
previous inorder value
minimum difference seen so far
```

For each current inorder node:

```text
difference = current.val - previous
minimum = min(minimum, difference)
previous = current.val
```

A PriorityQueue could sort all values, but it would discard the BST advantage: heap insertion gives `O(n log n)` overall, while inorder processes the already-ordered structure in `O(n)`.

### Iterative inorder

Use:

```java
Deque<TreeNode> stack = new ArrayDeque<>();
```

The explicit `Deque` performs the same pending-work role that Java's call stack performs in recursive inorder.

### Invariant

> `previous` is the immediately preceding value in ascending inorder order, so comparing it with the current value checks the only earlier candidate that can give the current node's smallest difference.

### Complexity

- Time: `O(n)`.
- Auxiliary space: `O(h)`.

---

# Deferred revision problem

## Delete Node in a BST — LeetCode 450

Not part of this preliminary core pass.

Revision should cover:

```text
no children -> remove node
one child   -> replace node with child
two children -> replace using inorder successor/predecessor, then repair subtree
```

---

# Chapter-level revision prompts

1. Can you distinguish ordinary Binary Search from Binary Search Tree reasoning?
2. Can you explain why a BST can eliminate one subtree during search?
3. Can you produce a counterexample showing why local child comparisons do not validate a BST?
4. Can you derive ancestor bounds for left and right recursive calls?
5. Can you explain why inorder is sorted only because of the BST invariant?
6. Can you derive Kth Smallest without using a heap?
7. Can you derive BST LCA without using the generic-tree LCA algorithm?
8. Can you insert iteratively while retaining the parent of the null position?
9. Can you implement iterative inorder using `Deque` + `ArrayDeque`?
10. Can you explain why adjacent inorder values are enough for Minimum Difference?
11. Can you state complexity using `h` first, then discuss balanced and skewed trees?
12. Can you solve the deferred Delete Node problem independently during revision?
