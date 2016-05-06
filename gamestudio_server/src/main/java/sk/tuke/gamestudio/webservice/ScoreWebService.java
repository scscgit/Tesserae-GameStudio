package sk.tuke.gamestudio.webservice;

import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.score.ScoreException;
import sk.tuke.gamestudio.service.score.ScoreService;

import javax.ejb.EJB;
import javax.jws.WebService;
import java.util.List;

//@WebService
@WebService (
	portName = "ScoreServicePort",
	serviceName = "ScoreService")
//http://kpi4he:8080/gamestudiojsf/ScoreWebServiceService?Tester
public class ScoreWebService
{
	@EJB
	private ScoreService scoreService;
	//    private ScoreService scoreService = new ScoreServiceImpl();

	public List<Score> getBestScoresForGame(String game) throws ScoreException
	{
		return scoreService.getBestScoresForGame(game);
	}

	public boolean addScore(Score score) throws ScoreException
	{
		//        if(score.getPlayedOn() == null) {
		//            score.setPlayedOn(new Date());
		//        }
		scoreService.addScore(score);
		return true;
	}
}
