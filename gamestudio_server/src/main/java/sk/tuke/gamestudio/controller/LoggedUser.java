package sk.tuke.gamestudio.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import java.io.Serializable;

@Named
@SessionScoped
public class LoggedUser implements Serializable {
	
	private String name;
	
	public String getName(){
		return this.name;
	}
	
	public String setName(String name){
		return this.name = name;
	}
	
	public boolean isLogged(){
		return name != null;
	}
}
