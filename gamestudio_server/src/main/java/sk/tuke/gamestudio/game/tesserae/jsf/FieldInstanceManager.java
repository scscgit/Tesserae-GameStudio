package sk.tuke.gamestudio.game.tesserae.jsf;

import sk.tuke.gamestudio.game.Game;
import sk.tuke.gamestudio.game.tesserae.core.field.Field;
import sk.tuke.gamestudio.game.tesserae.core.field.Settings;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.SimpleFieldBuilder;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.history.FieldHistoryRebuilder;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.history.FieldHistoryRebuilderNoHistoryException;
import sk.tuke.gamestudio.game.tesserae.cui.ColorMode;
import sk.tuke.gamestudio.game.tesserae.cui.FieldManager;
import sk.tuke.gamestudio.game.tesserae.cui.interpreter.FieldInterpreter;
import sk.tuke.gamestudio.game.tesserae.cui.interpreter.InterpreterException;
import sk.tuke.gamestudio.service.favorites.FavoriteException;
import sk.tuke.gamestudio.service.favorites.FavoriteGameDatabaseService;
import sk.tuke.gamestudio.service.favorites.FavoriteGameServiceJPA;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Steve on 18.04.2016.
 */
@Named ("tesseraemanager")
@SessionScoped
public class FieldInstanceManager implements Serializable, FieldManager
{
	//Fields
	//Instance of an interpreter that is interpreting commands and building a Field
	private FieldInterpreter interpreter;
	//Managed Field instance
	private Field field;
	//Current field color
	private ColorMode fieldColor = ColorMode.BLACK;
	//Database service
	//@EJB
	private FavoriteGameDatabaseService service;
	//History
	private FieldHistoryRebuilder history;

	private String lastMessage;
	private String inputMessage;

	//EJB constructor
	public FieldInstanceManager()
	{
		setLastMessage("Welcome to the Tesserae. If you need <help>, just ask for it.");

		this.service = new FavoriteGameServiceJPA();
		if (service == null)
		{
			throw new RuntimeException("DEBUG: NO SERVICE");
		}

		//The builder for interpreter is chosen during creation of an instance of UI
		this.interpreter = new FieldInterpreter(this, new SimpleFieldBuilder(Settings.SIMPLE_GAME), this.service);
		this.field = null;
	}

	public String getLastMessage()
	{
		return this.lastMessage;
	}
	protected void setLastMessage(String lastMessage)
	{
		this.lastMessage = lastMessage;
	}

	private void handleEndGameState()
	{
		if (this.field.getState().equals(Field.GameState.WON))
		{
			setLastMessage("Congratz, you've won!");
		}
		else if (this.field.getState().equals(Field.GameState.LOST))
		{
			setLastMessage("GG, you've lost.");
		}
	}

	private void markGameAsFavorite(Game currentGame)
	{
		try
		{
			this.service.addFavorite(getPlayer(), currentGame.toString());
		}
		catch (FavoriteException e)
		{
			setLastMessage("Sorry, there was a problem with the database:\n" +
			               e.getMessage() +
			               "\nTry to make the game your favorite again next time.");
			//switch (e.getErrorCode())
		}
	}

	//Interprets a command from the user, returning the result in a user-friendly format
	public void interpretCommand()
	{
		try
		{
			String result = this.interpreter.interpret(getInputMessage());
			setInputMessage("");
			setLastMessage("Result> " + (result.isEmpty() ? "OK" : result));
		}
		catch (InterpreterException e)
		{
			setLastMessage("Error# " + e.getMessage());
		}
	}

	@Override
	public Field getManagedField()
	{
		return this.field;
	}

	@Override
	public void setManagedField(Field field)
	{
		setLastMessage("New field has been set.");
		this.field = field;
		//Prepares a new timeline for the Field and saves the first position on top of the "stack"
		this.history = new FieldHistoryRebuilder(field.getRows(), field.getColumns());
		this.history.saveState(field);
	}

	@Override
	public String getPlayer()
	{
		//todo
		String property = System.getProperty("user.name");

		//I am paranoid so I don't even trust system methods. I also don't want conflict with users called like "None".
		if (property == null)
		{
			return "";
		}

		return property;
	}

	//Management of the current color setting
	@Override
	public ColorMode getFieldColor()
	{
		return this.fieldColor;
	}
	@Override
	public void setFieldColor(ColorMode color)
	{
		this.fieldColor = color;
	}

	//Goes forwards in time
	@Override
	public void fieldUpdatedCallback()
	{
		this.history.saveState(this.field);
	}
	//Goes back in time
	@Override
	public void goBackInTime() throws FieldHistoryRebuilderNoHistoryException
	{
		this.field = this.history.getField();
	}
	@Override
	public LinkedList<Field> getTimeline()
	{
		return this.history.getTimeline();
	}

	public String getInputMessage()
	{
		return inputMessage;
	}
	public void setInputMessage(String inputMessage)
	{
		this.inputMessage = inputMessage;
	}
}
