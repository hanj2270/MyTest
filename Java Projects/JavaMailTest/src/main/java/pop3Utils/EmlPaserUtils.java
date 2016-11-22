package pop3Utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmlPaserUtils {
	
	public static void parserFile(String emlPath) throws Exception {  
        System.out.println(emlPath);  
        Properties props = new Properties();  
        Session session = Session.getDefaultInstance(props, null);  
        InputStream in;  
        in = new FileInputStream(emlPath);  
        Message msg =new MimeMessage(session, in);  ;  
        parseEml(msg);  
    }  
	
	private static void parseEml(Message msg) throws Exception {  
        // 发件人信息  
        Address[] froms = msg.getFrom();  
        if (froms != null) {  
            // System.out.println("发件人信息:" + froms[0]);  
            InternetAddress addr = (InternetAddress) froms[0];  
            System.out.println("发件人地址:" + addr.getAddress());  
            System.out.println("发件人显示名:" + addr.getPersonal());  
        }  
        System.out.println("邮件主题:" + msg.getSubject());  
        // getContent() 是获取包裹内容, Part相当于外包装  
        Object o = msg.getContent();  
        if (o instanceof Multipart) {  
            Multipart multipart = (Multipart) o;  
            parserMultipart(multipart);  
        } else if (o instanceof Part) {  
            Part part = (Part) o;  
            parserPart(part);  
        } else {  
            System.out.println("类型" + msg.getContentType());  
            System.out.println("内容" + msg.getContent());  
        }  
    }  
	
	private static void parserMultipart(Multipart multipart){
	}
	
	private static void parserPart(Part part){
	}

}
