package sk.tuke.gamestudio.service.score;

import sk.tuke.gamestudio.entity.Score;
import sk.tuke.sorm.ISORM;
import sk.tuke.sorm.SORM;

import java.util.List;

public class ScoreServiceImplSorm implements ScoreService {
    private ISORM sorm;

    public ScoreServiceImplSorm(ISORM sorm)
    {
        this.sorm = sorm;
    }

    @Override
    public void addScore(Score score) throws ScoreException {
        try {
            sorm.insert(score);
        } catch (Exception e) {
            throw new ScoreException("Error saving score", e);
        }
    }

    @Override
    public List<Score> getBestScoresForGame(String game) throws ScoreException {
        try {
            return (List<Score>)sorm.select(Score.class, "game = '" + game + "'");
        } catch (Exception e) {
            throw new ScoreException("Error loading score", e);
        }
    }
}
