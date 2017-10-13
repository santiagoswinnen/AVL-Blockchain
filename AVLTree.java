
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *  @author Lï¿½rï¿½nt Mikolï¿½s
 */
/**
 *	Adelson Velskii Landis tree
 */
public class AVLTree<T> {
    private Node<T> root;
    private Comparator<T> cmp;

    public AVLTree(Comparator<T> cmp){
        this.cmp = cmp;
    }

       
    /**
     * Node of an AVLTree. Contains a key of type <T>, has left and right children nodes, 
     * as well as a height in the tree and a list of indices corresponding to blocks in a BlockChain 
     * that have modified it
     *	@see PrintableNode
     */
    private static class Node<T> implements PrintableNode {
        private Node<T> left;
        private Node<T> right;
        private T key;
        private int height = 0;
        
        /*indexes of the BlockChain's blocks  that affected this node*/
        private HashSet<Integer> modIndex = new HashSet<>();

        public Node(T key) {
            height = 0;
            left = null;
            right = null;
            this.key = key;
        }

        public Node(T key, int index){
            this(key);
            this.modIndex.add(index);
        }

        public Node(T key, Node<T> left, Node<T> right) {
            this.left = left;
            this.right = right;
            this.key = key;
        }

        
        /**
         * Returns a string describing the node. 
         * <p>
         * The string contains the node's key, its height in the tree and 
         * all of the indices of the blocks in the BlockChain that have modified this node.
         * @return the node's information in a string
         */
        @Override
        public String toString(){
            return  key.toString()  + "(" + height + ")"  + modIndex;
        }

        public int getHeight(){
            return height;
        }

        public Node<T> getLeft() {
            return left;
        }

