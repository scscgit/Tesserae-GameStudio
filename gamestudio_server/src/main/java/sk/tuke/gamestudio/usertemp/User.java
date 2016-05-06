package sk.tuke.gamestudio.usertemp;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by Steve on 28.04.2016.
 */
@SessionScoped
@Named("user")
public class User implements Serializable
{
	private int id;
	private String realName;
	private String emailAddress;

	public User()
	{
	}

	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getRealName()
	{
		return realName;
	}
	public void setRealName(String realName)
	{
		this.realName = realName;
	}
	public String getEmailAddress()
	{
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	//todo debug negated
	public boolean isLoggedIn()
	{
		return getRealName() != null;
	}
}
