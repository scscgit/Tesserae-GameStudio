package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jaros_000 on 10.3.2016.
 */
public class ScoreServiceImplFile implements ScoreService {

    private String getFileName(String game) {
        return System.getProperty("user.home") + "/" + game + ".score";
    }

    @Override
    public void addScore(Score score) throws ScoreException {
        List<Score> scores = getBestScoresForGame(score.getGame());
        scores.add(score);
        Collections.sort(scores);

        try(ObjectOutputStream oos =
                    new ObjectOutputStream(new FileOutputStream(getFileName(score.getGame())))) {
            oos.writeObject(scores);
        } catch (Exception e) {
            throw new ScoreException("Error saving score", e);
        }
    }

    @Override
    public List<Score> getBestScoresForGame(String game) throws ScoreException {
        List<Score> scores = new ArrayList<>();
        File file = new File(getFileName(game));
        if(file.exists()) {
            try(ObjectInputStream ois =
                        new ObjectInputStream(new FileInputStream(file))) {
                scores = (List<Score>)ois.readObject();
            } catch (Exception e) {
                throw new ScoreException("Error loading score", e);
            }
        }
        return scores;
    }
}
