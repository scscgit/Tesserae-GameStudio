package sk.tuke.gamestudio.controller;

import sk.tuke.gamestudio.entity.GamestudioUser;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoggedUser extends GamestudioUser implements Serializable
{
	private boolean logged;

	public LoggedUser()
	{
		logout();
	}

	public boolean isLogged()
	{
		return logged;
	}
	public void login()
	{
		this.logged = true;
	}
	public void logout()
	{
		this.logged = false;
	}

	public String getName()
	{
		if (isGoogle())
		{
			return getNickName();
		}
		else
		{
			return getUsername();
		}
	}
}
