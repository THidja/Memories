package services;

import java.io.OutputStream;

import javax.servlet.http.Part;

import org.json.JSONObject;
public class PicturesServices {
	
	public static void getPicture(String username,OutputStream output)
	{
		try
		{
			String filename = username;
			db.UserPicture.getUserPicture(filename, output);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static JSONObject setPicture(String key,String fileName,Part data)
	{
		JSONObject sessionProb = services.SessionServices.verifySession(key);
		if(sessionProb != null)
		{
			return sessionProb;
		}
		else if(fileName == null || fileName.isEmpty())
		{
			return services.GeneralServicesTools.error("Aucun fichier n'as etait envoyer",Codes.WRONG_DATA);
		}
		else 
		{
			try {
				db.Session.keepAlive(key);
				String newFileName = db.User.getUserName(db.Session.associatedUser(key));
				db.UserPicture.insertUserPicture(newFileName, data.getInputStream());
				return services.GeneralServicesTools.ok();
			}
			catch(Exception e)
			{
				return services.GeneralServicesTools.error("erreur de base de donéne",Codes.DATABASE);
			}
		}
			
	}

}