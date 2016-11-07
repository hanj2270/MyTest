package diggestTest;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class SHA1UtilsTest {
	
	
	@Test
	public void test() throws Exception {
		String text = "this is a test";
	    
	    byte[] jdkResult=SHA1Utils.jdkSHA1(text);
	    byte[] bcResult=SHA1Utils.bcSHA1(text);
	    assertArrayEquals(jdkResult, bcResult);
	    
	}

}
