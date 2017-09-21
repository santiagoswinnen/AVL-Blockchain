import java.util.HashMap;
import java.util.List;

/**
 * Created by sswinnen on 21/09/17.
 */
public class BlockChain<T> {
    private HashMap<AVLTree<T>,List<Integer>> modification;
    private Block<T> lastAdded;
    private Hasher hashGenerator;

    private static final class Block<T> implements BlockInterface<T> {

    }
}
