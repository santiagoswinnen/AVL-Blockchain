

/**
 * Created by sswinnen on 21/09/17.
 */
public interface BlockChainInterface<T> {
    boolean addBlock(BlockInterface block);
    BlockInterface remove(int index);


}
