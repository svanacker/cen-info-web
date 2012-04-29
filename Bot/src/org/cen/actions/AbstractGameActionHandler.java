package org.cen.actions;

public abstract class AbstractGameActionHandler implements IGameActionHandler {
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[description=" + getDescription() + ", position=" + getPositionOnRobot() + "]";
	}
}
