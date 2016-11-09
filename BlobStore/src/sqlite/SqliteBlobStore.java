package sqlite;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import main.BaseStore;
import main.BlobBean;




/** 
* @ClassName: SqliteBlobStore 
* @Description: 利用Sqlite进行持久化存储的BlobStore实体类
* @author hanjian  
*/
public class SqliteBlobStore extends BaseStore {


	private SqliteHelper mySqliteHelper;
	
	private final static String ALIAS="alias";
	private final static String PASSWORD="password";
	private final static String BLOB="Blob";
	private final static String ENCRYPTEDBLOB="EncryptedBlob";
	
	//传入文件路径进行构造
	public SqliteBlobStore(String storePath){
		try {
			mySqliteHelper=new SqliteHelper(storePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//返回BlobStore类型
	public String getType() {
		return "sqlite";
	}
	
	//从数据库读取数据
	public int load() {
		try {
			if(mySqliteHelper.count()!=0){//判断是否存在记录
				String[] aliasesList=mySqliteHelper.queryall();//获取全部记录
				for(String key:aliasesList){
					String alias=key;                               //如果存在,依次生成Bean并存入myblobMap
					String password=mySqliteHelper.query(PASSWORD, ALIAS, key);
					String blob=mySqliteHelper.query(BLOB, ALIAS, key);
					String encrptedBlob=mySqliteHelper.query(ENCRYPTEDBLOB, ALIAS, key);
					BlobBean blobBean=new BlobBean(alias, password, blob, encrptedBlob);
					MyBlobMap.put(key, blobBean);
				}
				return 1;
			}else{
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	
	
	//向数据库存入数据
	public int save() {
		try {
			for(Map.Entry<String, BlobBean> e:MyBlobMap.entrySet()){
				String alias=e.getKey();
				if(aliasInSqlite(alias)){                                   //如果记录存在进行更新
					mySqliteHelper.update(PASSWORD, alias, e.getValue().getPassword());
					mySqliteHelper.update(BLOB, alias, e.getValue().getBlob());
					mySqliteHelper.update(ENCRYPTEDBLOB, alias, e.getValue().getEncrptedBlob());
				}else{
					mySqliteHelper.insert(alias, e.getValue().getPassword(), //如果不存在进行插入
							e.getValue().getBlob(), e.getValue().getEncrptedBlob());
				}
			}
			return MyBlobMap.size();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	//检查数据库内是否有该条记录
	private boolean aliasInSqlite(String alias) throws SQLException{
		if( Arrays.asList(mySqliteHelper.queryall()).contains(alias)){
			return true;
		}else{
			return false;
		}
	}

}
