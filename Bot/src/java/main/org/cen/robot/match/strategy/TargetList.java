package org.cen.robot.match.strategy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TargetList implements ITargetList {
	protected Map<String, ITarget> targets = new HashMap<String, ITarget>();

	@Override
	public Iterator<ITarget> iterator() {
		return targets.values().iterator();
	}

	@Override
	public void registerTarget(ITarget target) {
		targets.put(target.getName(), target);
	}

	@Override
	public ITarget getTarget(String name) {
		return targets.get(name);
	}
}
