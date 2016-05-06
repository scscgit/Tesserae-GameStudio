package sk.tuke.gamestudio.game.mines.core;

import java.io.Serializable;

public abstract class Tile implements Serializable {
    private TileState state = TileState.CLOSED;

    public TileState getState() {
        return state;
    }

    void setState(TileState state) {
        this.state = state;
    }
}
