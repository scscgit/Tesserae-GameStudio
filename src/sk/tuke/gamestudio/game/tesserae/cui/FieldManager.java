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

package sk.tuke.gamestudio.game.tesserae.cui;

import sk.tuke.gamestudio.game.tesserae.core.field.Field;

import java.util.LinkedList;

/**
 * Gives a class responsibility over managing an instance of Field within the gameplay lifecycle.
 * <p/>
 * Created by Steve on 3.3.2016.
 */
public interface FieldManager
{
	//Provides a full access to the managed Field, or information of inaccessibility by returning null
	Field getManagedField();

	//When there are access rights to change the current field, takes care of fully setting up a new game
	void setManagedField(Field field);

	//Manager always handles current player name
	String getPlayer();

	//Current field color
	ColorMode getFieldColor();
	void setFieldColor(ColorMode color);

	//Goes back and forwards in time
	void fieldUpdatedCallback();
	void goBackInTime();
	LinkedList<Field> getTimeline();
}
