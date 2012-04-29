package org.cen.ui.gameboard.elements;

import java.awt.Color;

import org.cen.cup.cup2011.simulRobot.VisionArea;
public class SimulVisionElement extends ARobotAttributeElement {

	public static final String NAME = "visionArea";

	public SimulVisionElement(VisionArea pVisionArea, SimulRobotElement pRobotElement) {
		super(NAME, pVisionArea, pRobotElement);
		color = new Color(255, 215, 0, 95);
		order = 2;
	}
}
