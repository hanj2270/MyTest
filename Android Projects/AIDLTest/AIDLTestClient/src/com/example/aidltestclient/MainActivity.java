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
		//��ȡ����buttonʵ��
		Button bind_button=(Button) findViewById(R.id.bind_button);
		Button getRemote_button=(Button) findViewById(R.id.getRemoteService_button);
		Button unbind_button=(Button) findViewById(R.id.unbind_button);
		
		intent=new Intent();
		intent.setAction("com.example.aidltest.getRemoteService");
		conn=new Myconn();
		//���߼�,ͨ���Զ����Myconn���е�onServiceConnection����,���Զ�˷����ʵ��
		bind_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bindService(intent, conn, BIND_AUTO_CREATE);
			}
		});
		//ͨ�����е�Զ�˷���ʵ�����÷����еķ���
		getRemote_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(remoteService!=null){
				try {
					Toast.makeText(MainActivity.this, "����Զ�˵���Ϣ"+remoteService.getMessage(), Toast.LENGTH_LONG).show();
				} catch (RemoteException e) {
					e.printStackTrace();
					Toast.makeText(MainActivity.this, "Զ�˷������", Toast.LENGTH_LONG).show();
				}
				}else{
					Toast.makeText(MainActivity.this, "��Զ�˷���", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		//���,�Զ�����Myconn���е�onServiceDiscnnected����.
		unbind_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					unbindService(conn);
				}catch(IllegalArgumentException e){
					e.printStackTrace();
					Toast.makeText(MainActivity.this, "�������Ѱ󶨵�Զ�˷���", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	
	
	private class Myconn implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			remoteService=RemoteServiceInterface.Stub.asInterface(service);
			//��serviceת��ΪԶ�˷��������
			Toast.makeText(MainActivity.this, "�󶨷���", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			remoteService=null;
			Toast.makeText(MainActivity.this, "�������", Toast.LENGTH_LONG).show();
			
		}
		
	}
}
