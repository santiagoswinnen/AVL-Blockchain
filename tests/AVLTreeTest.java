import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertTrue;

class AVLTreeTest {
    private AVLTree<Integer> tree1 = new AVLTree<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    });
    private AVLTree<Integer> tree2 = new AVLTree<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    });
    @Test
    public void equalityTest(){
        tree1.add(2,1);
        tree1.add(1,2);
        tree1.add(3,3);
        tree1.add(5,4);
        tree2.add(1,1);
        tree1.add(1,2);
        tree1.add(3,3);
        tree1.add(5,4);
        assertTrue(tree1.equals(tree2));
    }

    @Test
    public void equalityTest2(){
        for(int i = 0; i < 30 ; i ++){
            tree1.add(i,i);
            tree2.add(i,i);
        }
        assertTrue(tree1.equals(tree2));
    }
    @Test
    public void addRepeatedElementTest(){
        tree1.add(2,1);
        tree2.add(2,1);
        tree2.add(2,1);
        assertTrue(tree1.equals(tree2));
    }

    @Test
    void test() {
    }

    @Test
    void balance() {
    }

    @Test
    void getBalance() {
    }

    @Test
    void remove() {
    }

    @Test
    void deleteKey() {
    }

    @Test
    void getInRange() {
    }

    @Test
    void equals() {
    }

}
