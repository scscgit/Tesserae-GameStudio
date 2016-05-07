package sk.tuke.gamestudio.game.tesserae.jsf;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Tesserae Theme.
 * Also contains static list of all available Themes.
 * <p>
 * Created by Steve on 07.05.2016.
 */
@Named
@RequestScoped
public class Theme implements Serializable
{
	private String displayedName;
	private boolean textOnly;
	private String pathName;

	public static final Theme DEFAULT_THEME = new Theme("Default text-only theme");

	private static ArrayList<Theme> allThemes = new ArrayList<>();

	static
	{
		allThemes.add(new Theme("Text-Only"));
		allThemes.add(new Theme("Browser War", "browser"));
	}

	public static List<Theme> allThemes()
	{
		return allThemes;
	}

	//JSF constructor
	public Theme()
	{
	}
	//Graphical Theme constructor
	public Theme(String displayedName, String pathName)
	{
		this.displayedName = displayedName;
		this.pathName = pathName;
		this.textOnly = false;
	}
	//TextOnly Theme option constructor
	public Theme(String displayedName)
	{
		this.displayedName = displayedName;
		this.textOnly = true;
	}

	public String getDisplayedName()
	{
		return displayedName;
	}
	public void setDisplayedName(String displayedName)
	{
		this.displayedName = displayedName;
	}

	public boolean isTextOnly()
	{
		return textOnly;
	}
	public void setTextOnly(boolean textOnly)
	{
		this.textOnly = textOnly;
	}

	public String getPathName()
	{
		return pathName;
	}
	public void setPathName(String pathName)
	{
		this.pathName = pathName;
	}

	@Override
	public String toString()
	{
		return getDisplayedName();
	}
}
