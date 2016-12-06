package com.example.httpstest;



import java.util.List;

import kmailTest.HttpsUtils;
import kmailTest.JsonUtils;
import kmailTest.MailBean;
import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class MainActivity extends Activity {
	private final static String url1="https://mail.koal.com";
	private final static String url2="https://mail.koal.com/userMail!queryMailList.do?currentFolder.folderId=10";

	private static EditText reEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		reEditText=(EditText) findViewById(R.id.response);
		new Thread(networkTask).start();  
		
	}
	
	Handler handler = new Handler() {  
	    @Override  
	    public void handleMessage(Message msg) {  
	        super.handleMessage(msg);  
	        Bundle data = msg.getData();  
	        String val = data.getString("value");  
	        Log.i("mylog", "请求结果为-->" + val);  
	        reEditText.setText(val);
	    }  
	};  
	
	
	
	Runnable networkTask = new Runnable() {  
		  
	    @Override  
	    public void run() {  
	    	HttpsUtils httpsUtils=new HttpsUtils(MainActivity.this);
			httpsUtils.getresponse(url1);  				//完成ssl认证，并通过登陆获取sessionId
			String html= httpsUtils.getresponse(url2); 	//获得收件箱页面
			String json=JsonUtils.getJsonObject(html);    //获得邮件json数据
			List<MailBean> l=JsonUtils.getMails(json);
			StringBuffer sb=new StringBuffer();
			int i=1;
			for(MailBean m:l){
				sb.append("<==========第"+i+"封邮件========> ");
				sb.append(m.toString());
				i++;
			}
			sb.append("<=========列表结束========>");
	        // 在这里进行 http request.网络请求相关操作  
	        Message msg = new Message();  
	        Bundle data = new Bundle();  
	        data.putString("value", sb.toString());  
	        msg.setData(data);  
	        handler.sendMessage(msg);  
	    }  
	}; 
}
