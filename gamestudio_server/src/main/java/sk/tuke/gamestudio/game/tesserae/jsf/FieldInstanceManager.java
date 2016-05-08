package sk.tuke.gamestudio.game.tesserae.jsf;

import sk.tuke.gamestudio.controller.LoggedUser;
import sk.tuke.gamestudio.game.tesserae.core.field.Field;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.history.FieldHistoryRebuilder;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.history.FieldHistoryRebuilderNoHistoryException;
import sk.tuke.gamestudio.game.tesserae.cui.ColorMode;
import sk.tuke.gamestudio.game.tesserae.cui.FieldManager;
import sk.tuke.gamestudio.game.tesserae.cui.interpreter.FieldInterpreter;
import sk.tuke.gamestudio.game.tesserae.cui.interpreter.InterpreterException;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Field Manager for JavaServer Faces.
 * Manages a new Field for every single user (Session).
 * Is a central point of storage of all the objects that the Field needs, including an interpreter of commands.
 * <p>
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

	//History
	private FieldHistoryRebuilder history;

	@Inject
	LoggedUser loggedUser;

	//Interpreter input and output
	private String lastMessage;
	private String inputMessage;

	//EJB constructor
	public FieldInstanceManager()
	{
		/*
		this.component = (TesseraeComponent) Utility.findBean("tesserae");
		if (this.component == null)
		{
			throw new RuntimeException("Fatal Error: Component could not be loaded into the FieldInstanceManager.");
		}
		this.service = this.component.getFavoriteService();
		if (service == null)
		{
			throw new RuntimeException("Fatal Error: Service could not be loaded from the Component.");
		}
		*/

		setLastMessage("Welcome to the Tesserae. If you need <help>, just ask for it :)");

		//this.service = new FavoriteGameServiceJPA();
		//this.service = new Oracle11gDatabaseServiceImpl();

		this.field = null;
	}

	//Interpreter is loaded from the Component externally
	public void setInterpreter(FieldInterpreter interpreter)
	{
		this.interpreter = interpreter;
	}
	public boolean isInterpreterAvailable()
	{
		return this.interpreter != null;
	}

	public String getLastMessage()
	{
		return this.lastMessage.trim();
	}
	protected void setLastMessage(String lastMessage)
	{
		//RequestContext context = RequestContext.getCurrentInstance();
		this.lastMessage = lastMessage;
		//context.update("fieldResultArea");
	}

	//Interprets a command from the user, returning the result in a very user-friendly format
	public void interpretCommand()
	{
		try
		{
			String result = this.interpreter.interpret(getInputMessage());
			setLastMessage("Result> " + (result.isEmpty() ? "OK" : result));
		}
		catch (InterpreterException e)
		{
			setLastMessage("Error# " + e.getMessage());
		}
		catch (Exception e)
		{
			setLastMessage("Fatal Error% " + e.toString() + ":\n" + e.getMessage());
		}
		finally
		{
			setInputMessage("");
		}
	}

	public void interpretHelp()
	{
		setInputMessage("help");
		interpretCommand();
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
		if (loggedUser.isLogged())
		{
			return loggedUser.getName();
		}
		else
		{
			return null;
		}
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
		if (this.history == null)
		{
			throw new FieldHistoryRebuilderNoHistoryException("There is no instance of the history.");
		}
		this.field = this.history.getField();
	}
	@Override
	public LinkedList<Field> getTimeline() throws FieldHistoryRebuilderNoHistoryException
	{
		if (this.history == null)
		{
			throw new FieldHistoryRebuilderNoHistoryException("There is no instance of the history.");
		}
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
