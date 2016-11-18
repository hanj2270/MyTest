package com.example.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class RemoteService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("myremoteService onbind");
		return new remoteServiceBinder();
	}
	
	private class remoteServiceBinder extends RemoteServiceInterface.Stub {

		@Override
		public String getMessage() throws RemoteException {
			String string=getString(R.string.remote_string)+getString(R.string.hello_world);
			return string;
		}
		
	}
	
	@Override
	public void onCreate() {
		System.out.println("myremoteService: oncreate");
		super.onCreate();
	}
	
	 @Override  
	public boolean onUnbind(Intent intent) {  
	    System.out.println("myremoteService£º onUnbind");  
	    return super.onUnbind(intent);  
	} 
	 
	 @Override
	public void onDestroy() {
		 System.out.println("myremoteService£º onDestory");  
		super.onDestroy();
	}

}
