class KthSmallestElementInBST {
    private int count;
    private int answer;

    public int kthSmallest(TreeNode root, int k) {
        count = 0;
        inorder(root, k);
        return answer;
    }

    private void inorder(TreeNode node, int k) {
        if (node == null) {
            return;
        }

        inorder(node.left, k);

        count++;
        if (count == k) {
            answer = node.val;
            return;
        }

        inorder(node.right, k);
    }
}
