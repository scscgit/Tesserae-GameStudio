package sk.tuke.gamestudio.controller;

import org.primefaces.context.RequestContext;
import sk.tuke.gamestudio.entity.GamestudioUser;
import sk.tuke.gamestudio.service.user.UserException;
import sk.tuke.gamestudio.service.user.UserService;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
//@Model
public class UserController
{
	@Inject
	private RequestedUser requestedUser;

	@Inject
	private LoggedUser loggedUser;

	@Inject
	private UserService userService;

	public String login()
	{
		try
		{
			GamestudioUser user = userService.login(requestedUser.getUsername(), requestedUser.getPassword());

			loggedUser.set(user.getUsername(), user.getPassword());
			loggedUser.login();

			return "favorites.xhtml";
		}
		catch (UserException e)
		{
			FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
				                 "Could not log the User in",
				                 e.getMessage()));
			//Do not navigate out of the page if the user information is not valid
			return null;
		}
	}

	public String register()
	{
		try
		{
			userService.register(requestedUser.getUsername(), requestedUser.getPassword());

			//Logs the user in
			return login();
		}
		catch (UserException e)
		{
			FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
				                 "Could not register the User",
				                 e.getMessage()));
			//Do not navigate out of the page if the user information is not valid
			return null;
		}
	}

	/*
	//Logout request changed to opposite direction, logout() gets called indirectly from the signOut hook
	public void logout()
	{
		//Current context
		//RequestContext context = RequestContext.getCurrentInstance();
		//if(loggedUser.isGoogle())
		//{
		//	context.execute("signOut();");
		//}

		loggedUser.logout();

		//context.execute("window.location.reload();");
	}
	*/

	public LoggedUser getLoggedUser()
	{
		return loggedUser;
	}

	/*public boolean isLogged()
	{
		return loggedUser.isLogged();
	}*/

	//Communication with the Google login system via callback using JavaScript
	public void loginGoogle()
	{
		//Current context
		RequestContext context = RequestContext.getCurrentInstance();

		//Attempt at receiving parameters in old version
		//Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		//Map<Object, Object> params = FacesContext.getCurrentInstance().getAttributes();
		//String id = (String)params.get("id");
		//String name = (String)params.get("name");
		//String givenName = (String)params.get("givenName");
		//String familyName = (String)params.get("familyName");
		//String imageUrl = (String)params.get("imageUrl");
		//String email = (String)params.get("email");

		//Logging in
		loggedUser.set(requestedUser.getIdGoogle(), requestedUser.getNickName(), requestedUser.getRealName(),
		               requestedUser.getProfileImage(), requestedUser.getEmailAddress());
		loggedUser.login();

		//Navigates to the main page
		context.execute("window.location.replace('.');");

		//Notify the user about success of the action
		//Does not work
		//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Google account logged in", "Welcome, " + name + "."));
	}
}
