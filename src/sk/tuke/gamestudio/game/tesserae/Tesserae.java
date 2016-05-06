/*********************************************************************
 * Zadanie na predmet Komponentove Programovanie
 * <p/>
 * scsc
 * Technicka univerzita v Kosiciach, Fakulta elektrotechniky a informatiky
 * <p/>
 * Copyright: Volny softver, Open-Source GNU GPL v3+
 * Vseobecna verejna licencia. Program je dovolene volne sirit a upravovat.
 * Upraveny program / cast programu moze ktokolvek vyuzit ako na osobne,
 * tak aj komercne ucely, ale nemoze ho vydat s vlastnym copyrightom,
 * ktory nie je kompatibilny s GNU GPL v3+. < gnu.org/licenses/gpl-faq.html >
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see < http://www.gnu.org/licenses/ >.
 */

package sk.tuke.gamestudio.game.tesserae;

import sk.tuke.gamestudio.game.Game;
import sk.tuke.gamestudio.game.tesserae.core.field.Settings;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.FieldBuilder;
import sk.tuke.gamestudio.game.tesserae.core.field.builder.SimpleFieldBuilder;
import sk.tuke.gamestudio.game.tesserae.cui.ConsoleUI;
import sk.tuke.gamestudio.service.favorites.*;
import sk.tuke.sorm.SORM;

/**
 * Game tester and launcher.
 * <p/>
 * Created by Steve on 22.2.2016.
 */
public class Tesserae
{
	//Fields
	private ConsoleUI consoleUI;

	public Tesserae(ConsoleUI consoleUI)
	{
		this.consoleUI = consoleUI;

		//Runs a simple game
		consoleUI.runConsoleSession();
		//this.consoleUI.runField(this.consoleUI.generateField(Settings.SIMPLE_GAME));
	}

	private FavoriteGameDatabaseService getOracleLocalDatabaseServiceOrPrintError()
	{
		FavoriteGameDatabaseService service = null;
		//Preventively creates a database table if there is none yet
		try
		{
			Oracle11gDatabaseServiceImpl oracleService = new Oracle11gDatabaseServiceImpl();
			oracleService.createTableIfNone();
			service = oracleService;
		}
		catch (FavoriteException e)
		{
			System.out.println("Database service could not connect, game will run without this feature.");
		}
		catch (UnsatisfiedLinkError e)
		{
			System.out.println("There was problem linking the database library, game will run without this feature.");
		}
		return service;
	}

	public static void main(String[] args)
	{
		FavoriteGameDatabaseService service;

		//Online database EJB service should not crash
		service = new FavoriteGameWebServiceClient();
		//service = new FavoriteGameRestServiceClient();

		//String url = "jdbc:oracle:oci:@localhost:1521:xe";
		//String login = "gamestudio";
		//String password = "gamestudio";
		//service = new FavoriteGameServiceImplSorm(new SORM(url, login, password));

		//Empty settings waiting for player to fully configure the Field
		//FieldBuilder builder = new SimpleFieldBuilder();

		//Default settings for a simple game
		FieldBuilder builder = new SimpleFieldBuilder(Settings.SIMPLE_GAME);

		Tesserae tesserae = new Tesserae(new ConsoleUI(builder, service));
	}

	public static Game getGame()
	{
		return new Game("Tesserae");
	}
}
