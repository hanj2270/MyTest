package com.example.commonUtils;

public interface Sm4Utils {
	public byte[] encrpyt_cbc(byte[] data,byte[] key,byte[] IV);
	public byte[] encrpyt_ecb(byte[] data,byte[] key);

}
