package diggestTest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;


/** 
* @ClassName: MacUtils 
* @Description: 利用jdk和bc实现Mac加密 
* @author hanjian  
*/
public class MacUtils {

	    
	    
	 
	    /**
	     * @return  
	    * @Description: 利用jdk实现MD5摘要
	    * @param @param key 密钥
	    * @param @param text    明文 
	    * @throws 
	    */
	    public static byte[] jdkHmacMD5(String key,String text ) throws Exception {
	            byte[] keyBytes = org.apache.commons.codec.binary.Hex.decodeHex(key.toCharArray());
	            // 还原密钥
	            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HmacMD5");
	            // 实例化MAC
	            Mac mac = Mac.getInstance(secretKeySpec.getAlgorithm());
	            // 初始化Mac
	            mac.init(secretKeySpec);
	            // 执行摘要
	            byte[] result = mac.doFinal(text.getBytes());
	            System.out.println("jdk hmacMD5 16进制摘要: " + Hex.toHexString(result));
	            System.out.println("jdk hmacMD5 Base64格式摘要："+Base64.encodeBase64(result)); 
	                 
	            return result;
	    }
	    
	
	    /**
	     * @return  
	    * @Description: 利用bc实现MD5摘要
	    * @param @param 密钥
	    * @param @param text    明文 
	    * @throws 
	    */
	    public static byte[] bcHmacMD5(String key,String text) {  
	        HMac hmac = new HMac(new MD5Digest());  
	        // 必须是16进制的字符，长度必须是2的倍数  
	        hmac.init(new KeyParameter(org.bouncycastle.util.encoders.Hex  
	                .decode(key)));  
	        hmac.update(text.getBytes(), 0, text.getBytes().length);  
	  
	        // 执行摘要  
	        byte[] hmacMD5Bytes = new byte[hmac.getMacSize()];  
	        hmac.doFinal(hmacMD5Bytes, 0);  
	        System.out.println("bc hmacMD5 16进制摘要:"  
	                + org.bouncycastle.util.encoders.Hex.toHexString(hmacMD5Bytes));
	        System.out.println("bc hmacMD5 Base64格式摘要："+Base64.encodeBase64(hmacMD5Bytes));
	        
	        return hmacMD5Bytes;
	  
	    }  

	}
