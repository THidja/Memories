package db;

import java.net.UnknownHostException;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

public abstract class NoSqlDB {
	
	public static Mongo getMongo() throws UnknownHostException, MongoException 
	{
		Mongo m = new Mongo("132.227.201.129",27130);
		return m;
	}

}
