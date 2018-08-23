package services.servlets.user;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import beans.UserBean;


public class CreateUser extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException
	{
		String nom = request.getParameter("nom");
		String prenom = request.getParameter("prenom");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UserBean user = new UserBean(nom,prenom,username,password);
		JSONObject jsonResponse = new JSONObject();
		jsonResponse = services.UserServices.createUser(user);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		response.getWriter().write(jsonResponse.toString());	
	}

}
