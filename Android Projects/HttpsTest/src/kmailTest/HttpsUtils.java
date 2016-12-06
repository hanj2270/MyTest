package kmailTest;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
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

import com.example.httpstest.R;

@SuppressWarnings("deprecation")
public class HttpsUtils {
	




	private final static String password="123456";
	
	private final static String host="192.168.1.80";
	private final static int PORT=443;
	
	public static String sessionId;
	private static HttpClient client;
	 

	
	
	public HttpsUtils(Context context) {

		 InputStream in;
	     try {
	    //閰嶇疆鐢ㄦ埛璇佷功鍜屾湇鍔″櫒璇佷功
	    	 KeyStore ClientStore = KeyStore.getInstance("PKCS12");
	    	 in =context.getResources().openRawResource(R.raw.hanjian);
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
	
	

		

	
	
	
	public String getresponse(String url){
		HttpPost httppost = new HttpPost(url); 
		try {				
			if(sessionId!=null){
				httppost.setHeader("Cookie", "JSESSIONID=" + sessionId);
			}
     
			HttpResponse response = client.execute(httppost);

			if (response.getStatusLine().getStatusCode() == 200) {
//			    if(toPrint==true){
//					HttpEntity entity = response.getEntity();
//					//鎵撳嵃html鏁版嵁					
//				    String message = EntityUtils.toString(entity, "gbk");
//				    System.out.println(message);
//				   }
			    
			    
			    //璇诲彇Cookie['JSPSESSID']鐨勫�煎瓨鍦ㄩ潤鎬佸彉閲忎腑锛屼繚璇佹瘡娆￠兘鏄悓涓�涓��
			    Header header = response.getFirstHeader("Set-Cookie");
			    if(header!=null){
			    String setCookie=header.getValue();
			    sessionId = setCookie.substring("JSESSIONID=".length(),
			        setCookie.indexOf(";"));
			    }
			    HttpEntity entity = response.getEntity();
			    return EntityUtils.toString(entity, "gbk");
                
			} else {
			    System.out.println("璇锋眰澶辫触");
			    System.out.println(response.getStatusLine().getStatusCode());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
