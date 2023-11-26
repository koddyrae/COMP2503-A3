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
    private BSTNode root = null;
    private int size = 0;
    private final Comparator<T> cmp;
    private final Stack<BSTNode> path = new Stack<>();
    // A node which is orphaned when the minimum of a right-subtree has a right child. It is promoted to its parents position.
    private BSTNode orphan = null;

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
    private void add(BSTNode root, BSTNode nodeToAdd) {
        int comparison = cmp.compare(root.getData(), nodeToAdd.getData());

        // There should never be a case where the comparison is equal, so it's not dealt with here.
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
        }
    }

    /**
     * @author Bryce Carson
     * Method to delete a node from the BST
     * @param t the data of the node that needs to be deleted
     *
     */
    public void delete(T t) {
        // Recall that find has the side effect of setting `path`.
        if (find(t) == t) {
            int children = 0;
            if (path.peek().getLeft() != null) children++;
            if (path.peek().getRight() != null) children++;

            BSTNode child, leftChild, rightChild, target, parent, grandparent, minimum;

            switch(children) {
                case 1:
                    child = ((path.peek().getLeft() == null) ? path.peek().getRight() : path.peek().getLeft());

                    if (path.peek() == root) {
                        root = child;
                        break;
                    }

                    target = path.pop();
                    grandparent = path.pop();

                    if ((grandparent.getLeft() == target)) {
                        grandparent.setLeft(child);
                    } else {
                        grandparent.setRight(child);
                    }

                    break;
                case 2:
                    target = path.pop();
                    grandparent = path.pop(); // Null when target == root.
                    leftChild = target.getLeft();
                    rightChild = target.getRight();
                    minimum = minimum(rightChild);

                    if (target == root) {
                        // Orphanage and adoption potentially occurs in this case; if the orphan is not null, then the
                        // orphan's grandparent adopts it to it's left.
                        minimum.setLeft(leftChild);
                        minimum.setRight(rightChild);
                        root = minimum;
                        if (orphan != null) {
                            BSTNode orphan = this.orphan;
                            minimum(rightChild).setLeft(orphan); // Acquire the grandparent of the orphan.
                            this.orphan = null; // Nullify unintended side-effect.
                        }
                    } else {
                        // Recall that minimum sets this.orphan; this is irrelevant when the node being removed is not
                        // root.
                        grandparent.setRight(minimum(rightChild).setLeft(leftChild));
                    }

                    break;
                case 0:
                default:
                    if (path.peek() == root) { root = null; break; } // Break early, there's no parent who disowns.
                    target = path.pop();
                    parent = path.pop();

                    // Finally delete the node by de-referencing the proper child of the parent (disown the child).
                    if (parent.getRight() == target) parent.setRight(null);
                    if (parent.getLeft() == target) parent.setLeft(null);

                    // Stop switching.
                    break;
            }

            // The path has been modified, so it must be emptied before any other operations may occur.
            while(path.size() > 0) {
                path.pop();
            }
        }
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
        while (path.size() > 0) {
            path.pop();
        }

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
        if (n == null) {
            // Empty the stack, because no element matching the search parameter was found.
            while (path.size() > 0) {
                path.pop();
            }
        } else {
            path.push(n);
            int c = cmp.compare(t, n.getData());
            if (c < 0) {
                find(t, n.getLeft());
            } else if (c > 0) {
                find(t, n.getRight());
            }
            /* If the comparison, c, is equal to zero we have found the element and no
             * other branch applies. Recursion stops, path is retained, and the method
             * returns to its caller.
             */
        }
    }

    /**
     * Method to return the minimum node of the tree
     * @return node the data of the node
     * @throws RuntimeException exception for if there is no minimum in the tree (it's null)
     */
    // Calling this method sets this.orphan, but the node is not actually orphaned.
    public T minimum() throws RuntimeException {
        if (root == null) throw new RuntimeException("There is no minimum in a tree with a null root.");
        return minimum(root).getData();
    }

    // Calling this method sets this.orphan, but the node is not actually orphaned.

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
            this.orphan = n.getRight(); // NULL is a permitted value for this field.
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
        return ( (r == null) ? -1 : (1 + Math.max(height(r.getLeft()), height(r.getRight()))) );
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
     * Static class to initialize the Pre-order iterator
     * @param <T> The type the iterator yields, which is the same type as the tree the iterator is initialized with.
     */
    static class PreOrderIterator<T extends Comparable<T>> implements Iterator<T> {
        private final Queue<T> typeQueue = new LinkedList<>();

        /**
         * Default constructor of the PreOrder iterator
         * @param tree the tree that needs to be traversed
         */
        PreOrderIterator(BST<T> tree) {
            preOrderTraversal(tree.root);
        }

        /**
         * Method to preOrder traverse through the tree
         * @param n the node to check
         */
        private void preOrderTraversal(BST<T>.BSTNode n) {
            if (n != null) {
                this.typeQueue.add(n.getData());
                preOrderTraversal(n.getLeft());
                preOrderTraversal(n.getRight());
            }
        }

        /**
         * Override method to return a boolean if there is a next
         * @return A boolean describing whether there are elements left to traverse.
         */
        @Override
        public boolean hasNext() {
            return !typeQueue.isEmpty();
        }

        /**
         * Override method to return the next element
         * @return T the next element in the Tree.
         */
        @Override
        public T next() {
            return typeQueue.remove();
        }
    }

    /**
     * Static class to initialize the Post-order iterator
     * @param <T> The type the iterator yields, which is the same type as the tree the iterator is initialized with.
     */
    static class PostOrderIterator<T extends Comparable<T>> implements Iterator<T> {
        private final Queue<T> typeQueue = new LinkedList<>();

        /**
         * Default constructor of the PostOrder iterator
         * @param tree the tree that needs to be traversed
         */
        PostOrderIterator(BST<T> tree) {
            postOrderTraversal(tree.root);
        }

        /**
         * Method to postOrder traverse through the tree
         * @param n the node to check
         */
        private void postOrderTraversal(BST<T>.BSTNode n) {
            if (n != null) {
                postOrderTraversal(n.getLeft());
                postOrderTraversal(n.getRight());
                this.typeQueue.add(n.getData());
            }
        }

        /**
         * Override method to return a boolean if there is a next
         * @return A boolean describing whether there are elements left to traverse.
         */
        @Override
        public boolean hasNext() {
            return !typeQueue.isEmpty();
        }

        /**
         * Override method to return the next element
         * @return T the next element in the Tree.
         */
        @Override
        public T next() {
            return typeQueue.remove();
        }
    }

    /**
     * Static class to initialize the In-order iterator
     * @param <T> The type the iterator yields, which is the same type as the tree the iterator is initialized with.
     */
    static class InOrderIterator<T extends Comparable<T>> implements Iterator<T> {
        private final Queue<T> typeQueue = new LinkedList<>();

        /**
         * Default constructor of the InOrder iterator
         * @param tree the tree that needs to be traversed
         */
        InOrderIterator(BST<T> tree) {
            inOrderTraversal(tree.root);
        }

        /**
         * Method to inOrder traverse through the tree
         * @param n the node to check
         */
        private void inOrderTraversal(BST<T>.BSTNode n) {
            if (n != null) {
                inOrderTraversal(n.getLeft());
                this.typeQueue.add(n.getData());
                inOrderTraversal(n.getRight());
            }
        }

        /**
         * Override method to return a boolean if there is a next
         * @return A boolean describing whether there are elements left to traverse.
         */
        @Override
        public boolean hasNext() {
            return !typeQueue.isEmpty();
        }

        /**
         * Override method to return the next element
         * @return T the next element in the Tree.
         */
        @Override
        public T next() {
            return typeQueue.remove();
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
         * @param l the node to be set as left child
         * @return node the current calling node
         */
        public BSTNode setLeft(BSTNode l) {
            left = l;
            return this;
        }

        /**
         * Method to set the right child of a node
         * @param r the node to be set as right child
         * @return node the current calling node
         */
        public BSTNode setRight(BSTNode r) {
            right = r;
            return this;
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
         * Method to check if the calling node is a leaf
         * @return true if node is a leaf, false otherwise
         */
        public boolean isLeaf() {
            return (getLeft() == null) && (getRight() == null);
        }

        /**
         * Comparison method to compare two nodes
         * @param n the object to be compared.
         * @return int the difference between the two nodes
         */
        public int compareTo(BSTNode n) {
            return this.getData().compareTo(n.getData());
        }

        /**
         * Comparison method to compare the data between a node and data
         * @param t the data of to be compared
         * @return int the difference between two nodes
         */
        public int compareTo(T t) {
            return this.getData().compareTo(t);
        }
    }
}
