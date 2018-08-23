package services;


import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import beans.FriendBean;
import db.Session;
import db.exceptions.DBException;

public class FriendsServices {
	
	public static JSONObject addFriend(String key,int friend_id) { // send friend request 
		
		try {
				JSONObject sessionprob = SessionServices.verifySession(key);
				if(sessionprob != null) // there is a problem with the session key
				{
					return sessionprob; // return a jsonObject that describe the session problem
				}
				else { // the session key is OK 
					db.Session.keepAlive(key);
					int user_id = Session.associatedUser(key);
					if(db.Friend.isFriend(user_id, friend_id))
					{
						return GeneralServicesTools.error("Vous êtes deja ami",Codes.DATA_EXISTS);
					}
					else if(db.Friend.haveSendFriendRequest(friend_id, user_id))
					{
						return GeneralServicesTools.error("Action non autoriser",Codes.DATA_EXISTS);
					}
					else
					{
						if(db.Friend.haveSendFriendRequest(user_id,friend_id))
						{
							return GeneralServicesTools.error("une demande d'ajout est deja envoyer ",Codes.DATA_EXISTS);
						}
						else
						{
							db.Friend.sendFriendRequest(user_id,friend_id);
							db.Notification.product(user_id,friend_id,"request");
							return GeneralServicesTools.ok();
						}
					}
				}
		}
		catch(DBException e)
		{
			e.printStackTrace();
			return GeneralServicesTools.error("Erreur de base de donnée",Codes.DATABASE);
		}
	}
	
	public static JSONObject removeFriend(String key,int friend_id)
	{
		try {
			JSONObject sessionprob = SessionServices.verifySession(key);
			if(sessionprob != null)
			{
				return sessionprob;
			}
			else {
				db.Session.keepAlive(key);
				int user_id = db.Session.associatedUser(key);
				if(!db.Friend.isFriend(user_id, friend_id))
				{
					return GeneralServicesTools.error("(--) vous n'etes pas ami",Codes.MISSED_DATA);
				}
				else
				{
					db.Notification.product(user_id, friend_id, "remove");
					db.Friend.removeFriend(user_id, friend_id);
					return GeneralServicesTools.ok();
				}
			}
		}
		catch(DBException e)
		{
			e.printStackTrace();
			return GeneralServicesTools.error("Erreur de base de donnée",Codes.DATABASE);
		}
	}
	
	public static JSONObject confirmFriendRequest(String key,int friend_id)
	{
		try {
			JSONObject sessionprob = SessionServices.verifySession(key);
			if(sessionprob != null)
			{
				return sessionprob;
			}
			else
			{
				db.Session.keepAlive(key);
				int user_id = db.Session.associatedUser(key);
				if(!db.Friend.haveSendFriendRequest(friend_id,user_id))
				{
					return GeneralServicesTools.error("Demande d'ajout n'existe pas",Codes.WRONG_DATA);
				}
				else {
					db.Friend.confirmFriendRequest(friend_id,user_id);
					db.Notification.product(user_id,friend_id,"accept");
					db.Notification.consume(friend_id,user_id,"request");
					return GeneralServicesTools.ok();
				}
			}
		}
		catch(DBException e)
		{
			e.printStackTrace();
			return GeneralServicesTools.error("Erreur de base de donnée",Codes.DATABASE);
		}
	}
	
	public static JSONObject refuseFriendRequest(String key,int friend_id) 
	{
		try {
			JSONObject sessionprob = SessionServices.verifySession(key);
			if(sessionprob != null)
			{
				return sessionprob;
			}
			else
			{
				db.Session.keepAlive(key);
				int user_id = db.Session.associatedUser(key);
				if(!db.Friend.haveSendFriendRequest(friend_id,user_id))
				{
					return GeneralServicesTools.error("Demande d'ajout n'existe pas",Codes.WRONG_DATA);
				}
				else {
					db.Friend.deleteFriendRequest(friend_id,user_id);
					db.Notification.product(user_id,friend_id,"refuse");
					db.Notification.consume(friend_id, user_id,"request");
					return GeneralServicesTools.ok();
				}
			}
		}
		catch(DBException e)
		{
			e.printStackTrace();
			return GeneralServicesTools.error("Erreur de base de donnée",Codes.DATABASE);
		}
	}
	
	public static JSONArray getFriends(String key) throws JSONException
	{
		try {
			JSONObject sessionprob = SessionServices.verifySession(key);
			if(sessionprob != null)
			{
				return new JSONArray(sessionprob);
			}
			else {
				db.Session.keepAlive(key);
				int user_id = db.Session.associatedUser(key);
				List<FriendBean> friends = db.Friend.getFriends(user_id);
				JSONArray reponse = new JSONArray();
				for(FriendBean friend : friends)
				{
					reponse.put(FriendBean.toJSON(friend));
				}
				return reponse;
			}
		}
		catch(DBException e)
		{
			e.printStackTrace();
			return new JSONArray(GeneralServicesTools.error("Erreur de base de donnée",Codes.DATABASE));
		}
	}
}
