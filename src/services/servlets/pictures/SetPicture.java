package services.servlets.pictures;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONObject;

public class SetPicture extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        Part part = request.getPart("picture");
        String key = request.getParameter("key");
        String fileName = getFileName(part);
        
        JSONObject out = services.PicturesServices.setPicture(key,fileName,part);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().write(out.toString());
    }

    
    private static String getFileName( Part part ) {
        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
            if ( contentDisposition.trim().startsWith( "filename" ) ) {
                return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
            }
        }
        return null;
    }   

}
