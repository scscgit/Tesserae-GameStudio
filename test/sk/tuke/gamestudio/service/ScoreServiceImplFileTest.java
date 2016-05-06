package sk.tuke.gamestudio.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.score.ScoreService;
import sk.tuke.gamestudio.service.score.ScoreServiceImplFile;

import java.io.File;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


public class ScoreServiceImplFileTest {
    private ScoreService scoreService = new ScoreServiceImplFile();

    @Before
    public void setUp() throws Exception {
        File file = new File(System.getProperty("user.home") + "/tiles.score");
        if(file.exists())
            file.delete();
    }

    @After
    public void tearDown() throws Exception {
        File file = new File(System.getProperty("user.home") + "/tiles.score");
        if(file.exists())
            file.delete();
    }

    @Test
    public void testAddScore1() throws Exception {
        assertTrue(scoreService.getBestScoresForGame("tiles").size() == 0);
    }

    @Test
    public void testAddScore2() throws Exception {
        Score score = new Score(1, "jaro", "tiles", 220, new Date());
        scoreService.addScore(score);
        assertTrue(scoreService.getBestScoresForGame("tiles").size() == 1);
    }

    @Test
    public void testAddScore3() throws Exception {
        Score score = new Score(1, "jaro", "tiles", 300, new Date());
        scoreService.addScore(score);
        score = new Score(1, "fero", "tiles", 400, new Date());
        scoreService.addScore(score);

        List<Score> scores = scoreService.getBestScoresForGame("tiles");
        assertTrue(scoreService.getBestScoresForGame("tiles").size() == 2);

        assertTrue(scores.get(0).getPlayer().equals("fero"));
        assertTrue(scores.get(0).getPoints() == 400);
    }
}
