package test.httpsproxytest;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import android.content.Context;

@SuppressWarnings("deprecation")
public class HttpsUtils {
	




	private final static String password="123456";
	
	private final static String host="192.168.41.70";
	private final static int PORT=1997;
	
	public static String sessionId;
	private static HttpClient client;
	 

	
	
	public HttpsUtils(Context context) {

		 InputStream in;
	     try {
	    //閰嶇疆鐢ㄦ埛璇佷功鍜屾湇鍔″櫒璇佷功
	    	 KeyStore ClientStore = KeyStore.getInstance("PKCS12");
	    	 in =context.getResources().openRawResource(R.raw.client);
	    	 ClientStore.load(in, password.toCharArray());
	    	 KeyStore ServerStore = KeyStore.getInstance("PKCS12");
	    	 in = context.getResources().openRawResource(R.raw.server);
	    	 ServerStore.load(in, password.toCharArray());
	         in.close();
	     //璁剧疆ssl鍙傛暟,鐢熸垚httpclient
	     SSLContext sslContext = SSLContexts.custom()
                 .loadTrustMaterial(ServerStore, new TrustSelfSignedStrategy())
                 .loadKeyMaterial(ClientStore,  password.toCharArray())
                 .setSecureRandom(new SecureRandom())
                 .useSSL()
                 .build();
	     
	     SSLConnectionSocketFactory sslsf =
	                new SSLConnectionSocketFactory(sslContext,null, null,
	                        SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	     
	     Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
                 .register("https", sslsf)
                 .build();

	     PoolingHttpClientConnectionManager sslConnectionManager = new PoolingHttpClientConnectionManager(r);
         HttpHost target = new HttpHost(host, PORT, "https");
         sslConnectionManager.setMaxPerRoute(new HttpRoute(target), 20);
         HttpClientBuilder secureHttpBulder = HttpClients.custom().setConnectionManager(sslConnectionManager);
         client=secureHttpBulder.build();
	     }catch (Exception e) {
	         e.printStackTrace();
	     }
	}
	
	

		

	
	
	
	public HttpResponse getresponse(String url){
		HttpPost httppost = new HttpPost(url); 
		try {				
			if(sessionId!=null){
				httppost.setHeader("Cookie", "JSESSIONID=" + sessionId);
			}
     
			HttpResponse response = client.execute(httppost);

			if (response.getStatusLine().getStatusCode() == 200) {
//			    if(toPrint==true){
//					HttpEntity entity = response.getEntity();				
//				    String message = EntityUtils.toString(entity, "gbk");
//				    System.out.println(message);
//				   }
			    
			    
			    Header header = response.getFirstHeader("Set-Cookie");
			    if(header!=null){
			    String setCookie=header.getValue();
			    sessionId = setCookie.substring("JSESSIONID=".length(),
			        setCookie.indexOf(";"));
			    }
			    
			    return response;
                
			} else {
			    System.out.println("请求失败");
			    System.out.println(response.getStatusLine().getStatusCode());
			}
			
		} catch (Exception e) {
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
			Header[] headers = response.getAllHeaders();
//			Header headvia=response.getFirstHeader("via");
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<headers.length;i++){
				sb.append(headers[i].toString());
			}
//			return headvia.toString();
			return sb.toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return null;
	}
	
	
}
