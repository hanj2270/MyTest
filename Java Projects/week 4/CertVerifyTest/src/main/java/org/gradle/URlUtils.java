package org.gradle;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import sun.security.x509.AccessDescription;
import sun.security.x509.AuthorityInfoAccessExtension;
import sun.security.x509.CRLDistributionPointsExtension;
import sun.security.x509.DistributionPoint;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.GeneralNames;
import sun.security.x509.URIName;
import sun.security.x509.X509CertImpl;

/** 
* @ClassName: URlUtils 
* @Description: 从证书中解析出证书签发者和crl吊销列表的url地址,用于验证和构造证书链
* @author hanjian  
*/
public class URlUtils {
	
	public static String getCrlURI(X509Certificate cert){
		 try {
			X509CertImpl certImpl=X509CertImpl.toImpl(cert);
			 CRLDistributionPointsExtension crld=certImpl.getCRLDistributionPointsExtension();
			 if(crld==null){
				 return null;
			 }
			 List<DistributionPoint> disPoints=crld.get(CRLDistributionPointsExtension.POINTS);
			 for (DistributionPoint d:disPoints){
				 GeneralNames names= d.getFullName();
				 for(int i=0;i<names.size();i++){
					 GeneralNameInterface name = names.get(i).getName();
					 if (name.getType() == GeneralNameInterface.NAME_URI) {
					     URIName uri = (URIName) name;
				         return uri.getURI().toString();
					  }
				 }
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return null;
	}
 
	

 
	
	@SuppressWarnings("deprecation")
     public static String getIssuerURI(X509Certificate cert) throws CertificateException {
		 X509CertImpl certImpl=X509CertImpl.toImpl(cert);
	     AuthorityInfoAccessExtension aia =certImpl.getAuthorityInfoAccessExtension();
	     if (aia == null) {
	           return null;
	      }
	     List<AccessDescription> descriptions = aia.getAccessDescriptions();
	     for (AccessDescription description : descriptions) {
	    	 if (description.getAccessMethod().equals(AccessDescription.Ad_CAISSUERS_Id)) {
	    		 GeneralName generalName = description.getAccessLocation();
			     if (generalName.getType() == GeneralNameInterface.NAME_URI) {
				     URIName uri = (URIName) generalName.getName();
			         return uri.getURI().toString();
				  }
			  }
	      }
		  return null;
      }
	

}
