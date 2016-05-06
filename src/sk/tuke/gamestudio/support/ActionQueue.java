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
 * Defines methods for an easy queue of Actions.
 * This system can be included in any class by extending from it.
 * Each call of runActions() method takes Input, which gets passed to other actions and returns Output.
 * <p/>
 * Created by Steve on 24.12.2015.
 */
public class ActionQueue<Input, Output>
{
	private AbstractAction<Input, Output> first;
	private AbstractAction<Input, Output> last;

	public ActionQueue()
	{
		resetActions();
	}

	//Deletes all actions and lets the garbage collector do the job it was meant to do
	public final ActionQueue<Input, Output> resetActions()
	{
		this.first = null;
		//this.last is implicitly deleted when this.first becomes null
		return this;
	}

	//Adds a new action to the "linked" list
	public final ActionQueue<Input, Output> addAction(AbstractAction<Input, Output> action)
	{
		if (this.first == null)
		{
			//If no action was added yet, the action will be a first action
			this.first = action;
			this.last = action;
		}
		else
		{
			this.last.setNextAction(action);
			this.last = action;
		}
		return this;
	}

	//Runs all linked actions on target type object
	public Output runActions(Input target)
	{
		if (this.first != null)
		{
			return this.first.doAction(target);
		}
		return null;
	}

	//Implicitly runs actions without any object of interest in the input.
	//It is advised to override this by some default behavior.
	public Output runActions()
	{
		return runActions(null);
	}
}
