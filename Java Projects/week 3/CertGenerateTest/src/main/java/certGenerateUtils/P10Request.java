package certGenerateUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Security;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMEncryptor;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMEncryptorBuilder;






@SuppressWarnings("deprecation")


public class P10Request {
	
	/** 
	* @Description: 生成p10请求,以Base64 格式String输出
	* @param subject 证书使用者信息
	* @param password  保存私钥用的密码
	* @param savingPath 保存私钥的地址
	* @return    设定文件 
	*/
	public static String createP10(String subject,String password,String savingPath){
		try {
			Security.addProvider(new BouncyCastleProvider());
			X509Name dn = new X509Name(subject);
			//生成密钥对
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");  
            keyGen.initialize(1024);  
            KeyPair kp = keyGen.generateKeyPair();
            PKCS10CertificationRequest p10 = new PKCS10CertificationRequest("MD5WithRSA",dn,kp.getPublic(),
            		new DERSet(),kp.getPrivate());
            byte[] der=p10.getEncoded();
            //增加pem格式的头尾
            String code ="-----BEGIN CERTIFICATE REQUEST-----\n";  
            code += Base64.encodeBase64String(der);  
            code += "\n-----END CERTIFICATE REQUEST-----\n";  
            //将私钥进行保存
            savePEM(kp.getPrivate(), password, savingPath); 
            return code;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
		
	/** 
	* @Description: 将私钥匙以pem格式保存
	* @param key
	* @param password
	* @param path
	* @throws Exception    设定文件 
	*/
	public static void savePEM(PrivateKey key,String password,String path) throws Exception {  
	       PEMWriter writer = new PEMWriter(new FileWriter(path)); 
	       PEMEncryptor encryptor = new JcePEMEncryptorBuilder("DES-EDE3-CBC").setProvider("BC")
	    		   .build(password.toCharArray());
	       writer.writeObject(key, encryptor);  
	       writer.close();  
	    } 	 	
	
	
	@SuppressWarnings("resource")
	public static PrivateKey getPrivateKey(String path,String password) {
		  try {
			File File = new File(path); // private key file in PEM format
			  PEMParser pemParser = new PEMParser(new FileReader(File));
			  Object object = pemParser.readObject();
			  PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build(password.toCharArray());
			  JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
			  KeyPair kp;
			  if (object instanceof PEMEncryptedKeyPair) {
				  kp = converter.getKeyPair(((PEMEncryptedKeyPair) object).decryptKeyPair(decProv));
			 } else {
				  kp = converter.getKeyPair((PEMKeyPair) object);
			 }
			  return kp.getPrivate();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		}
	
	
	 

}
