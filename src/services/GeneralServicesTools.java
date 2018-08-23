package services;

import org.json.JSONException;
import org.json.JSONObject;

public class GeneralServicesTools {
	
	public static JSONObject error(String message,int code)
	{
		JSONObject json = new JSONObject();
		try {
			json.put("code", code);
			json.put("message",message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static JSONObject ok()
	{
		JSONObject json = new JSONObject();
		return json;
	}
	

}
