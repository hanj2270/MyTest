package sqlite;

import java.util.Arrays;

import org.junit.Test;

public class MySqliteHelperTest {
	public MySqliteHelper mySqliteHelper;
	
	@Test
	public void testMySqliteHelper() {
		try {
			mySqliteHelper=new MySqliteHelper("./resource/test.db");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	


	@Test
	public void testUpdate() throws Exception {
		mySqliteHelper=new MySqliteHelper("./resource/test.db");
		mySqliteHelper.insert("test4","key4", "Blob4","encrptedBlob4");
		mySqliteHelper.update("Blob", "test4","Blob1updated");
	}

	@Test
	public void testQueryall() throws Exception {
		mySqliteHelper=new MySqliteHelper("./resource/test.db");
		String[] strings=mySqliteHelper.queryall();
		System.out.println(Arrays.toString(strings));
	}

	@Test
	public void testQuery() throws Exception {
		mySqliteHelper=new MySqliteHelper("./resource/test.db");
		mySqliteHelper.insert("test3","key3", "Blob3","encrptedBlob3");
		String strings=mySqliteHelper.query("Blob","alias","test3");
		System.out.println(strings);
	}

	
	@Test
	public void testCount() throws Exception {
		mySqliteHelper=new MySqliteHelper("./resource/test.db");
		mySqliteHelper.insert("test1","key1", "Blob1","encrptedBlob1");
		int i= mySqliteHelper.count();
		System.out.println("已有数据数量:"+i);
	}
	
	
	@Test
	public void testDelete() throws Exception {
		mySqliteHelper=new MySqliteHelper("./resource/test.db");
		mySqliteHelper.insert("test2","key2", "Blob2","encrptedBlob2");
		mySqliteHelper.delete("test1");
	}

	@Test
	public void testDrop() throws Exception {
		mySqliteHelper=new MySqliteHelper("./resource/test.db");
//		mySqliteHelper.drop();
	}

	



}
