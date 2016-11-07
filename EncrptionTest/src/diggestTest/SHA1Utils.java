package diggestTest;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.junit.Test;


/** 
* @ClassName: SHA1Utils 
* @Description: Jdk和bc实现SHA1摘要算法 
* @author hanjian  
*/
public class SHA1Utils {

	 /** 
	    * @Description: jdk实现sha1摘要 
	    * @param @param text 明文
	    * @param @return 密文byte数组
	    */
    public static byte[] jdkSHA1(String text) throws Exception {
        
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(text.getBytes());
            byte[] shaBytes = messageDigest.digest();
            System.out.println("jdk SHA1 16进制摘要: " + Hex.encodeHexString(shaBytes));
            System.out.println("jdk SHA1 Base64格式摘要："+Base64.encodeBase64(shaBytes)); 
            return shaBytes;
        
    }
    /** 
	    * @Description: bc实现sha1摘要 
	    * @param @param text 明文
	    * @param @return 密文byte数组
	    */
    @Test
    public static byte[] bcSHA1(String text) {
        Digest digest = new SHA1Digest();
        digest.update(text.getBytes(), 0, text.length());
        
        byte[] shaBytes = new byte[digest.getDigestSize()];
        digest.doFinal(shaBytes, 0);
        System.out.println("bc SHA1 16进制摘要: " + org.bouncycastle.util.encoders.Hex.toHexString(shaBytes));
        System.out.println("bc SHA1 Base64格式摘要："+Base64.encodeBase64(shaBytes)); 
        return shaBytes;
    }
    
   
}
