# Pattern 11 — Binary Search Tree

Workflow:

```text
BST ordering -> eliminate impossible subtree
BST inorder  -> values arrive in ascending order
```

A Binary Search Tree is not ordinary array Binary Search and it is not merely a generic binary tree. Its power comes from the ordering invariant:

```text
every value in left subtree  < node.val
every value in right subtree > node.val
```

This chapter assumes distinct values unless a problem explicitly defines duplicate behavior.

For problem-by-problem reasoning and traces, see [problem-walkthrough.md](problem-walkthrough.md).

## Chapter status

**Completed: preliminary core coverage with six problems.**

This records first-pass pattern coverage, not independent mastery. Delete Node in a BST is intentionally deferred to revision.

| Problem | Main idea | Learning status |
| --- | --- | --- |
| Search in a Binary Search Tree | Compare at current node and eliminate one subtree | Independently implemented |
| Validate Binary Search Tree | Carry global lower/upper bounds through DFS | Guided implementation |
| Kth Smallest Element in a BST | Inorder traversal produces ascending values | Guided implementation |
| Lowest Common Ancestor of a BST | Both left, both right, or first split point | Guided implementation |
| Insert into a Binary Search Tree | Follow ordering until the null insertion position | Independently reasoned / Guided implementation |
| Minimum Absolute Difference in BST | Inorder + previous value; compare adjacent sorted values | Guided implementation |

## Pattern 05 vs Pattern 11

Pattern 05 — Binary Search works on an ordered search space, commonly an array, and repeatedly halves that search space.

Pattern 11 — Binary Search Tree works on nodes connected by left/right references. The same ordering intuition appears, but tree height controls the cost.

```text
Binary Search array: index boundaries
BST search:          node + left/right ordering
```

## Connection to Pattern 10

Pattern 10 taught general tree traversal. Pattern 11 reuses DFS/inorder mechanics but adds a stronger invariant.

Always ask:

```text
Is this reasoning true for any binary tree?
Or only because this tree is a BST?
```

Examples:

- Recursive DFS mechanics are generic tree knowledge.
- `left < node < right` ordering is BST-specific.
- Inorder traversal exists for any binary tree, but it is sorted only for a valid BST.

## Core BST invariants

### Ordering is global, not local

This tree is invalid:

```text
        10
       /  \
      5    15
          /  \
         6    20
```

The node `6` is less than its parent `15`, but it lives in the right subtree of `10`, where every value must be greater than `10`.

That is why validation uses ancestor bounds:

```text
lower < node.val < upper
```

### Search eliminates one subtree

At each node:

```text
target < node.val -> go left
target > node.val -> go right
target == node.val -> found
```

Unlike generic tree search, a valid BST lets us discard one entire side.

### Inorder is sorted

For a valid BST:

```text
Left -> Node -> Right
```

produces ascending values.

That fact powers Kth Smallest and Minimum Absolute Difference.

## Reusable templates

### Ordered search

```java
while (node != null) {
    if (target < node.val) {
        node = node.left;
    } else if (target > node.val) {
        node = node.right;
    } else {
        return node;
    }
}
```

### Recursive inorder

```java
if (node == null) {
    return;
}

inorder(node.left);

// process node

inorder(node.right);
```

### Iterative inorder with Deque

```java
Deque<TreeNode> stack = new ArrayDeque<>();

while (current != null || !stack.isEmpty()) {
    while (current != null) {
        stack.push(current);
        current = current.left;
    }

    current = stack.pop();

    // process current

    current = current.right;
}
```

For iterative stack behavior, prefer `Deque<TreeNode> stack = new ArrayDeque<>()` over the legacy `Stack` class.

## Implementations

| Problem | Implementation |
| --- | --- |
| Search in a Binary Search Tree | [SearchInBinarySearchTree.java](SearchInBinarySearchTree.java) |
| Validate Binary Search Tree | [ValidateBinarySearchTree.java](ValidateBinarySearchTree.java) |
| Kth Smallest Element in a BST | [KthSmallestElementInBST.java](KthSmallestElementInBST.java) |
| Lowest Common Ancestor of a BST | [LowestCommonAncestorOfBST.java](LowestCommonAncestorOfBST.java) |
| Insert into a Binary Search Tree | [InsertIntoBinarySearchTree.java](InsertIntoBinarySearchTree.java) |
| Minimum Absolute Difference in BST | [MinimumAbsoluteDifferenceInBST.java](MinimumAbsoluteDifferenceInBST.java) |
| Local tree node support | [TreeNode.java](TreeNode.java) |

## Complexity

Let `n` be the number of nodes and `h` the tree height.

| Problem | Time | Auxiliary space |
| --- | ---: | ---: |
| Search | `O(h)` | `O(1)` iterative |
| Validate BST | `O(n)` | `O(h)` recursion |
| Kth Smallest | `O(n)` worst case | `O(h)` recursion |
| LCA of BST | `O(h)` | `O(h)` recursion |
| Insert | `O(h)` | `O(1)` iterative |
| Minimum Absolute Difference | `O(n)` | `O(h)` explicit stack |

Never automatically translate `O(h)` into `O(log n)`:

```text
balanced BST -> h = O(log n)
skewed BST   -> h = O(n)
```

## Common mistakes from this chapter

1. Checking only a node against its immediate children when validating a BST.
2. Forgetting that ancestor bounds constrain descendants.
3. Using `Integer.MIN_VALUE` / `Integer.MAX_VALUE` as strict validation sentinels when node values may equal those extremes; use `long` bounds.
4. Traversing both subtrees for Search or LCA when ordering already eliminates one side.
5. Treating recursion as synonymous with DFS; iterative DFS with an explicit stack is also DFS.
6. Forgetting that inorder is `Left -> Node -> Right`.
7. Incrementing the Kth Smallest counter while descending left instead of when processing the current inorder node.
8. Assuming `return` from one recursive call automatically stops all parent calls.
9. Losing the insertion parent by walking the current pointer all the way to `null`.
10. Comparing Minimum Difference only across physical parent/child pairs instead of adjacent inorder values.
11. Using a PriorityQueue to recreate sorted order that BST inorder already provides.
12. Saying BST operations are always `O(log n)`; skewed trees can make `h = n`.

## Revision checklist

- Can I state the strict BST ordering invariant?
- Can I explain why BST ordering is global rather than only parent/child?
- Can I search a BST without exploring both sides?
- Can I derive `lower < node.val < upper` for validation?
- Can I explain why inorder of a valid BST is ascending?
- Can I implement recursive inorder from scratch?
- Can I implement iterative inorder using `Deque` + `ArrayDeque`?
- Can I derive Kth Smallest using an inorder counter?
- Can I derive BST LCA from both-left / both-right / split?
- Can I insert iteratively without losing the parent pointer?
- Can I explain why the minimum difference in sorted values occurs between adjacent values?
- Can I distinguish `O(h)`, balanced `O(log n)`, and skewed `O(n)`?
- Can I explain when a solution uses generic tree traversal versus BST ordering?
- During revision, can I solve Delete Node in a BST, including the two-child case?

## Interview articulation template

> This is a BST, so I can use its ordering invariant instead of treating it as a generic binary tree. At each node / during inorder traversal, I maintain ___. This lets me eliminate ___ or process values in ___. The operation takes ___ time in terms of tree height or node count, with ___ auxiliary space. For an `O(h)` operation, a balanced tree gives `O(log n)`, while a skewed tree can degrade to `O(n)`.
