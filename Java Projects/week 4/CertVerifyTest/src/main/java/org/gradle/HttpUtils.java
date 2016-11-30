package org.gradle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;



@SuppressWarnings("deprecation")
public class HttpUtils {
	//下载到的证书文件的保存路径
	private final static String  floder="./src/main/resources/";
	private static HttpClient client;

	/** 
	* @Description: 根据证书路径下载证书
	* @param url
	* @return    设定文件 
	*/
	public boolean download(String url) {  
		client = new DefaultHttpClient();  
        HttpPost httppost = new HttpPost(url);  
        try {  
            HttpResponse httpResponse1 = client.execute(httppost);  
            StatusLine statusLine = httpResponse1.getStatusLine();  
            if (statusLine.getStatusCode() == 200) {
            	//根据url名称生成文件地址
            	String filepath=UrltoFilePath(url);
                File file = new File(filepath);  
                FileOutputStream outputStream = new FileOutputStream(file);  
                InputStream inputStream = httpResponse1.getEntity()  
                        .getContent();  
                byte b[] = new byte[1024];  
                int j = 0;  
                while ((j = inputStream.read(b)) != -1) {  
                    outputStream.write(b, 0, j);  
                }  
                outputStream.flush();  
                outputStream.close();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }finally{
        	httppost.releaseConnection();
        }
		return false; 
    } 
	
	/** 
	* @Description: 根据url生成证书存储地址
	* @param url
	* @return    设定文件 
	*/
	public static String UrltoFilePath(String url){
		Pattern pattern=Pattern.compile("([\\s\\S]*)/");  
		Matcher m = pattern.matcher(url);
		m.find();
		url=url.replace(m.group(0), "");
		String filepath=floder+url;
		return filepath;
	}
}
