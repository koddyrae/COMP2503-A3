import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

//Used the lab 7 skeleton code for BST class

public class BST<T extends Comparable<T>> {
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

        private BSTNode root;
        private int size;

    /**
     * Default Constructor for a BST
     */
    public BST() {
            root = null;
            size = 0;
        }

        //The sorting of the bst happens here???
        //brain dont understand this :(

    /**
     * Constructor for a BST that contains a comparator for ordering
     * @param comparator the desired comparator used
     */
    public BST(Comparator<T> comparator) {
            //TODO:
        }

    /**
     * Public method to add a node to the tree
     * @param data the data of the node being added
     */
    public void add(T data) {
            BSTNode n = new BSTNode(data);
            if (root == null) {
                root = n;
            } else {
                add(root, n);
            }
            size++;
        }

    /**
     * Private method to add nodes at a given root recursively
     * @param root location of where node should be added
     * @param nodeToAdd the node being added
     */
    private void add(BSTNode root, BSTNode nodeToAdd) {
            int c = nodeToAdd.compareTo(root);

            if (c < 0) {
                if (root.getLeft() == null) {
                    root.setLeft(nodeToAdd);
                }
                else {
                    add(root.getLeft(), nodeToAdd);
                }
            }
            else if (c > 0) {
                if (root.getRight() == null) {
                    root.setRight(nodeToAdd);
                }
                else {
                    add(root.getRight(), nodeToAdd);
                }
            }
            else {
                //Not sure how to deal with duplicates atm (probably increments frequency in assignment)
                //TODO:
                return;
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



}
