package pkcs7Sign;

import ioUtils.CertReadUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;
import org.bouncycastle.mail.smime.SMIMEUtil;



public class MailUtils {
	
	@SuppressWarnings("deprecation")
	public static byte[] Create(String data,String certPath,String password) throws Exception{

			Security.addProvider(new BouncyCastleProvider());
			//获取证书
			KeyStore keyStore=CertReadUtils.getKsformPfx(certPath, password);
			String keyalias=CertReadUtils.getKeyAlias(keyStore);
			X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyalias);
			MimeBodyPart msg = new MimeBodyPart();
			msg.setText(data);
			//利用SMIMEEnvelopedGenerator进行构造,添加接收者证书
			SMIMEEnvelopedGenerator  gen = new SMIMEEnvelopedGenerator();
			gen.addKeyTransRecipient(cert);
			//设置对称加密的加密方式,自动生成密钥
			MimeBodyPart mp = gen.generate(msg, SMIMEEnvelopedGenerator.AES256_CBC, "BC");
			Properties props = System.getProperties();
	        Session session = Session.getDefaultInstance(props, null);
	        //设置收发地址
	        Address fromUser = new InternetAddress("test@test.com");
	        Address toUser = new InternetAddress("test2@test.com");
	        MimeMessage body = new MimeMessage(session);
	        body.setFrom(fromUser);
	        body.setRecipient(Message.RecipientType.TO, toUser);
	        body.setSubject("example encrypted message");
	        body.setContent(mp.getContent(), mp.getContentType());
	        body.saveChanges();
	        ByteArrayOutputStream byto=new ByteArrayOutputStream();
	        body.writeTo(byto);
	        return byto.toByteArray();
		
	}
	
	public static String read(byte[] mail,String certPath,String password) throws Exception{
		//获取私钥用以解密对称加密密钥
		KeyStore keyStore=CertReadUtils.getKsformPfx(certPath, password);
		PrivateKey privKey=CertReadUtils.getPriKeyFromKS(keyStore, password);
		String keyalias=CertReadUtils.getKeyAlias(keyStore);
		X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyalias);
		//用证书信息指定接受者
		RecipientId recId = new JceKeyTransRecipientId(cert);
		Properties props = System.getProperties();
	    Session session = Session.getDefaultInstance(props, null);
	    //对获得封装
	    MimeMessage msg = new MimeMessage(session,new ByteArrayInputStream(mail));
	    SMIMEEnveloped m = new SMIMEEnveloped(msg);
	    //从相应接收者信息中解密对称加密密钥并最终还原为原文
	    RecipientInformationStore recipients = m.getRecipientInfos();
	    RecipientInformation recipient = recipients.get(recId);
	    MimeBodyPart res = SMIMEUtil.toMimeBodyPart(recipient.getContent(new JceKeyTransEnvelopedRecipient(privKey).setProvider("BC")));

	    return res.getContent().toString();
	}

}
