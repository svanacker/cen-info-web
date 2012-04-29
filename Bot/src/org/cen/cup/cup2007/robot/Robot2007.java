package org.cen.cup.cup2007.robot;

import org.cen.navigation.Location;
import org.cen.robot.AbstractRobot;

/**
 * Describes the robot for Eurobot 2007 and all of the devices
 * 
 * @author Stephane
 * @version 23/02/2007
 */
public class Robot2007 extends AbstractRobot {
	/**
	 * Initialization of the robot
	 */
	protected void init() {
		// RefreshRate = 30, speed = 500 mm / s, acceleration=1000 m / s²
		// controlEngine = new RobotControlEngine(30, 500, 1000);

		// PID for Theta
		// controlEngine.getTheta().setValues(5, 1, 0, 150);
		// PID for Alpha
		// controlEngine.getAlpha().setValues(5, 1, 0, 150);

		// gameBoard = new GameBoard2007(2100d, 3000d, 150d);
		// position = new RobotPosition(1950.0d, 140.0d, Math.toRadians(45));
		// position = new RobotPosition(1950.0d, 150.0d, Math.toRadians(45));
		// position = new RobotPosition(1950.0d, 150.0d, Math.toRadians(135));
		// position = getInitialPosition();

		// width, height, distance between wheels, wheel diameter, number of
		// pulse, Weight, Torque, Maximal rotation / second
		// double reductorFactor = 17.4d;
		// dimension = new RobotDimension(300, 300, 225, 74.2d, 2000, 8.0,
		// 0.0214 * reductorFactor, 5300.0d / 60.0d / reductorFactor);

		// WebCam
		// WebCamProperties wcp = new WebCamProperties();
		// // height of the camera
		// wcp.setPosition(new Vector3d(0, 0, 270));
		// // Vision angle
		// wcp.setVisionAngles(QuickCam4000.VISION_ANGLES);
		// // inclinaison
		// // wcp.setRotation(new Vector3d(Math.toRadians(21), 0, 0));
		// wcp.setVisionLength(2600);
		// // The dimension of the Camera
		// wcp.setImageDimension(new Dimension(320, 240));

		// Calibration data
		// CalibrationDescriptor descriptor = new CalibrationDescriptor(new
		// CalibrationData[] { new CalibrationData("red", 0, .5), new
		// CalibrationData("yellow", 1.03, .5), new CalibrationData("green",
		// 2.93, .5), new CalibrationData("blue", 4.11, .5), });
		// descriptor.setMaxCalibrationTime(10000);
		// webCam = new WebCam(wcp, descriptor);

		// markWhiteCircle();
		// markBorder();

		// matchStrategy = new MatchStrategy();

		// if (!hasProperty(ApplicationConst.PROPERTY_DONTWAITFORCOMPORT))
		// ComUtils.waitForSerialPort(properties.getProperty(ApplicationConst.PROPERTY_COMPORT));
		// initDevices();
	}

	protected void markBorder() {
		// Iterator i =
		// getGameBoard().getNavigationMap().getPathVectors().iterator();
		// while (i.hasNext()) {
		// PathVector v = (PathVector) i.next();
		// Location l1 = v.getStart();
		// Location l2 = v.getEnd();
		//
		// if (isToTheBorder(l1) || isToTheBorder(l2)) {
		// v.setWeight(AbstractNavigationMap.MAX_WEIGHT);
		// }
		// }
	}

	protected boolean isToTheBorder(Location location) {
		// if (location.getX() == 0 || location.getY() == 0)
		// return true;
		// if (location.getX() == getGameBoard().getXLength() || location.getY()
		// == getGameBoard().getYLength())
		// return true;
		return false;
	}

	/**
	 * Marks the zone for the white circle
	 */
	public void markWhiteCircle() {
		// int radius = 400;
		// GameBoard2007 gameBoard2007 = (GameBoard2007) this.getGameBoard();
		// Point2D position =
		// gameBoard2007.getPosition(gameBoard2007.getWhiteCirclePosition());
		// position = gameBoard2007.getPosition(0);
		// gameBoard2007.getNavigationMap().updateWeights(position, radius,
		// NavigationMap.MAX_WEIGHT);
		// position = gameBoard2007.getPosition(1);
		// gameBoard2007.getNavigationMap().updateWeights(position, radius,
		// NavigationMap.MAX_WEIGHT);
		// position = gameBoard2007.getPosition(2);
		// gameBoard2007.getNavigationMap().updateWeights(position, radius,
		// AbstractNavigationMap.MAX_WEIGHT);
		// position =
		// gameBoard2007.getPosition(GameBoard2007.OBJECT_POSITION_CAN);
		// gameBoard2007.getNavigationMap().updateWeights(position, radius,
		// 5000);
		// position =
		// gameBoard2007.getPosition(GameBoard2007.OBJECT_POSITION_BOTTLE);
		// gameBoard2007.getNavigationMap().updateWeights(position, radius,
		// 5000);
	}

	public String getName() {
		return "Robot 2007";
	}
}
