package org.gradle;

import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CertVerifyUtilsTest {
	private final static String CertPath = "./src/test/resources/KmailServer.cer" ;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testCertVerify() throws Exception {
		FileInputStream inputStream=new FileInputStream(CertPath);
		CertificateFactory cf=CertificateFactory.getInstance("X.509");
		X509Certificate certificate=(X509Certificate) cf.generateCertificate(inputStream );
		CertVerifyUtils.certVerify(certificate);
	}

}
