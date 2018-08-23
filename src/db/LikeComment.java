package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.exceptions.DBException;

public class LikeComment {
	
	public static void like(int user_id,String comment_id) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			String sql = "insert into like_comment values(?,?)";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, user_id);
			ps.setString(2, comment_id);
			ps.executeUpdate();
		} 
		catch (Exception e)
		{
			throw new DBException();
		}
		finally 
		{
			db.Utils.close(ps);
			db.Utils.close(conn);
		}
	}
	
	public static void unlike(int user_id,String comment_id) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			String sql = "delete from like_comment where user_id = ? and comment_id = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, user_id);
			ps.setString(2, comment_id);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			throw new DBException();
		}
		finally 
		{
			db.Utils.close(ps);
			db.Utils.close(conn);
		}
	}
	
	public static void deleteAssociatedLikes(String comment_id) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			String sql = "delete from like_comment where comment_id = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, comment_id);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			throw new DBException();
		}
		finally
		{
			db.Utils.close(ps);
			db.Utils.close(conn);
		}
	}
	
	public static int getNbLike(String comment_id) throws DBException 
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try
		{
			int nbLike = 0;
			String sql = "select count(*) as nblike from like_comment where comment_id = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1,comment_id);
			result = ps.executeQuery();
			if(result.next())
			{
				nbLike = result.getInt("nblike");
			}
			return nbLike;
		}
		catch(Exception e)
		{
			throw new  DBException(e);
		}
		finally 
		{
			db.Utils.close(result);
			db.Utils.close(ps);
			db.Utils.close(conn);
		}
	}
	
	public static boolean hasLiked(int user_id,String comment_id) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps= null;
		ResultSet result = null;
		try
		{
			String sql = "select * from like_comment where user_id = ? and comment_id = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, user_id);
			ps.setString(2, comment_id);
			result = ps.executeQuery();
			if (result.next()) {
				return true;
			}
			return false;
		}
		catch (Exception e)
		{
			throw new DBException();
		}
		finally
		{
			db.Utils.close(result);
			db.Utils.close(ps);
			db.Utils.close(conn);
		}
	}
}
