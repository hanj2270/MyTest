package diggestTest;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;

/** 
* @ClassName: MD5Utils 
* @Description: Jdk和bc实现MD5摘要算法 
* @author hanjian  
*/
public  class MD5Utils {
    

    /** 
    * @Description: jdk实现md5摘要 
    * @param @param text 明文
    * @param @return 密文byte数组
    */
    public static byte[] jdkMD5(String text) throws Exception {
        
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] mdBytes = messageDigest.digest(text.getBytes());
            System.out.println("jdk md5 16进制摘要: " + Hex.encodeHexString(mdBytes));
            System.out.println("jdk md5 Base64格式摘要："+Base64.encodeBase64(mdBytes)); 
            return mdBytes;
    }


    //
    
    /** 
    * @Description: bc实现md5摘要  
    * @param @param text 明文
    * @param @return    密文byte数组
    * @return byte[]    返回类型 
    */
    public static byte[] bcmMD5(String text) {
        Digest digest = new MD5Digest();
        digest.update(text.getBytes(), 0, text.length());
        byte[] mdBytes = new byte[digest.getDigestSize()];
        digest.doFinal(mdBytes, 0);
        System.out.println("bc md5 decode: " + Hex.encodeHexString(mdBytes));
        System.out.println("bc md5 Base64格式摘要："+Base64.encodeBase64(mdBytes));
        return mdBytes;
    }

    //
   
}