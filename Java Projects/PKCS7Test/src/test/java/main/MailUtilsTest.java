package main;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pkcs7Sign.MailUtils;

public class MailUtilsTest {
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
	public void testCreate() throws Exception {
		byte[] b=MailUtils.Create(text, pfx_path, password);
		System.out.println("解密后原文为:" +MailUtils.read(b, pfx_path, password));
	}

	
}
