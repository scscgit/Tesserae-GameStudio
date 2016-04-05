package sk.tuke.gamestudio.entity;

import sk.tuke.sorm.Id;

import java.io.Serializable;
import java.util.Date;

public class Score implements Serializable, Comparable<Score> {
    @Id
    private Integer ident;
    private String player;
    private String game;
    private int points;
    private Date playedOn;

    public Score() {}

    public Score(String player, String game, int points, Date playedOn) {
        this.player = player;
        this.game = game;
        this.points = points;
        this.playedOn = playedOn;
    }

    public Score(Integer ident, String player, String game, int points, Date playedOn) {
        this.ident = ident;
        this.player = player;
        this.game = game;
        this.points = points;
        this.playedOn = playedOn;
    }

    public Integer getIdent() {
        return ident;
    }

    public void setIdent(Integer ident) {
        this.ident = ident;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(Date playedOn) {
        this.playedOn = playedOn;
    }

    @Override
    public String toString() {
        return "Score{" +
                "ident=" + ident +
                ", player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", points=" + points +
                ", playedOn=" + playedOn +
                '}';
    }

    @Override
    public int compareTo(Score o) {
        return o.getPoints() - getPoints();
    }
}
