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
	
	private int testSizeFlag;  //������һ�ֲ���,0����64bytes,1����256bytes,2����1024bytes,3����8192bytes,4����һ���Բ���ȫ��
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		Clickset();
		squenceList=new ArrayList<String>();
		squenceList.add("���Խ����չʾ������");
		mArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,squenceList);
		squenceLv.setAdapter(mArrayAdapter);
	}
	
	
	
	/** 
	* @Description: ��ʼ������ؼ� 
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
	* @Description: ���ð�ť������spinner�ĵ���¼�
	*/
	private void Clickset(){
		langSp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				//���ò���ʹ�õ�ʵ������
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
				// ���ò���ʹ�õķ���ģʽ
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
//			�ڿ���̨��ӡ���
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
	
	
	
	//�����Խ�����µ�squencelist��
	private void SquenceHeader(){
		squenceList.clear();
		if((test.getSm4())instanceof SM4byCUtils){
			squenceList.add("ʵ������:c");
		}else{
			squenceList.add("ʵ������:java");
		}
		String block;
		if(test.getFlag()==0){
			block="ecb";
		}else{
			block="cbc";
		}
		squenceList.add("����ģʽ:"+block);
		squenceList.add("ִ��3����ܲ��ԵĽ��:");
		Toast.makeText(MainActivity.this, "�������", Toast.LENGTH_SHORT).show();

	}
	
	private void Squence64(){
		squenceList.add("64bytes���ݼ��ܴ���:"+test.getRec64());
		squenceList.add("64bytes���ݼ����ٶ�:"+test.getSpd64()+"KB/s");
	}
	
	private void Squence256(){
		squenceList.add("256bytes���ݼ��ܴ���:"+test.getRec256());
		squenceList.add("256bytes���ݼ����ٶ�:"+test.getSpd256()+"KB/s");
	}
	
	private void Squence1024(){
		squenceList.add("1024bytes���ݼ��ܴ���:"+test.getRec1024());
		squenceList.add("1024bytes���ݼ����ٶ�:"+test.getSpd1024()+"KB/s");
	}
	
	private void Squence8192(){
		squenceList.add("8192bytes���ݼ��ܴ���:"+test.getRec8192());
		squenceList.add("8192bytes���ݼ����ٶ�:"+test.getSpd8192()+"KB/s");
	}
}
