package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;

import beans.NotificationBean;
import db.exceptions.DBException;

public abstract class Notification {

	public static void product(int from,int to,String type) throws DBException
	{
		Connection conx = null;
		PreparedStatement ps = null;
		try 
		{
			String sql = "insert into notif values(?,?,?)";
			conx = db.Database.getMySQLConnection();
			ps =conx.prepareStatement(sql);
			ps.setInt(1,from);
			ps.setInt(2,to);
			ps.setString(3,type);
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
	
	public static void consume(int to) throws DBException
	{
		Connection conx = null;
		PreparedStatement ps = null;
		try 
		{
			String sql = "delete from notif where to_user = ? and type in ('accept','refuse') ";
			conx = db.Database.getMySQLConnection();
			ps = conx.prepareStatement(sql);
			ps.setInt(1,to);
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
	
	public static void consume(int from,int to,String type) throws DBException
	{
		Connection conx = null;
		PreparedStatement ps = null;
		try 
		{
			String sql = "delete from notif where from_user = ? and to_user = ? and type = ?";
			conx = db.Database.getMySQLConnection();
			ps = conx.prepareStatement(sql);
			ps.setInt(1,from);
			ps.setInt(2,to);
			ps.setString(3,type);
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
	
	public static JSONArray read(int to) throws DBException
	{
		Connection conx = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try 
		{
			String sql = "select * from notif where to_user = ?";
			conx = db.Database.getMySQLConnection();
			ps = conx.prepareStatement(sql);
			ps.setInt(1,to);
			result = ps.executeQuery();
			JSONArray toRet = new JSONArray();
			while(result.next())
			{
				NotificationBean notif = new NotificationBean();
				notif.setFrom(result.getInt("from_user"));
				notif.setTo(result.getInt("to_user"));
				notif.setType(result.getString("type"));
				notif.setLogin(db.User.getUserName(notif.getFrom()));
				toRet.put(notif.toJSON());
			}
			return toRet;
		}
		catch(Exception e)
		{
			throw new DBException(e);
		}
		finally 
		{
			db.Utils.close(result);
			db.Utils.close(ps);
			db.Utils.close(conx);
		}
	}
	
	public static boolean exists(int from,int to) throws DBException
	{
		Connection conx = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try
		{
			String sql = "select * from notif where to_user = ? and from_user = ?";
			conx = db.Database.getMySQLConnection();
			ps = conx.prepareStatement(sql);
			ps.setInt(1,to);
			ps.setInt(2,from);
			result = ps.executeQuery();
			boolean exist = false;
			if(result.next())
			{
				exist = true;
			}
			return exist;
		}
		catch(Exception e)
		{
			throw new DBException(e);
		}
		finally
		{
			db.Utils.close(result);
			db.Utils.close(ps);
			db.Utils.close(conx);
		}
	}

}
