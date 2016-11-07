package diggestTest;

import static org.junit.Assert.*;

import org.junit.Test;

public class MacUtilsTest {

	@Test
	public void test() throws Exception {
		String text = "this is a test";
	    String key = "bbbbbbbbbb";
	    
	    byte[] jdkResult=MacUtils.jdkHmacMD5(key, text);
	    byte[] bcResult=MacUtils.bcHmacMD5(key, text);
	    assertArrayEquals(jdkResult, bcResult);
	}

}
