package sk.tuke.gamestudio.game.mines.core;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Random;

@Named("minefield")
@SessionScoped
public class Field implements Serializable {
    private final int rowCount;

    private final int columnCount;

    private final int mineCount;

    private Tile[][] tiles;

    private GameState state = GameState.PLAYING;

    private long startMillis;

    private boolean marking;

    public Field() {
        this(9, 9, 2);
    }

    public Field(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        generate();
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public void generate() {
        state = GameState.PLAYING;
        tiles = new Tile[rowCount][columnCount];
        generateMines();
        fillWithClues();
        startMillis = System.currentTimeMillis();
    }

    private void generateMines() {
        Random random = new Random();

        int minesToSet = mineCount;
        while (minesToSet > 0) {
            int row = random.nextInt(rowCount);
            int column = random.nextInt(columnCount);
            if (tiles[row][column] == null) {
                tiles[row][column] = new Mine();
                minesToSet--;
            }
        }
    }

    private void fillWithClues() {
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (tiles[row][column] == null) {
                    tiles[row][column] = new Clue(countNeighbourMines(row, column));
                }
            }
        }
    }

    private int countNeighbourMines(int row, int column) {
        int count = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int acurRow = row + rowOffset;
            if (acurRow >= 0 && acurRow < rowCount) {
                for (int colOffset = -1; colOffset <= 1; colOffset++) {
                    int acurColumn = column + colOffset;
                    if (acurColumn >= 0 && acurColumn < columnCount) {
                        if (tiles[acurRow][acurColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }

    public void openTile(int row, int column) {
        final Tile tile = tiles[row][column];
        if (tile.getState() == TileState.CLOSED) {
            tile.setState(TileState.OPEN);

            if (tile instanceof Mine) {
                state = GameState.FAILED;
                return;
            }

            if (tile instanceof Clue) {
                if (((Clue) tile).getValue() == 0) {
                    openNeighborTile(row, column);
                }
            }

            if (isSolved()) {
                state = GameState.SOLVED;
            }
        }
    }

    private void openNeighborTile(int row, int column) {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int acurRow = row + rowOffset;
            if (acurRow >= 0 && acurRow < rowCount) {
                for (int colOffset = -1; colOffset <= 1; colOffset++) {
                    int acurColumn = column + colOffset;
                    if (acurColumn >= 0 && acurColumn < columnCount) {
                        openTile(acurRow, acurColumn);
                    }
                }
            }
        }
    }

    public void markTile(int row, int column) {
        final Tile tile = tiles[row][column];
        if (tile.getState().equals(TileState.CLOSED)) {
            tile.setState(TileState.MARKED);
        } else if (tile.getState().equals(TileState.MARKED)) {
            tile.setState(TileState.CLOSED);
        }
    }

    private boolean isSolved() {
        return rowCount * columnCount - mineCount == getNumberOfOpen();
    }

    public GameState getState() {
        return state;
    }

    public int getNumberOfOpen() {
        int count = 0;

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (tiles[row][column].getState() == TileState.OPEN) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getScore() {
        if (state == GameState.SOLVED) {
            int seconds = (int) ((System.currentTimeMillis() - startMillis) / 1000);
            return rowCount * columnCount * 3 - seconds;
        }
        return 0;
    }

    public void setMarking(boolean marking) {
        this.marking = marking;
    }

    public boolean isMarking() {
        return marking;
    }

    public void action(int row, int column) {
        if (marking) {
            markTile(row, column);
        } else {
            openTile(row, column);
        }
    }
}
