package org.cen.actions;

import java.util.List;

import org.cen.navigation.Location;

public interface IGameActionMap {
	/**
	 * Returns the game actions associated to the given map location.
	 * 
	 * @param location
	 *            the map location
	 * @return the list of actions associated to the given map location
	 */
	public List<IGameAction> getActions(Location location);
}
