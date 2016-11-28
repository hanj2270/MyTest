package ioUtils;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileIOUtilsTest {

	public static String path="./src/test/resources/testData.txt";
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File file=new File("./src/test/resources/testWrite.txt");
		if(file.exists()){
			file.delete();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
	}

	@Test
	public void testReadFile() {
		System.out.println("读出:"+FileIOUtils.readFile(path));
	}

	@Test
	public void testWriteFile() {
		FileIOUtils.writeFile("./src/test/resources/testWrite.txt", "write test");
		System.out.println("写入:"+FileIOUtils.readFile("./src/test/resources/testWrite.txt"));
	}

}
