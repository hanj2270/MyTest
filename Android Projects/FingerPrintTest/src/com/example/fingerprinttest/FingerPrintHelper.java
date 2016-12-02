package com.example.fingerprinttest;

import javax.crypto.Cipher;

import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.AuthenticationResult;
import android.hardware.fingerprint.FingerprintManager.CryptoObject;
import android.os.CancellationSignal;
import android.os.Message;
import android.widget.EditText;

import com.example.fingerprinttest.MainActivity.MyHandler;



public class FingerPrintHelper {
	public final static int AuthenticationError=0;
	
	private CancellationSignal signal;
	private FingerprintManager mFingerprintManager;
	private Cipher mCipher;
	private MyHandler mhandler;
	private EditText statusET;
	
	public FingerPrintHelper(Cipher mCipher, FingerprintManager mFingerprintManager,MyHandler mhandler,
			EditText statusET) {

		this.mFingerprintManager = mFingerprintManager;
		this.mCipher = mCipher;
		this.mhandler=mhandler;
		this.statusET = statusET;
		signal=new CancellationSignal();
	}

	
	
	
	//指纹识别方法
		public void FingerVerify(){
			CryptoObject mCryptoObject=new CryptoObject(mCipher);
			mFingerprintManager.authenticate(mCryptoObject, signal, 0, new MyCallBack(), mhandler);
		}
		
		
		
		 public class MyCallBack extends FingerprintManager.AuthenticationCallback{
			 @Override
			public void onAuthenticationError(int errorCode, CharSequence errString) {
				 if (errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) { 
	            	 //如果出错次数过多
	            	 signal.cancel();
	            	 statusET.setText("出错次数过多,请等待30秒后,重新输入用户名密码并再次进行指纹验证");
	             }else{
	            	 statusET.setText("指纹验证意外中止:"+"errMsgId=" + errorCode + "|" +errString);
	             }
				 //延时30秒,通知进行修改
				 Message msg=new Message();
				 msg.what=AuthenticationError;
				 mhandler.sendMessageDelayed(msg, 3*1000);
				 
			}
			 
			 @Override
			public void onAuthenticationSucceeded(AuthenticationResult result) {
				 statusET.setText("指纹验证成功");
//				 Intent intent= new intent();
//				 Intent.setaction();
//				 startActivity(intent));
//				 CryptoObject cObject=result.getCryptoObject();
//				 cObject.getCipher().doFinal(byte[] data);
//				 可以获得加密器,做为其他加密使用
			}
			 
			 @Override
			public void onAuthenticationFailed() {
				 statusET.setText("指纹验证失败,请重试");
			}
			 
			 @Override
			public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
				 statusET.setText("指纹未能识别,请再尝试一次,参考帮助:"+helpString);
			}
			 
		 }

}
