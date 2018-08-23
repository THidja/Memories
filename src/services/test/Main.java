package services.test;

import java.io.File;
import java.io.FileInputStream;

import com.mongodb.DB;
import com.mongodb.Mongo;

public abstract class Main {
	
	public static void main(String args[]) 
	{
		try {
			Mongo m = db.NoSqlDB.getMongo();
			DB bd = m.getDB("gr3_kasdi_hidja");
			File f = new File("WebContent/img/default.jpg");
			File f2 = new File("WebContent/img/sara.jpg");
			FileInputStream stream = new FileInputStream(f);
			db.UserPicture.insertUserPicture("01", stream);
			stream = new FileInputStream(f2);
			db.UserPicture.insertUserPicture("sarita", stream);
			m.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
