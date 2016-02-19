import sk.tuke.gamestudio.game.mines.core.Clue;
import sk.tuke.gamestudio.game.mines.core.Field;
import sk.tuke.gamestudio.game.mines.core.Mine;
import sk.tuke.gamestudio.game.mines.core.Tile;

public class Main {

    public static void main(String[] args) {
        Field field = new Field(9, 9, 10);

        for (int row = 0; row < field.getRowCount(); row++) {
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(row, column);
                System.out.print(" ");
                if(tile instanceof Clue) {
                    System.out.print(((Clue) tile).getValue());
                } else {
                    System.out.print("M");
                }
            }
            System.out.println();
        }

    }
}
