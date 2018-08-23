package services.servlets.notifications;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Read extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException
	{
		String key = request.getParameter("key");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		response.getWriter().write(services.NotificationServices.read(key).toString());
	}
}
