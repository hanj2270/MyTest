package encrption;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import encrption.DigestUtils;

public class DigestUtilsTest {


	@Test
	public void testDigest() throws Exception {
		String key = "bbbbbbbbbb";
		//SHA1交叉验证
		assertEquals(new String(DigestUtils.jdkDigest("text1","SHA",null)),
				new String(DigestUtils.bcDigest("text1","SHA",null)));
		//MD5交叉验证
		assertEquals(new String(DigestUtils.jdkDigest("text1","MD5",null)),
				new String(DigestUtils.bcDigest("text1","MD5",null)));
		//Hmac交叉验证
		assertEquals(new String(DigestUtils.jdkDigest("text1","HmacMD5",key)),
				new String(DigestUtils.bcDigest("text1","HmacMD5",key)));
	}
	
	
	
	@Test
	public void testPrintResult() throws Exception {
		String key = "bbbbbbbbbb";
		DigestUtils.printResult("text1","SHA",null,"jdk");
		DigestUtils.printResult("text1","SHA",null,"bc");
		DigestUtils.printResult("text1","MD5",null,"jdk");
		DigestUtils.printResult("text1","MD5",null,"bc");
		DigestUtils.printResult("text1","HmacMD5",key,"jdk");
		DigestUtils.printResult("text1","HmacMD5",key,"bc");
	}

}
