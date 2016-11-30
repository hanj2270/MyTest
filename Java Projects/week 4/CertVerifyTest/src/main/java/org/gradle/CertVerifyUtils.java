package org.gradle;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


public class CertVerifyUtils {

      
	  public static boolean certVerify(X509Certificate Servercert){
		  try {
			 List<X509Certificate> certChain=certChainInit(Servercert);
			 //遍历证书链,检验
			  Principal principalLast=null; 						// 前一级证书签发者
			  for(int i=certChain.size()-1;i>=0;i--){
				  X509Certificate cert=certChain.get(i);
				  Date date=new Date();
				  cert.checkValidity(date);  						//验证证书处于有效期期间
				  Principal principalIssuer=cert.getIssuerDN(); 	//获取证书签发者
				  Principal principalSubject=cert.getSubjectDN();	//获取证书主体,用以检验下一级证书 			  
				  if(principalLast!=null){
					  if(principalIssuer.equals(principalLast)){    //判断证书签发者是为上级ca,并用公钥验签
						  PublicKey publickey=certChain.get(i+1).getPublicKey();
						  cert.verify(publickey);
						  principalLast=principalSubject; 
					  }
				  }
			  }
			  
		
		} catch (SignatureException e) {
			System.out.println("证书链验证失败,请查验证书链完整性");
			return false;
		} catch (Exception e) {
			System.out.println("证书链验证失败");
			return false;
		}
		  System.out.println("服务器证书验证完成");
		  return true;  
	  }
	  
	  /** 
	* @Description: 检验各级证书是否在crl吊销列表中,同时生成证书链
	* @param Servercert
	* @return
	* @throws Exception    设定文件 
	*/
	private static List<X509Certificate> certChainInit(X509Certificate Firstcert) throws Exception{
		  X509Certificate currentCert=Firstcert;
		  HttpUtils myclient=new HttpUtils();
		  CertificateFactory cf=CertificateFactory.getInstance("X.509");
		  List<X509Certificate> certChainList=new ArrayList<X509Certificate>();//储存证书链
		  ArrayList<BigInteger> SerialNumlist=new ArrayList<BigInteger>();     //储存证书序列号,用来做crl验证
		  certChainList.add(currentCert);
		  SerialNumlist.add(currentCert.getSerialNumber());
		  while(URlUtils.getIssuerURI(currentCert)!=null){
			  
			  String url=URlUtils.getIssuerURI(currentCert);
			  myclient.download(url);
			  String filePath=HttpUtils.UrltoFilePath(url);
			  FileInputStream in=new FileInputStream(filePath);
			  currentCert=(X509Certificate) cf.generateCertificate(in);
			  certChainList.add(currentCert);
			  SerialNumlist.add(currentCert.getSerialNumber());
			  //进行吊销验证
			  if(isInCrl(currentCert,cf,myclient,SerialNumlist)){
				  System.out.println("证书已被吊销");
				  return null;  
			  }
		  }
		  return certChainList;
	  }
		 
	
	/** 
	* @Description: 验证证书是否在上级证书的crl中
	* @param currentcert
	* @param nextcert
	* @param cf
	* @param client
	* @return
	* @throws Exception    设定文件 
	*/

	private static boolean isInCrl(X509Certificate currentcert,CertificateFactory cf,
			HttpUtils client,ArrayList<BigInteger> serialNumList) throws Exception{
		if(URlUtils.getCrlURI(currentcert)!=null){
			String url=URlUtils.getIssuerURI(currentcert);
			client.download(url);
			String filePath=HttpUtils.UrltoFilePath(url);
			FileInputStream in=new FileInputStream(filePath);
			try{
				X509CRL X509crl=(X509CRL) cf.generateCRL(in);
				@SuppressWarnings("rawtypes")
				Set setEntries=X509crl.getRevokedCertificates();
				if(setEntries!=null && setEntries.isEmpty()==false){  
					for(Object o:setEntries){
						if(serialNumList.contains(((X509CRLEntry)o).getSerialNumber())){
							return true;
						}
					}
				}
			
			}catch(CRLException e){
				System.out.println("warning-序列号为："+currentcert.getSerialNumber()+"\n"
						+"（主体："+currentcert.getSubjectDN()+"）的证书吊销列表为空");
				return false;
			}
		}
		return false;
	}
	

      
}
      

