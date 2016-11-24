package mailUtils;

import ioUtils.CertReadUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEUtil;

public class EmlPaserUtils {
	private final static String PfxPath="./src/main/resources/hanjian2.pfx";
	private final static String password="123456";
	
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
        Address[] froms = msg.getFrom();  
        if (froms != null) {  
            InternetAddress addrFrom = (InternetAddress) froms[0];  
            System.out.println("发件人地址:" + addrFrom.getAddress());  
            System.out.println("发件人显示名:" + addrFrom.getPersonal());  
        }  
        Address[] tos=msg.getAllRecipients();
        if (tos != null) {  
            InternetAddress addrTo = (InternetAddress) tos[0];  
            System.out.println("收件人地址:" + addrTo.getAddress());  
            System.out.println("收件人显示名:" + addrTo.getPersonal());  
        }
        System.out.println("邮件主题:" + msg.getSubject()); 
        System.out.println("类型:" + msg.getContentType()); 
        System.out.println("发送日期:" + msg.getSentDate());
        System.out.println("邮件内容:");
        decrpt((MimeMessage)msg);
          
    }
	
	
	/** 
	* @Description: 将base64decoderSteam转为byte数组
	* @param content base64decoderSteam格式的object
	* @return
	* @throws Exception    设定文件 
	*/
	private static byte[] decodeContent(Object content) throws Exception{
		InputStream is = (InputStream) content;
		BufferedReader br = null;
		String totalLine = new String(), line;
		 
		br = new BufferedReader( new InputStreamReader(is) );
		 
		while( (line = br.readLine()) != null )  
		   totalLine += line + "\r\n";
		 
		byte[] bytes = totalLine.getBytes();
		 
		return bytes;
	}
	
	/** 
	* @Description: 对加密邮件进行解码
	* @param msg
	* @return
	* @throws Exception    设定文件 
	*/
	private static String decrpt(MimeMessage msg) throws Exception{
		//获取接收者的私钥和证书
		Security.addProvider(new BouncyCastleProvider());
		KeyStore keyStore=CertReadUtils.getKsformPfx(PfxPath, password);
		PrivateKey prikey=CertReadUtils.getPriKeyFromKS(keyStore, password);
		String keyAlias=CertReadUtils.getKeyAlias(keyStore);
		X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
		//利用接收者的证书构造接受者信息对象
		RecipientId recId = new JceKeyTransRecipientId(cert);

        SMIMEEnveloped m = new SMIMEEnveloped(msg);
        RecipientInformationStore recipients = m.getRecipientInfos();

        RecipientInformation recipient = recipients.get(recId);
        if (recipient == null) {
             return null;
         }
        MimeBodyPart res = SMIMEUtil.toMimeBodyPart(recipient.getContent(new JceKeyTransEnvelopedRecipient(prikey)));
        String result=decodeContent(res.getContent()).toString();
        getContentText((InputStream) res.getContent());
		return result;
	}
	
	/** 
	* @Description: 从Base64DecodeSteam中获得邮件正文
	* @param in
	* @throws IOException    设定文件 
	*/
	private static void getContentText(InputStream in) throws IOException{
		BufferedReader br=new BufferedReader(new InputStreamReader(in));
		String line;
		while((line = br.readLine())!=null){	
			if(line.contains("Content-Type: text/plain")){
				while(!(line = br.readLine()).contains("----")){
					System.out.println(line);
				}
			}
			
		}
	}
	
	
	

}
