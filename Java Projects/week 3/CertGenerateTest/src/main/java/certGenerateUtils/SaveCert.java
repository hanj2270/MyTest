package certGenerateUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class SaveCert {
	

	/** 
	* @Description: 把证书保存到pfx文件中 
	* @param alias
	* @param privKey
	* @param pwd
	* @param cert
	* @param filepath
	* @throws Exception    设定文件 
	*/
	public void savePfx(String alias, PrivateKey privKey, String pwd,
			Certificate cert, String filepath) throws Exception {
			FileOutputStream out = null;
			try{
			Certificate[] certChain={cert};
			KeyStore outputKeyStore = KeyStore.getInstance("pkcs12");
			outputKeyStore.load(null, pwd.toCharArray());
			outputKeyStore
			.setKeyEntry(alias, privKey, pwd.toCharArray(), certChain);
			out = new FileOutputStream(filepath);
			outputKeyStore.store(out, pwd.toCharArray());
			}finally{
			if(out!=null)
			  out.close();
			}
		}
	
	/** 
	* @Description: 把证书存入keystore
	* @param keyalias 证书别名
	* @param cert     x509证书
	* @param keystroePath 密钥库地址
	* @param Password    设定文件 
	*/
	public static void saveToKeyStore(String keyalias,X509Certificate cert,
			String keystroePath,String Password) {
		try {
			File file=new File(keystroePath);
			KeyStore ks=KeyStore.getInstance("jks");
			if(file.exists()){
				FileInputStream in=new FileInputStream(file);
				ks.load(in,Password.toCharArray());
			}else{
				ks.load(null,null);
			}
			ks.setCertificateEntry(keyalias, cert);
			ks.store(new FileOutputStream(file),Password.toCharArray());
		} catch (Exception e) {
			System.out.println("存入keystore失败");
			e.printStackTrace();
		} 
	}


}
