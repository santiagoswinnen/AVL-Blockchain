import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestBlockChain {

private BlockChain blockChain = new BlockChain(5,new Terminal());

	public void testValidateChain() {

		blockChain.operate("add",5);
		blockChain.operate("add",6);

		assertTrue(blockChain.validateChain());
		blockChain.modify(1,"Invalid Instruction");
		assertFalse(blockChain.validateChain());
		blockChain.operate("remove",6);
		assertTrue(blockChain.validateChain());


		blockChain.getTree().clearTree();
	}
	public void testSize() {
		blockChain.operate("add", 8);
		assertEquals(2,this.blockChain.size());
		blockChain.getTree().clearTree();


	}
}
