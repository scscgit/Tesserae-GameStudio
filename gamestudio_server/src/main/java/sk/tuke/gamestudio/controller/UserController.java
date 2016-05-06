package sk.tuke.gamestudio.controller;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
//@Model
public class UserController {
	@Inject
	private User user;
	
	@Inject
	private LoggedUser loggedUser;
	
	public String login(){
		if ("heslo".equals(user.getPassword())){
			loggedUser.setName(user.getUsername());
            return "index.xhtml";
		} else {
			FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Incorrect Username and Password",
                            "Please enter correct username and Password"));
			return "login.xhtml";
		}
	}

	public String register(){
		loggedUser.setName(user.getUsername());
		return "index.xhtml";
	}

	public String logout()
	{
		loggedUser.setName(null);
		return "index.xhtml";
	}

	public LoggedUser getLoggedUser() {
		return loggedUser;
	}

	public boolean isLogged() {
		return loggedUser.getName() != null;
	}
}
