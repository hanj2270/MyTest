package xml;

import main.BaseStore;

import org.dom4j.DocumentException;

/** 
* @ClassName: SqliteBlobStore 
* @Description: 利用XML进行持久化存储的BlobStore实体类
* @author hanjian  
*/
public class XmlBlobStore extends BaseStore{

	private XmlHelper xmlHelper;
	
	//传入文件路径进行构造
	public XmlBlobStore(String path){
		xmlHelper=new XmlHelper(path);
	}
	
	
	//返回BlobStore实体类类型
	public String getType() {
		return "XML";
	}
	
	
	//从xml文件中读取数据
	public int load() {
		try {
			xmlHelper.readXML(MyBlobMap); 
			return MyBlobMap.size();
		} catch (DocumentException e) {
			e.printStackTrace();
			return 0;
		}
		
	}

	
	//将内存中数据写入XMl文件
	public int save() {
		try {
			xmlHelper.writeXML(MyBlobMap);    
			return MyBlobMap.size();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}


}
