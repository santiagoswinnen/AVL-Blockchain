package chainpackage;

import java.security.MessageDigest;

public class TestSha256 {
	
	public String sha256(String base) {

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
	
	public static void main(String[] args) {
		TestSha256 func = new TestSha256();
		long nonce1 = 0;
		long nonce2 = 0;
		long nonce3 = 0;
		
		String f1 = func.sha256("asdasd01234asdascarry100" + "wep wep wep" + nonce1);
		while(f1.charAt(0) != '0') {
			nonce1++;
			f1 = func.sha256("asdasd01234asdascarry100" + "wep wep wep" + nonce1);
		}
		
		String f2 = func.sha256("0123"+"11dd"+"give away 100 " + nonce2);
		while(f2.charAt(0) != '0') {
			nonce2++;
			f2 = func.sha256("0123"+"11dd"+"give away 100 " + nonce2);
		}
		
		
		String f3 = func.sha256("0"+"996123"+"take away 2.5 " + nonce3);
		while((f3.charAt(0) != '0') || (f3.charAt(1) != '0')) {
			nonce3++;
			f3 = func.sha256("0"+"996123"+"take away 2.5 " + nonce3);
		}
		System.out.println("Hash 1 length is: " + f1.length() + " value is " + f1 + " and nonce is " + nonce1);
		System.out.println("Hash 2 length is: " + f2.length() + " value is " + f2 + " and nonce is " + nonce2);
		System.out.println("Hash 3 length is: " + f3.length() + " value is " + f3 + " and nonce is " + nonce3);
		
	}
	

}
