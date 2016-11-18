package main;

import java.io.UnsupportedEncodingException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pkcs7Sign.PKCS7SignUtils;

public class PKCS7SignUtilsTest {
	public static String pfx_path="./src/test/resources/hanjian1_pfx.pfx";
	public static String text="this is text";
	public static String password="123456";
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testSign() throws UnsupportedEncodingException {
		byte[] signedData=PKCS7SignUtils.sign(text, pfx_path, password);
		PKCS7SignUtils.verify(signedData);
		
	}

}
