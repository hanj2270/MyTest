package certUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

import android.util.Base64;

import android.util.Log;

public class DecodeCert {
	
	public static X509Certificate decodeCert(String base64Cert){
		 try {
			 CertificateFactory cf=CertificateFactory.getInstance("X.509");
			 InputStream in_nocode=new ByteArrayInputStream(Base64.decode(base64Cert,Base64.DEFAULT));
			 X509Certificate cert=(X509Certificate) cf.generateCertificate(in_nocode);
			return cert;
		} catch (Exception e) {
			Log.d("certsave", "证书解析失败");
			e.printStackTrace();
			return null;
		}
	}
	
	public static PrivateKey decodeKey(String base64PriKey){
		try {
			byte[] keyBytes = Base64.decode(base64PriKey,Base64.DEFAULT);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey DecodedprivateKey = keyFactory.generatePrivate(keySpec);
			return DecodedprivateKey;
		} catch (Exception e) {
			Log.d("certsave", "私钥解析失败");
			e.printStackTrace();
			return  null;
		} 
	}

}
