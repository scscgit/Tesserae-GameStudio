package sk.tuke.gamestudio.controller;

import org.primefaces.context.RequestContext;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Named
@RequestScoped
//@Model
public class UserController
{
	@Inject
	private User user;

	@Inject
	private LoggedUser loggedUser;

	public String login()
	{
		if ("heslo".equals(user.getPassword()))
		{
			loggedUser.setGoogle(false);
			loggedUser.setUsername(user.getUsername());
			loggedUser.login();

			return "index.xhtml";
		}
		else
		{
			FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_WARN,
				                 "Incorrect Username and Password",
				                 "Please enter correct username and Password"));
			return "favorites.xhtml";
		}
	}

	public String register()
	{
		loggedUser.set(user.getUsername());
		loggedUser.login();
		return "index.xhtml";
	}

	/*public void logout()
	{
		//Current context
		//RequestContext context = RequestContext.getCurrentInstance();
		//if(loggedUser.isGoogle())
		//{
		//	context.execute("signOut();");
		//}

		loggedUser.logout();

		//context.execute("window.location.reload();");
	}*/

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

		//Received parameters
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("id");
		String name = params.get("name");
		//String givenName = params.get("givenName");
		//String familyName = params.get("familyName");
		String imageUrl = params.get("imageUrl");
		String email = params.get("email");

		//Logging in
		loggedUser.set(id, name, imageUrl, email);
		loggedUser.login();

		//Navigates to the main page
		context.execute("window.location.replace('.');");

		//Notify the user about success of the action
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
			FacesMessage.SEVERITY_INFO, "Google account logged in", "Welcome, " + name + "."));
	}
}
