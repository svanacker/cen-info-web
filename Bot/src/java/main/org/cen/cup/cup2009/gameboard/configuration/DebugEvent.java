package org.cen.cup.cup2009.gameboard.configuration;

public class DebugEvent {
	private static final long serialVersionUID = 1L;

	private GameboardConfigurationHandler2009 handler;

	public DebugEvent(GameboardConfigurationHandler2009 handler) {
		super();
		this.handler = handler;
	}

	public GameboardConfigurationHandler2009 getHandler() {
		return (GameboardConfigurationHandler2009) handler;
	}
}
