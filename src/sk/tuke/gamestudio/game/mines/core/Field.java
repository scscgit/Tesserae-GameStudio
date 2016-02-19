package sk.tuke.gamestudio.game.mines.core;

import java.util.Random;

/**
 * Created by jaros_000 on 18.2.2016.
 */
public class Field {
    private final int rowCount;

    private final int columnCount;

    private final int mineCount;

    private final Tile[][] tiles;

    private GameState state = GameState.PLAYING;

    public Field(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        tiles = new Tile[rowCount][columnCount];
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

    private void generate() {
        generateMines();
        fillWithClues();
    }

    private void fillWithClues() {
        for(int row = 0; row < rowCount; row++) {
            for(int column = 0; column < columnCount; column++) {
                if (tiles[row][column] == null) {
                    tiles[row][column] = new Clue(countNeighMines(row, column));
                }
            }
        }
    }

    private int countNeighMines(int row, int column) {
        int count = 0;
        for(int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int acurRow = row + rowOffset;
            if(acurRow >= 0 && acurRow < rowCount) {
                for (int colOffset = -1; colOffset <= 1; colOffset++) {
                    int acurColumn = column + colOffset;
                    if(acurColumn >= 0 && acurColumn < columnCount) {
                        if(tiles[acurRow][acurColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private void generateMines() {
        Random random = new Random();

        int minesToSet = mineCount;
        while(minesToSet > 0){
            int row = random.nextInt(rowCount);
            int column = random.nextInt(columnCount);
            if (tiles[row][column] == null) {
                tiles[row][column] = new Mine();
                minesToSet--;
            }
        }
    }

    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }

    public void openTile(int row, int column) {
        Tile tile = tiles[row][column];
        tile.setState(TileState.OPEN);

        if(tile instanceof Mine) {
            state = GameState.FAILED;
        }

        if(isSolved()) {
            state = GameState.SOLVED;
        }
    }

    public void markTile(int row, int column) {
        Tile tile = tiles[row][column];
        if(tile.getState().equals(TileState.CLOSED)) {
            tile.setState(TileState.MARKED);
        } else if(tile.getState().equals(TileState.MARKED)) {
            tile.setState(TileState.CLOSED);
        }
    }

    private boolean isSolved() {
        return false;
    }

    public GameState getState() {
        return state;
    }
}
