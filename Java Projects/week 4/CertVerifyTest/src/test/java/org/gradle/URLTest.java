package org.gradle;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class URLTest {
	  private final static String loadingCertPath = "./src/test/resources/server.jks" ;
	  private final static String caCertPath = "./src/test/resources/KoalCa.cer" ;
//	  private final static String CAROOTPATH = "./src/test/resources/KoalCARoot.cer";
	  private final static String password = "123456";
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void CAGettest() throws Exception {	
		CertificateFactory cf=CertificateFactory.getInstance("x509");
		FileInputStream in=new FileInputStream(caCertPath);
		X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
   	    System.out.println(URlUtils.getIssuerURI(cert));
   	    System.out.println(URlUtils.getCrlURI(cert));
   	    in.close();
	}
	
	@Test
	public void CARootGettest() throws Exception {	
	   KeyStore ServerStore = KeyStore.getInstance("jks");
	   FileInputStream in = new FileInputStream(loadingCertPath);
	   ServerStore.load(in, password.toCharArray());
	   X509Certificate cert = (X509Certificate) ServerStore.getCertificate("server");
   	   System.out.println(URlUtils.getIssuerURI(cert));
   	   System.out.println(URlUtils.getCrlURI(cert));
   	   in.close();
	}

	
	




}
