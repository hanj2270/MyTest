package xml;

import java.util.HashMap;

import main.BlobBean;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class XmlHelperTest {

	public static XmlHelper myxmlHelper;
	public static HashMap<String, BlobBean> BlobMap=new HashMap<String, BlobBean>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		myxmlHelper=new XmlHelper("./resource/test.xml");
		BlobBean blobBean1 = new BlobBean("test1","key1", "Blob1","encrptedBlob1");
		BlobBean blobBean2 = new BlobBean("test2","key2", "Blob2","encrptedBlob2");
		BlobMap.put("test1", blobBean1);
		BlobMap.put("test2", blobBean2);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}


	@Test
	public void testReadXML() throws Exception {
		myxmlHelper.writeXML(BlobMap);
		myxmlHelper.readXML(BlobMap);
	}

	@Test
	public void testWriteXML() throws Exception {
		myxmlHelper.writeXML(BlobMap);
	}

}
