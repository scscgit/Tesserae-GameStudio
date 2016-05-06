package sk.tuke.gamestudio.webservice.score;

import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.score.ScoreException;
import sk.tuke.gamestudio.service.score.ScoreService;
import sk.tuke.gamestudio.service.score.ScoreServiceImpl;

import javax.ejb.EJB;
import javax.jws.WebService;
import java.util.List;

//http://kpi4he:8080/gamestudiojsf/ScoreWebServiceService?Tester
@WebService(serviceName = "ScoreService", portName = "ScoreServicePort")
public class ScoreWebService {
    @EJB
    private ScoreService scoreService;

    public List<Score> getBestScoresForGame(String game) throws ScoreException {
        return scoreService.getBestScoresForGame(game);
    }

    public boolean addScore(Score score) throws ScoreException {
        scoreService.addScore(score);
        return true;
    }
}
