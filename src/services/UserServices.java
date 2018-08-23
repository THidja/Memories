package services;




import org.json.JSONException;

//import java.sql.DBException;

import org.json.JSONObject;

import beans.UserBean;
import db.Session;
import db.exceptions.DBException;

public class UserServices {

	public static String nameFormat = "^[a-zA-Z]{3,}$";
	public static String userFormat = "^[a-zA-Z][a-zA-Z0-9]{2,}$";
	public static String passwordFormat = "^.{8,}$";
	
	public static JSONObject createUser(UserBean user) {
		try {
			String reponse = user.haveValideData();
			
			if(!(reponse == "YES"))
			{
				return services.GeneralServicesTools.error(reponse,Codes.WRONG_DATA);
			}
			if(db.User.exists(user.getUsername()))
			{
				return services.GeneralServicesTools.error("le nom d'utilisateur existe deja",Codes.DATA_EXISTS);
			}
			if(db.User.exists(user.getNom(),user.getPrenom()))
			{
				return GeneralServicesTools.error("un utilisateur avec le meme nom et prenom existe deja",Codes.DATA_EXISTS);
			}
			db.User.add(user);
			return services.GeneralServicesTools.ok();
		} 
		catch (DBException e) 
		{
			e.printStackTrace();
			return services.GeneralServicesTools.error(e.getMessage(),Codes.DATABASE);
		}
	}
	
	public static JSONObject login(String username,String password)
	{
		
		try {
			String message;
			
			if(username == null || password == null || !username.matches(userFormat))
			{
				message = "verifier que les champs sont remplis correctement";
				return services.GeneralServicesTools.error(message,Codes.WRONG_DATA);
			}
			else if(!db.User.exists(username))
			{
				message = "Utilisateur innconue";
				return services.GeneralServicesTools.error(message,Codes.WRONG_DATA);
			}
			else 
			{
				if(db.User.isCorrect(username, password))
				{	
					JSONObject response = new JSONObject();
					int userID = db.User.getUserID(username);
					String haveKey = Session.associatedKey(userID);
					if(haveKey != null)
					{
						Session.destroySession(haveKey);
					}
					String key = db.Session.insertSession(userID,false);
					response.put("id",userID);
					response.put("username",username);
					response.put("key",key);
					return response;
				}
				else 
				{
					message = "Mot de passe incorrect";
					return services.GeneralServicesTools.error(message,Codes.WRONG_DATA);
				}
			}
		}
		catch(DBException e)
		{
			e.printStackTrace();
			return services.GeneralServicesTools.error(e.getMessage(),Codes.DATABASE);
		}
		catch(JSONException e)
		{
			return services.GeneralServicesTools.error("Erreur JSON",Codes.JSON);
		}
	}
	
	public static JSONObject updateInfos(String key,String nom,String prenom) 
	{
		JSONObject sessionprob = SessionServices.verifySession(key);
		try {
			if(sessionprob != null) // there is a problem with the session key
			{
				return sessionprob; // return a jsonObject that describe the session problem
			}
			else if(nom == null || prenom == null || !nom.matches(nameFormat) || !prenom.matches(nameFormat))
			{
				return services.GeneralServicesTools.error("verifier votre saisie",Codes.WRONG_DATA);
			}
			else if(db.User.exists(nom, prenom))
			{
				return GeneralServicesTools.error("un utilisateur avec le meme nom et prenom existe deja",Codes.DATA_EXISTS);	
			}
			else
			{
				db.Session.keepAlive(key);
				int user_id = db.Session.associatedUser(key);
				db.User.updateUserInfos(user_id, nom, prenom);
				return GeneralServicesTools.ok();
			}
		}
		catch(DBException e)
		{
			e.printStackTrace();
			return services.GeneralServicesTools.error("Erreur de base de donnée",Codes.DATABASE);
		}
	}
	public static JSONObject updatePassword(String key,String prevPass,String newPass) 
	{
		JSONObject sessionprob = SessionServices.verifySession(key);
		try {
			if(sessionprob != null) // there is a problem with the session key
			{
				return sessionprob; // return a jsonObject that describe the session problem
			}
			else if(newPass == null || !newPass.matches(passwordFormat))
			{
				return services.GeneralServicesTools.error("verifier votre saisie",Codes.WRONG_DATA);
			}
			else 
			{
				int user_id = db.Session.associatedUser(key);
				String username = db.User.getUserName(user_id);
				if(db.User.isCorrect(username,prevPass))
				{
					db.Session.keepAlive(key);
					db.User.updatePassword(user_id,newPass);
					return services.GeneralServicesTools.ok();
				}
				else
				{
					return services.GeneralServicesTools.error("l'ancien mot de passe est incorrect",Codes.WRONG_DATA);
				}
			}
		}
		catch(DBException e)
		{
			return services.GeneralServicesTools.error("Erreur de base de donnée",Codes.DATABASE);
		}
	}
	
	public static JSONObject logout(String key)
	{
		try {
			db.Session.destroySession(key);
			return services.GeneralServicesTools.ok();
		} catch (DBException e) {
			return services.GeneralServicesTools.error("Erreur de deconnexion",Codes.DATABASE);
		}
	}
	
}
