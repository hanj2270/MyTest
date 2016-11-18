package encrption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/** 
* @ClassName: EncrptionUtils 
* @Description: 加密工具类,将明文password的md5摘要作为密钥,对原文进行AES加密
* @author hanjian  
*/
public class EncrptionUtils {
	public static final String CIPHER_ALGORITHM="AES/CBC/PKCS5Padding";
	public static final String KEY_ALGORITHM="AES";
	/** 
	* @Description: 输入明文密钥和加密原文，进行AES加密，输出密文
	* @param password
	* @param Blob
	* @return    设定文件 
	 * @throws  
	*/
	public static String encrpt(String password,String Blob) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
			//将password的md5值加倍变成256位并作为AES-256的密钥
			byte[] mdBytes=initKey(password);
			byte[] key=new byte[mdBytes.length*2];
	        System.arraycopy(mdBytes, 0, key, 0, mdBytes.length);
	        System.arraycopy(mdBytes, 0, key, mdBytes.length, mdBytes.length);
	        //用２５６位的ｋｅｙ数组生成密钥
			SecretKey secretKey=new SecretKeySpec(key,KEY_ALGORITHM);
			Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM,"BC");    
			cipher.init(Cipher.ENCRYPT_MODE, secretKey,new IvParameterSpec(initKey(password)));
			//用password的md5值作为初始向量
			byte[] encrptedByte=cipher.doFinal(Blob.getBytes());
			return Hex.encodeHex(encrptedByte).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** 
	* @Description: 利用ｍｄ５算法把明文password转化为１２８位密钥
	* @param password　
	* @return    byte数组格式的密钥 
	 * @throws NoSuchAlgorithmException 
	*/
	private static byte[] initKey(String password) throws NoSuchAlgorithmException{
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] mdBytes = messageDigest.digest(password.getBytes());
        return mdBytes;
	}
	
}
