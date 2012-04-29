package org.cen.cup.cup2011.simulRobot;

import java.awt.Dimension;

import javax.vecmath.Vector3d;

import org.cen.robot.RobotPosition;
import org.cen.simulRobot.AModelisableRobotAttribute;
import org.cen.vision.coordinates.Coordinates;
import org.cen.vision.coordinates.CoordinatesFactory;
import org.cen.vision.dataobjects.WebCamProperties;

public class VisionArea extends AModelisableRobotAttribute{

	private int screenHeight;
	private int screenWidth;
	private WebCamProperties wcp;

	public VisionArea(WebCamProperties pwcp){
		this.wcp = pwcp;
		Dimension screenDimension = pwcp.getImageDimension();
		screenHeight = (int)screenDimension.getHeight();
		screenWidth = (int)screenDimension.getWidth();
		computeRelativeCentralPoint();
	}

	@Override
	protected void computeRelativeCorners(){

		RobotPosition tempRobotPosition = new RobotPosition(0, 0, 0);
		Coordinates c = getCoordinates(tempRobotPosition);

		//construction des coordonnées du champs de vision
		upLeft = c.screenToGameBoard(0, screenHeight/3);
		upRight = c.screenToGameBoard(screenWidth, screenHeight/3 );
		bottomLeft = c.screenToGameBoard(0, screenHeight);
		bottomRight = c.screenToGameBoard(screenWidth, screenHeight);
	}

	private Coordinates getCoordinates(RobotPosition pTempRobotPosition) {
		wcp.setPosition(new Vector3d(pTempRobotPosition.getCentralPoint().getX(), pTempRobotPosition.getCentralPoint().getY(), wcp.getPosition().z));
		Coordinates c = (Coordinates) CoordinatesFactory.getCoordinates(wcp);
		return c;
	}
}
