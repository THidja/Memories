package services.servlets.notifications;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class Consume extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException 
	{
		String key = request.getParameter("key");
		int from = Integer.valueOf(request.getParameter("from"));
		String type = request.getParameter("type");
		JSONObject json = services.NotificationServices.consume(key, from, type);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		response.getWriter().write(json.toString());
	}

}
