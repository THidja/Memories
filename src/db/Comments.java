package db;

import java.util.Date;
import java.util.GregorianCalendar;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import beans.AuthorBean;
import beans.CommentBean;
import db.exceptions.DBException;

public abstract class Comments {
	
	
	public static String idOf(String author_login,Date date) throws DBException
	{
		try {
			Mongo m = db.NoSqlDB.getMongo();
			DB database = m.getDB("gr3_kasdi_hidja");
			DBCollection comments = database.getCollection("comments");
			DBObject objet = new BasicDBObject();
			objet.put("author_login",author_login);
			objet.put("at",date);
			DBCursor cursor = comments.find(objet);
			String id = cursor.next().get("_id").toString();
			cursor.close();
			m.close();
			return id;
		}
		catch(Exception e)
		{
			throw new DBException(e);
		}
	}
	
	public static JSONArray getComments(int user_id) throws DBException 
	{
		try {
			Mongo m = db.NoSqlDB.getMongo();
			DB database = m.getDB("gr3_kasdi_hidja");
			DBCollection comments = database.getCollection("comments");
			DBCursor cursor = comments.find();
			JSONArray ret = new JSONArray();
			JSONObject item = new JSONObject();
			while(cursor.hasNext())
			{
				DBObject comment = (DBObject) cursor.next();
				ObjectId comment_id = (ObjectId) comment.get("_id");
				item = getComment(comment_id.toString(),user_id).toJSON();
				ret.put(item);
			}
			m.close();
			return ret;
		}
		catch(Exception e)
		{
			throw new DBException(e);
		}
	}
	
	public static CommentBean getComment(String comment_id,int user_id) throws DBException
	{
		try {
			Mongo m = db.NoSqlDB.getMongo();
			DB database = m.getDB("gr3_kasdi_hidja");
			CommentBean comment = new CommentBean();
			DBCollection comments = database.getCollection("comments");
			DBObject query = new BasicDBObject();
			ObjectId id = new ObjectId(comment_id);
			query.put("_id",id);
			DBObject dbComment = comments.findOne(query);
			String text = (String) dbComment.get("text");
			Date date = (Date) dbComment.get("at");
			int author_id = (Integer) dbComment.get("author_id");
			String author_login = (String) dbComment.get("author_login");
			AuthorBean author = db.Utils.getAuthor(user_id, author_id, author_login);
			boolean ilike = db.LikeComment.hasLiked(user_id, comment_id);
			int nblike = db.LikeComment.getNbLike(comment_id);
			comment.setId(comment_id);
			comment.setAuthor(author);
			comment.setDate(date);
			comment.setText(text);
			comment.setIlike(ilike);
			comment.setNblike(nblike);
			m.close();
			return comment;
		}
		catch(Exception e)
		{
			throw new DBException(e);
		}
	}
	public static String[] addComment(int author_id,String author_login,String text) throws DBException
	{
		try {
			Mongo m = db.NoSqlDB.getMongo();
			DB database = m.getDB("gr3_kasdi_hidja");
			DBCollection comments = database.getCollection("comments");
			GregorianCalendar calendar = new GregorianCalendar();
			Date date_commentaire  = calendar.getTime();
			BasicDBObject doc = new BasicDBObject();
			doc.put("author_login",author_login);
			doc.put("author_id",author_id);
			doc.put("at",date_commentaire);
			doc.put("text",text);
			comments.insert(doc);
			String comment_id = db.Comments.idOf(author_login,date_commentaire);
			String at = String.valueOf(date_commentaire.getTime());
			String[] ret = {comment_id,at};
			m.close();
			return ret;
		}
		catch(Exception e)
		{
			throw new DBException(e);
		}
	}
	
	public static void modifyComment(String comment_id,String newText) throws DBException
	{
		try {
			Mongo m = db.NoSqlDB.getMongo();
			DB database = m.getDB("gr3_kasdi_hidja");
			DBCollection comments = database.getCollection("comments");
			ObjectId id = new ObjectId(comment_id);
			DBObject objetId = new BasicDBObject();
			objetId.put("_id",id);
			DBObject text = new BasicDBObject();
			text.put("text",newText);
			DBObject set = new BasicDBObject();
			set.put("$set",text);
			comments.update(objetId, set);
			db.LikeComment.deleteAssociatedLikes(comment_id);
			m.close();
		}
		catch(Exception e)
		{
			throw new DBException(e);
		}
	}
	
	public static void deleteComment(String comment_id) throws DBException
	{
		try {
			Mongo m = db.NoSqlDB.getMongo();
			DB database = m.getDB("gr3_kasdi_hidja");
			DBCollection comments = database.getCollection("comments");
			DBObject objet = new BasicDBObject();
			ObjectId id = new ObjectId(comment_id);
			objet.put("_id",id);
			DBObject toRemove = comments.findOne(objet);
			comments.remove(toRemove);
			db.LikeComment.deleteAssociatedLikes(comment_id);	
			m.close();
		}
		catch(Exception e)
		{
			throw new DBException(e);
		}
	}
	
	public static boolean exists(String comment_id) throws DBException
	{
		try {
			Mongo m = db.NoSqlDB.getMongo();
			DB database = m.getDB("gr3_kasdi_hidja");
			DBCollection comments = database.getCollection("comments");
			DBObject objet = new BasicDBObject();
			ObjectId id = new ObjectId(comment_id);
			objet.put("_id",id);
			DBObject result = comments.findOne(objet);
			if(result != null)
			{
				m.close();
				return true;
			}
			m.close();
			return false;
		}
		catch(IllegalArgumentException e)
		{
			return false;
		}
		catch(Exception e)
		{
			throw new DBException(e);
		}
	}
	public static boolean isAuthor(String comment_id,int user_id) throws DBException
	{
		try {
			Mongo m = db.NoSqlDB.getMongo();
			DB database = m.getDB("gr3_kasdi_hidja");
			DBCollection comments = database.getCollection("comments");
			DBObject objet = new BasicDBObject();
			ObjectId id = new ObjectId(comment_id);
			objet.put("_id",id);
			DBObject result = comments.findOne(objet);
			Integer author_id = (Integer) result.get("author_id");
			if(author_id.equals(user_id))
			{
				m.close();
				return true;
			}
			m.close();
			return false;
		}
		catch(IllegalArgumentException e)
		{
			return false;
		}
		catch(Exception e)
		{
			throw new DBException(e);
		}
	}
	
}
