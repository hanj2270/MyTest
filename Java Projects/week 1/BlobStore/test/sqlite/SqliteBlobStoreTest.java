package sqlite;

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import xml.XmlBlobStore;

public class SqliteBlobStoreTest {
	public static SqliteBlobStore mySqliteBlobStore;
	public static SqliteHelper mySqliteHelper;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mySqliteHelper=new SqliteHelper("./resource/test.db");
		mySqliteHelper.insert("test1","key1", "Blob1","encrptedBlob1");
		mySqliteHelper.insert("test2","key2", "Blob2","encrptedBlob2");
		mySqliteBlobStore=new SqliteBlobStore("./resource/test.db");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		mySqliteHelper.drop();
	}

	@Test
	public void testGetType() {
		System.out.println("type is:"+mySqliteBlobStore.getType());
	}

	@Test
	public void testLoad() {
		mySqliteBlobStore.load();
	}

	@Test
	public void testSave() {
		mySqliteBlobStore.load();
		mySqliteBlobStore.save();
	}

	@Test
	public void testSqliteBlobStore() throws Exception {
		mySqliteHelper=new SqliteHelper("./resource/test.db");
	}

	@Test
	public void testCopyTo() {
		XmlBlobStore xmlBlobStore=new XmlBlobStore("./resource/test.xml");
		mySqliteBlobStore.copyTo(xmlBlobStore);
	}

	@Test
	public void testSize() {
		mySqliteBlobStore.load();
		System.out.println("已读入数据："+mySqliteBlobStore.size());
	}

	@Test
	public void testListAliases() {
		mySqliteBlobStore.load();
		System.out.println("alias 列表:"+Arrays.toString(mySqliteBlobStore.listAliases()));;
	}

	@Test
	public void testContainsAlias() {
		mySqliteBlobStore.load();
		Assert.assertTrue(mySqliteBlobStore.containsAlias("test1"));
	}

	@Test
	public void testIsEncrypted() {
		mySqliteBlobStore.load();
		Assert.assertTrue(mySqliteBlobStore.isEncrypted("test1"));
	}

	@Test
	public void testFindAlias() {
		System.out.println("找到Blob1的原文:"+mySqliteBlobStore.findAlias("Blob1".getBytes()));
	}

	@Test
	public void testSetBlobStringString() {
		mySqliteBlobStore.setBlob("test2", "BLob2Update");
		System.out.println("blob2变更String:"+mySqliteBlobStore.getBlobAsString("test2"));
	}

	@Test
	public void testSetBlobStringByteArray() {
		mySqliteBlobStore.setBlob("test2", "BLob2UpdateByte".getBytes());
		System.out.println("blob2变更Byte:"+mySqliteBlobStore.getBlobAsString("test2"));
	}

	@Test
	public void testGetBlob() {
		System.out.println("获得blob:"+new String(mySqliteBlobStore.getBlob("test2")));
	}

	@Test
	public void testGetBlobAsString() {
		System.out.println("获得blob:"+mySqliteBlobStore.getBlobAsString("test2"));
	}

	@Test
	public void testSetEncryptedBlobStringStringString() {
		mySqliteBlobStore.setEncryptedBlob("test3", "key3", "bolb3");
	}

	@Test
	public void testSetEncryptedBlobStringStringByteArray() {
		mySqliteBlobStore.setEncryptedBlob("test4", "key4", "bolb4".getBytes());
	}

	@Test
	public void testGetEncryptedBlob() {
		System.out.println("test3密文加密:"+new String(mySqliteBlobStore.getEncryptedBlob("test3","key3")));
	}

	@Test
	public void testGetEncryptedBlobAsString() {
		System.out.println("test4密文加密:"+mySqliteBlobStore.getEncryptedBlob("test4","key4"));
	}

	@Test
	public void testDeleteBlob() {
		mySqliteBlobStore.setBlob("test5", "BLob5");
		mySqliteBlobStore.deleteBlob("test5");
		Assert.assertFalse(mySqliteBlobStore.containsAlias("test5"));
	}


}
