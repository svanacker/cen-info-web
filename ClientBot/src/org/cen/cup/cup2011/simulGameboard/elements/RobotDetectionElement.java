package org.cen.cup.cup2011.simulGameboard.elements;

import java.awt.Color;
import org.cen.simulRobot.AModelisableRobotAttribute;
import org.cen.ui.gameboard.elements.ARobotAttributeElement;
import org.cen.ui.gameboard.elements.ARobotElement;

public class RobotDetectionElement extends ARobotAttributeElement{

	public RobotDetectionElement(String NAME,
			AModelisableRobotAttribute pModelisableRobotArea,
			ARobotElement pRobotElement) {
		super(NAME, pModelisableRobotArea, pRobotElement);
		color = new Color(100, 100, 0, 95);
	}
}
