package sk.tuke.gamestudio.service.score;

import sk.tuke.gamestudio.entity.Score;

import javax.xml.datatype.DatatypeFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ScoreWebServiceClient implements ScoreService {
    private sk.tuke.gamestudio.webservice.score.ScoreWebService scoreService
            = new sk.tuke.gamestudio.webservice.score.ScoreService().getScoreServicePort();

    @Override
    public void addScore(Score score) throws ScoreException {
        try {
            sk.tuke.gamestudio.webservice.score.Score scoreWeb = new sk.tuke.gamestudio.webservice.score.Score();
            scoreWeb.setPlayer(score.getPlayer());
            scoreWeb.setGame(score.getGame());
            scoreWeb.setPoints(score.getPoints());
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(score.getPlayedOn());
            scoreWeb.setPlayedOn(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
            scoreService.addScore(scoreWeb);
        } catch (Exception e) {
            throw new ScoreException("Error saving score: "+e.getMessage(), e);
        }
    }

    @Override
    public List<Score> getBestScoresForGame(String game) throws ScoreException {
        List<Score> scores = new ArrayList<>();

        try {
            for(sk.tuke.gamestudio.webservice.score.Score scoreWeb : scoreService.getBestScoresForGame(game)) {
                Score score = new Score();
                score.setIdent(scoreWeb.getIdent());
                score.setPlayer(scoreWeb.getPlayer());
                score.setGame(scoreWeb.getGame());
                score.setPoints(scoreWeb.getPoints());
                score.setPlayedOn(scoreWeb.getPlayedOn().toGregorianCalendar().getTime());
                scores.add(score);
            }

            return scores;
        } catch (Exception e) {
            throw new ScoreException("Error loading score: "+e.getMessage(), e);
        }
    }
}
