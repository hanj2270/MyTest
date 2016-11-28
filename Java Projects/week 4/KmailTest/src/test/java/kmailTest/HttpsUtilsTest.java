package kmailTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class HttpsUtilsTest {
	private final static String url1="https://mail.koal.com";
	private final static String url2="https://mail.koal.com/userMail!queryMailList.do?currentFolder.folderId=10";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		HttpsUtils httpsUtils=new HttpsUtils();
		httpsUtils.getresponse(url1,false);
		System.out.println(url2+":JSESSIONID="+httpsUtils.sessionId);
		httpsUtils.getresponse(url2,true);
	}

}
