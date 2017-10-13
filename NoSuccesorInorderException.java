/**
 * Exception signifying that a node has no inorder successor
 */
public class NoSuccesorInorderException extends RuntimeException {
    public NoSuccesorInorderException(String message){
        super(message);
    }
}