        public Node<T> getRight() {
            return right;
        }

        
        /**
         * Returns the height of the node's right child node. If the node 
         * does not have a right child, returns -1
         *	@return the height of right child node
         */
        public int getRightChildHeight(){
            if(right == null){
                return -1;
            }
            return right.height;
        }

        
        /**
         * Returns the height of the node's left child node. If the node 
         * does not have a left child, returns -1
         *	@return the height of left child node
         */
        public int getLeftChildHeight(){
            if(left == null){
                return -1;
            }
            return left.height;
        }
    }
    //End Node
    
    
    public Node<T> getRoot() {
        return root;
    }

    
    /**
     * Adds a new element to the AVLTree, in a new node.
     * This method serves as a wrapper.
     * @param key element to add.
     * @param blockIndex index of the BlockChain's block that has the add operation.
     * @return boolean that is true if element is added or false otherwise
     */
    public boolean add(T key, int blockIndex) {
        Node<T> aux = add(key, root, blockIndex);
        if(aux == null) return false;
        root = aux;
        return true;
    }

    
    /**
     * Adds a new element to the AVLTree recursively. Checks balance, updates height and
     * updates the indices of the blocks that have affected the node on the way back of the
     * recursion. Once added, tree is checked for balance.
     * @param key element to add.
     * @param current current element in the recursion.
     * @param blockIndex index of the BlockChain's block that has the add operation.
     * @return child node in recursion or null if key is already present in the AVLTree.
     * @see balance
     */
    private Node<T> add(T key, Node<T> current, int blockIndex) {
        Node<T> aux;
        /*if the leaf is reached, add a new node*/
        if(current == null){
            return new Node<>(key, blockIndex);
        }
        /*advance through left child*/
        if(cmp.compare(key,current.key) < 0 ){

            aux = add(key, current.left, blockIndex); //calls recursive method.
            if(aux == null) return null;
            if(aux != current.left) current.modIndex.add(blockIndex);
            current.left = aux;
            /*update current nodes height*/
            current.height = Math.max(current.height, current.getLeftChildHeight() + 1);
        }
        /*advance through right child*/
        else if(cmp.compare(key,current.key) > 0 ){

            aux = add(key, current.right, blockIndex); //calls recursive method.
            if(aux == null) return null;
            if(aux != current.right) current.modIndex.add(blockIndex);
            current.right = aux;
            /*update current nodes height*/
            current.height = Math.max(current.height, current.getRightChildHeight() + 1);
        }
        else {
            return null;
        }
        current = balance(current, blockIndex);
        return current;
    }

    
    /**
     * Checks balance of the node.
     * <p>
     * Balance is found by recursively taking the height of the left child and 
     * subtracting the height of the right child. Any balance not -1, 0, or 1 signifies 
     * an unbalanced tree. To restore balance, limbs of the tree are rotated.
     * @param current Node, whose children will be checked for balance
     * @param blockIndex index of the BlockChain's block that has the add operation. It's used
     *                   in case  a rotation is needed.
     * @return a new node in case the current was affected by a rotation.
     * @see leftRotation
     * @see rightRotation
     */
    public Node<T> balance(Node<T> current, int blockIndex){
        int balance = getBalance(current);
        /*checks if skewed to the left*/
        if(balance > 1){
            /*left left*/
            if(getBalance(current.left) >= 0){
                current = rightRotation(current, blockIndex);

            }
            /*left right*/
            else{
                current.left = leftRotation(current.left, blockIndex);
                current = rightRotation(current, blockIndex);
            }
        }
        /*checks if skewed to the right*/
        else if(balance < -1){
            /*right right*/
            if(getBalance(current.right) <= 0) {
                current = leftRotation(current, blockIndex);

            }
            /*right left*/
            else{
                current.right = rightRotation(current.right, blockIndex);
                current = leftRotation(current, blockIndex);
            }
        }
        return current;
    }

    
    /**
     * Performs a left rotation from the current node. 
     * <p>
     * The rotation acts on the current node, its right child, and its right child's left child. 
     * The current is made to be the left child of its right child, and the right child's left child is made
     * to be the right child of current. The former right child of current is returned.    
     * @param current node to be rotated.
     * @param blockIndex index of the BlockChain's block that has the add operation. Every node
     *                   affected by this rotation will update the indexes of the blocks that modified said node.
     * @return the new root of the subtree.
     */
    private Node<T> leftRotation(Node<T> current, int blockIndex){
        Node<T> auxright = current.right;
        current.right = auxright.left;
        auxright.left = current;
        current.height = Math.max(current.getLeftChildHeight() + 1, current.getRightChildHeight() + 1);
        auxright.height = Math.max(auxright.height, current.height + 1);
        current.modIndex.add(blockIndex);
        auxright.modIndex.add(blockIndex);
        if(current.right != null) current.right.modIndex.add(blockIndex);
        return auxright;
    }
    
    
    /**
     * Performs a right rotation from the current node. 
     * <p>
     * The rotation acts on the current node, its left child, and its left child's right child. 
     * The current is made to be the right child of its left child, and the left child's right child is made
     * to be the left child of current. The former left child of current is returned.
     * @param current node to be rotated.
     * @param blockIndex index of the BlockChain's block that has the add operation. Every node
     *                   affected by this rotation will update the indexes of the blocks that modified said node.
     * @return the new root of the subtree.
     */
    private Node<T> rightRotation(Node<T> current, int blockIndex){
        Node<T> auxleft = current.left;
        current.left = auxleft.right;
        auxleft.right = current;
        current.height = Math.max(current.getLeftChildHeight() + 1, current.getRightChildHeight() + 1);
        auxleft.height = Math.max(auxleft.height, current.height + 1);
        current.modIndex.add(blockIndex);
        auxleft.modIndex.add(blockIndex);
        if(current.left != null) current.left.modIndex.add(blockIndex);
        return auxleft;
    }

