package org.cen.cup.cup2009.gameboard.configuration;

import java.util.EventListener;

public interface DebugEventListener extends EventListener {
	public void debug(DebugEvent event);
}
