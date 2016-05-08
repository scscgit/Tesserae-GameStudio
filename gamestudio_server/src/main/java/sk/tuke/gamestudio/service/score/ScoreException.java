package sk.tuke.gamestudio.service.score;

import sk.tuke.gamestudio.service.DatabaseException;

public class ScoreException extends DatabaseException
{
    public ScoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
