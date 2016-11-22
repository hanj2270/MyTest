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
	* @Description: ����p10����,��Base64 ��ʽString���
	* @param subject ֤��ʹ������Ϣ
	* @param password  ����˽Կ�õ�����
	* @return    �趨�ļ� 
	*/
	@SuppressLint("TrulyRandom")
	public static String createP10(String subject,String password){
		try {
			Security.addProvider(new BouncyCastleProvider());
			X509Name dn = new X509Name(subject);
			//������Կ��
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");  
            keyGen.initialize(1024);  
            KeyPair kp = keyGen.generateKeyPair();
            prikey=kp.getPrivate();
            PKCS10CertificationRequest p10 = new PKCS10CertificationRequest("MD5WithRSA",dn,kp.getPublic(),
            		new DERSet(),kp.getPrivate());
            byte[] der=p10.getEncoded();
            //����pem��ʽ��ͷβ
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
	* @Description: ���첢ǩ��֤��
	* @param P10request
	* @param caPriKey
	* @param caDN
	* @return    �趨�ļ� 
	*/
	private static X509Certificate P10response(String P10request,PrivateKey caPriKey,String caDN){
		   try {
			//��ȡp10�����е���Ϣ
			PKCS10CertificationRequest request=getRequestFromString(P10request);
			CertificationRequestInfo certInfo=request.getCertificationRequestInfo();
			
			//����֤�����Ŀ
			X509V3CertificateGenerator v3CertGen = new X509V3CertificateGenerator();
			v3CertGen.setSerialNumber(BigInteger.valueOf(12345678));
			v3CertGen.setIssuerDN(new X509Principal(caDN));
			//��ֹ����
			v3CertGen.setNotBefore(new Date(System.currentTimeMillis()));
			v3CertGen.setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * aviable_day)));
			v3CertGen.setSubjectDN(new X509Principal(certInfo.getSubject()));
			v3CertGen.setPublicKey(request.getPublicKey());
			v3CertGen.setSignatureAlgorithm("MD5WithRSA");

			// ��CA��˽Կ��֤�����ǩ��
			X509Certificate Usercert = v3CertGen.generate(caPriKey,"BC");
			return Usercert;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("���ɲ�ǩ��֤��ʧ��");
			return null;
		}
	}

	/** 
	* @Description: ��p10request��String��������ʵ��
	* @param P10request
	* @return    �趨�ļ� 
	*/
	private static PKCS10CertificationRequest getRequestFromString(String p10request) {
		p10request=p10request.replaceAll("-----BEGIN CERTIFICATE REQUEST-----\n", "");
		p10request=p10request.replaceAll("\n-----END CERTIFICATE REQUEST-----\n", "");
		PKCS10CertificationRequest request=new PKCS10CertificationRequest(Base64.decode(p10request,Base64.DEFAULT));
		return request;
	}

	/** 
	* @Description: ����ǩ��֤�飬��������֤�����֤���ļ�
	* @param P10request String��ʽ��P10����
	* @param CACertPath CA֤���·��
	* @param CApassword CA֤�����Կ
	* @param userCertPath �洢����֤���·��
	* @return
	* @throws Exception    �趨�ļ� 
	*/
	public static X509Certificate createCert(String P10request,InputStream in,
			String CApassword) {
		try {
			//��ca�ĵ�ַ��password��ȡCA�����Ϣ
			KeyStore keyStore=SaveUtils.getKsformPfx(in, CApassword);
			String keyalias=SaveUtils.getKeyAlias(keyStore);
			X509Certificate CAcert = (X509Certificate) keyStore.getCertificate(keyalias);
			PrivateKey caPrivateKey=SaveUtils.getPriKeyFromKS(keyStore, CApassword);
			String caDN = CAcert.getIssuerDN().toString();

			X509Certificate userCert=P10response(P10request, caPrivateKey, caDN);
			return userCert;
		} catch (KeyStoreException e) {
			Log.d("test","���ܴ��ļ��л��CA֤��");
			e.printStackTrace();
		} catch (Exception e) {
			Log.d("test","����֤��洢ʧ��");
			e.printStackTrace();
		}        
		return null;
	}
	
	

	

}
