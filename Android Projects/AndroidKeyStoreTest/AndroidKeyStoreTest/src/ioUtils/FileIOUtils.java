package ioUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileIOUtils {
	/** 
	* @Description: 从文件中读取需要签名的原文
	* @param path 输入文件路径
	* @return    设定文件 
	*/
	public static String readFile(String path){
		
		try {
			File file=new File(path);
            if(file.isFile() && file.exists()){ //判断文件是否存在
            	StringBuffer sb=new StringBuffer();
            	FileReader fileReader = new FileReader(file);
            	BufferedReader bufferedReader = new BufferedReader(fileReader);
				String line;
				while((line=bufferedReader.readLine())!=null){
					sb.append(line);
					sb.append("\n");
				}
				bufferedReader.close();
				fileReader.close();
				return sb.toString();
            }else{
            	System.out.println("错误的文件路径");
            }
		}catch (IOException e) {
			e.printStackTrace();
			System.out.println("读取文件失败");
		}
		return null; 
	}
	
	/** 
	* @Description: 将签名写入文件
	* @param path    文件路径
	*/
	public static void writeFile(String path,String signString){
		try {
			File file =new File(path);
			if(file.exists()){
				System.out.println("文件名重复,请重新输入");
			}else{
				file.createNewFile();
				FileWriter fileWritter = new FileWriter(file.getAbsoluteFile(),true);
			    BufferedWriter bufferedWritter = new BufferedWriter(fileWritter);
			    bufferedWritter.write(signString);
			    bufferedWritter.close();

			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("写入文件失败");
		}
	}

}
