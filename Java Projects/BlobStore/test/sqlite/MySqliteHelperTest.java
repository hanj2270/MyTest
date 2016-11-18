package sqlite;

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MySqliteHelperTest {
	public static SqliteHelper mySqliteHelper;
	@BeforeClass
	public static void test(){
		try {
			mySqliteHelper=new SqliteHelper("./resource/test.db");
			mySqliteHelper.insert("test1","key1", "Blob1","encrptedBlob1");
			mySqliteHelper.insert("test3","key3", "Blob3","encrptedBlob3");
			mySqliteHelper.insert("test4","key4", "Blob4","encrptedBlob4");
			mySqliteHelper.insert("test2","key2", "Blob2","encrptedBlob2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		mySqliteHelper.delete("test1");
		mySqliteHelper.delete("test2");
		mySqliteHelper.delete("test3");
		mySqliteHelper.delete("test4");

	}
	
	@Test
	public void testMySqliteHelper() {
		try {
			mySqliteHelper=new SqliteHelper("./resource/test.db");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	


	@Test
	public void testUpdate() throws Exception {
		mySqliteHelper.update("Blob", "test4","Blob1updated");
	}

	@Test
	public void testQueryall() throws Exception {
		String[] strings=mySqliteHelper.queryall();
		System.out.println(Arrays.toString(strings));
	}

	@Test
	public void testQuery() throws Exception {
		String strings=mySqliteHelper.query("Blob","alias","test3");
		System.out.println(strings);
	}

	
	@Test
	public void testCount() throws Exception {
		int i= mySqliteHelper.count();
		System.out.println("已有数据数量:"+i);
	}
	
	
	@Test
	public void testDelete() throws Exception {
		mySqliteHelper.delete("test1");
	}


	



}
