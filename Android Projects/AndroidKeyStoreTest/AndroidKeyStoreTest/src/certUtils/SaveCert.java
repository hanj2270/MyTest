package certUtils;

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import android.content.Context;

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
	public static void savePfx(Context context,String alias, PrivateKey privKey, String password,
			Certificate cert, String filepath) throws Exception {
			FileOutputStream out = null;
			try{
			Certificate[] certChain={cert};
			KeyStore outputKeyStore = KeyStore.getInstance("pkcs12");
			outputKeyStore.load(null, password.toCharArray());
			outputKeyStore.setKeyEntry(alias, privKey, password.toCharArray(), certChain);
			out = new FileOutputStream(android.os.Environment.getExternalStorageDirectory()+ "/"+alias);
			outputKeyStore.store(out, password.toCharArray());
			}finally{
			if(out!=null)
			  out.close();
			}
		}
	
	
	
}
