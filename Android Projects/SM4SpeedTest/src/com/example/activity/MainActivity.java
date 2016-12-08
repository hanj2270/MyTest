package com.example.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.commonUtils.TestData;
import com.example.commonUtils.TestUtils;
import com.example.sm4byJava.SM4byJavaUtils;
import com.example.sm4byc.SM4byCUtils;
import com.example.sm4byctest.R;

public class MainActivity extends Activity {
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TestUtils test=new TestUtils(new SM4byCUtils());
		TestData td=new TestData();
		test.testALL(td, TestUtils.ecb);
		Log.d("testresult",""+test.getRec8192());
		Log.d("testresult",""+test.getRec1024());
		Log.d("testresult",""+test.getRec256());
		Log.d("testresult3",""+test.getRec64());
		TestUtils test2=new TestUtils(new SM4byJavaUtils());
		test2.testALL(td, TestUtils.ecb);
		Log.d("testresult",""+test2.getRec8192());
		Log.d("testresult",""+test2.getRec1024());
		Log.d("testresult",""+test2.getRec256());
		Log.d("testresult3",""+test2.getRec64());
	}
}
