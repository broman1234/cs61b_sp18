public class IntTree {
    public int item;
    public IntTree left, right;

    public IntTree constrctTree(int[] preorder, int[] inorder) {
        IntTree root = new IntTree();
        return constructTreeHelper(root, preorder, inorder, 0, preorder.length - 1, 0, inorder.length - 1);
    }


    private IntTree constructTreeHelper(IntTree it, int[] preorder, int[] inorder, int preStart, int preEnd, int inStart, int inEnd) {
        if (preStart > preEnd) {
            return null;
        }
        it.item = preorder[preStart];
        if (preEnd - preStart + 1 == 1) {
            return it;
        }

        int i = 0;
        for (; i < inEnd - inStart + 1; i += 1) {
            if (inorder[i] == it.item) {
                break;
            }
        }

        it.left = constructTreeHelper(it.left, preorder, inorder, preStart + 1, i, inStart, i - 1);
        it.right = constructTreeHelper(it.right, preorder, inorder, i + 1, preEnd, i + 1, inEnd);
        return it;
    }
}
