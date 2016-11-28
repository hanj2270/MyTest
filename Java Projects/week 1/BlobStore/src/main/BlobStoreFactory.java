package main;


/** 
* @ClassName: BlobStoreFactory 
* @Description: BlobStore工厂类，输入类别和路径,生成不同的BlobStore对象类
* @author hanjian  
*/
public class BlobStoreFactory {
	static IBlobStore createInstance(String type, String storePath) {
       if(type.equals("sqlite")){
    	   return new sqlite.SqliteBlobStore(storePath);
       }else if (type.equals("xml")) {
		   return new xml.XmlBlobStore(storePath);
	}else{
		System.out.println("没有这种格式");
		return null;
	}
    }
}
