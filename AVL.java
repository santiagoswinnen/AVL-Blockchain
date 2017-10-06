import java.util.Comparator;


public class AVL<T> {
	
	private Node<T> root;
	private Comparator<T> cmp;
	
	public AVL(Comparator<T> cmp) {
		this.cmp = cmp;
	}
	
	private static class Node<T> {
		private Node<T> left;
		private Node<T> right;
		private T value;
		private int height = 1;
		
		public Node(T value) {
			this.value = value;
		}
	}
	
	public void add(T elem) {
		root = addR(this.root,elem);
	}
	

	public int height(Node<T> node) {
		if(node == null) return 0;
		
		return node.height;
	}
	private Node<T> leftRotate(Node<T> x) {
		
        Node<T> y = x.right;
        Node<T> T2 = y.left;
 
        // Perform rotation
        y.left = x;
        x.right = T2;
 
        //  Update heights
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;
 
        // Return new root
        return y;
    }
	
	private Node<T> rightRotate(Node<T> y) {
		
        Node<T> x = y.left;
        Node<T> T2 = x.right;
 
        // Perform rotation
        x.right = y;
        y.left = T2;
 
        // Update heights
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;
 
        // Return new root
        return x;
    }
	int getBalance(Node<T> node) {
        if (node == null)
            return 0;
 
        return height(node.left) - height(node.right);
    }	
	
	private int max(int a, int b) {
		return a>=b ? a : b;
	}
	private Node<T> addR(Node<T> node, T elem) {
		//  Perform the normal BST insertion 
		if (node == null)
			return (new Node<T>(elem));
			
		int comp = cmp.compare(elem,node.value);
		if (comp < 0 )
			node.left = addR(node.left, elem);
		else if ( comp > 0)
			node.right = addR(node.right, elem);
		else 				// Duplicate values not allowed
			return node;

				//Update height of this ancestor node, no way to know if insertion was performed in the left or right subtree
		node.height = 1 + max(height(node.left),height(node.right));

				//Get the balance factor of this ancestor node to check whether this node became unbalanced 
		int balance = getBalance(node);

				// If this node becomes unbalanced, then there
				// are 4 cases LL RR RL LR
		
				//Left Left Case
		if (balance > 1 && cmp.compare(elem, node.left.value) < 0)
			return rightRotate(node);

				// Right Right Case
		if (balance < -1 && cmp.compare(elem,node.right.value) > 0)
			return leftRotate(node);

				// Left Right Case
		if (balance > 1 && cmp.compare(elem,node.left.value) > 0) {
			node.left = leftRotate(node.left);
			return rightRotate(node);
		}

				// Right Left Case
		if (balance < -1 && cmp.compare(elem, node.right.value) < 0) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}
		
		return node;
    
	}
	
	public void delete(T elem) {
		root = deleteR(root,elem);
	}
	private Node<T> deleteR(Node<T> node, T elem) {
		if(node == null)
			return null;
		int comp = cmp.compare(elem,node.value);
		if(comp > 0)
			node.right = deleteR(node.right,elem);
		else if(comp < 0)
			node.left = deleteR(node.left,elem);
		else { //found the element to delete
			
			if(isLeaf(node)) {
				node = null;        //returns null
			}
			else if(node.left == null)
				node = node.right;
			else if(node.right == null)
				node = node.left;
			else {
				
				Node<T> aux = getMinNode(node.right);
				node.value = aux.value;
				node.right = deleteR(node.right,aux.value);	
			}
		}
		if(node == null)
			return null;
		
		node.height = max(height(node.left),height(node.right)) + 1;
		
		int balance = getBalance(node);
			
		if (balance > 1 && getBalance(node.left) >= 0) // Left Left Case
			return rightRotate(node);
	        
	    if (balance > 1 && getBalance(node.left) < 0) { // Left Right Case
	    	node.left = leftRotate(node.left);
	        return rightRotate(node);
	    }
	        
	    if (balance < -1 && getBalance(node.right) <= 0) // Right Right Case
	         return leftRotate(node);
	 
	    if (balance < -1 && getBalance(node.right) > 0) { // Right Left Case
	    	node.right = rightRotate(node.right);
	        return leftRotate(node);
	    }
	    return node;
	}
	
	public boolean isLeaf(Node<T> node) {
		if(node == null) 
			throw new IllegalArgumentException();
		return node.left == null && node.right == null;
	}
	
	private Node<T> getMinNode(Node<T> node) {
		if(node == null)
			return null;
		if(node.left == null)
			return node;
		else
			return getMinNode(node.left);
	}
}
