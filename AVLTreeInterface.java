import java.util.List;

/**
 * Created by sswinnen on 21/09/17.
 */
public interface AVLTreeInterface<T> {
        /*sin usar balanceo*/

    public void add(T key);

    public void removeAVL(T key);



    public List<T> getInRange(T inf, T sup);

    public void remove(T key);

    public void printNodesByLevel(AVLTree<T> tree);

    public boolean contains(T key);

    public int getLevel(T key);
    public int getLeavesCount();

    public T getMax();

    public void printDescendants(T element);


    public boolean equals(Object o);

    public int hashCode();

    public int size();
}
