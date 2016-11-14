package ioUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CertReadUtils {
	
	
	
	/** 
	* @Description: 从KeyStore中获取证书名称,默认获取第一张证书的名称
	* @param keyStore
	* @return
	* @throws KeyStoreException    设定文件 
	*/
	public static String getKeyAlias(KeyStore keyStore){
		try {
			if(keyStore!=null){
				String keyAlias=null;
				Enumeration<String> enumas = keyStore.aliases();
				if (enumas.hasMoreElements()) {
					keyAlias = (String) enumas.nextElement();
				}
				return keyAlias;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/** 
	* @Description: 从keystore中获得公钥 
	* @param keystore
	* @param Password
	* @return
	* @throws Exception    设定文件 
	*/
	public static PublicKey getPukfrommKs(KeyStore keyStore,String password) {
		try {
			if(keyStore!=null){
				String keyAlias=getKeyAlias(keyStore);
				Certificate cert = keyStore.getCertificate(keyAlias);
				PublicKey pubkey = cert.getPublicKey();
				return pubkey;
			}	
		} catch (KeyStoreException e) {
			System.out.println("证书内容为空");
			e.printStackTrace();
		}
		return null;
	}
	/** 
	* @Description: 从keystore中获得私钥 
	* @param keystore
	* @param strPassword
	* @return
	* @throws Exception    设定文件 
	*/
	public static PrivateKey getPriKeyFromKS(KeyStore keyStore,String password) {
		try {
			if(keyStore!=null){
				String keyAlias=getKeyAlias(keyStore);
				PrivateKey prikey = (PrivateKey) keyStore.getKey(keyAlias, password.toCharArray());
				return prikey;
			}
		} catch (Exception e) {
			System.out.println("证书内容为空");
			e.printStackTrace();
		}
		return null;
	}
	
	/** 
	* @Description: 从pfx文件生成keystore对象
	* @param path pfx文件路径
	* @param password 密码
	* @return
	* @throws Exception    设定文件 
	*/
	public static KeyStore getKsformPfx(String path, String password) {
		try {
			Security.addProvider(new BouncyCastleProvider());
			KeyStore ks;
			ks = KeyStore.getInstance("PKCS12", "BC");
			FileInputStream in = new FileInputStream(path);
			if (in!=null) {
				ks.load(in, password.toCharArray());
				in.close();
			}
			return ks;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("未找到相应文件");
		}  catch (CertificateException e) {
			e.printStackTrace();
			System.out.println("证书解析错误,请确认件路径为证书文件");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 
	* @Description: 获取crt证书中的公钥
	* @param path
	* @param password
	* @return
	 * @throws Exception 
	* @throws Exception    设定文件 
	*/
	public static PublicKey getPuKFromCrt(String path, String password) {
		InputStream in;
		try {
			in = new FileInputStream(path);
			CertificateFactory cf = CertificateFactory.getInstance("x509");
			Certificate cert = cf.generateCertificate(in);
			return cert.getPublicKey();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("未找到相应文件");
		} catch (CertificateException e) {
			e.printStackTrace();
			System.out.println("证书解析错误,请确认件路径为证书文件");
		}		
		return null;
	}



	

}
