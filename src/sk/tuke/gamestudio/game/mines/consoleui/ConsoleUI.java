package sk.tuke.gamestudio.game.mines.consoleui;

import sk.tuke.gamestudio.game.mines.core.Clue;
import sk.tuke.gamestudio.game.mines.core.Field;
import sk.tuke.gamestudio.game.mines.core.GameState;
import sk.tuke.gamestudio.game.mines.core.Tile;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jaros_000 on 25.2.2016.
 */
public class ConsoleUI {
    private Field field;

    private static final Pattern INPUT_REGEX = Pattern.compile("([OM])([A-I])([1-9])");

    private Scanner scanner;

    public ConsoleUI(Field field) {
        this.field = field;
        scanner = new Scanner(System.in);
    }

    public void play() {
        do {
            show();
            handleInput();
        } while(field.getState() == GameState.PLAYING);

        show();

        if(field.getState() == GameState.SOLVED) {
            System.out.println("HURAAA");
        } else if(field.getState() == GameState.FAILED){
            System.out.println("SKUS ZNOVA ALE RADSEJ DACO INE");
        }
    }

    private void handleInput() {
        System.out.print("Please enter command: ");
        String line = scanner.nextLine().toUpperCase();
        if("X".equals(line)) {
            System.exit(0);
        }
        Matcher matcher = INPUT_REGEX.matcher(line);
        if(matcher.matches()) {
            int row = matcher.group(2).charAt(0) - 'A';
            int column = Integer.parseInt(matcher.group(3)) - 1;

            if("O".equals(matcher.group(1))) {
                field.openTile(row, column);
            } else {
                field.markTile(row, column);
            }
        } else {
            System.out.println("ROB PORIADNE");
        }
    }

    private void show() {
        System.out.print("   ");
        for (int column = 0; column < field.getColumnCount(); column++) {
            System.out.print(column + 1);
            System.out.print(" ");
        }

        System.out.println();

        for (int row = 0; row < field.getRowCount(); row++) {
            System.out.print((char)('A' + row));
            System.out.print(" ");
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(row, column);
                System.out.print(" ");
                switch (tile.getState()) {
                    case CLOSED:
                        System.out.print("-");
                        break;
                    case MARKED:
                        System.out.print("M");
                        break;
                    case OPEN:
                        if (tile instanceof Clue) {
                            System.out.print(((Clue) tile).getValue());
                        } else {
                            System.out.print("X");
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected tile state " + tile.getState());
                }
            }
            System.out.println();
        }
    }
}
