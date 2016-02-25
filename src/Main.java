import sk.tuke.gamestudio.game.mines.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.mines.core.Clue;
import sk.tuke.gamestudio.game.mines.core.Field;
import sk.tuke.gamestudio.game.mines.core.Mine;
import sk.tuke.gamestudio.game.mines.core.Tile;

public class Main {

    public static void main(String[] args) {
        Field field = new Field(9, 9, 1);
        ConsoleUI ui = new ConsoleUI(field);
        ui.play();
    }
}
