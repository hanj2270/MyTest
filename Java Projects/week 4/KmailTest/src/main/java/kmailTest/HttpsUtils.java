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
	private static HttpClient client;

	
	
	/** 
	* @Description: 导入服务器和客户端证书,配置httpconnection
	*/
	static{
		 FileInputStream in;
	     try {
	    //配置用户证书和服务器证书
	    	 KeyStore ClientStore = KeyStore.getInstance("PKCS12");
	    	 in = new FileInputStream(CLIENTCERTPATH);
	    	 ClientStore.load(in, password.toCharArray());
	    	 KeyStore ServerStore = KeyStore.getInstance("jks");
	    	 in = new FileInputStream(ServerCertPath);
	    	 ServerStore.load(in, password.toCharArray());
	         in.close();
	     //设置ssl参数,生成httpclient
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
//					//打印html数据					
//				    String message = EntityUtils.toString(entity, "gbk");
//				    System.out.println(message);
//				   }
			    
			    
			    //读取Cookie['JSPSESSID']的值存在静态变量中，保证每次都是同一个值
			    Header header = response.getFirstHeader("Set-Cookie");
			    if(header!=null){
			    String setCookie=header.getValue();
			    sessionId = setCookie.substring("JSESSIONID=".length(),
			        setCookie.indexOf(";"));
			    }
			    HttpEntity entity = response.getEntity();
			    return EntityUtils.toString(entity, "gbk");
                
			} else {
			    System.out.println("请求失败");
			    System.out.println(response.getStatusLine().getStatusCode());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httppost.releaseConnection();
		}
		return null;
	}
	
}
