package xml;

import static org.junit.Assert.*;

import java.util.HashMap;

import main.BlobBean;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class XmlBlobStoreTest {
	public static XmlBlobStore myxmlBlobStore;
	public static HashMap<String, BlobBean> BlobMap=new HashMap<String, BlobBean>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		myxmlBlobStore=new XmlBlobStore("./resource/test.xml");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testGetType() {
		assertEquals("XML", myxmlBlobStore.getType());
	}

	@Test
	public void testLoad() {
		assertEquals(2, myxmlBlobStore.load());
	}

	@Test
	public void testSave() {
		assertEquals(2, myxmlBlobStore.save());
	}


}
