package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import beans.UserBean;
import db.exceptions.DBException;

public abstract class User {
	
	
	public static void add(UserBean user) throws DBException
	{
		Connection conn = null;
		PreparedStatement  statement = null;
		try
		{
			String sql = "insert into user(username,password,nom,prenom) values(?,SHA2(?,256),?,?)";
			conn = db.Database.getMySQLConnection();
			statement = conn.prepareStatement(sql);
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getNom());
			statement.setString(4, user.getPrenom());
			statement.executeUpdate();
		} 
		catch (Exception e)
		{
			throw new DBException(e);
		}
		finally 
		{
			db.Utils.close(statement);
			db.Utils.close(conn);
		}
	}
	
	public static boolean isCorrect(String username,String password) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			boolean correct = false;
			String sql = "select user_id from user where username = ? and password = SHA2(?,256) ";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet result = ps.executeQuery();
			if (result.next()) {
				correct = true;
			}
			return correct;
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
	
	public static boolean exists(String username) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try 
		{
			boolean found = false;
			String sql = "select user_id from user where username = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			result = ps.executeQuery();
			if (result.next()) {
				found = true;
			}
			return found;
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
	
	public static boolean exists(String nom,String prenom) throws DBException 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try 
		{
			boolean found = false;
			String sql = "select user_id from user where nom=? and prenom=?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, nom);
			ps.setString(2, prenom);
			result = ps.executeQuery();
			if (result.next()) {
				found = true;
			}
			return found;
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
	
	public static int getUserID(String username) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try 
		{
			int user_id = 0;
			String sql = "select user_id from user where username = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			result = ps.executeQuery();
			if (result.next()) {
				user_id = result.getInt(1);
			}
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
	
	public static String getUserName(int user_id) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try
		{
			String username = null;
			String sql = "select username from user where user_id = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, user_id);
			result = ps.executeQuery();
			if(result.next())
			{
				username = result.getString("username");
			}
			return username;
		}
		catch(Exception e)
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
	
	public static void updateUserInfos(int user_id,String nom,String prenom) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			String sql = "update user set nom = ? , prenom = ? where user_id = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, nom);
			ps.setString(2, prenom);
			ps.setInt(3, user_id);
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
	
	public static void updatePassword(int user_id,String password) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			String sql = "update user set password = SHA2(?,256) where user_id = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, password);
			ps.setInt(2, user_id);
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
}
