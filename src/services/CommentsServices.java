package services;

import org.json.JSONArray;
import org.json.JSONObject;

import db.Comments;

public abstract class CommentsServices {
	
	public static JSONArray getComments(String key)
	{
		try {
			JSONObject sessionprob = services.SessionServices.verifySession(key);
			if(sessionprob != null)
			{
				JSONArray json = new JSONArray();
				json.put(sessionprob);
				return json;
			}
			else 
			{
				int user_id = db.Session.associatedUser(key);
				JSONArray response = db.Comments.getComments(user_id);
				return response;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			JSONArray json = new JSONArray();
			json.put(services.GeneralServicesTools.error("Erreur de base de donnée",Codes.DATABASE));
			return json;
		}
	}
	public static JSONObject addComment(String key,String text)
	{
		try 
		{
			JSONObject sessionprob = services.SessionServices.verifySession(key);
			if(sessionprob != null)
			{
				return sessionprob;
			}
			else
			{
				db.Session.keepAlive(key);
				if(text == null || text.length() < 3)
				{
					String message = "un commentaire ne peux pas avoir moins de 3 caracteres";
					return services.GeneralServicesTools.error(message,Codes.WRONG_DATA);
				}
				else 
				{
					int author_id = db.Session.associatedUser(key);
					String author_login = db.User.getUserName(author_id);
					String[] commentInfo = db.Comments.addComment(author_id, author_login, text);
					String id = commentInfo[0];
					String at = commentInfo[1];
					JSONObject response = new JSONObject();
					response.put("id",id);
					response.put("at",at);
					return  response;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return services.GeneralServicesTools.error("Erreur de base de donnée",Codes.DATABASE);
		}
	}
	
	public static JSONObject deleteComment(String key,String comment_id) 
	{
		try 
		{
			JSONObject sessionprob = services.SessionServices.verifySession(key);
			if(sessionprob != null)
			{
				return sessionprob;
			}
			else
			{
				db.Session.keepAlive(key);
				if(!Comments.exists(comment_id)) 
				{
					return services.GeneralServicesTools.error("commentaire non trouver",Codes.WRONG_DATA);
				}
				else 
				{
					int user_id = db.Session.associatedUser(key);
					if(!Comments.isAuthor(comment_id,user_id))
					{
						String message = "vous ne pouvez pas supprimer un commentaire que vous n'etes pas "
								+ "l'auteur";
						return services.GeneralServicesTools.error(message,Codes.WRONG_DATA);
					}
					else
					{
						db.Comments.deleteComment(comment_id);
						return services.GeneralServicesTools.ok();
					}
				}	
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return services.GeneralServicesTools.error("Erreur de base de donnée",Codes.DATABASE);
		}
	}

}
