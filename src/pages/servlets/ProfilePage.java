package pages.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.exceptions.DBException;

public class ProfilePage extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException
	{
		request.setAttribute("goToProfile",true);
		this.getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException
	{
		try {
			String key = request.getParameter("key");
			if(db.Session.exists(key))
			{
				if(db.Session.isExpired(key))
				{
					request.setAttribute("session_expired",true);
					this.getServletContext().getRequestDispatcher("/index.jsp").forward(request,response);
				}
				else
				{
					this.getServletContext().getRequestDispatcher("/WEB-INF/profile.jsp").forward(request,response);
				}
			}
			else
			{
				response.setStatus(403);
				response.setContentType("text/html");
				response.getWriter().write("<h3>403 forbidden</h3>");
			}
		}
		catch(DBException e)
		{
			e.printStackTrace();
			response.setStatus(500);
			response.setContentType("text/html");
			response.getWriter().write("<h3>500 Internal Server Error</h3>");
		}
	}
	

}
