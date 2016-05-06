package sk.tuke.gamestudio.game.mines;

import sk.tuke.gamestudio.game.Game;
import sk.tuke.gamestudio.game.mines.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.mines.core.Field;

public class Mines
{
	//Singleton game representation
	private static Game mines = new Game("Mines");

	public static void main(String[] args) throws Exception
	{
		Field field = new Field(9, 9, 1);
		ConsoleUI ui = new ConsoleUI(field);
		ui.play();
	}

	public static Game getGame()
	{
		return Mines.mines;
	}
}
