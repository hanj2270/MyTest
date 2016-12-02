package com.example.fingerprinttest;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

public class KeyHelper {
	private final static String defaultKeyName="mkey";
	//Ĭ�ϼ��ܷ���"AES/CBC/PKCS7Padding"
	private String mCipheralgorithm="AES";
	private String mBlockModes="CBC";
	private String mPaddings="PKCS7Padding";
	
	private static Cipher defaultCipher;
	
	public Cipher getDefaultCipher() {
		return defaultCipher;
	}

	public KeyHelper(String Cipheralgorithm,String BlockModes,String Paddings){
		//ͨ��������ָ�����ܷ���
		if(Cipheralgorithm!=null&&BlockModes!=null&&Paddings!=null){
			this.mCipheralgorithm=Cipheralgorithm;
			this.mBlockModes=BlockModes;
			this.mPaddings=Paddings;
		}
		//���ɼ��ܶ���
		try {
			  KeyStore mKeyStore = KeyStore.getInstance("AndroidKeyStore");
			  mKeyStore.load(null);
			  //������Կ
			  KeyGenerator mKeyGenerator = KeyGenerator
				      .getInstance(mCipheralgorithm, "AndroidKeyStore");
			 
			  KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(defaultKeyName,
			      KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
			      .setBlockModes(mBlockModes)
			      .setUserAuthenticationRequired(true)
			      .setEncryptionPaddings(mPaddings);
			  	  
			  mKeyGenerator.init(builder.build());
			  mKeyGenerator.generateKey();
			 //����cipher���� 
			  defaultCipher = Cipher.getInstance(mCipheralgorithm+"/"+mBlockModes+"/"+mPaddings);
			  SecretKey key = (SecretKey) mKeyStore.getKey(defaultKeyName, null);
			  defaultCipher.init(Cipher.ENCRYPT_MODE, key);
			} catch (Exception e) {
			  Log.d("test", "����Cipher����");
			}
	}
}
