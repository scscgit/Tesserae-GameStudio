package sk.tuke.gamestudio.service.user;

import sk.tuke.gamestudio.service.DatabaseException;

/**
 * Problems with a User entry in the Database.
 * <p>
 * Created by Steve on 08.05.2016.
 */
public class UserException extends DatabaseException
{
	public UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public UserException()
	{
		super();
	}
	public UserException(String message)
	{
		super(message);
	}
	public UserException(String message, Throwable cause)
	{
		super(message, cause);
	}
	public UserException(Throwable cause)
	{
		super(cause);
	}
}
