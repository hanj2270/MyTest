package certGenerateUtils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class P10RequestTest {

	private static final String subject="C=CN, L=上海 , O=测试企业 ,CN=testUser";
	private static final String password="123456";
	private static final String pemPath="./src/test/resources/test.pem";

	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
	}

	
	
	@Test
	public void testreadpem() throws Exception{
		System.out.println(P10Request.createP10(subject, password, pemPath));
		System.out.println(P10Request.getPrivateKey(pemPath, password).toString());
		PrivateKey key=P10Request.getPrivateKey(pemPath, password);
	
		
		String keyString=Base64.encodeBase64String(key.getEncoded());
		
		byte[] keyBytes;
        keyBytes = Base64.decodeBase64(keyString);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey DecodedprivateKey = keyFactory.generatePrivate(keySpec);
        P10Request.savePEM(DecodedprivateKey, password, pemPath);
        System.out.println(P10Request.getPrivateKey(pemPath, password).toString());
	}



	

}
