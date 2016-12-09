package com.example.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.commonUtils.Sm4Utils;
import com.example.commonUtils.TestData;
import com.example.commonUtils.TestUtils;
import com.example.sm4byJava.SM4byJavaUtils;
import com.example.sm4byc.SM4byCUtils;
import com.example.sm4byctest.R;

public class MainActivity extends Activity implements OnClickListener {
	private Sm4Utils sm4j,sm4c;
	private TestData td;
	private TestUtils test;
	
	private Spinner langSp,blockSp,sizeSp;
	private Button stratBt;
	
	private ListView squenceLv;
	private ArrayAdapter<String> mArrayAdapter; 
	private ArrayList<String> squenceList;
	
	private int testSizeFlag;  //进行哪一种测试,0代表64bytes,1代表256bytes,2代表1024bytes,3代表8192bytes,4代表一次性测试全部
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		Clickset();
		squenceList=new ArrayList<String>();
		squenceList.add("测试结果将展示在这里");
		mArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,squenceList);
		squenceLv.setAdapter(mArrayAdapter);
	}
	
	
	
	/** 
	* @Description: 初始化界面控件 
	*/
	private void initView(){
		sm4c=new SM4byCUtils();
		sm4j=new SM4byJavaUtils();
		td=new TestData();
		test=new TestUtils(sm4c,td);
		langSp=(Spinner) findViewById(R.id.lang_sp);
		blockSp=(Spinner) findViewById(R.id.block_sp);
		sizeSp=(Spinner) findViewById(R.id.size_sp);
		stratBt=(Button) findViewById(R.id.start);
		squenceLv=(ListView) findViewById(R.id.squence_list);
	}
	
	
	
	
	
	
	/** 
	* @Description: 设置按钮和三个spinner的点击事件
	*/
	private void Clickset(){
		langSp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				//设置测试使用的实现语言
				switch (position) {
				case 0:
					test.setSm4(sm4c);
					break;
				case 1:
					test.setSm4(sm4j);
					break;
				default:
					break;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}		
		});
		
		blockSp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// 设置测试使用的分组模式
				switch (position) {
				case 0:
					test.setFlag(TestUtils.ecb);
					break;
				case 1:
					test.setFlag(TestUtils.cbc);
					break;
				default:
					break;
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}		
		});
		
		sizeSp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				testSizeFlag=position;
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}		
		});
		
		stratBt.setOnClickListener(MainActivity.this);
	}

	
	
	
	
	


	@Override
	public void onClick(View v) {
		switch (testSizeFlag) {
		case 0:
			test.test64();
			SquenceHeader();
			Squence64();
			mArrayAdapter.notifyDataSetChanged();
			break;
		case 1:
			test.test256();
			SquenceHeader();
			Squence256();
			mArrayAdapter.notifyDataSetChanged();
			break;
		case 2:
			test.test1024();
			SquenceHeader();
			Squence1024();
			mArrayAdapter.notifyDataSetChanged();
			break;
		case 3:
			test.test8192();
			SquenceHeader();
			Squence8192();
			mArrayAdapter.notifyDataSetChanged();
			break;
		case 4:
			test.testALL();
			SquenceHeader();
			Squence64();
			Squence256();
			Squence1024();
			Squence8192();
			mArrayAdapter.notifyDataSetChanged();
//			在控制台打印结果
//			StringBuffer sb = new StringBuffer();
//			for(String s:squenceList){
//				sb.append(s);
//			}
//			Log.d("test", sb.toString());
			break;	
		default:
			break;
		}
		
	}
	
	
	
	//将测试结果更新到squencelist中
	private void SquenceHeader(){
		squenceList.clear();
		if((test.getSm4())instanceof SM4byCUtils){
			squenceList.add("实现语言:c");
		}else{
			squenceList.add("实现语言:java");
		}
		String block;
		if(test.getFlag()==0){
			block="ecb";
		}else{
			block="cbc";
		}
		squenceList.add("分组模式:"+block);
		squenceList.add("执行3秒加密测试的结果:");
		Toast.makeText(MainActivity.this, "测试完成", Toast.LENGTH_SHORT).show();

	}
	
	private void Squence64(){
		squenceList.add("64bytes数据加密次数:"+test.getRec64());
		squenceList.add("64bytes数据加密速度:"+test.getSpd64()+"KB/s");
	}
	
	private void Squence256(){
		squenceList.add("256bytes数据加密次数:"+test.getRec256());
		squenceList.add("256bytes数据加密速度:"+test.getSpd256()+"KB/s");
	}
	
	private void Squence1024(){
		squenceList.add("1024bytes数据加密次数:"+test.getRec1024());
		squenceList.add("1024bytes数据加密速度:"+test.getSpd1024()+"KB/s");
	}
	
	private void Squence8192(){
		squenceList.add("8192bytes数据加密次数:"+test.getRec8192());
		squenceList.add("8192bytes数据加密速度:"+test.getSpd8192()+"KB/s");
	}
}
