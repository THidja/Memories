package services;


import org.json.JSONObject;

import db.Session;
import db.exceptions.DBException;

class SessionServices {
	
	public static JSONObject verifySession(String key)
	{
		try {
			if(key == null)
			{
				return GeneralServicesTools.error("le service exige une clef de session",Codes.MISSED_DATA);
			}
			else if(!db.Session.exists(key))				
			{
				return GeneralServicesTools.error("la clef que vous avez fourni est eroner",Codes.WRONG_DATA);
		    }
			else if(Session.isExpired(key))
			{
				return GeneralServicesTools.error("Votre session a expirer",Codes.SESSION_EXPIRED);
			}
	    	else
	    	{
	    		return null;
	    	}
		}
		catch(DBException e)
		{
			return GeneralServicesTools.error("Erreur de base de donnée",Codes.DATABASE);
		}
	}
	
}
