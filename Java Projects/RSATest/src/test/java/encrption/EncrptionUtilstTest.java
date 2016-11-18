package encrption;

import static org.junit.Assert.*;
import ioUtils.CertReadUtils;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class EncrptionUtilstTest {
	public static String pfx_path="./src/test/resources/hanjian1_pfx.pfx";
	public static String crt_path="./src/test/resources/hanjian1_crt.cer";
	public static String password="123456";
	public static String data="this is a test";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void signtest() throws Exception {
		KeyStore ks=ioUtils.CertReadUtils.getKsformPfx(pfx_path, password);
		PrivateKey prKey=CertReadUtils.getPriKeyFromKS(ks, password);
		System.out.println(EncrptionUtils.sign(prKey, data));
		
	}
	
	@Test
	public void veifytest() throws Exception {
		KeyStore ks=ioUtils.CertReadUtils.getKsformPfx(pfx_path, password);
		PublicKey puKey=CertReadUtils.getPuKFromCrt(crt_path, password);
		PrivateKey prKey=CertReadUtils.getPriKeyFromKS(ks, password);
		String signString= EncrptionUtils.sign(prKey, data);
		assertTrue(EncrptionUtils.verify(puKey, data, signString));
	}

}
