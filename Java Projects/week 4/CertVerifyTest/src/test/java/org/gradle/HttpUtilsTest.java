package org.gradle;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class HttpUtilsTest {
	private final String urlString="http://192.168.1.8:8880/download/KoalCa.cer";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testDownload() {
		HttpUtils myclient=new HttpUtils();
		myclient.download(urlString);
	}

	@Test
	public void testUrltoFilePath() {
		System.out.println(HttpUtils.UrltoFilePath(urlString));
	}

}
