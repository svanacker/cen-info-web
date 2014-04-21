package org.cen.actions;

import java.util.ArrayList;
import java.util.Collection;

import org.cen.robot.IRobotAttribute;

public class GameActionAttribute implements IRobotAttribute {
	protected Collection<IGameActionHandler> handlers = new ArrayList<IGameActionHandler>();

	public Collection<IGameActionHandler> getHandlers() {
		return handlers;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[handlers=" + handlers + "]";
	}
}
