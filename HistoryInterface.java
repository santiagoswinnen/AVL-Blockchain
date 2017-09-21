/**
 * Created by sswinnen on 21/09/17.
 */
public interface HistoryInterface<T> {
    public void clean(int distance);
    public int getCount();
    public void add(int index, AVLTree<T> tree);
    public boolean remove(int index);
}
