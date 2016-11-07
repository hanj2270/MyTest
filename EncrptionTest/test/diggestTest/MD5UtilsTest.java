package diggestTest;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class MD5UtilsTest {
	@Test
	public void test() throws Exception {
		String text = "this is a test";
	    
	    byte[] jdkResult=MD5Utils.jdkMD5(text);
	    byte[] bcResult=MD5Utils.bcmMD5(text);
	    assertArrayEquals(jdkResult, bcResult);
	    
	}
}
