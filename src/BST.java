import java.util.*;

/**
 * @author Bryce, Koddy, Nandan
 * This generalized binary search tree is initialized with a
 * Comparator, which will thus control the order elements are added to
 * the tree. Elements will thus be added in-order, in a trivial,
 * non-balancing manner. They will not, hopefully, be added in a sorted
 * order, or the tree will be degenerate. Hopefully the elements will be
 * received out of order, and the tree will be constructed appropriately,
 * though naively.
 */
public class BST<T extends Comparable<T>> {
    // DONE: revert to private after using TreePrinter to test.
    // public BSTNode root = null;
    private BSTNode root = null;
    private int size = 0;
    private final Comparator<T> cmp;
    private final Stack<BSTNode> path = new Stack<>();

    /**
     * Constructor for a BST that contains a comparator for ordering
     * @param comparator the desired comparator used
     */
    public BST(Comparator<T> comparator) {
        cmp = comparator;
    }
    public BST() {
        cmp = Comparator.naturalOrder();
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
    private void add(BSTNode root, BSTNode nodeToAdd) throws RuntimeException {
        int comparison = cmp.compare(nodeToAdd.getData(), root.getData());

        if (comparison < 0) {
            if (root.getLeft() == null) {
                root.setLeft(nodeToAdd);
            }
            else {
                add(root.getLeft(), nodeToAdd);
            }
        }
        else if (comparison > 0) {
            if (root.getRight() == null) {
                root.setRight(nodeToAdd);
            }
            else {
                add(root.getRight(), nodeToAdd);
            }
        } else {
            throw new RuntimeException("Existing element attempting to be added to tree.");
        }
    }

    /**
     * @author Bryce Carson
     * Method to delete an element from the BST
     * @param targetElement the data of the node that needs to be deleted
     *
     */
    public void delete(T targetElement) {
        if (root != null) {
            root = delete(root, targetElement);
        }
    }

    /**
     * @author Nandan
     * @param subtreeRoot The subtreeRoot of the subtree.
     * @param targetElement The element to delete from the tree.
     * @return A node, which is used to update the tree in previous recursive calls.
     */
    private BSTNode delete(BSTNode subtreeRoot, T targetElement) {
        if (subtreeRoot != null) {
            int comparison = cmp.compare(targetElement, subtreeRoot.getData());

            if (comparison < 0) {
                subtreeRoot.setLeft(delete(subtreeRoot.getLeft(), targetElement));
            } else if (comparison > 0) {
                subtreeRoot.setRight(delete(subtreeRoot.getRight(), targetElement));
            } else {
                // targetElement is equal to the subtreeRoot node.
                if (subtreeRoot.getLeft() == null) {
                    return subtreeRoot.getRight();
                } else if (subtreeRoot.getRight() == null) {
                    return subtreeRoot.getLeft();
                }

                // Node with two children: get the inorder successor (smallest
                // in the right subtree)
                subtreeRoot.setData(minimum(subtreeRoot.getRight()).getData());

                // Delete the inorder successor
                subtreeRoot.setRight(delete(subtreeRoot.getRight(), subtreeRoot.getData()));
            }
        }
        return subtreeRoot;
    }

    /**
     * If the data is found within the tree, the path along the edges of the tree are
     * available in the tree's path field. If the data is not found, the path is
     * emptied and null is returned.
     * @param t The data to look for in the tree.
     * @return The data found: the corresponding element equivalent to the data searched for, t, or null.
     */
    public T find(T t) {
        // Empty the stack before beginning a search.
        path.clear();

        // Search for an element containing data equivalent to the search parameter object.
        find(t, root);

        // Return null if the path stack is empty; otherwise, return the found object.
        return (path.empty()) ? null : path.peek().getData();
    }

    /**
     * Method to find a specific node
     * @param t the data that is being searched for
     * @param n the starting point to search at
     */
    private void find(T t, BSTNode n) {
        while (n != null) {
            path.push(n);
            int comparison = cmp.compare(t, n.getData());
            if (comparison < 0) {
                n = n.getLeft();
            } else if (comparison > 0) {
                n = n.getRight();
            } else {
                /* If the comparison is equal to zero we have found the element and no
                 * other branch applies. Looping stops, path is retained (because n is not
                 * null), and the method returns to its caller.
                 */
                break;
            }
        }

        // Empty the stack, because no element matching the search parameter was found.
        if (n == null) {
            path.clear();
        }
    }

    /**
     * Method to return the minimum node of the tree
     * @param n the starting node (either smallest or needs to recurse)
     * @return node the smallest node of the tree
     */
    private BSTNode minimum(BSTNode n) {
        // Either the current node n is the smallest node, because it has no
        // lesser node, or the method needs to recurse.
        if (n.getLeft() != null) {
            return minimum(n.getLeft()); // Tail-recurse
        } else {
            return n; // The minimum in the subtree.
        }
    }

    /**
     * Method to return size of the BST
     * @return size of tree
     */
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
        return ( (r == null) ? 0 : (1 + Math.max(height(r.getLeft()), height(r.getRight()))));
    }

