package sk.tuke.gamestudio.controller;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Model;
import javax.inject.Named;
import javax.validation.constraints.Size;

//@Named
//@RequestScoped
@Model
public class User {
    @Size(min = 5, max = 10)
    private String username;

    @Size(min = 5, max = 20)
    //@Pattern(regexp=".*\\d.*")
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
