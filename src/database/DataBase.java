package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 连接MySQL的类
 * @author shy
 *
 */
public class DataBase {
	protected String driverName;
	protected String dbUrl;
	protected Connection cnnt;
    protected Statement statement;
    protected ResultSet resultset;
	
	public static void main(String[] args)
	{
		DataBase db = new DataBase();
		try{
			db.connect();
		}catch (Exception e)
		{
			
		}
		
	}
	
	/**
	 * 连接数据库
	 * <br/>driver: com.mysql.jdbc.Driver
	 * <br/>url: jdbc:mysql://localhost:3306/mychat
	 */
	public void connect()throws SQLException
	{
		driverName ="com.mysql.jdbc.Driver";
		dbUrl = "jdbc:mysql://localhost:3306/mychat";
		  
		try
		    {
		        Class.forName(driverName);
		    }
		    catch(Exception e)
		    {
		    	System.out.println("装载驱动失败");
		    	System.out.println(e.toString());
		    }
		try{
		    cnnt = DriverManager.getConnection(dbUrl,"root","");
		    
		}catch(Exception ex)
		    {
		        System.out.println("建立连接失败");
		    	System.out.println(ex.toString());
		        cnnt = null;
		}
	}
	
	/**
	 * 关闭数据库
	 */
	public void disconnect()
	{
		try{
			if (cnnt != null)
			{
				cnnt.close();
				cnnt = null;
				System.out.println("数据库关闭成功");
			}
	
		}
		catch(Exception e)
		{
			System.out.println("关闭数据库连接失败");
			System.out.println(e.toString());
		}
				
	}
	
	/**
	 * 执行查询语句
	 * @param strsql
	 * @return ResultSet
	 */
	public ResultSet query(String strsql)
	{
		try{
			statement = cnnt.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultset = statement.executeQuery(strsql);
//			statement.close();
		}catch (Exception e)
		{
			System.out.println("执行sql语句失败");
			System.out.println(e.toString());
		}
		finally
		{
//			statement = null;
		}
		return resultset;
	}
	
	/**
	 * 执行更新语句，包含添加、修改、删除等
	 * @param strsql
	 * @return void
	 */
	public void update(String strsql)
	{
		try{
			statement = cnnt.createStatement();
			statement.executeUpdate(strsql);
			statement.close();
		}catch(Exception e)
		{
			
		}finally{
			statement = null;
		}
	}
}
