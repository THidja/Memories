package services.servlets.friends;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class AddFriend extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException , ServletException
	{
		String key = request.getParameter("key");
		Integer friend_id = Integer.parseInt(request.getParameter("friend_id"));
		JSONObject serviceResponse = services.FriendsServices.addFriend(key, friend_id);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		response.getWriter().write(serviceResponse.toString());
	}

}
