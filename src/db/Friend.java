package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import beans.FriendBean;
import db.exceptions.DBException;

public abstract class Friend {
	
	public static boolean isFriend(int user_id,int friend_id) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try 
		{
			boolean friend = false;
			String sql = "select * from friend where (user_id1 = ? and user_id2 = ?) or (user_id1 = ? and user_id2 = ?)";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1,user_id);
			ps.setInt(2,friend_id);
			ps.setInt(3,friend_id);
			ps.setInt(4,user_id);
			result = ps.executeQuery();
			if(result.next())
			{
				friend = true;
			};
			return friend;
		}
		catch(Exception e)
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
	
	public static boolean haveSendFriendRequest(int user_id1,int user_id2) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try 
		{
			boolean havesend = false;
			String sql = "select * from friend_request where user_id1 = ? and user_id2 = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1,user_id1);
			ps.setInt(2,user_id2);
			result = ps.executeQuery();
			if(result.next())
			{
				havesend = true;
			}
			return havesend;
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
	
	public static void confirmFriendRequest(int user_id1,int user_id2) throws DBException // the order of arguments is important
	{
		Connection conx = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		
		try 
		{
			// confirm the friend request
			String sql = "insert into friend values(?,?)";
			conx = db.Database.getMySQLConnection();
			conx.setAutoCommit(false);
			ps1 = conx.prepareStatement(sql);
			ps1.setInt(1,user_id1);
			ps1.setInt(2,user_id2);
			ps1.executeUpdate();
			// remove the friend request
			sql = "delete from friend_request where user_id1 = ? and user_id2 = ?";
			ps2 = conx.prepareStatement(sql);
			ps2.setInt(1,user_id1);
			ps2.setInt(2,user_id2);
			ps2.executeUpdate();
			//commit
			conx.commit();
			conx.setAutoCommit(true);
		}
		catch(Exception e)
		{
			throw new DBException(e);
		}
		finally 
		{
			db.Utils.close(ps1);
			db.Utils.close(ps2);
			db.Utils.close(conx);
		}
	}
	
	public static void deleteFriendRequest(int user_id1,int user_id2) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try 
		{
			String sql = "delete from friend_request where user_id1 = ? and user_id2 = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, user_id1);
			ps.setInt(2, user_id2);
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
	
	public static void sendFriendRequest(int user_id1,int user_id2) throws DBException // user_id1 send friend request to user_id2
	{
		PreparedStatement ps = null;
		Connection conn = null;
		try 
		{
			String sql = "insert into friend_request values(?,?)";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, user_id1);
			ps.setInt(2, user_id2);
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
	
	public static void removeFriend(int user_id,int friend_id) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			String sql = "delete from friend where (user_id1 = ? and user_id2 = ?) or (user_id1 = ? and user_id2 = ?)";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, user_id);
			ps.setInt(2, friend_id);
			ps.setInt(3, friend_id);
			ps.setInt(4, user_id);
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
	
	public static List<FriendBean> getFriends(int user_id) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try 
		{
			String sql = "select user_id2 as friend_id from friend where user_id1 = ? "
					+ "union "
					+ "select user_id1 as friend_id from friend where user_id2 = ?";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1,user_id);
			ps.setInt(2,user_id);
			result = ps.executeQuery();
			List<FriendBean> friends = new ArrayList<FriendBean>();
			FriendBean friend;
			while(result.next())
			{
				friend = getFriend(result.getInt("friend_id"));
				friends.add(friend);
			}
			return friends;
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
	
	private static FriendBean getFriend(int friend_id) throws DBException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try 
		{
			String sql = "select nom,prenom,username from user where user_id = ? ";
			conn = db.Database.getMySQLConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, friend_id);
			result = ps.executeQuery();
			result.next();
			FriendBean friend = new FriendBean();
			friend.setId(friend_id);
			friend.setNom(result.getString("nom"));
			friend.setPrenom(result.getString("prenom"));
			friend.setUsername(result.getString("username"));
			return friend;
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
