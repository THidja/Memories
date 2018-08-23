package services;

import org.json.JSONArray;
import org.json.JSONObject;

import db.exceptions.DBException;

public class NotificationServices {

	public static JSONArray read(String key)
	{
		JSONArray response = new JSONArray();
		try {
			JSONObject session = services.SessionServices.verifySession(key);
			if(session != null)
			{
				return response.put(session);
			}
			else 
			{
				int to = db.Session.associatedUser(key);
				response = db.Notification.read(to);
				return response;
			}
		}
		catch(DBException e)
		{
			return response.put(GeneralServicesTools.error("Erreur de base de donnée",Codes.DATABASE));
		}
	}
	
	public static JSONObject consume(String key,int from,String type)
	{
		JSONObject response = services.SessionServices.verifySession(key);
		try {
			if(response != null)
			{
				return response;
			}
			else 
			{
				int to = db.Session.associatedUser(key);
				if(db.Notification.exists(from, to))
				{
					db.Notification.consume(from, to,type);
					return services.GeneralServicesTools.ok();
				}
				else
				{
					return services.GeneralServicesTools.error("notification non trouver",Codes.MISSED_DATA);
				}
			}
		}
		catch(DBException e)
		{
			return services.GeneralServicesTools.error("erreur de base de donnée",Codes.DATABASE);
		}
	}

}
