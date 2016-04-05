package sk.tuke.gamestudio.service;

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
        return entityManager.createNamedQuery("Score.getBestScoresForGame").setParameter("game", game).getResultList();
    }
}
