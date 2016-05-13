package sk.tuke.gamestudio.game.tesserae.jsf;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Current Tesserae Theme choice.
 * Also contains static list of all available Themes.
 * <p>
 * Created by Steve on 08.05.2016.
 */
@Named
@SessionScoped
public class ThemeChooser implements Serializable
{
	public static final Theme DEFAULT_THEME = new Theme("Default text-only theme");
	private static Collection<Theme> allThemes = new LinkedList<>();

	static
	{
		allThemes.add(new Theme("Text-Only"));
		allThemes.add(new Theme("Android Classics", "android"));
		allThemes.add(new Theme("RGB Color Mixing", "rgb"));
		allThemes.add(new Theme("Browser Wars", "browser"));
	}

	private Theme theme = DEFAULT_THEME;

	public Collection<Theme> getAllThemes()
	{
		return Collections.unmodifiableCollection(allThemes);
	}

	public Theme getTheme()
	{
		return theme;
	}

	//Getter and setter for a fake-name of the Theme used for easy iteration
	//of the <p:selectOneMenu> which I've spent 3 hours trying to fix by other means
	public String getThemeByName()
	{
		return this.theme.getDisplayedName();
	}
	public void setThemeByName(String displayedName)
	{
		for (Theme theme : allThemes)
		{
			if (theme.getDisplayedName().equals(displayedName))
			{
				this.theme = theme;
				return;
			}
		}
		this.theme = null;
	}
}
