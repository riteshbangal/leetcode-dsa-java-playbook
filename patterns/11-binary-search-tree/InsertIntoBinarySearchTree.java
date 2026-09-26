class InsertIntoBinarySearchTree {
    public TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }

        TreeNode node = root;
        TreeNode parent = null;

        while (node != null) {
            parent = node;

            if (val < node.val) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        if (val < parent.val) {
            parent.left = new TreeNode(val);
        } else {
            parent.right = new TreeNode(val);
        }

        return root;
    }
}
