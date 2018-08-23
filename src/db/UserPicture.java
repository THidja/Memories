package db;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public abstract class UserPicture {
	
	public static void insertUserPicture(String filename,InputStream in) throws MongoException, IOException
	{
		deletePicture(filename);
		Mongo m = db.NoSqlDB.getMongo();
		DB database = m.getDB("gr3_kasdi_hidja");
	    GridFS gfs = new GridFS(database,"user_pictures");
		GridFSInputFile gfsFile = gfs.createFile(in);
		gfsFile.setFilename(filename);
		gfsFile.save();
		m.close();
	}
	
	public static void deletePicture(String filename) throws MongoException , IOException
	{
		Mongo m = db.NoSqlDB.getMongo();
		DB database = m.getDB("gr3_kasdi_hidja");
		GridFS gfsPhoto = new GridFS(database, "user_pictures");
		GridFSDBFile image = gfsPhoto.findOne(filename);
		if(image != null)
		{
			gfsPhoto.remove(image);
		}
		m.close();
	}
	
	public static void getUserPicture(String filename,OutputStream output) throws IOException
	{
		Mongo m = db.NoSqlDB.getMongo();
		DB database = m.getDB("gr3_kasdi_hidja");
		GridFS gfsPhoto = new GridFS(database, "user_pictures");
		GridFSDBFile imageForOutput = gfsPhoto.findOne(filename);
		if(imageForOutput == null)
		{
			imageForOutput = gfsPhoto.findOne("01");
		}
		imageForOutput.writeTo(output); 
		m.close();
	}
}
