package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class ScoreRestServiceClient implements ScoreService {
    private static final String URL = "http://localhost:8080/gamestudio_server/webapi/score";

    @Override
    public void addScore(Score score) throws ScoreException {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(URL)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(score, MediaType.APPLICATION_JSON), Response.class);
        } catch (Exception e) {
            throw new ScoreException("Error saving score", e);
        }
    }

    @Override
    public List<Score> getBestScoresForGame(String game) throws ScoreException {
        try {
            Client client = ClientBuilder.newClient();
            List<Score> scores = client.target(URL)
                    .path("/" + game)
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<Score>>() {
                    });
            return scores;
        } catch (Exception e) {
            throw new ScoreException("Error loading score", e);
        }
    }
}
