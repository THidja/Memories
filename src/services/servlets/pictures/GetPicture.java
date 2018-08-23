package services.servlets.pictures;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetPicture extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException
	{
		String username = request.getParameter("username");
		response.setContentType("image/jpeg");
		services.PicturesServices.getPicture(username,response.getOutputStream());

	}

}