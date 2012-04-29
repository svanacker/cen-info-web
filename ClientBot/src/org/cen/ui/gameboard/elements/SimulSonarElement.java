package org.cen.ui.gameboard.elements;

import java.awt.Color;

import org.cen.cup.cup2011.simulRobot.SonarArea;
public class SimulSonarElement extends ARobotAttributeElement {

	public static final String NAME = "sonarArea";

	public SimulSonarElement(SonarArea pSonarArea, SimulRobotElement pRobotElement) {
		super(NAME, pSonarArea, pRobotElement);
		color = new Color(255, 0, 0, 90);
		order = 2;
	}
}
