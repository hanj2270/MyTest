package com.example.server;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.sql.Date;

import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.util.Log;


@SuppressLint("TrulyRandom")
@SuppressWarnings("deprecation")
public class CertGenerate {
	public final static String password="123456";
	public final static String CA_path="123456";
	private final static long aviable_day=365;
	public static PrivateKey prikey=null;
	
	
	/** 
	* @Description: 生成p10请求,以Base64 格式String输出
	* @param subject 证书使用者信息
	* @param password  保存私钥用的密码
	* @return    设定文件 
	*/
	@SuppressLint("TrulyRandom")
	public static String createP10(String subject,String password){
		try {
			Security.addProvider(new BouncyCastleProvider());
			X509Name dn = new X509Name(subject);
			//生成密钥对
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");  
            keyGen.initialize(1024);  
            KeyPair kp = keyGen.generateKeyPair();
            prikey=kp.getPrivate();
            PKCS10CertificationRequest p10 = new PKCS10CertificationRequest("MD5WithRSA",dn,kp.getPublic(),
            		new DERSet(),kp.getPrivate());
            byte[] der=p10.getEncoded();
            //增加pem格式的头尾
            String code ="-----BEGIN CERTIFICATE REQUEST-----\n";  
            code +=  android.util.Base64.encodeToString(der, android.util.Base64.DEFAULT);
            code += "\n-----END CERTIFICATE REQUEST-----\n";  
            return code;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/** 
	* @Description: 构造并签名证书
	* @param P10request
	* @param caPriKey
	* @param caDN
	* @return    设定文件 
	*/
	private static X509Certificate P10response(String P10request,PrivateKey caPriKey,String caDN){
		   try {
			//获取p10请求中的信息
			PKCS10CertificationRequest request=getRequestFromString(P10request);
			CertificationRequestInfo certInfo=request.getCertificationRequestInfo();
			
			//构造证书各项目
			X509V3CertificateGenerator v3CertGen = new X509V3CertificateGenerator();
			v3CertGen.setSerialNumber(BigInteger.valueOf(12345678));
			v3CertGen.setIssuerDN(new X509Principal(caDN));
			//起止日期
			v3CertGen.setNotBefore(new Date(System.currentTimeMillis()));
			v3CertGen.setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * aviable_day)));
			v3CertGen.setSubjectDN(new X509Principal(certInfo.getSubject()));
			v3CertGen.setPublicKey(request.getPublicKey());
			v3CertGen.setSignatureAlgorithm("MD5WithRSA");

			// 用CA的私钥对证书进行签名
			X509Certificate Usercert = v3CertGen.generate(caPriKey,"BC");
			return Usercert;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("生成并签名证书失败");
			return null;
		}
	}

	/** 
	* @Description: 从p10request的String构造请求实体
	* @param P10request
	* @return    设定文件 
	*/
	private static PKCS10CertificationRequest getRequestFromString(String p10request) {
		p10request=p10request.replaceAll("-----BEGIN CERTIFICATE REQUEST-----\n", "");
		p10request=p10request.replaceAll("\n-----END CERTIFICATE REQUEST-----\n", "");
		PKCS10CertificationRequest request=new PKCS10CertificationRequest(Base64.decode(p10request,Base64.DEFAULT));
		return request;
	}

	/** 
	* @Description: 构造签名证书，并将生成证书存入证书文件
	* @param P10request String格式的P10请求
	* @param CACertPath CA证书的路径
	* @param CApassword CA证书的密钥
	* @param userCertPath 存储生成证书的路径
	* @return
	* @throws Exception    设定文件 
	*/
	public static X509Certificate createCert(String P10request,InputStream in,
			String CApassword) {
		try {
			//从ca的地址和password获取CA相关信息
			KeyStore keyStore=SaveUtils.getKsformPfx(in, CApassword);
			String keyalias=SaveUtils.getKeyAlias(keyStore);
			X509Certificate CAcert = (X509Certificate) keyStore.getCertificate(keyalias);
			PrivateKey caPrivateKey=SaveUtils.getPriKeyFromKS(keyStore, CApassword);
			String caDN = CAcert.getIssuerDN().toString();

			X509Certificate userCert=P10response(P10request, caPrivateKey, caDN);
			return userCert;
		} catch (KeyStoreException e) {
			Log.d("test","不能从文件中获得CA证书");
			e.printStackTrace();
		} catch (Exception e) {
			Log.d("test","生成证书存储失败");
			e.printStackTrace();
		}        
		return null;
	}
	
	

	

}
