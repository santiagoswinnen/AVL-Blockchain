import java.util.HashMap;
import java.util.List;

/**
 * Created by sswinnen on 21/09/17.
 */
public class BlockChain<T> implements BlockChainInterface<T>{
    private HashMap<AVLTree<T>,List<Integer>> modification;
    private Block<T> lastAdded;
    private int zeros;

    private static final class Block<T> implements BlockInterface<T> {
        private int blockNumber;
        private int nonce;
        private String data;
        private String previous;
        private String hash;

        public Block(String data){
            this.data=data;
            this.blockNumber=-1;
            this.nonce=-1;
            this.previous=null;
            this.hash=null;

        }

        public void buildHash(){

        }

        public int getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(int blockNumber) {
            this.blockNumber = blockNumber;
        }

        public int getNonce() {
            return nonce;
        }

        public void setNonce(int nonce) {
            this.nonce = nonce;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getPrevious() {
            return previous;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }
    }

    public boolean addBlock(BlockInterface block){
        block.setBlockNumber(lastAdded.getBlockNumber()+1);
        block.setPrevious(lastAdded.getHash());
        block.buildHash();
        return true;
    }

    public boolean validate(){
        return true;
    }

    public BlockInterface remove(int index){
        return null;
    }

    public boolean isInAVL(T elem){
        return false;
    }
}
