package sk.tuke.gamestudio.service.user;

import sk.tuke.gamestudio.entity.GamestudioUser;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Another JPA magic. Persists the User in the database.
 * <p>
 * Created by Steve on 08.05.2016.
 */
@Stateless
public class UserServiceImplJPA implements UserService
{
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public GamestudioUser login(String username, String password) throws UserException
	{
		List<GamestudioUser> users;

		try
		{
			users = entityManager
				.createNamedQuery("User.getByUsername")
				.setParameter("username", username)
				.getResultList();
		}
		catch (Exception e)
		{
			throw new UserException("User login problem:\n" + e.getMessage());
		}

		if (users.size() == 1)
		{
			GamestudioUser user = users.get(0);
			if (user.getPassword().equals(password))
			{
				//Could find the user with such username and password: returns a valid User
				return user;
			}
			else
			{
				throw new UserException("The entered password is wrong");
			}
		}
		else if (users.size() == 0)
		{
			throw new UserException("There is no user with this username");
		}
		else
		{
			throw new UserException("Fatal error: Wrong number of users was returned");
		}

	}
	@Override
	public void register(String username, String password) throws UserException
	{

		List<GamestudioUser> usersWithSameUsername;

		try
		{
			usersWithSameUsername = entityManager
				.createNamedQuery("User.getByUsername")
				.setParameter("username", username)
				.getResultList();
		}
		catch (Exception e)
		{
			throw new UserException("User registration problem during duplicate user check:\n" + e.getMessage());
		}

		if (usersWithSameUsername.size() != 0)
		{
			throw new UserException("The player with this username is already registered in our Gamestudio.");
		}

		try
		{
			GamestudioUser user = new GamestudioUser();
			user.set(username, password);
			entityManager.persist(user);
		}
		catch (Exception e)
		{
			throw new UserException("User registration problem:\n" + e.getMessage());
		}
	}
}
