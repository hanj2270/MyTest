package mailUtils;

import mailUtils.EmlPaserUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmlPaserUtilsTest {
	private final static String EML_PATH="./src/test/resources/test.eml";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testParserFile() throws Exception {
		EmlPaserUtils.parserFile(EML_PATH);
	}

}
