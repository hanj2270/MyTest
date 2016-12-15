package test.httpsproxytest;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

	private final static String TESTURL="https://192.168.41.70:1997";
	
	private EditText urlET,proxyET,browser;
	private Button startBt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();

	}
	
	private void initView(){
		urlET=(EditText) findViewById(R.id.url);
		proxyET=(EditText) findViewById(R.id.proxy);
		browser= (EditText) findViewById(R.id.browser);
		startBt=(Button) findViewById(R.id.gotoBt);
		startBt.setOnClickListener(MainActivity.this);
	}
	
	Handler handler = new Handler() {  
	    @Override  
	    public void handleMessage(Message msg) {  
	        super.handleMessage(msg);  
	        Bundle data = msg.getData();  
	        String content = data.getString("content");  
	        String headers=data.getString("headers");
//	        Log.d("text",content);
	        browser.setText(content);
	        proxyET.setText(headers);
	    }  
	};
		
		Runnable networkTask = new Runnable() {  
			  
		    @Override  
		    public void run() {  
		    	String content=null;
		    	String headers=null;
		    	
		    	HttpsUtils httpsUtils=new HttpsUtils(MainActivity.this);
		    	HttpResponse response = httpsUtils.getresponse(urlET.getText().toString());
		    	
		    	if(response!=null){
					content=httpsUtils.getContent(response);
					headers=httpsUtils.getHeader(response);
				}
		    	
		    	Message msg = new Message();  
		        Bundle data = new Bundle();  
		        data.putString("content", content);
		        data.putString("headers", headers);
		        msg.setData(data);  
		        handler.sendMessage(msg);  
		    }  
		};

		@Override
		public void onClick(View v) {
			new Thread(networkTask).start();
			
		} 
		
	}
