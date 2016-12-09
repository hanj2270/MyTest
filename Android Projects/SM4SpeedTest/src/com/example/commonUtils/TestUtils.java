package com.example.commonUtils;


public class TestUtils {
	private final static int testTime=3000; //测试3000ms
	
	private long rec64,rec256,rec1024,rec8192;//存储一定时间内加密次数的变量
	private double spd64,spd256,spd1024,spd8192;//加密速度,单位MB/s
    private TestData td;

	private Sm4Utils sm4;   //持有的sm4加密对象,用来确定加密的实现语言
	private int flag;       //设置加密的分组方法,0为ecb,1为cbc

	public final static int ecb=0;
	public final static int cbc=1;






	public TestUtils(Sm4Utils sm4,TestData td) {
		this.sm4=sm4;
		this.td=td;
	}
	
	
	
	/** 
	* @Description: 进行一次加密测试
	* @param data   待加密数据
	* @param key    密钥
	* @param IV     初始向量,ecb时为null
	* @return    设定文件 
	*/
	private int testOnce(byte[]data,byte[]key,byte[]IV){
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
	* @Description: 测试64bytes数据
	*/
	public void test64(){
		rec64=testOnce(td.getIn16(), td.getKey(), td.getIV());
		spd64=rec64*64/1024;
	}
	
	/** 
	* @Description: 测试256bytes数据
	*/
	public void test256(){
		rec256=testOnce(td.getIn256(), td.getKey(), td.getIV());
		spd256=rec256*256/1024;
	}
	
	/** 
	* @Description: 测试1024bytes数据
	*/
	public void test1024(){
		rec1024=testOnce(td.getIn1024(), td.getKey(), td.getIV());
		spd1024=rec1024;
	}
	
	/** 
	* @Description: 测试8192bytes数据
	*/
	public void test8192(){
		rec8192=testOnce(td.getIn8192(), td.getKey(), td.getIV());
		spd8192=rec8192*8192/1024;
	}
	
	/** 
	* @Description: 一次性测试全部
	*/
	public void testALL(){
		test64();
		test256();
		test1024();
		test8192();
	}
	
	
	//内部变量的get/set方法
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

	
	
	public double getSpd64() {
		return spd64;
	}

	public double getSpd256() {
		return spd256;
	}

	public double getSpd1024() {
		return spd1024;
	}

	public double getSpd8192() {
		return spd8192;
	}


	
	
	public void setSm4(Sm4Utils sm4) {
		this.sm4 = sm4;
	}
	
	
	
	public Sm4Utils getSm4() {
		return sm4;
	}



	public void setFlag(int flag) {
		this.flag = flag;
	}



	public int getFlag() {
		return flag;
	}
	
	
}
