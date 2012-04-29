package org.cen.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cen.navigation.Location;

public abstract class AbstractGameActionMap implements IGameActionMap {
	Map<Location, List<IGameAction>> map = new HashMap<Location, List<IGameAction>>();

	protected void addAction(Location location, IGameAction action) {
		List<IGameAction> l = getActions(location);
		if (l == null) {
			l = new ArrayList<IGameAction>();
		}
		l.add(action);
	}

	@Override
	public List<IGameAction> getActions(Location location) {
		return map.get(location);
	}
}
