package beans;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendBean {
	
	private int id;
	private String nom;
	private String prenom;
	private String username;
	
	public FriendBean()
	{
		
	}
	
	public FriendBean(int id, String nom, String prenom, String username) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public static JSONObject toJSON(FriendBean friend) throws JSONException
	{
		JSONObject reponse = new JSONObject();
		reponse.put("id",friend.getId());
		reponse.put("username",friend.getUsername());
		reponse.put("nom",friend.getNom());
		reponse.put("prenom",friend.getPrenom());
		return reponse;
	}

	@Override
	public String toString() {
		return "FriendBean [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", username=" + username + "]";
	}
}
