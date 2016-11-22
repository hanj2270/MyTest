package certGenerateUtils;

import ioUtils.CertReadUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
	
public class P10ResponseTest {
	
	private static final String subject="C=CN, L=上海 , O=测试企业 ,CN=testUser";
	private static final String password="123456";
	private static final String pemPath="./src/test/resources/test.pem";
	private static final String CA_path="./src/test/resources/hanjian1_pfx.pfx";
	private static final String userCertSaved_path="./src/test/resources/testUser.cer";
	private static final String KEYSTROE_path="./src/test/resources/testKs.jks";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void createRootCert() throws Exception {
		String s=P10Request.createP10(subject, password, pemPath);
		P10Response.createCert(s,CA_path,password,userCertSaved_path);
		PublicKey puKey=CertReadUtils.getPuKFromCrt(userCertSaved_path, password);
		System.out.println("crt 公钥为");
		System.out.println(puKey);		
	}

	@Test
	public void testSaveToKeyStore() {
		String s=P10Request.createP10(subject, password, pemPath);
		X509Certificate cert=P10Response.createCert(s,CA_path,password,userCertSaved_path);
		P10Response.saveToKeyStore("testCert", cert, KEYSTROE_path, password);
	}
	
	
	@Test
	public void testdeliver() throws Exception {
		String s=P10Request.createP10(subject, password, pemPath);
		X509Certificate cert=P10Response.createCert(s,CA_path,password,userCertSaved_path);
		String encert=Base64.encodeBase64String(cert.getEncoded());
		 CertificateFactory cf = CertificateFactory.getInstance("X.509");
		 InputStream   in_nocode   =   new   ByteArrayInputStream(Base64.decodeBase64(encert));
		 X509Certificate decodecert=(X509Certificate) cf.generateCertificate(in_nocode);
		 System.out.println(decodecert);
		 
	}

}
