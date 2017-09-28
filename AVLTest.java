import java.util.Comparator;

public class AVLTest {
    public static void main(String[]args){
        AVLTree<Integer> tree = new AVLTree<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        tree.add(3);
        tree.add(4);
        tree.add(2);
        tree.add(1);
        tree.add(1);
        tree.remove(1);
        tree.printNodesByLevel();
    }
}
