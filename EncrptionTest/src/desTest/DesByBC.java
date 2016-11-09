package desTest;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class DesByBC {
	

	
	/** 
	* @Description: 生成一个密钥
	* @param key 密钥字符串
	* @return 密钥
	* @throws NoSuchAlgorithmException  
	*/
	private static byte[] keyGenerator(String key,String key_algorithm) throws NoSuchAlgorithmException {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());  
		KeyGenerator kg=KeyGenerator.getInstance(key_algorithm);
		if(key_algorithm=="DES"){
			kg.init(56,new SecureRandom(key.getBytes()));
		}else{
			kg.init(128, new SecureRandom(key.getBytes()));
		}
        //DES采用56位密码 ,并使用一个初始随机源
		//AES采用128位密码
        SecretKey secretKey=kg.generateKey();  
        return secretKey.getEncoded();  
    }
	
	/** 
	* @Description: 进行加密
	* @param data 明文
	* @param key des密钥
	* @return byte[]格式密文
	* @throws Exception  
	*/
	public static byte[] encrypt(byte[] data,byte[] key, String key_algorithm,String cipher_algorithm) throws Exception{    
		SecretKey des_key=new SecretKeySpec(key,key_algorithm); 
		Security.addProvider(new BouncyCastleProvider());
	    Cipher cipher=Cipher.getInstance(cipher_algorithm);  
	        //初始化，设置为加密模式  
	    cipher.init(Cipher.ENCRYPT_MODE, des_key);  
	    return cipher.doFinal(data);  
	 }  
	
	
	/** 
	* @Description: 解密函数 
	* @param data 密文
	* @param key des密钥
	* @return byte[]格式明文
	* @throws Exception  
	*/
	public static byte[] decrypt(byte[] data,byte[] key, String key_algorithm,String cipher_algorithm) throws Exception{  
		SecretKey des_key =new SecretKeySpec(key,key_algorithm);    
        Cipher cipher=Cipher.getInstance(cipher_algorithm);  
        cipher.init(Cipher.DECRYPT_MODE, des_key);  
        return cipher.doFinal(data);  
    }  
	
	/** 
	* @Description: jdk实现des加密
	* @param data 明文
	* @param key  密钥
	 * @throws NoSuchAlgorithmException 
	*/
	public static void jdkDes(String data,String key,String key_algorithm,String cipher_algorithm) throws Exception{
		System.out.println("利用jdk加密：");
		System.out.println("原文："+data);
		System.out.println("加密方式:"+cipher_algorithm);
		byte[] bin_key=keyGenerator(key,key_algorithm);   
		//加密数据 
		byte[] encrpytion_data=encrypt(data.getBytes(), bin_key,key_algorithm,cipher_algorithm);
		System.out.println("十六进制格式密文：");
		System.out.println(Hex.encodeHex(encrpytion_data)); 
        System.out.println("Base64格式密文："+Base64.encodeBase64(encrpytion_data));  
        //解密数据  
        byte[] decryption_data=decrypt(encrpytion_data, bin_key,key_algorithm,cipher_algorithm);  
        System.out.println("解密后："+new String(decryption_data));  
	}
}
