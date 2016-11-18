package sqlite;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



/** 
* @ClassName: SqliteHelper 
* @Description: sqlite操作类,完成sqlite增删查改逻辑 
* @author hanjian  
*/
public class SqliteHelper  {
	
	private static String path=null;
	private final static String TABLENAME="BlobDatabase";
	

	/**@Description: 构造函数,传入数据库路径,如果没有表单的话自动生成表单
	 * @param path sqlite数据库路径
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public SqliteHelper(String path) throws Exception{
		Class.forName("org.sqlite.JDBC");
		this.path=path;
		String sql="create table if not exists "+TABLENAME+"(alias varchar(50) primary key,password varchar(50)," +
				"Blob varchar(2000),EncryptedBlob varchar(2000))";
		executeUpdate(sql);
	}
	
	/** 
	* @Description: 打开连接,执行sql语句,最终关闭连接.
	* @param sql 需要执行的sql语句
	* @throws SQLException     
	*/
	private static void executeUpdate(String sql) throws SQLException{
		Connection conn =DriverManager.getConnection("jdbc:sqlite:"+path);
		Statement stat = conn.createStatement();
		stat.executeUpdate(sql);
		conn.close();
	}
	
	
	
	/** 
	* @Description: 插入一条数据 
	* @param alias  记录名,作为主键
	* @param password 密钥
	* @param Blob    明文
	* @param EncryptedBlob 密文
	* @return 成功返回1
	* @throws SQLException
	* @throws UnsupportedEncodingException    设定文件 
	*/
	public int insert(String alias,String password,String Blob,String EncryptedBlob ) throws SQLException, UnsupportedEncodingException{  
		String sql="INSERT INTO "+TABLENAME+"(alias,password,Blob,EncryptedBlob) VALUES ('"+alias+"','"+password+"','"+
				Blob+"','"+EncryptedBlob+"')"; 
		executeUpdate(sql);
		return 1;
    } 
	
	/** 
	* @Description: 更新数据 
	* @param column 更新的字段
	* @param alias  记录名称
	* @param value  更新数据内容
	* @return
	* @throws SQLException    
	*/
	public int update(String column,String alias,String value) throws SQLException{  
		String sql=" UPDATE "+TABLENAME+" SET "+column+"='"+value+"' WHERE alias='"+alias+"'";
		executeUpdate(sql);
        return 1;
    } 

	/** 
	* @Description: 获得已有全部数据的名称列表
	* @return       名称String数组
	* @throws SQLException    设定文件 
	*/
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
	

	/** 
	* @Description: 查询字段值
	* @param target 查询目标字段
	* @param column 条件字段
	* @param value  条件值
	* @return
	* @throws SQLException    设定文件 
	*/
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
	
	
	
	/** 
	* @Description: 删除某条名称对应记录 
	* @param alias  记录名称
	* @throws SQLException    设定文件 
	*/
	public void delete(String alias) throws SQLException{
		String sql="DELETE FROM "+TABLENAME+" WHERE alias='"+alias+"'";
		executeUpdate(sql);
	}
	
	
	/** 
	* @Description: 删除整张数据表
	* @throws SQLException    设定文件 
	*/
	public void drop() throws SQLException{
		String sql="drop table if exists "+TABLENAME;
		executeUpdate(sql);
	}
	
	
	
	/** 
	* @Description: 统计记录条数目 
	* @return
	* @throws SQLException    设定文件 
	*/
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
	
	public void Close() throws SQLException{
		
	}

}
