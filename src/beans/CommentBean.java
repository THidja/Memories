package beans;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class CommentBean {
	
	private String id;
	private String text;
	private Date date;
	private AuthorBean author;
	private boolean ilike;
	private int nblike;
	
	public CommentBean()
	{
		super();
		author = new AuthorBean();
	}
	
	public CommentBean(String id, String text, Date date, AuthorBean author, boolean ilike, int nblike) {
		super();
		this.id = id;
		this.text = text;
		this.date = date;
		this.author = author;
		this.ilike = ilike;
		this.nblike = nblike;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public AuthorBean getAuthor() {
		return author;
	}

	public void setAuthor(AuthorBean author) {
		this.author = author;
	}

	public boolean isIlike() {
		return ilike;
	}

	public void setIlike(boolean ilike) {
		this.ilike = ilike;
	}

	public int getNblike() {
		return nblike;
	}

	public void setNblike(int nblike) {
		this.nblike = nblike;
	}
	
	
	public JSONObject toJSON() throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put("id",this.id);
		json.put("author",this.author.toJSON());
		json.put("texte",this.text);
		json.put("date",this.date.getTime());
		json.put("nblike",this.nblike);
		json.put("ilike",this.ilike);
		return json;
	}

	@Override
	public String toString() {
		return "CommentBean [id=" + id + ", text=" + text + ", date=" + date + ", author=" + author + ", ilike=" + ilike
				+ ", nblike=" + nblike + "]";
	}
	
}
