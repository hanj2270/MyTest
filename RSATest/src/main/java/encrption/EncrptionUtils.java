package encrption;

import java.security.KeyException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class EncrptionUtils {
	private static final String DIGEST_ALGORITHM="MD5";
	private static final String CIPHER_ALGORITHM="RSA/ECB/PKCS1Padding";
	
	
	/** 
	* @Description: 利用私钥进行签名
	* @param prikey 私钥
	* @param data 需要签名的原文
	* @return
	* @throws Exception    设定文件 
	*/
	public static String sign(PrivateKey prikey,String data) {
		try {
			MessageDigest digest=MessageDigest.getInstance(DIGEST_ALGORITHM); //获取原文摘要
			byte[] digestbytes=digest.digest(data.getBytes("UTF-8"));
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, prikey);
			byte[] signBytes=cipher.doFinal(digestbytes);
			return Base64.encodeBase64String(signBytes);
		}catch(KeyException e){
			System.out.println("密钥错误,请检查已选择的证书");
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}


	/** 
	* @Description: 利用公钥进行验签
	* @param pubkey 公钥
	* @param data   原文
	* @param sign   签名
	* @return
	* @throws Exception    设定文件 
	*/
	public static boolean verify(PublicKey pubkey,String data,String sign) {
		try {
			byte [] signbytes=Base64.decodeBase64(sign);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, pubkey);
			byte[] signDecode=cipher.doFinal(signbytes);
			String digest1=Base64.encodeBase64String(signDecode); //从签名对原文摘要进行恢复
			String digest2=Base64.encodeBase64String(MessageDigest.getInstance(DIGEST_ALGORITHM). 
					digest(data.getBytes("UTF-8")));   //获取原文摘要
			//比从签名计算出的摘要与原文摘要,相同则为真
			if(digest1.equals(digest2)){   
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
