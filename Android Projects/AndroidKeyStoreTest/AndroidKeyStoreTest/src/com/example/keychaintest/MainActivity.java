package com.example.keychaintest;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import certUtils.DecodeCert;
import certUtils.SaveCert;

import com.example.server.myService;

public class MainActivity extends Activity implements OnClickListener {
	private final static String PASSWORD="123456";
	private final static String FILEPATH="test.pfx";
	private myService signBinder=null;
	private ServiceConnection conn;
	
	private EditText aliasText,cText,lText,oText,cnText,certText;
	
	private Button generateButton,showButton,saveButton;
	
	private String cert,prikey;
	
	private static int flag=0;//flag=0的时候为无证书,为1的时候为有证书.

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			initview();
			Intent intent=new Intent();
			intent.setAction("com.example.server.SignCertService.getService");
			conn=new Myconn();
			bindService(intent, conn, BIND_AUTO_CREATE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public class Myconn implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			signBinder=myService.Stub.asInterface(service);
			Log.d("test", "绑定服务");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			signBinder=null;
			
		}
		
	}
	
	@Override
	protected void onDestroy() {
		unbindService(conn);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.show:
			if(flag==1){
			X509Certificate decodecert=DecodeCert.decodeCert(cert);
			certText.setText(decodecert.toString());
			}else{
				Toast.makeText(this, "没有证书可以展示", Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.generate:
			try {
				String alias = aliasText.getText().toString();
				String country=cText.getText().toString();
				String location=lText.getText().toString();
				String organization=oText.getText().toString();
				String cn=cnText.getText().toString();
				if(alias==null||country==null&&location==null&&organization==null&&cn==null){
					Toast.makeText(this, "缺少证书使用者信息,请补全", Toast.LENGTH_LONG).show();
					break;
				}
				String subject="C="+country+", L="+location+",O="+organization+", CN="+cn+";";
				cert=signBinder.getCert(subject,alias);
				prikey=signBinder.getPrikey();
				certText.setText("您的证书已经可以展示和存储");
				flag=1;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.save:
			if(flag==1){	
				try {
						String alias = aliasText.getText().toString()+".pfx";
						X509Certificate savecert=DecodeCert.decodeCert(cert);
						PrivateKey saveprikey=DecodeCert.decodeKey(prikey);
						SaveCert.savePfx(this,alias, saveprikey, PASSWORD, savecert, FILEPATH);
						
						Dialog dialog=new AlertDialog.Builder(this)
								.setTitle("证书安装说明")
								.setMessage("点击\"从SD卡安装\"选项,从左侧标签栏选择\"内部存储空间\",选择与设置名称相同的pfx文件,进行安装.")
								.setPositiveButton("确认", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent=new Intent(Settings.ACTION_SECURITY_SETTINGS);
										startActivity(intent);
										
									}
								})
								.create();
						dialog.show();
					} catch (Exception e) {
						e.printStackTrace();
					}
			}else{
				Toast.makeText(this, "没有证书可以存储", Toast.LENGTH_LONG).show();
			}
			break;
		}
		
	}
	
	private void initview(){
		aliasText=(EditText) findViewById(R.id.aliasText);
		cText=(EditText) findViewById(R.id.CText);
		lText=(EditText) findViewById(R.id.LText);
		oText=(EditText) findViewById(R.id.OText);
		cnText=(EditText) findViewById(R.id.CNText);
		certText=(EditText) findViewById(R.id.cert);
		generateButton=(Button) findViewById(R.id.generate);
		showButton=(Button) findViewById(R.id.show);
		saveButton=(Button) findViewById(R.id.save);
		generateButton.setOnClickListener(this);
		showButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
	}
	
	
	
	
}
