package com.example.sm4byc;

import com.example.commonUtils.Sm4Utils;

public class SM4byCUtils implements Sm4Utils {

	
	static{
		System.loadLibrary("SM4byC");
	}
	public native byte[] encrypt_by_SM4CBC(byte[] data,byte[] key,byte[] IV,int size);
	public native byte[] encrypt_by_SM4ECB(byte[] data,byte[] key,int size);
	public native byte[] decrypt_by_SM4CBC(byte[] encrptedData,byte[] key,byte[] IV,int size);
	public native byte[] decrypt_by_SM4ECB(byte[] encrptedData,byte[] key,int size);
	
	
	@Override
	public byte[] encrpyt_cbc(byte[] data, byte[] key,byte[] IV) {
		return encrypt_by_SM4CBC(data, key,IV,data.length);
	}
	
	
	@Override
	public byte[] encrpyt_ecb(byte[] data, byte[] key) {
		return encrypt_by_SM4ECB(data, key,data.length);
	}
	
	
	
	
}
