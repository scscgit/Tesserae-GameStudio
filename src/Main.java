import sk.tuke.gamestudio.game.mines.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.mines.core.Field;

public class Main
{

	public static void main(String[] args) throws Exception
	{
		Field field = new Field(9, 9, 1);
		ConsoleUI ui = new ConsoleUI(field);
		ui.play();
	}
}
