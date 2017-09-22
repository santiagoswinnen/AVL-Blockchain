/**
 * Created by sswinnen on 21/09/17.
 */
public interface BlockInterface<T> {

    /*
     * Se invoca luego de setear el blockNumber, la data y el bloque
     * previo. El algoritmo calcula una combinación de nonce y hashcode
     * a partir de estos datos de forma que el hash sea valido.
     */
    void buildHash();


    /*
     * Getters y setters
     */
    int getBlockNumber();

    void setBlockNumber(int blockNumber);

    int getNonce();

    void setNonce(int nonce);

    String getData();

    void setData(String data);

    String getPrevious();

    void setPrevious(String previous);

    String getHash();

    void setHash(String hash);

}
