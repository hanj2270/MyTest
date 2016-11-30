package kmailTest;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;

import org.jsoup.Jsoup;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
	

public class JsonUtilsTest {
	private String htmlPath="./src/test/resources/html.txt";
	private String dataPath="./src/test/resources/data.txt";
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testGetJsonObject() throws Exception {
		FileInputStream in =new FileInputStream(htmlPath);
		String html=Jsoup.parse(in,"GBK","").toString();
		System.out.println(JsonUtils.getJsonObject(html));
		in.close();
	}

	@Test
	public void testGetMails() throws Exception {
		FileInputStream in =new FileInputStream(dataPath);
		int size=in.available();
	    byte[] buffer=new byte[size];
	    in.read(buffer);
	    in.close();
	    String str=new String(buffer,"GBK");
	    assertEquals(8, JsonUtils.getMails(str).size());
	}

}
