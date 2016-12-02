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

	
	
	
	//ָ��ʶ�𷽷�
		public void FingerVerify(){
			CryptoObject mCryptoObject=new CryptoObject(mCipher);
			mFingerprintManager.authenticate(mCryptoObject, signal, 0, new MyCallBack(), mhandler);
		}
		
		
		
		 public class MyCallBack extends FingerprintManager.AuthenticationCallback{
			 @Override
			public void onAuthenticationError(int errorCode, CharSequence errString) {
				 if (errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) { 
	            	 //��������������
	            	 signal.cancel();
	            	 statusET.setText("�����������,��ȴ�30���,���������û������벢�ٴν���ָ����֤");
	             }else{
	            	 statusET.setText("ָ����֤������ֹ:"+"errMsgId=" + errorCode + "|" +errString);
	             }
				 //��ʱ30��,֪ͨ�����޸�
				 Message msg=new Message();
				 msg.what=AuthenticationError;
				 mhandler.sendMessageDelayed(msg, 3*1000);
				 
			}
			 
			 @Override
			public void onAuthenticationSucceeded(AuthenticationResult result) {
				 statusET.setText("ָ����֤�ɹ�");
//				 Intent intent= new intent();
//				 Intent.setaction();
//				 startActivity(intent));
//				 CryptoObject cObject=result.getCryptoObject();
//				 cObject.getCipher().doFinal(byte[] data);
//				 ���Ի�ü�����,��Ϊ��������ʹ��
			}
			 
			 @Override
			public void onAuthenticationFailed() {
				 statusET.setText("ָ����֤ʧ��,������");
			}
			 
			 @Override
			public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
				 statusET.setText("ָ��δ��ʶ��,���ٳ���һ��,�ο�����:"+helpString);
			}
			 
		 }

}
