package db;


import beans.AuthorBean;
import db.exceptions.DBException;

public abstract class Utils {
	
	public static AuthorBean getAuthor(int user_id,int author_id,String author_login) throws DBException
	{
		try {
			AuthorBean author = new AuthorBean();
			int  contact = 0;
			if(user_id != author_id)
			{
				contact = db.Friend.isFriend(user_id,author_id) ? 1 : 0;
				if(contact == 0)
				{
					if(db.Friend.haveSendFriendRequest(user_id, author_id))
					{
						contact = 2;
					}
				    if(db.Friend.haveSendFriendRequest(author_id, user_id))
				    {
				    	contact = 3;
				    }
				}
			}
			author.setId(author_id);
			author.setLogin(author_login);
			author.setContact(contact);
			return author;
		}
		catch(Exception e)
		{
			throw new DBException(e);
		}
	}
	
	public static void close(AutoCloseable closeable) 
	{
		try {
			closeable.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
