package sk.tuke.gamestudio.game.mines;

import sk.tuke.gamestudio.game.Game;

public class Mines
{
	//Singleton game representation
	private static Game mines = new Game("Mines");

	public static Game getGame()
	{
		return Mines.mines;
	}
}
