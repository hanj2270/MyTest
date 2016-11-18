package xml;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import main.BlobBean;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;



/** 
* @ClassName: XmlHelper 
* @Description: Xml操作类,完成xml的读写
* @author hanjian  
*/
public class XmlHelper {
	public final static String ROOTELEMENT="BlobXML";
	public final static String RECORDELEMENT="record";
	public final static String ALIAS="alias";
	public final static String PASSWORD="password";
	public final static String BLOB="Blob";
	public final static String ENCRPTEDBLOB="EncryptedBlob";
	
	
	private String path;
	

	/**
	 * @param path 设定xml文件路径,构造xmlHelper对象
	 */
	public XmlHelper(String path) {
		super();
		this.path = path;
	}


	/** 
	* @Description: 从指定路径读取xml文件,将文件中的内容写入BlobMap
	* @param BlobMap 内存中的数据容器
	 * @throws DocumentException 
	*/
	@SuppressWarnings("unchecked")
	public void readXML(HashMap<String, BlobBean> BlobMap) throws DocumentException {
		  SAXReader saxReader = new SAXReader();// 获取读取xml的对象。
		 
		   Document document = saxReader.read(path);// 得到xml所在位置。然后开始读取。并将数据放入doc中
		   Element root_el = document.getRootElement();					    // 获取xml的根节点。
		   Iterator<Element> it = root_el.elementIterator();
		   while (it.hasNext()) {							
			  Element record_el = (Element) it.next();					// 遍历子节点,读出每一条记录
			  String alias=record_el.elementTextTrim(ALIAS);
			  String password=record_el.elementTextTrim(PASSWORD);
			  String blob=record_el.elementTextTrim(BLOB);
			  String encrptedBlob=record_el.elementTextTrim(ENCRPTEDBLOB);
			  BlobBean blobBean=new BlobBean(alias, password, blob, encrptedBlob);
			  BlobMap.put(alias, blobBean);								// 将记录存入hashmap中
			  }
	}
	
	
	/** 
	* @Description: 将目前内存中BlobMap中的数据存入指定路径的xml文件中
	* @param BlobMap 内存中的数据容器
	 * @throws Exception 
	*/
	public void writeXML(HashMap<String, BlobBean> BlobMap) throws Exception{
			Document document = DocumentHelper.createDocument();
			document.setXMLEncoding("UTF-8");
			Element root_el = document.addElement(ROOTELEMENT);
			for(Map.Entry<String, BlobBean> record:BlobMap.entrySet()){   //遍历map
				Element record_el=root_el.addElement(RECORDELEMENT);
				BlobBean blobBean=record.getValue();                      //获得每条记录的BlobBean实体
				record_el.addElement(ALIAS).addText(blobBean.getAlias()); //写入四条属性
				record_el.addElement(PASSWORD).addText(blobBean.getPassword());
				record_el.addElement(BLOB).addText(blobBean.getBlob());
				record_el.addElement(ENCRPTEDBLOB).addText(blobBean.getEncrptedBlob());
			}
			FileOutputStream fos = new FileOutputStream(path);
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
			OutputFormat of = new OutputFormat();                        //设置输出格式
		    of.setEncoding("UTF-8");  
		    of.setIndent(true);  
		    of.setIndent("    ");  
		    of.setNewlines(true);  
		    XMLWriter writer = new XMLWriter(osw, of);  
		    writer.write(document);  
		    writer.close(); 
		
	}
	
	
	
	
}