    /**
     * Calculates the height difference between left and right child trees. 
     * An Empty AVLTree is considered balanced.
     * @param Current Node, whose children will be checked for balance
     * @return Integer representing the left child's height - the right child's height
     */
    public int getBalance(Node<T> current){
        if(current == null) return 0;
        return current.getLeftChildHeight() - current.getRightChildHeight();
    }

    
    /**
     * Removes a key from the AVLTree.
     * A DataPair is used to allow for two return values for the remove method.
     * @param key element to be removed from AVLTree.
     * @param blockIndex index of the BlockChain's block that has the remove operation.
     * @return true if removal was successful or false otherwise.
     * @see DataPair
     */
    public boolean remove(T key, int blockIndex){
        DataPair<Boolean,Node<T>> aux = remove(key, root, blockIndex);
        root = aux.getElement2();
        return aux.getElement1();

    }
    
    
    /**
     * Removes a key from the AVLTree recursively. Checks balance, updates height and
     * updates the indexes of the blocks that affected the node on the way back of the
     * recursion.
     * This method serves as a wrapper. A DataPair is used to allow for two return values for the remove method.
     * @param key element to be removed from AVLTree.
     * @param blockIndex index of the BlockChain's block that has the remove operation.
     * @return a DataPair. The element1 of the DataPair indicates if the removal was successful or not with a
     * boolean and element2 is the new child returned in the recursion.
     * @see DataPair
     */
    private DataPair<Boolean, Node<T>> remove(T key, Node<T> current, int blockIndex){
        /*if the element was not found returns false (unsuccessful removal)*/
        if(current == null){
            return new DataPair<>(false,null);
        }
        DataPair<Boolean,Node<T>> aux;
        Node<T> node;
        /*advance through left child*/
        if(cmp.compare(key, current.key) < 0){
            aux = remove(key, current.left, blockIndex);
            node = aux.getElement2();
            if(current.left != node) current.modIndex.add(blockIndex);
            current.left = node;
        }
        /*advance through right child*/
        else if(cmp.compare(key, current.key) > 0){
            aux = remove(key, current.right, blockIndex);
            node = aux.getElement2();
            if(current.right != node) current.modIndex.add(blockIndex);
            current.right = node;
        }
        /*element found*/
        else {
            current = deleteKey(current, blockIndex);
            current = balance(current, blockIndex);
            return new DataPair<>(true, current);
        }
        /*if removal was successful, updates height and checks balance on the way back*/
        if(aux.getElement1()) {
            current.height = Math.max(current.getLeftChildHeight() + 1, current.getRightChildHeight() + 1);
            current = balance(current, blockIndex);
        }
        return new DataPair<>(aux.getElement1(), current);
    }

    /**
     * Removes key from the AVLTree in different ways depending on the existence of right and left children.
     * @param node  to be removed from the AVLTree/
     * @param blockIndex index of the BlockChain's block that has the remove operation.
     * @return the new node or null that will take the place of the removed node.
     * @see DataPair
     */

    public Node<T> deleteKey( Node<T> node, int blockIndex) {
        /*no children*/
        if (node.right == null && node.left == null) {
            return null;
        /*has only left child*/
        } else if (node.right == null) {
            return node.left;
        /*has only right child*/
        } else if (node.left == null) {
            return node.right;
        /*has both left and right children*/
        /*search for the inorder successor*/
        } else {
            DataPair<Node<T>, Node<T>> aux = eliminateMostLeft(node.right, blockIndex);
            Node<T> ret = aux.getElement2();
            /*it may be that the inorder successor is the right child*/
            if(ret == node.right) ret.right = null;
            else ret.right = node.right;
            ret.left = node.left;

            ret.height = Math.max(ret.getLeftChildHeight() + 1, ret.getRightChildHeight() + 1);
            ret.modIndex.add(blockIndex);
            return ret;
        }
    }

    /**
     * Removes the inorder successor from its current position and returns it recursively. 
     * Checks balance, updates height and updates the indices of the blocks that
     * affected the node on the way out of the recursion.
     * @param current node in the recursion.
     * @param blockIndex index of the BlockChain's block that has the remove operation.
     * @return a DataPair in which the element1 is the new child in recursion and element2 is the succesor inorder.
     * @see DataPair
     * @see NoSuccesorInorderException
     */
    private DataPair<Node<T>,Node<T>> eliminateMostLeft(Node<T> current, int blockIndex){
        if(current == null){
            throw new NoSuccesorInorderException("There was no succesor inorder.");
        }
        DataPair<Node<T>, Node<T>> aux;
        if(current.left != null){
            /*call recursion*/
            aux = eliminateMostLeft(current.left, blockIndex);
            if(current.left != aux.getElement1()) current.modIndex.add(blockIndex);
            current.left = aux.getElement1();
        } else {
            return new DataPair<>(null, current);
        }
        current.height = Math.max(current.getLeftChildHeight() + 1, current.getRightChildHeight() + 1);
        current = balance(current, blockIndex);
        return new DataPair<>(current, aux.getElement2());
    }




