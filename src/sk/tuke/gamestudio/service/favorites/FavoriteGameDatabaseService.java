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

package sk.tuke.gamestudio.service.favorites;

import sk.tuke.gamestudio.entity.FavoriteGameEntity;
import sk.tuke.gamestudio.game.Game;

import java.util.List;

/**
 * Interface for a favo(u)rite game service.
 * <p>
 * Client side - uses Game objects instead of Strings for an encapsulation to allow for safe working with Games
 * (programmers can be limited to just certain singleton representations of Games uniquely distributed to each game).
 * <p/>
 * Created by Steve on 14.03.2016.
 */
public interface FavoriteGameDatabaseService
{
	void addFavorite(String player, Game game) throws FavoriteException;
	void addFavorite(FavoriteGameEntity favorite) throws FavoriteException;
	void removeFavorite(String player, Game game) throws FavoriteException;

	List<FavoriteGameEntity> getFavorites(String player) throws FavoriteException;

	//Deprecated support, will be optional for implementations
	//void recreateTable() throws FavoriteException;
	//void createTable() throws FavoriteException;
	//void dropTable() throws FavoriteException;
}
