

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BlockChain<T> {
	
	private int zeros;
	private List<Block<T>> blockChain = new ArrayList<Block<T>>();
	
	public BlockChain(int zeros) {
		this.zeros = zeros;
		createGenesisBlock();
	}
	
	public int getZeros() {
		return zeros;
	}
	
	private class Block<T> {
		
		private int index;
		private long nonce;
		private String instruction;
		private String hash;
		private String prevHash;
		private AVL<T> tree;
		
		public Block(int index, String instruction, AVL<T> tree, String prevHash) {
			this.index = index;
			this.tree = tree;
			this.prevHash = prevHash;
			this.hash =  calculateHash();
		}
		
		public AVL<T> getAVL() {
			return tree;
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
		
		public String calculateHash() {
			
			int nonce = 0;
			String comb = getIndex() + getInstruction() + getAVL() + getPrevHash();
			String hash = sha256(comb + nonce);
			
			while(!validHash(hash)) {
				nonce++;
				hash = sha256(comb + nonce);
			}
			
			this.nonce = nonce;
			return hash;
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

	public void add(String action, int number){

	}
	
	public void createGenesisBlock() {
		
		AVL<T> tree = new AVL<T>((Comparator<T>) new MyComparator());
		String dummyHash = "00000000";	
		blockChain.add(new Block<T>(0,"No instruction",tree,dummyHash));
	}
	
	public void addElement(T elem) {
		
		Block<T> latestBlock = getLatestBlock();
		AVL<T> tree = latestBlock.getAVL();     //Need to make a copy of the tree, instead of getting the direct reference.
		tree.add(elem);						
		String prevHash = latestBlock.getHash();
		
		Block<T> block = new Block<T>(getChain().size(),"add" + elem.toString(),tree,prevHash);
		getChain().add(block);
	}
	
	public List<Block<T>> getChain() {
		return blockChain;
	}
	
	public boolean validateChain() {
		
		List<Block<T>> bc = getChain();
		
		for(int i=1 ; i < bc.size() ; i++) {
			
			Block<T> current = bc.get(i);
			Block<T> prev = bc.get(i-1);
			
			if(current.getHash() !=  current.calculateHash()) 
				return false;
			if(current.getPrevHash() != prev.getHash())
				return false;
		}
		return true;
	}
	
	public void remove(T elem) {
		
		Block<T> latestBlock = getLatestBlock();
		AVL<T> tree = latestBlock.getAVL();
		tree.delete(elem);
		String prevHash = latestBlock.getHash();
		Block<T> block = new Block<T>(getChain().size(),"remove" + elem.toString(),tree,prevHash);
		getChain().add(block);
	}
	
	public Block<T> getLatestBlock() {
		return getChain().get(getChain().size() - 1);
	}

}
