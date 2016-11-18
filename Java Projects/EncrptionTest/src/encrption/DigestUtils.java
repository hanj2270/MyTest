package encrption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;


public class DigestUtils {
		/** 
		* @Description: 运用jdk实现摘要加密,支持md5\sha1\hmac
		* @param text　明文
		* @param cipher_algorithm　加密算法
		* @return　密文byte数组
		 * @throws Exception 
		* @throws NoSuchAlgorithmException    设定文件 
		*/
		public static byte[] jdkDigest(String text,String cipher_algorithm,String key) throws Exception {
			byte[] result;
			if(cipher_algorithm.equals("HmacMD5")){
				//将密钥编码为十六进制字符
				byte[] keyBytes = org.apache.commons.codec.binary.Hex.decodeHex(key.toCharArray());
		        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HmacMD5");
		        Mac mac = Mac.getInstance("HmacMD5");
		        mac.init(secretKeySpec);
		        result = mac.doFinal(text.getBytes());
			}else{
				MessageDigest digest=MessageDigest.getInstance(cipher_algorithm);
				digest.update(text.getBytes());
				result=digest.digest();
			}
			return result;
		}
		

		/** 
		* @Description: 运用BC实现摘要加密,支持md5\sha1\hmac
		* @param text　明文
		* @param cipher_algorithm　加密算法
		* @return　密文ｂｙｔｅ数组
		*/
		public static byte[] bcDigest(String text, String cipher_algorithm, String key)
				throws Exception {
			byte[] result;
			Digest digest;
			if(cipher_algorithm.equals("HmacMD5")){
				 	HMac hmac = new HMac(new MD5Digest());  // 必须是16进制的字符，长度必须是2的倍数  
			        hmac.init(new KeyParameter(org.bouncycastle.util.encoders.Hex  
			                .decode(key)));  
			        hmac.update(text.getBytes(), 0, text.getBytes().length);  // 执行摘要  
			        result = new byte[hmac.getMacSize()];  
			        hmac.doFinal(result, 0);
			        return result;
			}else if(cipher_algorithm.equals("SHA")){
				digest=new SHA1Digest();
			}else{
				digest=new MD5Digest();
			}
			digest.update(text.getBytes(), 0, text.length());
			result=new byte[digest.getDigestSize()];
			digest.doFinal(result, 0);
			return result;
		}

 

	
	public static void printResult(String text,String cipher_algorithm,String key,String implement) throws Exception{
			byte[] result;
		if(implement.equals("jdk")){
			System.out.println("jdk 实现摘要加密:");
			result=jdkDigest(text, cipher_algorithm, key);
		}else{
			System.out.println("BC 实现摘要加密");
			result=bcDigest(text, cipher_algorithm, key);
		}
		System.out.println("加密方式:"+cipher_algorithm);
		if(key!=null){
			System.out.println("密钥:"+key);
		}
		System.out.println("16进制密文:"+Hex.encodeHexString(result));
		System.out.println("Base64格式密文:"+Base64.encodeBase64String(result));
	}
}
