package com.example.aidltestclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.aidltest.RemoteServiceInterface;

public class MainActivity extends Activity {
	private Intent intent;
	private Myconn conn;
	private RemoteServiceInterface remoteService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//获取几个button实例
		Button bind_button=(Button) findViewById(R.id.bind_button);
		Button getRemote_button=(Button) findViewById(R.id.getRemoteService_button);
		Button unbind_button=(Button) findViewById(R.id.unbind_button);
		
		intent=new Intent();
		intent.setAction("com.example.aidltest.getRemoteService");
		conn=new Myconn();
		//绑定逻辑,通过自定义的Myconn类中的onServiceConnection方法,获得远端服务的实例
		bind_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bindService(intent, conn, BIND_AUTO_CREATE);
			}
		});
		//通过持有的远端服务实例调用服务中的方法
		getRemote_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(remoteService!=null){
				try {
					Toast.makeText(MainActivity.this, "来自远端的信息"+remoteService.getMessage(), Toast.LENGTH_LONG).show();
				} catch (RemoteException e) {
					e.printStackTrace();
					Toast.makeText(MainActivity.this, "远端服务出错", Toast.LENGTH_LONG).show();
				}
				}else{
					Toast.makeText(MainActivity.this, "无远端服务", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		//解绑,自动调用Myconn类中的onServiceDiscnnected方法.
		unbind_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					unbindService(conn);
				}catch(IllegalArgumentException e){
					e.printStackTrace();
					Toast.makeText(MainActivity.this, "不存在已绑定的远端服务", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	
	
	private class Myconn implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			remoteService=RemoteServiceInterface.Stub.asInterface(service);
			//把service转型为远端服务的类型
			Toast.makeText(MainActivity.this, "绑定服务", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			remoteService=null;
			Toast.makeText(MainActivity.this, "解除服务", Toast.LENGTH_LONG).show();
			
		}
		
	}
}
