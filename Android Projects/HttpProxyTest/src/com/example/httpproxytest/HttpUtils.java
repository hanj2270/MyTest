package com.example.httpproxytest;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;



public class HttpUtils {
	
	
	private final static String proxyStr = "192.168.41.70"; 
	private final static int proxyPort=1994;
	private static DefaultHttpClient httpClient;
	
	public HttpUtils() {
		httpClient = new DefaultHttpClient();
		 httpClient.getCredentialsProvider().setCredentials(
				  new AuthScope(proxyStr, proxyPort),
				  new UsernamePasswordCredentials("userName", "password"));
		HttpHost proxy = new HttpHost(proxyStr,proxyPort);
		httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);	 
	}
	
	public HttpResponse httpGet(String url){
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
			
			    return response;
                
			} else{
				Log.d("test", "网络请求失败");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public String getContent(HttpResponse response){
		  
		   try {
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "utf-8");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return null;
	}
	
	public String getHeader(HttpResponse response){
		   try {
//			Header[] headers = response.getAllHeaders();
			Header headvia=response.getFirstHeader("via");
//			StringBuffer sb=new StringBuffer();
//			for(int i=0;i<headers.length;i++){
//				sb.append(headers[i].toString());
//			}
			return headvia.toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return null;
	}
	
	
}
