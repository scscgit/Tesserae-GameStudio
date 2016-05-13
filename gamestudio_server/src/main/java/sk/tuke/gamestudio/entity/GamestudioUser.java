package sk.tuke.gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Size;

/**
 * Common functionality between a temporary RequestedUser and a current LoggedUser.
 * <p>
 * Created by Steve on 07.05.2016.
 */
@Entity (name = "GamestudioUser")
@NamedQuery (name = "User.getByUsername",
	query = "SELECT u FROM GamestudioUser u WHERE u.username=:username")
public class GamestudioUser
{
	@Id
	@Size (min = 5, max = 10)
	private String username;

	@Size (min = 5, max = 20)
	//@Pattern(regexp=".*\\d.*")
	private String password;

	private boolean isGoogle;

	private String idGoogle;
	private String nickName;
	private String realName;
	private String emailAddress;
	private String profileImage;

	public GamestudioUser()
	{
	}

	/*
	public GamestudioUser(String username)
	{
		set(username);
	}
	public GamestudioUser(String username, String password)
	{
		set(username, password);
	}
	public GamestudioUser(long idGoogle, String realName, String profileImage, String emailAddress)
	{
		set(idGoogle, realName, profileImage, emailAddress);
	}
	*/

	public void set(String username)
	{
		setGoogle(false);
		setUsername(username);
	}
	public void set(String username, String password)
	{
		setGoogle(false);
		setUsername(username);
		setPassword(password);
	}
	public void set(String idGoogle, String nickName, String realName, String profileImage, String emailAddress)
	{
		setGoogle(true);
		setIdGoogle(idGoogle);
		setNickName(nickName);
		setRealName(realName);
		setProfileImage(profileImage);
		setEmailAddress(emailAddress);
	}

	//Google user disambiguation
	public boolean isGoogle()
	{
		return isGoogle;
	}
	public void setGoogle(boolean google)
	{
		isGoogle = google;
	}

	//Normal user values

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

	//Google user values

	public String getIdGoogle()
	{
		return idGoogle;
	}
	public void setIdGoogle(String idGoogle)
	{
		this.idGoogle = idGoogle;
	}

	public String getNickName()
	{
		return nickName;
	}
	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public String getRealName()
	{
		return realName;
	}
	public void setRealName(String realName)
	{
		this.realName = realName;
	}

	public String getProfileImage()
	{
		return profileImage;
	}
	public void setProfileImage(String profileImage)
	{
		this.profileImage = profileImage;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}
}
