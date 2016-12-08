package com.example.sm4byJava;

import koal.security.gb.sm4.Sm4;

import com.example.commonUtils.Sm4Utils;

/*
 * 用公司的SM4库进行SM4单线程测速
 */
public class SM4byJavaUtils implements Sm4Utils{
	
	
	private Sm4 sm4;
	
	public SM4byJavaUtils() {
		sm4=new Sm4();
	}
	
	@Override
	public byte[] encrpyt_cbc(byte[] data, byte[] key,byte[] IV) {
		return sm4.sm4Cbc(key, data, IV, true);
	}

	@Override
	public byte[] encrpyt_ecb(byte[] data, byte[] key) {
		return sm4.sm4Ecb(key, data, true);
	}
	
	
	



}
