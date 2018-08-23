package services.servlets.comments;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class AddComment extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException
	{
		String key = request.getParameter("key");
		String text = request.getParameter("text");
		JSONObject reponse = services.CommentsServices.addComment(key, text);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		response.getWriter().write(reponse.toString());
	}

}
