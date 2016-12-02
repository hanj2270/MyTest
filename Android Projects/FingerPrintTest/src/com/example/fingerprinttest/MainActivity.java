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
		//初始化界面
		initview();
		
		//获取指纹manager
		fingerprintManager = (FingerprintManager)getSystemService(Context.FINGERPRINT_SERVICE);
		keyguardManager =(KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
		mKeyHelper=new KeyHelper(null,null,null);
		mHandler=new MyHandler(MainActivity.this.getMainLooper());
		mFingerPrintHelper= new FingerPrintHelper(mKeyHelper.getDefaultCipher(), fingerprintManager, mHandler,statusET);
		loginButton.setOnClickListener(this);
	}
	
	
	//初始化界面
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
				Toast.makeText(this, "没有指纹验证设备，不能验证",Toast.LENGTH_LONG).show();
				break;
			}else if(!keyguardManager.isKeyguardSecure()||!fingerprintManager.hasEnrolledFingerprints()){
				Toast.makeText(this, "你未作指纹库录入,请在系统设置中录入指纹后使用该功能",Toast.LENGTH_LONG).show();
				break;
			}else if(!username.equals("123")||!key.equals("123")){
				Toast.makeText(this, "用户名密码错误",Toast.LENGTH_LONG).show();
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
				usernameET.setHint("请重新输入用户名");
	       	 	keyET.setHint("请重新输入密码");
	       	 	loginButton.setVisibility(View.VISIBLE);
	       	 	fingerImage.setVisibility(View.INVISIBLE);
				break;
			}
			
		}
	}
}
