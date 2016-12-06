package kmailTest;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;








public class JsonUtils {
	private final static String JsonTag="input[id=\"mailDataTableJson\"]";
	private final static String JsonElementId="value";
	
	/** 
	* @Description: 从html字符串中解析出json数据块
	* @param html
	* @return    设定文件 
	*/
	public static String getJsonObject(String html){
		String jsonStr=null;
		Document doc=Jsoup.parse(html);
		Elements items=doc.select(JsonTag);
		for(Element e:items){
			jsonStr=e.attr(JsonElementId);
		}
		return jsonStr;
	}
	
	/** 
	* @Description: 将json数据解析为mail队列
	* @param JsonStr
	* @return    设定文件 
	*/
	public static List<MailBean> getMails(String JsonStr){ 
		List<MailBean> lst = new ArrayList<MailBean>();
		JsonArray array = new JsonParser().parse(JsonStr).getAsJsonArray();
		for(final JsonElement elem : array){
			lst.add(new Gson().fromJson(elem, MailBean.class));
			}
		return lst;
	}
	
	
	
}
