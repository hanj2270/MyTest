package main;

import org.junit.BeforeClass;
import org.junit.Test;

public class BlobStoreFactoryTest {
	public static BlobStoreFactory blobStoreFactory;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		blobStoreFactory=new BlobStoreFactory();
	}


	@Test
	public void test() {
		BlobStoreFactory.createInstance("sqlite", "./resource/test.db");
		BlobStoreFactory.createInstance("xml", "./resource/test.db");
	}

}
