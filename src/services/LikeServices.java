package services;

import org.json.JSONObject;

public abstract class LikeServices {
	
	public static JSONObject likeComment(String key,String comment_id)
	{
		try  {
			JSONObject sessionprob = services.SessionServices.verifySession(key);
			if(sessionprob != null)
			{
				return sessionprob;
			}
			else
			{
				db.Session.keepAlive(key);
				if(!db.Comments.exists(comment_id))
				{
					String message = "commentraire non trouver";
					return services.GeneralServicesTools.error(message, Codes.WRONG_DATA);
				}
				else {
					int user_id = db.Session.associatedUser(key);
					if(db.Comments.isAuthor(comment_id, user_id))
					{
						String message ="vous ne pouvez pas aimer un commentaire dont vous etes l'auteur";
						return services.GeneralServicesTools.error(message,Codes.WRONG_DATA);
					}
					else if(db.LikeComment.hasLiked(user_id, comment_id))
					{
						String message ="Vous aimez deja le commentaire";
						return services.GeneralServicesTools.error(message, Codes.DATA_EXISTS);
					}
					else
					{
						db.LikeComment.like(user_id, comment_id);
						return services.GeneralServicesTools.ok();
					}
				}
			}
		}
		catch(Exception e)
		{
			return services.GeneralServicesTools.error("Erreur de base de donnée",Codes.DATABASE);
		}
	}
	
	public static JSONObject unlikeComment(String key,String comment_id)
	{
		try  {
			JSONObject sessionprob = services.SessionServices.verifySession(key);
			if(sessionprob != null)
			{
				return sessionprob;
			}
			else
			{
				db.Session.keepAlive(key);
				if(!db.Comments.exists(comment_id))
				{
					String message = "commentraire non trouver";
					return services.GeneralServicesTools.error(message, Codes.WRONG_DATA);
				}
				else {
					int user_id = db.Session.associatedUser(key);
					if(!db.LikeComment.hasLiked(user_id, comment_id))
					{
						String message ="Vous n'avez pas aimez ce commentaire";
						return services.GeneralServicesTools.error(message, Codes.WRONG_DATA);
					}
					else
					{
						db.LikeComment.unlike(user_id, comment_id);
						return services.GeneralServicesTools.ok();
					}
				}
			}
		}
		catch(Exception e)
		{
			return services.GeneralServicesTools.error("Erreur de base de donnée",Codes.DATABASE);
		}
	}

}
