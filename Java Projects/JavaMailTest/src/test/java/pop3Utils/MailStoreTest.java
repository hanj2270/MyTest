package pop3Utils;

import static org.junit.Assert.*;

import javax.mail.Store;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MailStoreTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testStore() throws Exception {
		MailStore.store();
	}

}
