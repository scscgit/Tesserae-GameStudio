package sk.tuke.gamestudio.service.score;

import sk.tuke.gamestudio.entity.Score;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
CREATE TABLE score (
    player VARCHAR(32) NOT NULL,
    game VARCHAR(32) NOT NULL,
    points INTEGER NOT NULL,
    playedon DATE NOT NULL
);
 */
public class ScoreServiceImpl implements ScoreService {
    private static final String URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "postgres";

    private static final String INSERT_STMT =
        "INSERT INTO score (ident, player, game, points, playedon) VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_STMT_BEST =
        "SELECT ident, player, game, points, playedon FROM score WHERE game = ? ORDER BY points DESC LIMIT 10";

    private static final String SELECT_STMT_ALL =
        "SELECT ident, player, game, points, playedon FROM score WHERE game = ? ORDER BY points DESC";

    @Override
    public void addScore(Score score) throws ScoreException {
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            PreparedStatement ps = connection.prepareStatement(INSERT_STMT)) {
            ps.setInt(1, score.getIdent());
            ps.setString(2, score.getPlayer());
            ps.setString(3, score.getGame());
            ps.setInt(4, score.getPoints());
            ps.setDate(5, new Date(score.getPlayedOn().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ScoreException("Error saving score", e);
        }
    }

    @Override
    public List<Score> getBestScoresForGame(String game) throws ScoreException {
        List<Score> scores = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            PreparedStatement ps = connection.prepareStatement(SELECT_STMT_BEST)) {
            ps.setString(1, game);
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Score score = new Score(rs.getInt(1), rs.getString(2), rs.getString(3),
                            rs.getInt(4), rs.getDate(5));
                    scores.add(score);
                }
            }
        } catch (SQLException e) {
            throw new ScoreException("Error loading score", e);
        }

        return scores;
    }
    @Override
    public List<Score> getAllScoresForGame(String game) throws ScoreException
    {
        List<Score> scores = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            PreparedStatement ps = connection.prepareStatement(SELECT_STMT_ALL)) {
            ps.setString(1, game);
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Score score = new Score(rs.getInt(1), rs.getString(2), rs.getString(3),
                                            rs.getInt(4), rs.getDate(5));
                    scores.add(score);
                }
            }
        } catch (SQLException e) {
            throw new ScoreException("Error loading score", e);
        }

        return scores;
    }
}
