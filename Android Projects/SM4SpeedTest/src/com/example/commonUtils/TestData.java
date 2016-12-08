package com.example.commonUtils;

/** 
* @ClassName: CommonUtils 
* @Description: 生成不同大小的数据块和密钥
* @author hanjian  
*/
public class TestData {

	private byte[] in16 = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab,
			(byte) 0xcd, (byte) 0xef, (byte) 0xfe, (byte) 0xdc,
			(byte) 0xba, (byte) 0x98, 0x76, 0x54, 0x32, 0x10 };
	
	private byte[] key = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab,
			(byte) 0xcd, (byte) 0xef, (byte) 0xfe, (byte) 0xdc,
			(byte) 0xba, (byte) 0x98, 0x76, 0x54, 0x32, 0x10 };
	
	private byte[] iv = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab,
			(byte) 0xcd, (byte) 0xef, (byte) 0xfe, (byte) 0xdc,
			(byte) 0xba, (byte) 0x98, 0x76, 0x54, 0x32, 0x10 };
	
	private byte[]in64,in256,in1024,in8192;
	
	
	/**
	 * 生成不同大小的测试数据
	 */
	public TestData() {				
					// 64byte输入
					in64 = new byte[64];
					for (int i = 0; i < 16; i++) {
						for (int j = 0; j < 4; j++) {
							in64[i + j * 16] = in16[i];
						}
					}
					// 256byte输入
					in256 = new byte[256];
					for (int i = 0; i < 64; i++) {
						for (int j = 0; j < 4; j++) {
							in256[i + j * 64] = in64[i];
						}
					}
					// 1024byte输入
					in1024 = new byte[1024];
					for (int i = 0; i < 256; i++) {
						for (int j = 0; j < 4; j++) {
							in1024[i + 256 * j] = in256[i];
						}
					}
					// 8192byte输入
					in8192 = new byte[8192];
					for (int i = 0; i < 1024; i++) {
						for (int j = 0; j < 8; j++) {
							in8192[i + 1024 * j] = in1024[i];
						}
					}

					
	}
	
	

	public byte[] getIn16() {
		return in16;
	}

	public byte[] getIn64() {
		return in64;
	}

	public byte[] getIn256() {
		return in256;
	}

	public byte[] getIn1024() {
		return in1024;
	}

	public byte[] getIn8192() {
		return in8192;
	}

	public byte[] getKey() {
		return key;
	}

	public byte[] getIV() {
		return iv;
	}
	
}
