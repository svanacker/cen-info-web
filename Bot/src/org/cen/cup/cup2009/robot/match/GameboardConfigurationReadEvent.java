package org.cen.cup.cup2009.robot.match;

import org.cen.robot.match.IMatchEvent;

public class GameboardConfigurationReadEvent implements IMatchEvent {
	private int card;

	public GameboardConfigurationReadEvent(int columnElementsCard) {
		super();
		card = columnElementsCard;
	}

	public int getCard() {
		return card;
	}
}
