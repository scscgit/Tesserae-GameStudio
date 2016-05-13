package sk.tuke.gamestudio.service.favorites;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.support.Utility;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Steve on 12.04.2016.
 */
@Stateless
public class FavoriteGameServiceJPA implements FavoriteGameDatabaseService
{
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void addFavorite(String player, String game) throws FavoriteException
	{
		addFavorite(new FavoriteGameEntity(player, game, Utility.getCurrentSqlTimestamp()));
	}
	@Override
	public void addFavorite(FavoriteGameEntity favorite) throws FavoriteException
	{
		entityManager.persist(favorite);
	}
	@Override
	public void removeFavorite(String player, String game) throws FavoriteException
	{
		entityManager
			.createNamedQuery("FavoriteGameEntity.removeEntity")
			.setParameter("player", player)
			.setParameter("game", game)
			.executeUpdate();
	}
	@Override
	public List<FavoriteGameEntity> getFavoriteGames(String player) throws FavoriteException
	{
		return entityManager
			.createNamedQuery("FavoriteGameEntity.getFavoriteGames")
			.setParameter("player", player)
			.getResultList();
	}
	@Override
	public List<FavoriteGameEntity> getFavoriteGamePlayers(String game) throws FavoriteException
	{
		return entityManager
			.createNamedQuery("FavoriteGameEntity.getFavoriteGamePlayers")
			.setParameter("game", game)
			.getResultList();
	}
}
