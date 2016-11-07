package sqlite;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.sqlite.SQLiteDataSource;



public class MySqliteHelper  {
	
	private static SQLiteDataSource dataSource;  
	private static QueryRunner runner;
	private final static String TABLENAME="BlobDatabase";
	
	MySqliteHelper(String path) throws SQLException{
		dataSource=new SQLiteDataSource();
		dataSource.setUrl("jdbc:sqlite:"+path);
		runner=new QueryRunner(dataSource);
		dataSource.getConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS "+TABLENAME+
				"(alias varchar(50) primary key,password varchar(50)," +  
                "Blob varchar(2000),EncryptedBlob varchar(2000))");
	}
	
	public int insert(String alias,String password,String Blob,String EncryptedBlob ) throws SQLException, UnsupportedEncodingException{  
		String sql="INSERT INTO "+TABLENAME+"(alias,password,Blob,EncryptedBlob) VALUES (?,?,?,?)"; 
		Object params[] = {alias,password,Blob,EncryptedBlob}; 
        int n = runner.update(sql,params); 
        System.out.println("成功插入" + n + "条数据！");   
        return n;
    } 
	
	public int update(String column,String alias,String value) throws SQLException{  
		String sql=" UPDATE "+TABLENAME+" SET "+column+"=? WHERE alias=? ";
        int n = runner.update(sql,value,alias);   
        System.out.println("成功更新" + n + "条数据！");
        return n;
    } 
	
	public Object[] queryall() throws SQLException{
		String sql="SELECT alias FROM "+TABLENAME;  
        Object[] b=runner.query(sql, new ArrayHandler());  
        return b;  
	}
	
	public String query(String target,String column,String value) throws SQLException{
		String sql="SELECT ? FROM "+TABLENAME+" WHERE "+column+"=?";
		Object params[] = {target,value};
		@SuppressWarnings("deprecation")
		Object result=runner.query(sql,params,new ScalarHandler());
		return (String)result;
	}
	
	public void delete(String alias) throws SQLException{
		String sql="DELETE * FROM "+TABLENAME+" WHERE alias=?";
		runner.update(sql,alias);
	}
	public void drop() throws SQLException{
		String sql="DROP TABLE"+TABLENAME;
		runner.update(sql);
	}
	
	public int count() throws SQLException{
		String sql="select count(*) from "+TABLENAME;
		return runner.update(sql);
	}
	
	void Close(){
		dataSource=null;  
        runner=null;  
	}

}
