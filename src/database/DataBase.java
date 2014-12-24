package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ����MySQL����
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
	 * �������ݿ�
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
		    	System.out.println("װ������ʧ��");
		    	System.out.println(e.toString());
		    }
		try{
		    cnnt = DriverManager.getConnection(dbUrl,"root","");
		    
		}catch(Exception ex)
		    {
		        System.out.println("��������ʧ��");
		    	System.out.println(ex.toString());
		        cnnt = null;
		}
	}
	
	/**
	 * �ر����ݿ�
	 */
	public void disconnect()
	{
		try{
			if (cnnt != null)
			{
				cnnt.close();
				cnnt = null;
				System.out.println("���ݿ�رճɹ�");
			}
	
		}
		catch(Exception e)
		{
			System.out.println("�ر����ݿ�����ʧ��");
			System.out.println(e.toString());
		}
				
	}
	
	/**
	 * ִ�в�ѯ���
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
			System.out.println("ִ��sql���ʧ��");
			System.out.println(e.toString());
		}
		finally
		{
//			statement = null;
		}
		return resultset;
	}
	
	/**
	 * ִ�и�����䣬������ӡ��޸ġ�ɾ����
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
