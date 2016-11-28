package kmailTest;

import java.io.FileInputStream;
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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class HttpsUtils {
	private final static String password="123456";
	private final static String ServerCertPath="./src/main/resources/server.jks";
	private final static String CLIENTCERTPATH="./src/main/resources/hanjian.pfx";
	
	private final static String host="192.168.1.80";
	private final static int PORT=443;
	public static String sessionId;
	

	
	
	/** 
	* @Description: 导入服务器和客户端证书,配置httpconnection
	*/
	private static CloseableHttpClient getHttpClient(){
		 FileInputStream in;
	     try {
	    	 KeyStore ClientStore = KeyStore.getInstance("PKCS12");
	    	 in = new FileInputStream(CLIENTCERTPATH);
	    	 ClientStore.load(in, password.toCharArray());
	    	 KeyStore ServerStore = KeyStore.getInstance("jks");
	    	 in = new FileInputStream(ServerCertPath);
	    	 ServerStore.load(in, password.toCharArray());
	         in.close();
	     
	     SSLContext sslContext = SSLContexts.custom()
                 .loadTrustMaterial(ServerStore, new TrustSelfSignedStrategy())
                 .loadKeyMaterial(ClientStore,  password.toCharArray())
                 .setSecureRandom(new SecureRandom())
                 .useSSL()
                 .build();
	     
	     SSLConnectionSocketFactory sslsf =
	                new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1"}, null,
	                        SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	     
	     Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
                 .register("https", sslsf)
                 .build();

	     PoolingHttpClientConnectionManager sslConnectionManager = new PoolingHttpClientConnectionManager(r);
         HttpHost target = new HttpHost(host, PORT, "https");
         sslConnectionManager.setMaxPerRoute(new HttpRoute(target), 20);
         HttpClientBuilder secureHttpBulder = HttpClients.custom().setConnectionManager(sslConnectionManager);
         return secureHttpBulder.build();
	     }catch (Exception e) {
	         e.printStackTrace();
	         return null;
	     }
	}

	
	
	
	public static void getresponse(String url,boolean toPrint){
		try {
			HttpClient client = getHttpClient(); 
			
			HttpPost httppost = new HttpPost(url); 
			if(sessionId!=null){
				httppost.setHeader("Cookie", "JSESSIONID=" + sessionId);
			}
     
			HttpResponse response = client.execute(httppost);

			if (response.getStatusLine().getStatusCode() == 200) {
			    if(toPrint==true){
					//打印html数据
					HttpEntity entity = response.getEntity();
				    String message = EntityUtils.toString(entity, "gbk");
				    System.out.println(message);
				   }
			    
			    
			    //这里是读取Cookie['JSPSESSID']的值存在静态变量中，保证每次都是同一个值
			    Header header = response.getFirstHeader("Set-Cookie");
			    if(header!=null){
			    String setCookie=header.getValue();
			    sessionId = setCookie.substring("JSESSIONID=".length(),
			        setCookie.indexOf(";"));
			    }
                
			} else {
			    System.out.println("请求失败");
			    System.out.println(response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
