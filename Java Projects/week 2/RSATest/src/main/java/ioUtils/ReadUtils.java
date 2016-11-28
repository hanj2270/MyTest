package ioUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class ReadUtils {
	
	
	
	/** 
	* @Description: 从KeyStore中获取证书名称,默认获取第一张证书的名称
	* @param keyStore
	* @return
	* @throws KeyStoreException    设定文件 
	*/
	private static String getKeyAlias(KeyStore keyStore) throws KeyStoreException{
		String keyAlias=null;
		Enumeration<String> enumas = keyStore.aliases();
		if (enumas.hasMoreElements()) {
			keyAlias = (String) enumas.nextElement();
		}
		return keyAlias;
		
	}
	
	
	/** 
	* @Description: 从keystore中获得公钥 
	* @param keystore
	* @param strPassword
	* @return
	* @throws Exception    设定文件 
	*/
	public static PublicKey getPukfrommKs(KeyStore keyStore,String password)
			throws Exception {
		String keyAlias=getKeyAlias(keyStore);
		Certificate cert = keyStore.getCertificate(keyAlias);
		PublicKey pubkey = cert.getPublicKey();
		return pubkey;
	}
	/** 
	* @Description: 从keystore中获得私钥 
	* @param keystore
	* @param strPassword
	* @return
	* @throws Exception    设定文件 
	*/
	public static PrivateKey getPriKeyFromKS(KeyStore keyStore,String password)
			throws Exception {
		String keyAlias=getKeyAlias(keyStore);
		PrivateKey prikey = (PrivateKey) keyStore.getKey(keyAlias, password.toCharArray());
		return prikey;
	}
	
	/** 
	* @Description: 从pfx文件生成keystore对象
	* @param path pfx文件路径
	* @param password 密码
	* @return
	* @throws Exception    设定文件 
	*/
	public static KeyStore getKsformPfx(String path, String password)
			throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
		FileInputStream in = new FileInputStream(path);
		if (in!=null) {
			ks.load(in, password.toCharArray());
			in.close();
		}
		return ks;
	}
	
	/** 
	* @Description: 获取crt证书中的公钥
	* @param path
	* @param password
	* @return
	 * @throws Exception 
	* @throws Exception    设定文件 
	*/
	public static PublicKey getPuKFromCrt(String path, String password) throws Exception{
		InputStream in = new FileInputStream(path);
		CertificateFactory cf = CertificateFactory.getInstance("x509");
		Certificate cert = cf.generateCertificate(in);
		return cert.getPublicKey();
	}
	

}
