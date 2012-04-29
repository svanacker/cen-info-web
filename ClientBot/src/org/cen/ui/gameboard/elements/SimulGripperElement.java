package org.cen.ui.gameboard.elements;

import java.awt.Color;

import org.cen.cup.cup2011.simulRobot.GripperArea;
public class SimulGripperElement extends ARobotAttributeElement {

	public static final String NAME = "gripperArea";

	public SimulGripperElement(GripperArea pGripperArea, SimulRobotElement pRobotElement) {
		super(NAME, pGripperArea, pRobotElement);
		color = new Color(100, 100, 0, 95);
		order = 2;
	}
}
