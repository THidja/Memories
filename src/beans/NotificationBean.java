package beans;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationBean {

	private int from;
	private int to;
	private String login;
	private String type;
	
	public NotificationBean()
	{
		
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public JSONObject toJSON() throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put("from",from);
		json.put("to",to);
		json.put("login", login);
		json.put("type",type);
		return json;
	}
	
	@Override
	public String toString() {
		return "NotificationBean [from=" + from + ", to=" + to + ", login=" + login + ", type=" + type + "]";
	}
	
	
	
}
