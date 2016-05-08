package sk.tuke.gamestudio.game.mines;

import sk.tuke.gamestudio.game.Game;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class Minesweeper
{
	//Singleton game representation
	private static Game minesweeper = new Game("Minesweeper", "mines");

	public Game getGame()
	{
		return Minesweeper.minesweeper;
	}
	public static Game getGameStatic()
	{
		Game game = Minesweeper.minesweeper;

		//Easy DEBUG hook with neutral action if everything is OK
		if(game == null)
		{
			throw new RuntimeException("MINESWEEPER STATIC GAME IS NULL");
		}

		return game;
	}
}