    /**
     * Copies the values of an AVLTree in a range between two values into a LinkedList. 
     * A wrapper function.
     * @param low minimum value to be copied
     * @param high maximum value to be copied
     * @return the list of values as a LinkedList
     */
    public List<T> getInRange(T low, T high) {
        List<T> result = new LinkedList<>();
        getInRange(root, result, low, high, cmp);
        return result;
    }

    
    /**
     * Recursively moves through tree, adding all values between the minimum and maximum search 
     * parameters into the list of values
     * @param current current node being polled
     * @param result the list of values
     * @param low minimum value to be copied
     * @param high maximum value to be copied
     * @param cmp comparator to use
     */
    private void getInRange(Node<T> current, List<T> result, T low, T high, Comparator<T> cmp) {
        if (current == null) {
            return;
        }
        /*If value found between low and high, put into list*/
        if (cmp.compare(low, current.key) < 0 && cmp.compare(high, current.key) > 0) {
            result.add(current.key);
        }
        getInRange(current.right, result, low, high, cmp);
        getInRange(current.left, result, low, high, cmp);
        return;
    }
    
    
    
    
    public void print(){
        printNodesByLevel();

    }

    
    /**
     * Prints the AVLTree to the screen by level, starting from the root node to the leaves.
     */
    public void printNodesByLevel() {
        Deque<Node<T>> queue = new LinkedList<>();
        if (root == null){
            return;
        }
        queue.offer(root);
        int i = 0 ;
        double number = 1;
        while (!queue.isEmpty()) {
            Node<T> aux = queue.remove();
            if(	Math.log(number)/Math.log(2.0) == i ){
                System.out.println(" LEVEL + 1 = " + Math.log(number)/Math.log(2.0) );
                i++;
            }
            if (aux != null) {
                queue.offer(aux.left);
                queue.offer(aux.right);
                System.out.print( aux.toString() + "    ");
            } else {
                System.out.print("-EMPTY-    ");
            }
            number++;
        }
    }


    /**
     * Finds and returns the height of any node by finding the maximum height of its children.
     * @param current the node being polled for height
     * @return	the height of that particular node
     */
    public static <T> int getHeight(Node<T> current) {
        if (current == null) return -1;
        return 1 + Math.max(getHeight(current.left), getHeight(current.right));
    }


    /**
     * Searches for key in the Tree and returns modIndex set.
     * This method serves as a wrapper
     * @param key to be searched in tree.
     * @return a DatarPair in which element1 is a Boolean (true if key was found, false otherwise)
     * and a Set of Integers with the indexes of the blocks that modified that node.
     */
    public DataPair<Boolean,Set<Integer>> lookup(T key) {
        return lookup(key,root);
    }

    /**
     * Searches for key in the Tree and returns modIndex set recursively.
     * @param key to be searched in tree.
     * @return a DatarPair in which element1 is a Boolean (true if key was found, false otherwise)
     * and a Set of Integers with the indexes of the blocks that modified that node.
     */
    private DataPair<Boolean,Set<Integer>> lookup(T key, Node<T> current) {
        if (current == null)
             return new DataPair<>(false, null);
        if (cmp.compare(key, current.key) < 0) {
            return lookup(key, current.left);
        }
        if (cmp.compare(key, current.key) > 0) {
            return lookup(key, current.right);
        }
        return new DataPair<>(true, current.modIndex);
    }



