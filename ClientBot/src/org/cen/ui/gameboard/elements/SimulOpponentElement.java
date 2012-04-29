package org.cen.ui.gameboard.elements;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.Properties;

import org.cen.cup.cup2011.simulGameboard.elements.RobotDetectionElement;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.MatchSide;
import org.cen.simulRobot.match.simulOpponent.RobotDetectionArea;

public class SimulOpponentElement extends ARobotElement {

	public static final String NAME = "opponent";
	private static final int RADIUS_BASE = 150;
	private final ARobotAttributeElement robotDetectionElement;

	public SimulOpponentElement(Properties pProperties, MatchData pData) {
		super(NAME, pProperties, pData);
		setFromProperties(pProperties, "initialPosition" + "."
				+ ((pData.getSide().equals(MatchSide.RED)) ? MatchSide.VIOLET : MatchSide.RED) + ".");
		bounds = new Ellipse2D.Float(-RADIUS_BASE, -RADIUS_BASE, (RADIUS_BASE * 2), (RADIUS_BASE * 2));
		robotDetectionElement = new RobotDetectionElement("robotDetection", new RobotDetectionArea(), this);
		color = new Color(0, 0, 0, 95);
	}

	public ARobotAttributeElement getRobotDetectionElement() {
		return robotDetectionElement;
	}
}
