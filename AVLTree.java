import com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator;

import java.util.*;

/**
 * Created by sswinnen on 21/09/17.
 */
public class AVLTree<T> {
    private Node<T> root;
    private Comparator<T> cmp;

    public AVLTree(Comparator<T> cmp){
        this.cmp = cmp;
    }
    private static class Node<T> {
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
        public String toString(){
            return "-" + key.toString()  + "(" + height + ")" +"-";
        }
        public int getHeight(){
            return height;
        }
        public int getRightChildHeight(){
            if(right == null){
                return -1;
            }
            return right.height;
        }

        public int getLeftChildHeight(){
            if(left == null){
                return -1;
            }
            return left.height;
        }
    }



    public boolean add(T key) {
        Node<T> aux = add(key, root);
        if(aux == null) return false;
            return true;
    }

    private Node<T> add(T key, Node<T> current) {
        Node<T> aux;
        if(current == null){
            return new Node<T>(key);
        }
        if(cmp.compare(key,current.key) < 0 ){
            aux = add(key, current.left);
            if(aux == null) return null;
            current.left = aux;
            current.height = Math.max(current.height, current.getLeftChildHeight() + 1);
        }
        else if(cmp.compare(key,current.key) > 0 ){
            aux = add(key, current.right);
            if(aux == null) return null;
            current.right = aux;
            current.height = Math.max(current.height, current.getRightChildHeight() + 1);
        }
        else {
            return null;
        }
        current = balance(current);
        return current;
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
                current.left = leftRotation(current.left);
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
                current.right = rightRotation(current.right);
                current = leftRotation(current);
            }
        }
        return current;
    }


    private Node<T> leftRotation(Node<T> current){
        Node<T> auxright = current.right;
        current.right = auxright.left;
        auxright.left = current;
        current.height = Math.max(current.getLeftChildHeight(), current.getRightChildHeight());
        auxright.height = Math.max(auxright.height, current.height + 1);
        return auxright;
    }

    private Node<T> rightRotation(Node<T> current){
        Node<T> auxleft = current.left;
        current.left = auxleft.right;
        auxleft.right = current;
        current.height = Math.max(current.getLeftChildHeight() + 1, current.getRightChildHeight() + 1);
        auxleft.height = Math.max(auxleft.height, current.height + 1);
        return auxleft;
    }

    public int getBalance(Node<T> current){
        int leftH, rightH;
        return current.getLeftChildHeight() - current.getRightChildHeight();
    }
    public boolean remove(T key){
        DataPair<Boolean,Node<T>> aux = remove(key, root);
        root = aux.getElement2();
        return aux.getElement1();

    }
    private DataPair<Boolean, Node<T>> remove(T key, Node<T> current){
        if(current == null){
            return new DataPair<>(false,null);
        }
        DataPair<Boolean,Node<T>> aux;
        if(cmp.compare(key, current.key) < 0){
            aux = remove(key, current.left);
            if(aux.getElement1()) current.left = aux.getElement2();
            current.left = aux.getElement2();
        }
        else if(cmp.compare(key, current.key) > 0){
            aux = remove(key, current.right);
            if(aux.getElement1()) current.right = aux.getElement2();
            return new DataPair<>(aux.getElement1(), current);
        }
        else {
            current = deleteKey(current);
            current = balance(current);
            return new DataPair<>(true, current);
        }
        current.height = Math.max(current.getLeftChildHeight() + 1, current.getRightChildHeight() + 1);
        current = balance(current);
        return new DataPair<>(aux.getElement1(), current);
    }


    public Node<T> deleteKey( Node<T> node) {
        if (node.right == null && node.left == null) {
            return null;
        } else if (node.right == null) {
            return node.left;
        } else if (node.left == null) {
            return node.right;
        } else {
            /*busco el sucesor inorder*/
            DataPair<Node<T>, Node<T>> aux = eliminateMostLeft(node.right);
            Node<T> ret = aux.getElement2();
            /*puede pasar que el nodo sucesor inorder sea el hijo derecho
            del que quiero eliminar*/
            if(ret == node.right) ret.right = null;
            else ret.right = node.right;
            ret.left = node.left;

            ret.height = Math.max(ret.left. height, ret.getRightChildHeight() + 1);
            return ret;
        }
    }

    private DataPair<Node<T>,Node<T>> eliminateMostLeft(Node<T> current){
        if(current == null){
            throw new NoSuccesorInorderException("There was no succesor inorder.");
        }
        DataPair<Node<T>, Node<T>> aux;
        if(current.left != null){
            aux = eliminateMostLeft(current.left);
            current.left = aux.getElement1();
        }
        else if(current.right != null){
            aux = eliminateMostLeft(current.right);
            current.right = aux.getElement1();
        } else {
            return new DataPair<>(null, current);
        }
        current.height = Math.max(current.getLeftChildHeight() + 1, current.getRightChildHeight() + 1);
        return new DataPair<>(current, aux.getElement2());
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
        double number = 1;
        while (!queue.isEmpty()) {

            Node<T> aux = queue.remove();

            if(	Math.log(number)/Math.log(2.0) == i ){
                System.out.println(" LEVEL + 1 = " + Math.log(number)/Math.log(2.0) );
                i++;

            }
            System.out.print(" " + aux.toString() + " ");

            if (aux.left != null) {
                queue.offer(aux.left);
            }
            if (aux.right != null) {
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
