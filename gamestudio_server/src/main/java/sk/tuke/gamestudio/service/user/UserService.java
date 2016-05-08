package sk.tuke.gamestudio.service.user;

import sk.tuke.gamestudio.entity.GamestudioUser;

/**
 * User sign-up and sign-in service.
 * <p>
 * Created by Steve on 08.05.2016.
 */
public interface UserService
{
	GamestudioUser login(String username, String password) throws UserException;
	void register(String username, String password) throws UserException;
}
