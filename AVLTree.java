import com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator;

import java.util.*;

/**
 * Created by sswinnen on 21/09/17.
 */
public class AVLTree<T> {
    private Node<T> root;
    private Comparator<T> cmp;
    private int height;


    protected static class Node<T> {
        private Node<T> root;
        private Comparator<T> cmp;
        private Node<T> left;
        private Node<T> right;
        private T key;
        private int height = 0;

        public Node(T key) {
            height = 0;
            left = null;
            right = null;
            this.key = key;
        }

        public Node(T key, Node<T> left, Node<T> right) {
            this.left = left;
            this.right = right;
            this.key = key;
        }

    }



    public void add(T key) {
        if(root == null) {
            root = new Node<>(key);
            return;
        }
        add(key, root);
    }

    private int add(T key, Node<T> current) {

        if(cmp.compare(key,current.key) < 0 ){
            int leftH;
            if(current.left != null){
                leftH = add(key, current.left);
            }
            else{
                current.left = new Node<T>(key);
                leftH = 0;
            }
            current.height = Math.max(leftH + 1, current.height);
        }
        else if(cmp.compare(key,current.key) > 0 ){
            int rightH;
            if(current.right != null) {
                rightH = add(key, current.right);
            }
            else {
                current.right = new Node<T>(key);
                rightH = 0;
            }
            current.height = Math.max(rightH + 1, current.height);
        }
        current = balance(current);
        return current.height;
    }

    public Node<T> balance(Node<T> current){
        int balance = getBalance(current);
        if(balance > 1){
            /*left left*/
            if(getBalance(current.left) >= 0){
                current = rightRotation(current);

            }
            /*left right*/
            else{
                current = leftRotation(current);
                current = rightRotation(current);
            }
        }
        else if(balance < -1){
            /*right right*/
            if(getBalance(current.right) <= 0) {
                current = leftRotation(current);

            }
            /*right left*/
            else{
                current = rightRotation(current);
                current = leftRotation(current);
            }
        }
        return current;
    }

    public int getBalance(Node<T> current){
        return current.left.height-current.right.height;
    }

    public boolean remove(T key){
        if(root == null)
            return false;
        else if(root.key.equals(key)){
            root = null;
            return true;
        }
        if(remove(key, root, cmp) == -1)
            return false;
        return true;
    }

    private int  remove(T key, Node<T> current, Comparator<T> cmp){
        /*bajo por la izquierda*/
        if(cmp.compare(key, current.key) < 0 ){
            int leftH;
            if(current.left != null){
                if(current.left.key.equals(key)){
                    current.left = deleteKey(current.left);
                    leftH = current.left.height;
                } else {
                    leftH = remove(key, current.left, cmp);
                }
                if(leftH == -1)
                    return -1;
            } else{
                return -1;
            }
            current.height = Math.max(current.right.height + 1, leftH + 1);
        }
        /*bajo por la derecha*/
        else if(cmp.compare(key, current.key) > 0){
            int rightH;
            if(current.right != null){
                if(current.right.key.equals(key)){
                    current.right = deleteKey(current.right);
                    rightH = current.right.height;
                } else {
                    rightH = remove(key, current.right, cmp);
                }
                if(rightH == -1){
                    return -1;
                }
            }else{
                return -1;
            }
            current.height = Math.max(current.left.height + 1, rightH + 1);
        }
        current = balance(current);
        return current.height;
    }

    public Node<T> deleteKey(Node<T> current){
        if(current.left !=null && current.right != null) {
            Node<T> aux = getMostLeftWrapper(current.right);
            aux.left = current.left;
            if(aux.right == current.right){
                aux.right = null;
            }
            else{
                aux.right = current.right;
            }
            current = aux;
        }else if (current.left == null && current.right != null)
            current = current.right;
        else if (current.left != null)
            current = current.left;
        else
            current = null;
        return current;
    }

    private Node<T> getMostLeftWrapper(Node<T> current){
        if(current == null){
            throw new NoSuchElementException("There is un sucesor inorder");
        }
        return getMostLeft(current);
    }

    private Node<T> getMostLeft(Node<T> current) {
        Node<T> aux;
        if (current.left != null) {
            aux = current.left;
            if (aux.right == null && aux.left == null) {
                current.left = null;
                current.height = Math.max(current.height - 1, current.right.height + 1);
                return aux;
            }
            current.height = Math.max(current.height-1 , current.right.height +1);
            return getMostLeft(current.left);
        } else if (current.right != null){
            aux = current.right;
            if (aux.right == null && aux.left == null) {
                current.right = null;
                current.height = Math.max(current.height - 1, current.left.height + 1);
                return aux;
            }
            current.height = Math.max(current.height-1, current.left.height +1);
            return getMostLeft(current.right);
        }
        else{
            return current;
        }
    }



