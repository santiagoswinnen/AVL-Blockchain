import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BlockChain {
	
	private int zeros;
	private AVLTree<Integer> tree = new AVLTree<Integer>(new Comparator); //no hay que usar un comparator ya que usamos solo int, hay que cambiarlo en el AVL
	private List<Block> blockChain = new ArrayList<Block>();
	
	public BlockChain(int zeros) {
		this.zeros = zeros;
		createGenesisBlock();
	}
	
	public int getZeros() {
		return zeros;
	}
	
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
		
		public String calculateHashNoNonce() {  //calculates hash of Block with current nonce and data.
		
			String combination = getIndex() + getInstruction() + getPrevHash() + getNonce();
			return sha256(combination);
		}		
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
	}

	public void add(String action, int number){  //No entiendo que seria el String action, osea si estas llamando a add ya sabes que es un "add" + number

	}
	
	private void createGenesisBlock() {
		
		blockChain.add(new Block(0,"No instruction","00000000"));
	}
	
	public void addElement(Integer num) {   
		
		Block lastBlock = getLatestBlock();
		int index = lastBlock.getIndex() + 1;
		boolean inserted = tree.add(num,index);	
		String instruction = "add" + num.toString() + (inserted? "true" : "false");
		Block block = new Block(index,instruction,lastBlock.getHash());
		blockChain.add(block);
		
	}
	
	public List<Block> getChain() {
		return blockChain;
	}
	
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
	
	public void remove(Integer num) {
			
		Block lastBlock = getLatestBlock();
		int index = lastBlock.getIndex() + 1;
		boolean removed = tree.remove(num,index);	
		String instruction = "remove" + num.toString() + (removed? "true" : "false");
		Block block = new Block(index,instruction,lastBlock.getHash());
		blockChain.add(block);

	}
	
	public void lookup(Integer num) {
		
		Block lastBlock = getLatestBlock();
		int index = lastBlock.getIndex() + 1;
		DataPair<Block,boolean> info = tree.contains(num);	 //Deberia retornar un data pair con true/false si encontro o no y el bloque para poder tener los indices
		boolean found = info.getElement2();
		String instruction = "lookup" + num.toString() + (found? "true" : "false");
		Block block = new Block(index,instruction,lastBlock.getHash());
		blockChain.add(block);
		
		if(found) {
			
			System.out.println("Indexes that modified Node with data (" + num.toString() + ") : " + info.getElement1().getSet().toString());			
		}
		else {
			System.out.println("Element (" + num.toString() + ") was not found in AVL Tree" );
		}
	}
	public Block getLatestBlock() {
		return getChain().get(getChain().size() - 1);
	}
}