package ioUtils;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Enumeration;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CertReadUtilsTest {

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
	public void testgetKeyStore() throws Exception {
		KeyStore ks=ioUtils.CertReadUtils.getKsformPfx(pfx_path, password);
		Enumeration<String> e=ks.aliases( );  
	        while(e.hasMoreElements()) {  
	            java.security.cert.Certificate c=ks.getCertificate((String)e.nextElement());
	            System.out.println(c.toString());
	        } 
	}
	
	@Test
	public void testgetkeyfromPFX() throws Exception {
		KeyStore ks=ioUtils.CertReadUtils.getKsformPfx(pfx_path, password);
		PublicKey puKey=CertReadUtils.getPukfrommKs(ks,password);
		System.out.println("pfx 公钥为");
		System.out.println(puKey);
		PrivateKey prKey=CertReadUtils.getPriKeyFromKS(ks, password);
		System.out.println("私钥为");
		System.out.println(prKey);
	}

	@Test
	public void testgetkeyfromcrt() throws Exception {
		PublicKey puKey=CertReadUtils.getPuKFromCrt(crt_path, password);
		System.out.println("crt 公钥为");
		System.out.println(puKey);
	}
}
