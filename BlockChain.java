import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * The BlockChain containing the history of an AVLTree.
 * The BlockChain contains a Terminal, a user-defined number of leading zeroes for the hashes, and an AVLTree
 * @see AVLTree
 */
public class BlockChain {
	private Terminal terminal;
	private int zeros;


	private AVLTree<Integer> tree = new AVLTree<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    });
	
	private List<Block> blockChain = new ArrayList<Block>();
	
	public BlockChain(int zeros, Terminal terminal) {
		this.terminal = terminal;
	    this.zeros = zeros;
		createGenesisBlock();

	}
	
	public int getZeros() {
		return zeros;
	}
	public AVLTree<Integer> getTree() {
		return tree;
	}
	
	
	/**
	 * Individual block of the BlockChain
	 * Contains an index describing its position inside of the chain,
	 * a number nonce, making the block's specific content evaluate to a hash value with a set number of zeroes
	 * a string instruction describing the instruction to be saved by the block
	 * a hash number specific to the block and its contents
	 * a previous hash as an unalterable link to the hash that came before it
	 */
	private class Block {
		
		private int index;
		private long nonce;
		private String instruction; //"add 5 true", "remove 3 true"
		private String hash;
		private String prevHash;
		
		
		public Block(int index, String instruction, String prevHash) {
			this.index = index;
			this.instruction = instruction;
			this.prevHash = prevHash;
			this.hash =  calculateHash();
		}
		
		public String getPrevHash() {
			return prevHash;
		}
		
		public long getNonce() {
			return nonce;
		}
		
		public String getHash() {
			return hash;
		}
		
		public void setHash(String hash) {
			this.hash = hash;
		}
		
		public int getIndex() {
			return index;
		}
		
		public String getInstruction() {
			return instruction;
		}
		/**
		 * Calculates the hash value specific to the block and its contents.
		 * <p>
		 * The block's index, instruction, the previous hash, and the nonce are combined and
		 * used to create the hash specific to this block
		 * @return the string of the block's hash
		 */
		public String calculateHash() {  //calculates a valid hash according to zeros 
			
			int nonce = 0;
			String comb = getIndex() + getInstruction() + getPrevHash();
			String hash = sha256(comb + nonce);
			
			while(!validHash(hash)) {
				nonce++;
				hash = sha256(comb + nonce);
			}
			
			this.nonce = nonce;
			return hash;
		}
		/**
		 * Creates the has belonging to the block without the nonce value
		 * @return the string of the hash
		 */
		public String calculateHashNoNonce() {  //calculates hash of Block with current nonce and data.
		
			String combination = getIndex() + getInstruction() + getPrevHash() + getNonce();
			return sha256(combination);
		}		
		
		
		/**
		 * Checks if the hash is valid.
		 * By checking if the hash, with its nonce, has evaluated to a string with a sufficient number of leading zeroes 
		 * @param hash the string has being tested
		 * @return true if the has has a sufficient number of leading zeroes
		 */
		private boolean validHash(String hash) {

			boolean valid = true;
			for(int i = 0 ; i < getZeros() ; i++) {
				if(hash.charAt(i) != '0')
					valid = false;
			}
			
			return valid;
		}
		
		private String sha256(String base) {

			try	{
				
		        MessageDigest digest = MessageDigest.getInstance("SHA-256");
		        byte[] hash = digest.digest(base.getBytes("UTF-8"));
		        StringBuffer hexString = new StringBuffer();

		        	for (int i = 0; i < hash.length; i++) {
		        		String hex = Integer.toHexString(0xff & hash[i]);
		        			if(hex.length() == 1) hexString.append('0');
		        				hexString.append(hex);
		        	}
		        return hexString.toString();
		        
		    } catch(Exception ex){
		       throw new RuntimeException(ex);
		    }
			
		}

		
		public void setinstruction(String data){
			this.instruction = data;
		}

		/**
		 * Formats the contents of the block conveniently for printing to the screen.
		 */
		public String toString() {
			String ret = "[ " + "index:" + index + "/nonce: " + nonce;
			ret += "/instruction: " + instruction + "]";
			ret += "prevHash: " + prevHash + " Hash: " + hash;
			return ret;
		}
	}

    /**
     * Receives instruction to perform on AVLTree, calls the correct method to execute it and stores result in new block.
     * <p>
     * Valid operations include adding, removing, and looking up a value
     * @param action Method to call
     * @param number a key to either add, remove or look up
     * @throws IllegalOperationException
     * @see DataPair
     * @see IllegalOperationException
     */
	public void operate(String action, int number){  
        int currentIndex = size();
        Boolean success;
        String instruction;
        switch(action){
            case "add": success = tree.add(number, currentIndex); break;
            case "remove": success = tree.remove(number,currentIndex); break;
            case "lookup": DataPair<Boolean,Set<Integer>> aux = tree.lookup(number);
                            success = aux.getElement1();
                            if(success) {
                                terminal.printMessage("Indexes that modified Node with data (" + number + "):");
                                terminal.printMessage(aux.getElement2().toString());
                            } else {
                                terminal.printMessage("Element (" + number + ") was not found in AVL Tree" );
                            }break;
            default: throw new IllegalOperationException("not a valid operation to perform");
        }
        instruction = action + number + success.toString();
        Block block = new Block(currentIndex, instruction, getLatestBlock().getHash());
        add(block);
	}

	public void add(Block block){
		blockChain.add(block);
	}

	
	public void modify(int number, String data){
		if(number < 0 || number >= size()){
			throw new IndexOutOfBoundsException("Index is out of bounds. BlockChain does not contain that block.");
		}
		blockChain.get(number).setinstruction(data);
		return;
	}



	/**
	 * Creates first block, empty, and with a 000 hash
	 */
	private void createGenesisBlock() {
		
		blockChain.add(new Block(0,"No instruction","00000000"));
	}


	public List<Block> getChain() {

	    return blockChain;
	}
	
	
	/**
	 * Checks the validity of the BlockChain
	 * @return true if each block is valid
	 */
	public boolean validateChain() {
		
		List<Block> bc = getChain();
		
		for(int i=1 ; i < bc.size() ; i++) {
			
			Block current = bc.get(i);
			Block prev = bc.get(i-1);
			
			if(current.getHash() !=  current.calculateHashNoNonce()) 
				return false;
			if(current.getPrevHash() != prev.getHash())
				return false;
		}
		return true;
	}


	public int size(){
		return blockChain.size();
	}
	public Block getLatestBlock() {
		return blockChain.get(size() - 1);
	}

	/**
	 * Creates a string containing all of the information of each block in the chain
	 * @return the string representing the entire chain
	 */
	@Override
	public String toString(){
		String ret = new String();
		int i = 0;
		for(Block block: blockChain){
			ret += block.toString() + "-->";
			i++;
			if(i%4 == 0) ret += "\n";
		}
		return ret;

	}

	protected void showInsider(){
		TreePrinter.print(tree.getRoot());
	}
}
