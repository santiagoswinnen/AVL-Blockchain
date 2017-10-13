/**
 * Exception signifying that an operation has been entered by the user that is
 * not compatible with those built into the BlockChain
 */
public class IllegalOperationException extends RuntimeException {
    public IllegalOperationException(String msg){
        super(msg);
    }
}
