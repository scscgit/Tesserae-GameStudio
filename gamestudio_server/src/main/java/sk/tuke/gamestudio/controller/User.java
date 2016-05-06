package sk.tuke.gamestudio.controller;

import javax.enterprise.inject.Model;
import javax.validation.constraints.Size;

//@Named
//@RequestScoped
@Model
public class User
{
	@Size (min = 5, max = 10)
	private String username;

	@Size (min = 5, max = 20)
	//@Pattern(regexp=".*\\d.*")
	private String password;

	private String emailAddress;
	private String realName;
	private long idGoogle;
	private String profileImage;

	public User()
	{
	}

	public User(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	public String getRealName()
	{
		return realName;
	}
	public void setRealName(String realName)
	{
		this.realName = realName;
	}

	public long getIdGoogle()
	{
		return idGoogle;
	}
	public void setIdGoogle(long idGoogle)
	{
		this.idGoogle = idGoogle;
	}

	public String getProfileImage()
	{
		return profileImage;
	}
	public void setProfileImage(String profileImage)
	{
		this.profileImage = profileImage;
	}
}
