package encrption;

import java.io.ByteArrayOutputStream;
import java.security.Key;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EncrptionUtils {
	public static byte[] encrypt(Key key, byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA",new BouncyCastleProvider());
		cipher.init(Cipher.ENCRYPT_MODE, key);
		int blockSize = cipher.getBlockSize();
		int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
		int leavedSize = data.length % blockSize;
		int blocksSize = leavedSize != 0 ? data.length / blockSize + 1
				: data.length / blockSize;
		byte[] raw = new byte[outputSize * blocksSize];
		int i = 0;
		while (data.length - i * blockSize > 0) {
			if (data.length - i * blockSize > blockSize)
				cipher.doFinal(data, i * blockSize, blockSize, raw, i
						* outputSize);
			else
				cipher.doFinal(data, i * blockSize,
						data.length - i * blockSize, raw, i * outputSize);
			// 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把
			// byte[]放到ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到
			// 了此时加密块大小很可能已经超出了OutputSize所以只好用dofinal方法。
			i++;
		}
		return raw;
	}
	
	
	
	public static byte[] decrypt(Key key, byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA",
				new org.bouncycastle.jce.provider.BouncyCastleProvider());
		cipher.init(Cipher.DECRYPT_MODE, key);
		int blockSize = cipher.getBlockSize();
		ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
		int j = 0;
		while (data.length - j * blockSize > 0) {
			bout.write(cipher.doFinal(data, j * blockSize, blockSize));
			j++;
		}
		return bout.toByteArray();
	}
}


}
