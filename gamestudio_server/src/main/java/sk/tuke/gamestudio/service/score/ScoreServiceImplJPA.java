package sk.tuke.gamestudio.service.score;

import sk.tuke.gamestudio.entity.Score;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ScoreServiceImplJPA implements ScoreService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score) throws ScoreException {
        entityManager.persist(score);
    }

    @Override
    public List<Score> getBestScoresForGame(String game) throws ScoreException {
        return entityManager.createNamedQuery("Score.getScoresForGame")
                .setParameter("game", game).setMaxResults(10).getResultList();
    }
    @Override
    public List<Score> getAllScoresForGame(String game) throws ScoreException {
        return entityManager.createNamedQuery("Score.getScoresForGame")
                            .setParameter("game", game).getResultList();
    }
}
