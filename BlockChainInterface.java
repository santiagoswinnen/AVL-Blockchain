

/**
 * Created by sswinnen on 21/09/17.
 */
public interface BlockChainInterface<T> {
    /*
     * Se le debe pasar por parámetro un bloque que solo
     * contenga la data, el metodo se encarga de completar
     * los campos un vincularlo a la lista.
     */
    boolean addBlock(BlockInterface block);

    BlockInterface remove(int index);

    boolean isInAVL(T elem);


}
