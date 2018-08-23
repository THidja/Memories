package beans;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthorBean {
	
	private int id;
	private String login;
	private int contact;
	
	public AuthorBean()
	{
		super();
	}

	public AuthorBean(int id, String login, int contact) {
		super();
		this.id = id;
		this.login = login;
		this.contact = contact;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public int getContact() {
		return contact;
	}

	public void setContact(int contact) {
		this.contact = contact;
	}
	
	public JSONObject toJSON() throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put("id",this.id);
		json.put("login",this.login);
		json.put("contact",this.contact);
		return json;
	}
	
	@Override
	public String toString() {
		return "AuthorBean [id=" + id + ", login=" + login + ", contact=" + contact + "]";
	}
	
}
