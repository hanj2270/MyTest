package com.example.fingerprinttest;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private EditText usernameET,keyET,statusET;
	private Button loginButton;
	private ImageView fingerImage;
	private LinearLayout statusLayout;
	
	private FingerprintManager fingerprintManager;
	private KeyguardManager keyguardManager;
    private MyHandler mHandler;
    private KeyHelper mKeyHelper;
    private FingerPrintHelper mFingerPrintHelper;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//��ʼ������
		initview();
		
		//��ȡָ��manager
		fingerprintManager = (FingerprintManager)getSystemService(Context.FINGERPRINT_SERVICE);
		keyguardManager =(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
		mKeyHelper=new KeyHelper(null,null,null);
		mHandler=new MyHandler(MainActivity.this.getMainLooper());
		mFingerPrintHelper= new FingerPrintHelper(mKeyHelper.getDefaultCipher(), fingerprintManager, mHandler,statusET);
		loginButton.setOnClickListener(this);
	}
	
	
	//��ʼ������
	private void initview(){
		usernameET=(EditText) findViewById(R.id.username);
		keyET=(EditText) findViewById(R.id.key);
		statusET=(EditText) findViewById(R.id.status);
		loginButton=(Button) findViewById(R.id.login);
		fingerImage=(ImageView) findViewById(R.id.finger);
		statusLayout=(LinearLayout)findViewById(R.id.statusLayout);
	}
	
	



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			String username=usernameET.getText().toString();
			String key=keyET.getText().toString();
			if(!fingerprintManager.isHardwareDetected()){
				Toast.makeText(this, "û��ָ����֤�豸��������֤",Toast.LENGTH_LONG).show();
				break;
			}else if(!keyguardManager.isKeyguardSecure()||!fingerprintManager.hasEnrolledFingerprints()){
				Toast.makeText(this, "��δ��ָ�ƿ�¼��,����ϵͳ������¼��ָ�ƺ�ʹ�øù���",Toast.LENGTH_LONG).show();
				break;
			}else if(!username.equals("123")||!key.equals("123")){
				Toast.makeText(this, "�û����������",Toast.LENGTH_LONG).show();
				break;
			}else{
				loginButton.setVisibility(View.INVISIBLE);
				fingerImage.setVisibility(View.VISIBLE);
				statusLayout.setVisibility(View.VISIBLE);
				mFingerPrintHelper.FingerVerify();
			}
			break;

		default:
			break;
		}
		
	}
	
	class MyHandler extends Handler {
		public MyHandler(Looper l){
			super(l);
		}
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case FingerPrintHelper.AuthenticationError:
				usernameET.setText(null);
				keyET.setText(null);
				usernameET.setHint("�����������û���");
	       	 	keyET.setHint("��������������");
	       	 	loginButton.setVisibility(View.VISIBLE);
	       	 	fingerImage.setVisibility(View.INVISIBLE);
				break;
			}
			
		}
	}
}
