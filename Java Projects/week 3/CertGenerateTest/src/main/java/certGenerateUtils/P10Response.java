package certGenerateUtils;

import ioUtils.CertReadUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;

@SuppressWarnings("deprecation")
public class P10Response {
	/** 
	* @Description: 从p10request的String构造请求实体
	* @param P10request
	* @return    设定文件 
	*/
	private static PKCS10CertificationRequest getRequestFromString(String P10request){
		P10request=P10request.replaceAll("-----BEGIN CERTIFICATE REQUEST-----\n", "");
		P10request=P10request.replaceAll("\n-----END CERTIFICATE REQUEST-----\n", "");
		PKCS10CertificationRequest request=new PKCS10CertificationRequest(Base64.decodeBase64(P10request));
		return request;
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
			v3CertGen.setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365)));
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
	* @Description: 构造签名证书，并将生成证书存入证书文件
	* @param P10request String格式的P10请求
	* @param CACertPath CA证书的路径
	* @param CApassword CA证书的密钥
	* @param userCertPath 存储生成证书的路径
	* @return
	* @throws Exception    设定文件 
	*/
	public static X509Certificate createCert(String P10request,String CACertPath,
			String CApassword,String userCertPath) {
		try {
			//从ca的地址和password获取CA相关信息
			KeyStore keyStore=CertReadUtils.getKsformPfx(CACertPath, CApassword);
			String keyalias=CertReadUtils.getKeyAlias(keyStore);
			X509Certificate CAcert = (X509Certificate) keyStore.getCertificate(keyalias);
			PrivateKey caPrivateKey=CertReadUtils.getPriKeyFromKS(keyStore, CApassword);
			String caDN = CAcert.getIssuerDN().toString();
			
			X509Certificate userCert=P10response(P10request, caPrivateKey, caDN);
			
			FileOutputStream out = new FileOutputStream(userCertPath);
			out.write(userCert.getEncoded());
			out.close();
			return userCert;
		} catch (CertificateEncodingException e) {
			System.out.println("CA证书文件读取失败");
			e.printStackTrace();
		} catch (KeyStoreException e) {
			System.out.println("不能从文件中获得CA证书");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("生成证书存储失败");
			e.printStackTrace();
		}        
		return null;
	}
	
	
	/** 
	* @Description: 把证书存入keystore
	* @param keyalias 证书别名
	* @param cert     x509证书
	* @param keystroePath 密钥库地址
	* @param Password    设定文件 
	*/
	public static void saveToKeyStore(String keyalias,X509Certificate cert,
			String keystroePath,String Password) {
		try {
			File file=new File(keystroePath);
			KeyStore ks=KeyStore.getInstance("jks");
			if(file.exists()){
				FileInputStream in=new FileInputStream(file);
				ks.load(in,Password.toCharArray());
			}else{
				ks.load(null,null);
			}
			ks.setCertificateEntry(keyalias, cert);
			ks.store(new FileOutputStream(file),Password.toCharArray());
		} catch (Exception e) {
			System.out.println("存入keystore失败");
			e.printStackTrace();
		} 
	}
}