    private Node<T> rightRotation(Node<T> current){
        Node<T> auxright = current.right;
        current.right = auxright.left;
        auxright.left = current;
        current.height = Math.max(current.left.height, current.right.height);
        auxright.height = Math.max(auxright.left.height, auxright.right.height);
        return auxright;
    }

    private Node<T> leftRotation(Node<T> current){
        Node<T> auxleft = current.left;
        current.left = auxleft.right;
        auxleft.right = current;
        current.height = Math.max(current.left.height, current.right.height);
        auxleft.height = Math.max(auxleft.left.height, auxleft.right.height);
        return auxleft;
    }


    public List<T> getInRange(T inf, T sup) {
        List<T> result = new LinkedList<>();
        getInRange(root, result, inf, sup, cmp);
        return result;
    }

    private void getInRange(Node<T> current, List<T> result, T inf, T sup, Comparator<T> cmp) {
        if (current == null) {
            return;
        }
        if (cmp.compare(inf, current.key) < 0 && cmp.compare(inf, current.key) > 0) {
            result.add(current.key);
        }
        getInRange(current.right, result, inf, sup, cmp);
        getInRange(current.left, result, inf, sup, cmp);
        return;
    }





    public void printNodesByLevel() {
        Deque<Node<T>> queue = new LinkedList<>();
        if (root == null)
            return;
        queue.offer(root);
        int i = 0 ;
        double number = 0;
        while (!queue.isEmpty()) {
            Node<T> aux = queue.remove();
            if(	Math.log(number)/Math.log(2.0) == i ){
                System.out.println();
                i++;
            }
            System.out.print(aux.key + " ");

            if (aux.left != null) {
                queue.offer(aux.left);
            }
            if (aux.left != null) {
                queue.offer(aux.right);
            }
            number++;
        }
    }

    public static <T> int getHeight(Node<T> current) {
        if (current == null) return -1;
        return 1 + Math.max(getHeight(current.left), getHeight(current.right));
    }



    public boolean contains(T key) {
        return contains(key, root);
    }

    private boolean contains(T key, Node<T> current) {
        if (current == null)
            return false;
        if (cmp.compare(key, current.key) < 0) {
            return contains(key, current.left);
        }
        if (cmp.compare(key, current.key) > 0) {
            return contains(key, current.right);
        }
        return true;

    }



    public static <T> boolean isAVL(AVLTree<T> tree) {
        return isAVL(tree.root, tree.cmp);
    }

    public static <T> boolean isAVL(Node<T> bst, Comparator<T> cmp) {
        if (bst == null) return true;
        int l, r;
        Node<T> current = bst;
        l = getHeight(current.left);
        r = getHeight(current.right);
        if (Math.abs(l - r) <= 1 && isAVL(current.left, cmp) && isAVL(current.right, cmp)) {
            if (current.left != null && cmp.compare(current.key, current.left.key) < 0) {
                return false;
            }
            if (current.right != null && cmp.compare(current.key, current.right.key) > 0) {
                return false;
            }
            return true;
        }
        return false;
    }

    public int getLevel(T key) {
        return getLevel(key, this.root, 0, this.cmp);
    }

    private int getLevel(T key, Node<T> current, int level, Comparator<T> cmp) {
        if (current == null) return -1;
        if (current.key.equals(key)) return level;

        if (current.right != null && cmp.compare(key, current.key) > 0)
            return getLevel(key, current.right, level + 1, cmp);
        if (current.left != null && cmp.compare(key, current.key) < 0)
            return getLevel(key, current.left, level + 1, cmp);
        return -1;
    }

    public int getLeavesCount() {
        return getLeavesCount(this.root);
    }

    public int getLeavesCount(Node<T> current) {
        if (current == null) return 0;
        int aux = getLeavesCount(current.left) + getLeavesCount(current.right);
        if (aux == 0)
            return 1;
        return aux;
    }

    public T getMax() {
        return getMax(this.root);
    }

    private T getMax(Node<T> current) {
        if (current == null) {
            return null;
        }
        if (current.right != null) {
            return getMax(current.right);
        }
        return current.key;
    }

    public void printDescendants(Node<T> node) {
        printDescendants(root, node, false);

    }

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

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof AVLTree)) {
            return false;
        }
        AVLTree<T> bst = (AVLTree<T>) o;
        return equals(root, bst.root);
    }

    private boolean equals(Node<T> current, Node<T> other) {
        if (current == null && other == null)
            return true;
        boolean right, left;
        if (current.key.equals(other.key)) {
            return equals(current.right, other.right) && equals(current.left, other.left);
        }
        return false;

    }

    public int hashCode() {
        return hashCode(root);
    }

    private int hashCode(Node<T> current) {
        if (current == null)
            return 1;
        return 31 * current.key.hashCode() + hashCode(current.right) + hashCode(current.left);
    }


    public int size() {
        return size(root);
    }

    private int size(Node<T> current) {
        if (current == null) return 0;
        return 1 + size(current.right) + size(current.left);
    }

}
