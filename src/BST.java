import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Bryce, Koddy, Nandan
 * This generalized binary search tree is able to be initialized with a
 * Comparator, which will thus control the order elements are added to
 * the tree. Elements will thus be added in-order, in a trivial,
 * non-balancing manner. They will not, hopefully, be added in a sorted
 * order, or the tree will be degenerate. Hopefully the elements will be
 * received out of order, and the tree will be constructed appropriately,
 * though naively.
 */
public class BST<T extends Comparable<T>> {
    private BSTNode root = null;
    private int size = 0;
    // @FunctionalInterface, so the field (some data) can be called (applied, in LISP parlance) as a method later.
    private Comparator<T> cmp = Comparator.NaturalOrder();

    /**
     * Constructor for a BST that contains a comparator for ordering
     * @param comparator the desired comparator used
     */
    public BST(Comparator<T> comparator) {
        cmp = comparator;
    }

    /**
     * Public method to add a node to the tree
     * @param data the data of the node being added
     */
    public void add(T data) {
        BSTNode n = new BSTNode(data);

        // If the tree is empty, make the node the root of the tree.
        if (root == null) {
            root = n;
        } else {
            add(root, n);
        }

        // Increment the size of the tree in this basal method so that it is
        // not forgotten or done incorrectly the recursive method [add(BSTNode, BSTNode)].  
        size++;
    }

    /**
     * Private method to add nodes at a given root recursively
     * @param root location of where node should be added
     * @param nodeToAdd the node being added
     */
    private void add(BSTNode root, BSTNode nodeToAdd) {
        int comparison = cmp(root, nodeToAdd);

        if (comparison < 0) {
            if (root.getLeft() == null) {
                root.setLeft(nodeToAdd);
            }
            else {
                add(root.getLeft(), nodeToAdd);
            }
        }
        else (comparison > 0) {
            if (root.getRight() == null) {
                root.setRight(nodeToAdd);
            }
            else {
                add(root.getRight(), nodeToAdd);
            }
        }
    }

    public void delete(T nodeToAdd) {
        //TODO:
    }

    public T find(T nodeToFind) {
        //TODO:
        return null;
    }

    public T min() {
        //TODO:
        return null;
    }

    public int size() { return size; }

    /**
     * Method to return height of the tree
     * @return height of tree
     */
    public int height() { return height(root); }

    /**
     * Method to check for the height of the tree recursively
     * @param r the root node
     * @return height of tree
     */
    private int height(BSTNode r) {
        int h = -1;
        if (r == null) {
            //if tree doesn't exist then it should just return -1
            return h;
        }
        else {
            //Height = max length to deepest node
            h = 1 + Math.max(height(r.getLeft()), height(r.getRight()));
        }
        return h;
    }

    public Iterator<T> iterator() {
        //TODO:
        return null;
    }

    //Not sure if we even need these but i think its good to keep them in here in case
    private void visit(BSTNode node) {
        //Cant have print statements so need another way to do this with iterator
        //TODO:
    }

    private void levelOrderTraversal(BSTNode r) {
        if (r == null) {
            return;
        }

        Queue<BSTNode> queue = new LinkedList<>();
        queue.add(r);

        while (!queue.isEmpty()) {
            BSTNode curr = queue.remove();
            visit(curr);

            if (curr.getLeft() != null) {
                queue.add(curr.getLeft());
            }
            if (curr.getRight() != null) {
                queue.add(curr.getRight());
            }
        }

    }

    class BSTNode implements Comparable<BSTNode> {
        private T data;
        private BSTNode left;
        private BSTNode right;

        public BSTNode(T d) {
            setLeft(null);
            setRight(null);
            setData(d);
        }

        public T getData() {
            return data;
        }

        public void setData(T d) {
            data = d;
        }

        public void setLeft(BSTNode l) {
            left = l;
        }

        public void setRight(BSTNode r) {
            right = r;
        }

        public BSTNode getLeft() {
            return left;
        }

        public BSTNode getRight() {
            return right;
        }

        public boolean isLeaf() {
            return (getLeft() == null) && (getRight() == null);
        }

        public int compareTo(BSTNode o) {
            return this.getData().compareTo(o.getData());
        }
    }
}
