package beans;

import services.UserServices;

public class UserBean {
	
	private String username;
	private String password;
	private String prenom;
	private String nom;
	
	

	public UserBean()
	{
		
	}
	
	public UserBean(String nom,String prenom,String username,String password)
	{
		this.nom = nom;
		this.prenom = prenom;
		this.username = username;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String haveValideData()
	{
		String message = "";
		
		
		// verification des champs
		
		if(nom == null || prenom == null || username == null || password == null)
		{
			message = "tous les champs doivent etre rempli";
			return message;
		}
		if(!nom.matches(UserServices.nameFormat))
		{
			message += "le \"Nom\" est non valide! \n";
		}
		if(!prenom.matches(UserServices.nameFormat))
		{
			message += "le \"Prenom\" est non valide! \n";
		}
		if(!username.matches(UserServices.userFormat))
		{
			message += "le \"Nom d'utilisateur\" est non valide! \n";
		}
		if(!password.matches(UserServices.passwordFormat))
		{
			message += "le \"Mot de passe\" est non valide! \n";
		}
		
		// tout les champs sont correct
		
		if(message == "")
		{
			message = "YES";
		}
		
		return message;
	}
	

}
