package ioUtils;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Enumeration;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ReadUtilsTest {

	public static String pfx_path="./src/test/resources/hanjian1_pfx.pfx";
	public static String crt_path="./src/test/resources/hanjian2_crt.cer";
	public static String password="123456";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testgetKeyStore() throws Exception {
		KeyStore ks=ioUtils.ReadUtils.getKsformPfx(pfx_path, password);
		Enumeration<String> e=ks.aliases( );  
	        while(e.hasMoreElements()) {  
	            java.security.cert.Certificate c=ks.getCertificate((String)e.nextElement());
	            System.out.println(c.toString());
	        } 
	}
	
	@Test
	public void testgetkeyfromPFX() throws Exception {
		KeyStore ks=ioUtils.ReadUtils.getKsformPfx(pfx_path, password);
		PublicKey puKey=ReadUtils.getPukfrommKs(ks,password);
		System.out.println("pfx 公钥为");
		System.out.println(puKey.toString());
		PrivateKey prKey=ReadUtils.getPriKeyFromKS(ks, password);
		System.out.println("私钥为");
		System.out.println(prKey.toString());
	}

	@Test
	public void testgetkeyfromcrt() throws Exception {
		PublicKey puKey=ReadUtils.getPuKFromCrt(crt_path, password);
		System.out.println("crt 公钥为");
		System.out.println(puKey.toString());
	}
}
