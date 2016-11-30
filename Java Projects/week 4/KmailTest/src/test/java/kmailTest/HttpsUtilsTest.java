package kmailTest;

import java.util.List;

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
		httpsUtils.getresponse(url1);
		String s= httpsUtils.getresponse(url2);
		String js=JsonUtils.getJsonObject(s);
		List<MailBean> l=JsonUtils.getMails(js);
		System.out.println(l.size());
		MailBean m1=l.get(0);
		System.out.println(m1.toString());
		for(MailBean m:l){
			System.out.println(m.toString());
		}
		
	}
	
	

}
