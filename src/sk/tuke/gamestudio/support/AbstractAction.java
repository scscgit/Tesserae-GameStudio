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

package sk.tuke.gamestudio.support;

/**
 * Ready, set, action!
 * Chain of responsibility superclass.
 * Supports input type (that each action receives) and output type (that only gets returned once after chain
 * execution).
 * <p/>
 * Created by Steve on 23.11.2015.
 */
public abstract class AbstractAction<Input, Output>
{
	private /*protected*/ AbstractAction<Input, Output> nextAction;

	public AbstractAction()
	{
		this.nextAction = null;
	}

	//Nastavi novu nasledujucu akciu pre (aktualnu) danu akciu
	public final void setNextAction(AbstractAction<Input, Output> action)
	{
		this.nextAction = action;
	}

	//Ak existuje nasledujuca akcia, tak sa spusti
	public final Output doNextAction(Input actor)
	{
		if (this.nextAction != null)
		{
			return nextAction.doAction(actor);
		}
		return null;
	}

	//Kazda akcia by mala spustat nasledujucu akciu, no zalezi na implementacii.
	//Vratenim hodnoty typu Output sa skonci retazec vykonavania.
	public Output doAction(Input actor)
	{
		return doNextAction(actor);
	}
}
