package com.example.server;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;

import com.example.androidkeystoreserver.R;

public class SignCertService extends Service {
	private class myBinder extends myService.Stub{

		@Override
		public String getCert(String subject,String alias)
				throws RemoteException {
			try {
				String request=CertGenerate.createP10(subject, CertGenerate.password);
				InputStream in=getResources().openRawResource(R.raw.ca);
				X509Certificate cert=CertGenerate.createCert(request, in, CertGenerate.password);
				PrivateKey prikey=CertGenerate.prikey;
				if(prikey!=null){
					SaveUtils.SaveinKeystore(SignCertService.this, alias, prikey, cert);
				}
				return Base64.encodeToString(cert.getEncoded(), Base64.DEFAULT);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} 
		}

		@Override
		public String getPrikey() throws RemoteException {
			return Base64.encodeToString(CertGenerate.prikey.getEncoded(), Base64.DEFAULT);
		}
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new myBinder();
	}
}
