package main;

/** 
* @ClassName: BlobBean 
* @Description: Blob的java实体类,包括四个属性 
* @author hanjian  
*/
public class BlobBean {
	
	private String alias;//名称
	private String password;//密钥
	private String Blob;//明文
	private String encrptedBlob;//密文
	
	
	
	
	
	public BlobBean(String alias, String password, String blob,
			String encrptedBlob) {
		this.alias = alias;
		this.password = password;
		this.Blob = blob;
		this.encrptedBlob = encrptedBlob;
	}
	
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getBlob() {
		return Blob;
	}
	public void setBlob(String blob) {
		Blob = blob;
	}
	public String getEncrptedBlob() {
		return encrptedBlob;
	}
	public void setEncrptedBlob(String encrptedBlob) {
		this.encrptedBlob = encrptedBlob;
	}
	
	
	
	

}
