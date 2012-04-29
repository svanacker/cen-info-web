package org.cen.simulRobot.match.simulOpponent;

import java.awt.geom.Point2D;

import org.cen.simulRobot.AModelisableRobotAttribute;

public class RobotDetectionArea extends AModelisableRobotAttribute{

	public RobotDetectionArea(){
		computeRelativeCentralPoint();
	}

	@Override
	protected void computeRelativeCorners() {
		//inversion des x et des y pour compenser les different repéres
		upLeft = new Point2D.Double(300, -300);
		upRight = new Point2D.Double(300, 300);
		bottomLeft = new Point2D.Double(0, -50 );
		bottomRight = new Point2D.Double(0, 50);
	}
}