    /**
     * Static class to initialize the level order iterator
     * @param <T> The type the iterator yields, which is the same type as the tree the iterator is initialized with.
     */
    static class LevelOrderIterator<T extends Comparable<T>> implements Iterator<T> {

        /*
         Non-static; it is very important that there are no static fields in this static nested class; every iterator
         is its own object and needs to be so, such that the typeQueue relates to a specific iteration, not the whole
         class of Iterators!
        */
        private final Queue<T> typeQueue = new LinkedList<>();

        /**
         * Default constructor of the LevelOrder iterator
         * @param tree the tree that needs to be traversed
         */
        public LevelOrderIterator(BST<T> tree) {
            if (tree.root == null) {
                // The root node is null, so the returnQueue remains empty;
                // hasNext() will report false, and next() will throw an exception
                // per convention.
                return;
            }

            Queue<BST<T>.BSTNode> queue = new LinkedList<>();
            
            // Add the root of the tree to the queue to begin the iterative processing.
            queue.add(tree.root);

            while (!queue.isEmpty()) {
                BST<T>.BSTNode curr = queue.remove();
                
                // Provide the data to the iterator.
                this.typeQueue.add(curr.getData());

                if (curr.getLeft() != null) {
                    queue.add(curr.getLeft());
                }
                if (curr.getRight() != null) {
                    queue.add(curr.getRight());
                }
            }
        }

        /**
         * Override method to return a boolean if there is a next
         * @return A boolean describing whether there are elements left to traverse.
         */
        public boolean hasNext() {
            return !typeQueue.isEmpty();
        }

        /**
         * Override method to return the next element
         * @return T the next element in the Tree.
         */
        public T next() {
            return typeQueue.remove();
        }
    }

    /**
     * Static class to initialize the In-order iterator
     * @param <T> The type the iterator yields, which is the same type as the tree the iterator is initialized with.
     */
    static class InOrderIterator<T extends Comparable<T>> implements Iterator<T> {
        private final Queue<T> queue = new LinkedList<>();

        public InOrderIterator(BST<T> tree) throws IllegalArgumentException {
            if (tree == null) {
                throw new IllegalArgumentException("tree parameter cannot be null.");
            } else {
                Stack<BST<T>.BSTNode> stack = new Stack<>();
                BST<T>.BSTNode current = tree.root;

                // FIXME: there is a circularity in the tree when using input one.
                while(!stack.empty() || current != null) {
                    if (current != null) {
                        current = stack.push(current).getLeft();
                    }
                    else {
                        current = stack.pop();
                        this.queue.add(current.getData());
                        current = current.getRight();
                    }
                }
            }
        }

        /**
         * Override method to return a boolean if there is a next
         * @return A boolean describing whether there are elements left to traverse.
         */
        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        /**
         * Override method to return the next element
         * @return T the next element in the Tree.
         */
        @Override
        public T next() {
            return queue.remove();
        }
    }

    /**
     * Inner class for BSTNode
     */
    class BSTNode implements Comparable<BSTNode> {
        private T data;
        private BSTNode left;
        private BSTNode right;

        /**
         * Default constructor for a BST Node
         * @param d the data to be stored in the node
         */
        public BSTNode(T d) {
            setLeft(null);
            setRight(null);
            setData(d);
        }

        /**
         * Method to return the data inside a node
         * @return data the data in the node
         */
        public T getData() {
            return data;
        }

        /**
         * Method to set the data of a node if not set at creation
         * @param d the data in the node
         */
        public void setData(T d) {
            data = d;
        }

        /**
         * Method to set the left child of a node
         *
         * @param l the node to be set as left child
         */
        public void setLeft(BSTNode l) {
            left = l;
        }

        /**
         * Method to set the right child of a node
         *
         * @param r the node to be set as right child
         */
        public void setRight(BSTNode r) {
            right = r;
        }

        /**
         * Method to return the left child of a node
         * @return node the left child
         */
        public BSTNode getLeft() {
            return left;
        }

        /**
         * Method to return the right child of a node
         * @return node the right child
         */
        public BSTNode getRight() {
            return right;
        }

        /**
         * Comparison method to compare two nodes
         * @param n the object to be compared.
         * @return int the difference between the two nodes
         */
        public int compareTo(BSTNode n) {
            return this.getData().compareTo(n.getData());
        }

    }
}
