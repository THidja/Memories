package services.servlets.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class UpdateInfo extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		String key = request.getParameter("key");
		String nom = request.getParameter("nom");
		String prenom = request.getParameter("prenom");
		JSONObject json = services.UserServices.updateInfos(key, nom, prenom);
		response.getWriter().write(json.toString());
		
	}

}