    /**
     * Finds and returns the level of any given node in the tree, or -1 if never found.
     * A wrapper function
     * @param key the key being searched 
     * @return	the level at which the key was found, as an integer
     */
    public int getLevel(T key) {
        return getLevel(key, this.root, 0, this.cmp);
    }

    
    /**
     * Finds and returns the level of any given node in a tree, or -1 if never found.
     * @param key the value being searched
     * @param current the current given node
     * @param level the current level in the tree
     * @param cmp the comparator being used
     * @return the level at which the key was found, or -1 if not found
     */
    private int getLevel(T key, Node<T> current, int level, Comparator<T> cmp) {
        if (current == null) return -1;
        if (current.key.equals(key)) return level;

        if (current.right != null && cmp.compare(key, current.key) > 0)
            return getLevel(key, current.right, level + 1, cmp);
        if (current.left != null && cmp.compare(key, current.key) < 0)
            return getLevel(key, current.left, level + 1, cmp);
        return -1;
    }

    
    /**
     * Finds and returns the number of leaves of the entire AVLTree.
     * A wrapper function
     * @return the number of leaves
     */
    public int getLeavesCount() {
        return getLeavesCount(this.root);
    }

    
    /**
     * Finds and returns the number of leaves of any given tree or subtree.
     * @param current any current given node
     * @return the number of leaves stemming from the given node
     */
    public int getLeavesCount(Node<T> current) {
        if (current == null) return 0;
        int aux = getLeavesCount(current.left) + getLeavesCount(current.right);
        if (aux == 0)
            return 1;
        return aux;
    }

    /**
     * Finds and returns the maximum value of the entire AVLTree.
     * A wrapper function
     * @return the maximum value
     */
    public T getMax() {
        return getMax(this.root);
    }

    
    /**
     * Finds and returns the maximum value of any tree or subtree
     * @param current any given node
     * @return the maximum value stemming from any given node of the AVLTree
     */
    private T getMax(Node<T> current) {
        if (current == null) {
            return null;
        }
        if (current.right != null) {
            return getMax(current.right);
        }
        return current.key;
    }

    
    /**
     * Prints the direct descendants of a given node in an AVLTree.
     * A wrapper function
     * @param node
     */
    public void printDescendants(Node<T> node) {
        printDescendants(root, node, false);

    }

    /**
     * Prints to the screen the direct descendants of a given node in an AVLTree.
     * @param current the current node being checked in the tree
     * @param node the node with the descendants being searched
     * @param descendant boolean of whether or not the current node is a direct descendant
     */
    private void printDescendants(Node<T> current, Node<T> node, boolean descendant) {
        if (current == null) {
            return;
        }
        if (descendant && current.equals(node)) {
            printDescendants(current.right, node, true);
            printDescendants(current.left, node, true);
        }
        if (descendant) {
            System.out.println(current.toString());
        }
    }

    
    /**
     * Checks if the AVLTree is equal to the object
     * A wrapper function
     */
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof AVLTree)) {
            return false;
        }
        AVLTree<T> bst = (AVLTree<T>) o;
        return equals(root, bst.root);
    }

    
    /**
     * Checks recursively through two trees to verify that they are equal in each and every node.
     * @param current the current node of the base tree 
     * @param other	the current node of the node being tested
     * @return true if the node of which the nodes current and other are roots are exactly identical,
     * false otherwise
     */
    private boolean equals(Node<T> current, Node<T> other) {
        if (current == null && other == null)
            return true;
        boolean right, left;
        if (current.key.equals(other.key) && current.height == other.height) {
            return equals(current.right, other.right) && equals(current.left, other.left);
        }
        return false;

    }

    
    /**
     * The hashCode of the entire AVLTree.
     * A wrapper
     */
    public int hashCode() {
        return hashCode(root);
    }

    
    /**
     * Returns the hashCode of any given subtree
     * @param current the given root node of a subtree
     * @return the hash value of this tree
     */
    private int hashCode(Node<T> current) {
        if (current == null)
            return 1;
        return 31 * current.key.hashCode() + hashCode(current.right) + hashCode(current.left);
    }


    /**
     * Finds and returns the number of nodes in the entire AVLTree
     * @return the size of the tree
     */
    public int size() {
        return size(root);
    }

    
    /**
     * Finds and returns the number of nodes of any given AVLTree or subtree
     * @param current any given root node
     * @return this tree's number of nodes
     */
    private int size(Node<T> current) {
        if (current == null) return 0;
        return 1 + size(current.right) + size(current.left);
    }
    
    /**
     * Deletes the tree by setting it's root to null
     */
    public void clearTree() {
    	this.root = null;
    }

}
