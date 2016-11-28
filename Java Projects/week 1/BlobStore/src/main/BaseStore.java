package main;

import java.util.HashMap;
import java.util.Map;

/** 
* @ClassName: BaseStore 
* @Description: BlobStore基类，持有一个HashMap作为内存中数据存储容器
* 实现除读写数据库和getType之外的其余所有操作逻辑,数据库读写相关save\load\gettype三个方法由子类实现
* @author hanjian  
*/
public abstract class BaseStore implements IBlobStore {
	
	protected static HashMap<String, BlobBean> MyBlobMap=new HashMap<String, BlobBean>();
	//内存中储存记录的容器,用alias作为key进行索引
	
	
	public int copyTo(IBlobStore other) {
		other.setHashMap(MyBlobMap);      //将数据拷贝到另一个IBlobStore对象中
		return 0;
	}

	public int size() {
	    return MyBlobMap.size();
		
	}

	public String[] listAliases() {
		String[] listAliases=new String[MyBlobMap.keySet().size()];		//得到map中的所有名称，并转化为string数组
		MyBlobMap.keySet().toArray(listAliases);
		return listAliases;
	}

	public boolean containsAlias(String alias) {
		if(MyBlobMap.containsKey(alias)){
			return true;
		}else{
		return false;
		}
	}

	public boolean isEncrypted(String alias) {
		//map中包含了这条记录且密文不为空，说明已加密
		if(MyBlobMap.containsKey(alias)&&MyBlobMap.get(alias).getEncrptedBlob()!=null){
			return true;
		}else{
			return false;
		}
	}

	public String findAlias(byte[] value) {
		String stringValue=new String(value);
		for(Map.Entry<String, BlobBean> e:MyBlobMap.entrySet()){
			if(e.getValue().getBlob().equals(stringValue)){
				return e.getValue().getAlias();
			}
		}
		System.out.println("没有相应记录");
		return null;
	}

	public int setBlob(String alias, String value) {
		if(containsAlias(alias)){                               //如果有对应记录,更新数据
			BlobBean blobBean=MyBlobMap.get(alias);
			blobBean.setBlob(value);
			MyBlobMap.put(alias, blobBean);
			return 1;
		}else{                                                 //如果没有对应记录,插入数据
			BlobBean blobBean=new BlobBean(alias, null, value, null);
			MyBlobMap.put(alias, blobBean);
			return 1;
		}
		
	}

	public int setBlob(String alias, byte[] value) {
		String stringValue=new String(value);
		if(containsAlias(alias)){								//如果有对应记录,更新数据
			BlobBean blobBean=MyBlobMap.get(alias);
			blobBean.setBlob(stringValue);
			MyBlobMap.put(alias, blobBean);
			return 1;
		}else{													//如果没有对应记录,插入数据
			BlobBean blobBean=new BlobBean(alias, null, stringValue, null);
			MyBlobMap.put(alias, blobBean);
			return 1;
		}
	}

	public byte[] getBlob(String alias) {
		if(containsAlias(alias)){								//检查是否有对应记录
			String blob=MyBlobMap.get(alias).getBlob();
			return blob.getBytes();
		}else{
			return null;
		}
	}

	public String getBlobAsString(String alias) {
		if(containsAlias(alias)){								//检查是否有对应记录
			String blob=MyBlobMap.get(alias).getBlob();
			return blob;
		}else{
			return null;
		}
	}

	public int setEncryptedBlob(String alias, String entryPassword, String value) {
		String encrptedBlob=encrption.EncrptionUtils.encrpt(entryPassword,value);  //计算密文
		BlobBean blobBean=new BlobBean(alias, entryPassword, value, encrptedBlob); //存入map
		MyBlobMap.put(alias, blobBean);
		return 1;
	}

	public int setEncryptedBlob(String alias, String entryPassword, byte[] value) {
		String stringValue=new String(value);
		String encrptedBlob=encrption.EncrptionUtils.encrpt(entryPassword,stringValue); //计算密文
		BlobBean blobBean=new BlobBean(alias, entryPassword, stringValue, encrptedBlob);//存入map
		MyBlobMap.put(alias, blobBean);
		return 1;
	}

	public byte[] getEncryptedBlob(String alias, String entryPassword) {
		if(containsAlias(alias)&&MyBlobMap.get(alias).getEncrptedBlob()!=null){	//有记录有密文直接返回
			String encrptedBlob=MyBlobMap.get(alias).getEncrptedBlob();
			return encrptedBlob.getBytes();
		}else if(containsAlias(alias)&&MyBlobMap.get(alias).getEncrptedBlob()==null){ //有记录无密文进行计算
			String encrptedBlob=encrption.EncrptionUtils.encrpt(entryPassword, MyBlobMap.get(alias).getBlob());
			return encrptedBlob.getBytes();
		}else{
			return null;              //无记录返回空
		}
	}

	public String getEncryptedBlobAsString(String alias, String entryPassword) {
		if(containsAlias(alias)&&MyBlobMap.get(alias).getEncrptedBlob()!=null){	//有记录有密文直接返回
			String encrptedBlob=MyBlobMap.get(alias).getEncrptedBlob();
			return encrptedBlob;
		}else if(containsAlias(alias)&&MyBlobMap.get(alias).getEncrptedBlob()==null){ //有记录无密文进行计算
			String encrptedBlob=encrption.EncrptionUtils.encrpt(entryPassword, MyBlobMap.get(alias).getBlob());
			return encrptedBlob;
		}else{
			return null;              //无记录返回空
		}
	}

	public int deleteBlob(String alias) {
		if(containsAlias(alias)){								//检查是否有对应记录
			MyBlobMap.remove(alias);
			return 1;
		}else{
			return 0;
		}
	}

	public int clearAll() {
		MyBlobMap.clear();
		return 0;
	}

	public void setHashMap(
			HashMap<String, BlobBean> BlobMap) {
		MyBlobMap=BlobMap;
	}

	
	//由子类实现的方法
	public abstract String getType(); 

	public abstract int load(); 

	public abstract int save(); 

}
