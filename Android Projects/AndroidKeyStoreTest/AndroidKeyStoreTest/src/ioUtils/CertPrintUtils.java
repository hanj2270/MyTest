package ioUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Hex;

public class CertPrintUtils {
	public static void printPFX(String path,String password){
		try {
			KeyStore keyStore=CertReadUtils.getKsformPfx(path, password);
			String keyAlias=CertReadUtils.getKeyAlias(keyStore);
			X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
			printInfo(cert);
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void printCRT(String path){
		try {
			InputStream in = new FileInputStream(path);
			CertificateFactory cf = CertificateFactory.getInstance("x509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
			printInfo(cert);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void printInfo(X509Certificate cert) {
		 try {
			System.out.println("<<=============证书的基本信息===============>>");
			 System.out.println("证书版本:	"+cert.getVersion());
			 System.out.println("序列号:     "+cert.getSerialNumber());
			 System.out.println("签发者:     "+cert.getIssuerDN());
			 System.out.println("主体名:	    "+cert.getSubjectDN());
			 System.out.println("开始时间:	"+cert.getNotBefore());
			 System.out.println("结束时间:	"+cert.getNotAfter());
			 System.out.println("签名算法:   "+cert.getSigAlgName());
			 System.out.println("公钥:      "+cert.getPublicKey());
			 System.out.print("签名值:     ");
			 System.out.println(Hex.encodeHex(cert.getSignature()));
			 System.out.println("<<===========证书的扩展信息==============>>");
			 System.out.println("扩展密钥用途:");
			 for (String s:cert.getExtendedKeyUsage()){
				 System.out.print(cert.getExtensionValue(s)+";");
			 }
			 System.out.println("\n"+"是否是根证书:"+cert.getBasicConstraints());
			 System.out.println("其他非关键扩展地址:"+cert.getNonCriticalExtensionOIDs());
		} catch (Exception e) {
			System.out.println("证书解析出错,请检验文件完整性");
			e.printStackTrace();
		}
	}
}
