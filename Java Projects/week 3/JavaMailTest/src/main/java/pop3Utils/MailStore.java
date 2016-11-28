package pop3Utils;

import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;




public class MailStore {
	public static void store() throws Exception{
		  Properties prop = new Properties(); 
		  prop.setProperty("mail.store.protocol", "pop3"); // 协议 
		  prop.setProperty("mail.pop3.port", "110"); // 端口 
		  prop.setProperty("mail.pop3.host", "pop3.163.com"); // pop3服务器 
		  
		  Session session = Session.getInstance(prop); 
		  Store store = session.getStore("pop3"); 
		  store.connect("hjc2270@163.com", "test123456"); 
		  
		  Folder folder = store.getFolder("INBOX"); 
		  folder.open(Folder.READ_WRITE); 
		  Message[] messages = folder.getMessages();
		  MimeMessage msg = (MimeMessage) messages[messages.length-1]; 
		  StringBuffer content = new StringBuffer(30); 
		  getMailTextContent(msg, content); 
		  System.out.println("邮件正文：" + (content.length() > 100 ? content.substring(0,100) + "..." : content)); 
		  System.out.println("------------------第" + msg.getMessageNumber() + "封邮件解析结束-------------------- "); 
		  System.out.println(); 
		  // 5、关闭
		  folder.close(false);
		  store.close();
	}
	
	public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException { 
		//如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断 
		boolean isContainTextAttach = part.getContentType().indexOf("name") > 0; 
		if (part.isMimeType("text/*") && !isContainTextAttach) { 
			content.append(part.getContent().toString()); 
			} else if (part.isMimeType("message/rfc822")) { 
			getMailTextContent((Part)part.getContent(),content); 
			} else if (part.isMimeType("multipart/*")) { 
			Multipart multipart = (Multipart) part.getContent(); 
			int partCount = multipart.getCount(); 
			for (int i = 0; i < partCount; i++) { 
				BodyPart bodyPart = multipart.getBodyPart(i); 
				getMailTextContent(bodyPart,content); 
				} 
			} 
		} 

}
