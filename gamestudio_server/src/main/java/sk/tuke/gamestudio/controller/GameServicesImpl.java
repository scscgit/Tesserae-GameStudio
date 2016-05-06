package sk.tuke.gamestudio.controller;

import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.GameServices;
import sk.tuke.gamestudio.service.ScoreException;
import sk.tuke.gamestudio.service.ScoreService;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

@Named("gameServices")
@RequestScoped
public class GameServicesImpl implements GameServices {
    @Inject
    private UserController userController;

    @EJB
    private ScoreService scoreService;

    @Override
    public void saveScore(String game, int points) throws ScoreException {
        if (userController.getLoggedUser().isLogged()) {
            scoreService.addScore(new Score(userController.getLoggedUser().getName(), game, points, new Date()));
        }
    }
}
