package desTest;

import org.junit.Test;

public class DesTest {

	@Test
	public void test() throws Exception {
		//密钥密文初始化
		String key="key";
		String data="this is a encrption test";
		//Des加密配置
		String KEY_ALGORITHM1="DES"; 
		String CIPHER_ALGORITHM1 = "DES/ECB/PKCS5Padding";
		
		System.out.println("DES加密测试");
		
		DesByJdk.jdkDes(data, key,KEY_ALGORITHM1,CIPHER_ALGORITHM1);
		DesByBC.BCDes(data, key,KEY_ALGORITHM1,CIPHER_ALGORITHM1);
		
		
		//Aes加密配置
		String KEY_ALGORITHM2="AES"; 
		String CIPHER_ALGORITHM2 = "AES/ECB/PKCS5Padding";
		
		System.out.println("AES加密测试");
		
		DesByJdk.jdkDes(data, key,KEY_ALGORITHM2,CIPHER_ALGORITHM2);
		DesByBC.BCDes(data, key,KEY_ALGORITHM1,CIPHER_ALGORITHM2);
	}

}
