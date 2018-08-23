package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.GregorianCalendar;

import db.exceptions.DBException;

public abstract class Session {
	
	public static String insertSession(int user_id,boolean root) throws DBException
	{ 	
		Connection conn = null;
		PreparedStatement ps = null;
		try 
		{
			String key = "";
		    String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; 
		    for(int x=0;x<256;x++)
		    {
		       int i = (int)Math.floor(Math.random() * 62);
		       key += chars.charAt(i);
		    }
			String sql = "insert into session values(?,?,DATE_ADD(now(),INTERVAL 1 HOUR),?)";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, key);
			ps.setInt(2, user_id);
			ps.setBoolean(3, root);
			ps.executeUpdate();
			return key;
		} 
		catch (Exception e) 
		{
			throw new DBException(e);
		}
		finally
		{
			db.Utils.close(ps);
			db.Utils.close(conn);
		}
	}
	
	
	
	public static boolean exists(String key) throws DBException 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			String sql = "select `key` from session where `key` = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, key);
			if (ps.executeQuery().next()) {
				return true;
			}
			return false;
		} 
		catch (Exception e)
		{
			throw new DBException(e);
		}
		finally 
		{
			db.Utils.close(ps);
			db.Utils.close(conn);
		}
	}
	
	public static void destroySession(String key) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try 
		{
			String sql = "delete from session where `key` = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, key);
			ps.executeUpdate();
		} 
		catch (Exception e) 
		{
			throw new DBException(e);
		}
		finally
		{
			db.Utils.close(ps);
			db.Utils.close(conn);
		}
	}
	
	public static boolean isExpired(String key) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try
		{
			boolean expired = true;
			String sql = "select expiration from session where `key` = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, key);
			result = ps.executeQuery();
			if (result.next()) {
				Date expiration = new Date(result.getTimestamp("expiration").getTime());
				GregorianCalendar calendar = new GregorianCalendar();
				Date now = calendar.getTime();
				if (expiration.compareTo(now) == 1) {
					expired = false;
				}
			}
			return expired;
		} 
		catch (Exception e)
		{
			throw new DBException(e);
		}
		finally
		{
			db.Utils.close(result);
			db.Utils.close(ps);
			db.Utils.close(conn);
		}
	}
	
	public static int associatedUser(String key) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try 
		{
			String sql = "select user_id from session where`key` = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, key);
			result = ps.executeQuery();
			result.next();
			int user_id =  result.getInt("user_id");
			return user_id;
		} 
		catch (Exception e)
		{
			throw new DBException(e);
		}
		finally
		{
			db.Utils.close(result);
			db.Utils.close(ps);
			db.Utils.close(conn);
		}
	}
	public static String associatedKey(int user_id) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try 
		{
			String sql = "select `key` from session where user_id = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, user_id);
			result = ps.executeQuery();
			String key = null;
			if (result.next()) {
				key = result.getString("key");
			}
			return key;
		} 
		catch (Exception e)
		{
			throw new DBException(e);
		}
		finally 
		{
			db.Utils.close(result);
			db.Utils.close(ps);
			db.Utils.close(conn);
		}
	}
	
	public static void keepAlive(String key) throws DBException
	{
		Connection conx = null;
		PreparedStatement ps = null;
		try 
		{
			String sql = "update session set expiration = DATE_ADD(now(),INTERVAL 1 HOUR) where `key` = ?";
			conx = db.Database.getMySQLConnection();
			ps = conx.prepareStatement(sql);
			ps.setString(1,key);
			ps.executeUpdate();
		}
		catch(Exception e)
		{
			throw new DBException(e);
		}
		finally 
		{
			db.Utils.close(ps);
			db.Utils.close(conx);
		}
	}

}
