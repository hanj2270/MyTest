package encrption;

import org.junit.Test;

import encrption.DesUtils;

public class DesUtilsTest {

	@Test
	public void test() throws Exception {
		//密钥密文初始化
		String key="key";
		String data="this is a encrption test";
		//Des加密配置
		String KEY_ALGORITHM1="DES"; 
		String CIPHER_ALGORITHM1 = "DES/ECB/PKCS5Padding";
		
		System.out.println("DES加密测试");
		
		DesUtils.printResult(data, key, KEY_ALGORITHM1, CIPHER_ALGORITHM1, "jdk");
		DesUtils.printResult(data, key, KEY_ALGORITHM1, CIPHER_ALGORITHM1, "bc");
		//Aes加密配置
		String KEY_ALGORITHM2="AES"; 
		String CIPHER_ALGORITHM2 = "AES/ECB/PKCS5Padding";
		
		System.out.println("AES加密测试");
		
		DesUtils.printResult(data, key, KEY_ALGORITHM2, CIPHER_ALGORITHM2, "jdk");
		DesUtils.printResult(data, key, KEY_ALGORITHM2, CIPHER_ALGORITHM2, "bc");
	}

}
