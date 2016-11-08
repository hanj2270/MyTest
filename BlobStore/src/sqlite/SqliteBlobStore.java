package sqlite;

import java.sql.SQLException;

import blobStore.IBlobStore;

public class SqliteBlobStore implements IBlobStore {

	private String storePath;
	private MySqliteHelper mySqliteHelper;
	private final static String alias="alias";
	private final static String password="password";
	private final static String Blob="Blob";
	private final static String EncryptedBlob="EncryptedBlob";
	
	
	SqliteBlobStore(String storePath){
		this.storePath=storePath;
	}
	
	public String getType() {
		return "sqlite";
	}

	public int load() {
		try {
			mySqliteHelper=new MySqliteHelper(storePath);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}

	public int save() {
		try {
			mySqliteHelper.Close();
			return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	public int copyTo(IBlobStore other) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int size() {
		try {
			return mySqliteHelper.count();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		
	}

	public String[] listAliases() {
		try {
			return mySqliteHelper.queryall();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public boolean containsAlias(String alias) {
		try {
			if(mySqliteHelper.query("*", SqliteBlobStore.alias, alias)!=null){
				return true;
			}else{
				return false;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean isEncrypted(String alias) {
		try {
			if(mySqliteHelper.query(EncryptedBlob, SqliteBlobStore.alias, alias)!=null){
				return true;
			}else{
				return false;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public String findAlias(byte[] value) {
		try {
			String stringValue=new String(value);	
			return mySqliteHelper.query(alias, Blob, stringValue);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int setBlob(String alias, String value) {
		try {
			int i=0;
			if (containsAlias(alias)){
				i=mySqliteHelper.update(Blob, alias, value);
			}else{
				i=mySqliteHelper.insert(alias, null, value, null);
			}
			return i;
		} catch (Exception e) {
			return 0;
		}
		
		
	}

	public int setBlob(String alias, byte[] value) {
		try {
			int i=0;
			String stringValue=new String(value);
			if (containsAlias(alias)){	
				i=mySqliteHelper.update(Blob, alias, stringValue);
			}else{
				i=mySqliteHelper.insert(alias, null, stringValue, null);
			}
			return i;
		} catch (Exception e) {
			return 0;
		}
	}

	public byte[] getBlob(String alias) {
		try {
			String blob=mySqliteHelper.query(Blob, SqliteBlobStore.alias, alias);
			return blob.getBytes();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public String getBlobAsString(String alias) {
		try {
			return mySqliteHelper.query(Blob, SqliteBlobStore.alias, alias);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
/*
 * (non-Javadoc)需要补充加密逻辑
 * @see blobStore.IBlobStore#setEncryptedBlob(java.lang.String, java.lang.String, java.lang.String)
 */
	public int setEncryptedBlob(String alias, String entryPassword, String value) {
		
		try {
			if(containsAlias(alias)){
				mySqliteHelper.update(alias,EncryptedBlob, value);
				mySqliteHelper.update(alias, password, entryPassword);
				return 1;
			}else{
				mySqliteHelper.insert(alias, entryPassword, value, value);
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
			
		
	}

	public int setEncryptedBlob(String alias, String entryPassword, byte[] value) {
		String stringValueString=new String(value);
		try {
			if(containsAlias(alias)){
				mySqliteHelper.update(alias,EncryptedBlob, stringValueString);
				mySqliteHelper.update(alias, password, entryPassword);
				return 1;
			}else{
				mySqliteHelper.insert(alias, entryPassword, stringValueString, stringValueString);
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public byte[] getEncryptedBlob(String alias, String entryPassword) {
		try {
			if(containsAlias(alias)&&
					mySqliteHelper.query(EncryptedBlob, SqliteBlobStore.alias, alias)!=null){
				String encrptedBlob= mySqliteHelper.query(EncryptedBlob, SqliteBlobStore.alias, alias);
				return encrptedBlob.getBytes();
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getEncryptedBlobAsString(String alias, String entryPassword) {
		try {
			if(containsAlias(alias)&&
					mySqliteHelper.query(EncryptedBlob, SqliteBlobStore.alias, alias)!=null){
				String encrptedBlob= mySqliteHelper.query(EncryptedBlob, SqliteBlobStore.alias, alias);
				return encrptedBlob;
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public int deleteBlob(String alias) {
		try {
			mySqliteHelper.delete(alias);
			return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	public int clearAll() {
		try {
			mySqliteHelper.drop();
			return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}

}
