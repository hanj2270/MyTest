package com.example.commonUtils;


public class TestUtils {
	private final static int testTime=3000; //����3000ms
	
	
	private long rec64,rec256,rec1024,rec8192;//�洢һ��ʱ���ڼ��ܴ����ı���


	public final static int ecb=0;
	public final static int cbc=1;

	private Sm4Utils sm4;
	
	public TestUtils(Sm4Utils sm4) {
		this.sm4=sm4;
	}
	
	
	
	/** 
	* @Description: ����һ�μ��ܲ���
	* @param data   ����������
	* @param key    ��Կ
	* @param IV     ��ʼ����,ecbʱΪnull
	* @param flag   0����ecb,1����cbc
	* @return    �趨�ļ� 
	*/
	public int testOnce(byte[]data,byte[]key,byte[]IV,int flag){
		int count=0;
		long startTime = System.currentTimeMillis();
		long endTime=startTime+testTime;
		while(System.currentTimeMillis()<endTime){
			if(flag==cbc){
				sm4.encrpyt_cbc(data, key, IV);
			}else{
				sm4.encrpyt_ecb(data, key);
			}
			count++;
		}
		return count;
	}
	
	/** 
	* @Description: һ���Բ���ȫ��
	* @param td   �ṩ���ݵ�TestData
	* @param flag    ����������Ϊecb����cbc
	*/
	public void testALL(TestData td,int flag){
		rec64=testOnce(td.getIn16(), td.getKey(), td.getIV(), flag);
		rec256=testOnce(td.getIn256(), td.getKey(), td.getIV(), flag);
		rec1024=testOnce(td.getIn1024(), td.getKey(), td.getIV(), flag);
		rec8192=testOnce(td.getIn8192(), td.getKey(), td.getIV(), flag);
	}
	
	public long getRec64() {
		return rec64;
	}



	public long getRec256() {
		return rec256;
	}



	public long getRec1024() {
		return rec1024;
	}



	public long getRec8192() {
		return rec8192;
	}
}
