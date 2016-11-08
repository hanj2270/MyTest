package sqlite;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class MySqliteHelper  {
	
	private static String path=null;
	private final static String TABLENAME="BlobDatabase";
	

	@SuppressWarnings("static-access")
	MySqliteHelper(String path) throws Exception{
		Class.forName("org.sqlite.JDBC");
		this.path=path;
		String sql="create table if not exists "+TABLENAME+"(alias varchar(50) primary key,password varchar(50)," +
				"Blob varchar(2000),EncryptedBlob varchar(2000))";
		executeUpdate(sql);
	}
	
	private static void executeUpdate(String sql) throws SQLException{
		Connection conn =DriverManager.getConnection("jdbc:sqlite:"+path);
		Statement stat = conn.createStatement();
		stat.executeUpdate(sql);
		conn.close();
	}
	
	
	
	public int insert(String alias,String password,String Blob,String EncryptedBlob ) throws SQLException, UnsupportedEncodingException{  
		String sql="INSERT INTO "+TABLENAME+"(alias,password,Blob,EncryptedBlob) VALUES ('"+alias+"','"+password+"','"+
				Blob+"','"+EncryptedBlob+"')"; 
		executeUpdate(sql);
		return 1;
    } 
	
	public int update(String column,String alias,String value) throws SQLException{  
		String sql=" UPDATE "+TABLENAME+" SET "+column+"='"+value+"' WHERE alias='"+alias+"'";
		executeUpdate(sql);
        return 1;
    } 

	public String[] queryall() throws SQLException{
		Connection conn =DriverManager.getConnection("jdbc:sqlite:"+path);
		Statement stat = conn.createStatement();
		List<String> list=new ArrayList<String>();  
		String sql="SELECT alias FROM "+TABLENAME;  
        ResultSet rs = stat.executeQuery(sql);
        while(rs.next())  
        {  
          list.add(rs.getString("alias")); 
        }
        rs.close();
        conn.close();
        String[] strings=new String[list.size()];
        return list.toArray(strings);
        
	}
	

	public String query(String target,String column,String value) throws SQLException{
		Connection conn =DriverManager.getConnection("jdbc:sqlite:"+path);
		Statement stat = conn.createStatement();
		String sql="SELECT "+target+" FROM "+TABLENAME+" WHERE "+column+"='"+value+"'";
		ResultSet rs =stat.executeQuery(sql);
		String result=rs.getString(target);
		rs.close();
        conn.close();
		return result;
	}
	
	public void delete(String alias) throws SQLException{
		String sql="DELETE FROM "+TABLENAME+" WHERE alias='"+alias+"'";
		executeUpdate(sql);
	}
	public void drop() throws SQLException{
		String sql="drop table if exists "+TABLENAME;
		executeUpdate(sql);
	}
	
	public int count() throws SQLException{
		Connection conn =DriverManager.getConnection("jdbc:sqlite:"+path);
		Statement stat = conn.createStatement();
		String sql="select count(*) as numb from "+TABLENAME;
		ResultSet rs =stat.executeQuery(sql);
		int numb=rs.getInt("numb");
		rs.close();
        conn.close();
		return numb;
	}
	
	void Close() throws SQLException{
		
	}

}
