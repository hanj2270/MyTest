package main;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

	

public class MainTest {
	public static String pfx_path="./src/test/resources/hanjian1_pfx.pfx";
	public static String crt_path="./src/test/resources/hanjian1_crt.cer";
	public static String password="123456";
	public static String inputpath="./src/test/resources/testData.txt";
	public static String outputpath="./src/test/resources/testWrite.txt";

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
	public void testMain() {
		Main.inputFile(inputpath);
		Main.file(pfx_path,password);
		Main.sign();
		Main.verify();
	}

}
