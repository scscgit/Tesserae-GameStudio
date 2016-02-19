package sk.tuke.gamestudio.game.mines.core;

/**
 * Created by jaros_000 on 18.2.2016.
 */
public abstract class Tile {
    private TileState state = TileState.CLOSED;

    public TileState getState() {
        return state;
    }

    void setState(TileState state) {
        this.state = state;
    }
}
