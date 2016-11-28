package ioUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CertPrintUtilsTest {
	
	public static String pfx_path="./src/test/resources/hanjian1_pfx.pfx";
	public static String crt_path="./src/test/resources/hanjian1_crt.cer";
	public static String password="123456";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testPrintPFX() {
		CertPrintUtils.printPFX(pfx_path, password);
	}

	@Test
	public void testPrintCRT() {
		CertPrintUtils.printCRT(crt_path);
	}

}
