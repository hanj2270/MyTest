package kmailTest;

import java.util.List;

public class Main {
	private final static String url1="https://mail.koal.com";
	private final static String url2="https://mail.koal.com/userMail!queryMailList.do?currentFolder.folderId=10";
	/** 
	 * @Description:  完成ssl双向认证登陆,获取邮箱邮件列表
	 * @param args    设定文件 
	 */
	public static void main(String[] args) {
		HttpsUtils httpsUtils=new HttpsUtils();
		httpsUtils.getresponse(url1);  				//完成ssl认证，并通过登陆获取sessionId
		String html= httpsUtils.getresponse(url2); 	//获得收件箱页面
		String json=JsonUtils.getJsonObject(html);    //获得邮件json数据
		List<MailBean> l=JsonUtils.getMails(json);
		System.out.println("<==============邮件列表==============>");
		int i=1;
		for(MailBean m:l){
			System.out.println("<==============第"+i+"封邮件==============>");
			System.out.println(m.toString());
			i++;
		}
		System.out.println("<==============列表结束==============>");
	}

}
