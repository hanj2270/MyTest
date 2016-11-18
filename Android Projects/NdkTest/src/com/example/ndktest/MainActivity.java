package com.example.ndktest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	static {
		System.loadLibrary("HelloWorld");
	}
	
	public native String HelloFromJNI();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button get_hello_button=(Button) findViewById(R.id.get_hello);
		get_hello_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String title_text=HelloFromJNI();
				TextView title_tv=(TextView) findViewById(R.id.tilte_text);
				title_tv.setText(title_text);
				
			}
		});
		
	}
}
