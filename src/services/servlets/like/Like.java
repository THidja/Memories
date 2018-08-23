package services.servlets.like;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class Like extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException
	{
		String comment_id = request.getParameter("comment_id");
		String key = request.getParameter("key");
		JSONObject reponse = services.LikeServices.likeComment(key, comment_id);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		response.getWriter().write(reponse.toString());
		
	}

}
