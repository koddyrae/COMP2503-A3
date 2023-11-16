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
public class BST<T extends Comparable<T>> implements Iterator {
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

    /**
     * @author Bryce Carson
     */
    public T delete(T t) {
            // TODO:
            return null;
    }

    public T find(T t) {
        return find(t, root);
    }

    // This is quite dependent on the equality of the types being compared. If
    // they are simple base types, it is fine; for the Token class, the tokens
    // must be equal based on their string contents.
    private T find(T t, BSTNode n) {
        int c = n.getData().compareTo(t);

        // Base case, the node that this call was made with is what we're searching for.
        if (c == 0) {
            // The data was found at this node, so we return it.
            // NOTE: it is *imperative* that the argument t is not returned,
            // because it was never entered into the tree. The existing data
            // must be returned so that it can be modified if necessary by
            // the caller.
            return n.getData();
        } else if ((c < 0) && (n.getLeft() != null)) {
            // The data should be found to the left of the current node.
            return find(t, n.getLeft());
        } else if ((c > 0) && (n.getRight() != null)) {
            // The data should be found to the right of the current node.
            return find(t, n.getRight());
        } else {
            // The data was not found at any node in the tree.
            return null;
        }
    }

    public T minimumNode() {
        return minimumNode(root);
    }

    private T  minimumNode(BSTNode n) {
        // Either the curent node n is the smallest node, because it has no
        // lesser node, or the method needs to recurse.
        return ((n.getLeft() == null) ? n : minimumNode(n.getLeft()));
    }

    public int size() {
        return size;
    }

    /**
     * Method to return height of the tree
     * @return height of tree
     */
    public int height() {
        return height(root);
    }

    /**
     * Method to check for the height of the tree recursively
     * @param r the root node
     * @return height of node
     */
    private int height(BSTNode r) {
        return ( (r == null) ? -1 : (1 + Math.max(height(r.getLeft()), height(r.getRight()))) );
    }

    // NOTE: this should be how it is done, according to https://stackoverflow.com/questions/50329874/how-to-iterate-over-alternative-elements-using-an-iterator and https://www.baeldung.com/java-iterator-vs-iterable.
    // :shrug: I think; this is totally untested and only written in GitHub.dev, so I don't have any IntelliSense or langauge server features yelling at me when I make mistakes.
    public class LevelOrderIterator<T> implements Iterator<T> {

        private Queue<T> returnQueue = new LinkedList<>();

        public LevelOrderIterator() {
            if (this.root == null) {
                // The root node is null, so the returnQueue remains empty;
                // hasNext() will report false, and next() will throw an exception
                // per convention.
                return;
            }

            Queue<BSTNode> queue = new LinkedList<>();
            
            // Add the root of the tree to the queue to begin the iterative processing.
            queue.add(this.root);

            while (!queue.isEmpty()) {
                BSTNode curr = queue.remove();
                
                // Provide the data to the iterator.
                this.returnQueue.add(curr);

                if (curr.getLeft() != null) {
                    queue.add(curr.getLeft());
                }
                if (curr.getRight() != null) {
                    queue.add(curr.getRight());
                }
            }
        }

        public boolean hasNext() {
            return !returnQueue.isEmpty();
        }

        public T next() {
            return returnQueue.remove();
        }
    }

    // TODO: convert to an public inner class like the LevelOrderIterator above.
    private void preOrderTraversal(BSTNode n) {
        if (n == null) {
            return;
        } else {
            visit(n);
            preOrderTraversal(n.getLeft());
            preOrderTraversal(n.getRight());
        }
	}
	
    // TODO: convert to an public inner class like the LevelOrderIterator above.
	private void postOrderTraversal(BSTNode n) {
	    if (n == null) {
	        return;
	    } else {
	        postOrderTraversal(n.getLeft());
	        postOrderTraversal(n.getRight());
	        visit(n);
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
