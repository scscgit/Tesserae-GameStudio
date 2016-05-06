package sk.tuke.gamestudio.service.favorites;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.game.Game;
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
	public void addFavorite(String player, Game game) throws FavoriteException
	{
		addFavorite(new FavoriteGameEntity(player, game, Utility.getCurrentSqlTimestamp()));
	}
	@Override
	public void addFavorite(FavoriteGameEntity favorite) throws FavoriteException
	{
		entityManager.persist(favorite);
	}
	@Override
	public void removeFavorite(String player, Game game) throws FavoriteException
	{
		entityManager
			.createNamedQuery("FavoriteGameEntity.removeEntity")
			.setParameter("player", player)
			.setParameter("game", game)
			.executeUpdate();
	}
	@Override
	public List<FavoriteGameEntity> getFavorites(String player) throws FavoriteException
	{
		return entityManager
			.createNamedQuery("FavoriteGameEntity.getFavoriteGames")
			.setParameter("player", player)
			.getResultList();
	}
}
