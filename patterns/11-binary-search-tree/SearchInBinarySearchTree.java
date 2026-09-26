class SearchInBinarySearchTree {
    public TreeNode searchBST(TreeNode root, int target) {
        TreeNode node = root;

        while (node != null) {
            if (target < node.val) {
                node = node.left;
            } else if (target > node.val) {
                node = node.right;
            } else {
                return node;
            }
        }

        return null;
    }
}
