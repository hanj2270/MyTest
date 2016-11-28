package main;



import ioUtils.CertReadUtils;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;



public class PKCS7SignUtils {

	/** 
	* @Description: pkcs7格式签名
	* @param data   被签名的数据
	* @param Path   PFX证书路径
	* @param password       PFX密钥
	* @return    设定文件 
	*/
	@SuppressWarnings({"rawtypes", "unchecked" })
	public static byte[] sign(String data,String Path,String password){
		try {
			Security.addProvider(new BouncyCastleProvider());
			//获取私钥和证书
			KeyStore keyStore=CertReadUtils.getKsformPfx(Path, password);
			String keyalias=CertReadUtils.getKeyAlias(keyStore);
			X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyalias);
			ArrayList certList = new ArrayList();
			certList.add(cert);
			Store certs = new JcaCertStore(certList);
			PrivateKey privKey=CertReadUtils.getPriKeyFromKS(keyStore, password);
			//构造签名
			Signature signature = Signature.getInstance("MD5WithRSA", "BC");
	        signature.initSign(privKey);
	        signature.update(data.getBytes());
	        CMSTypedData msg = new CMSProcessableByteArray(signature.sign());
	        //CMS生成器
	        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
	        ContentSigner sha1Signer = new JcaContentSignerBuilder("MD5withRSA").setProvider("BC").build(privKey);
	        gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build()).build(sha1Signer, cert));
	        gen.addCertificates(certs);
	        CMSSignedData s = gen.generate(msg,true);
	        //如果选择false或者默认值,会生成不带原文信息的签名,则验签时需要与原文一起验证
			return s.getEncoded(); 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		

	/** 
	* @Description: 验签函数
	* @param signedData
	* @return    设定文件 
	*/
	@SuppressWarnings("rawtypes")
	public static boolean verify(byte[] signedData){
		boolean verifyed = false;
		try { 
			//如果以Base64传递，需要先解码
			   CMSSignedData sign = new CMSSignedData(signedData);              
               Store store = sign.getCertificates(); 
               SignerInformationStore signers = sign.getSignerInfos(); 
               Collection c = signers.getSigners(); 
               Iterator it = c.iterator(); 
               //如果有多个证书,需要依次进行验证
               while (it.hasNext()) { 
                   SignerInformation signer = (SignerInformation)it.next(); 
                   Collection certCollection = store.getMatches(signer.getSID()); 
                   Iterator certIt = certCollection.iterator(); 

                   X509CertificateHolder certHolder = (X509CertificateHolder)certIt.next(); 
                   X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder); 

                   if (signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(cert))) {
                	   verifyed = true; 
                   }else{
                	   verifyed=false;
                   }
               }
		}catch (Exception e) {
        	     verifyed = false;
                 e.printStackTrace();
          }
		if(verifyed){
			System.out.println("验签成功");
		}else{
			System.out.println("验签失败");
		}
		return verifyed;
		}
}
	

	


