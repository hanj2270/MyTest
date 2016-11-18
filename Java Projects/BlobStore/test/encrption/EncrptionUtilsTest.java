package encrption;

import org.junit.Test;

public class EncrptionUtilsTest {

	@Test
	public void testEncrpt() {
		String encrptedString=EncrptionUtils.encrpt("key", "this is a test");
		System.out.println("密文"+encrptedString);
	}

}
