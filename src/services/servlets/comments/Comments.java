package services.servlets.comments;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

public class Comments extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		String key = request.getParameter("key");
		JSONArray json_response = services.CommentsServices.getComments(key);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		response.getWriter().write(json_response.toString());
	}


}
