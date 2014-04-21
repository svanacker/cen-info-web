package org.cen.cup.cup2007.gameboard;

import java.awt.geom.Point2D;

import org.cen.robot.GameBoard;

/**
 * Board for the Eurobot 2007.
 * 
 * @author Stephane
 * @version 27/02/2007
 */
public class GameBoard2007 extends GameBoard {

	/** The fixed position object */
	public static final int OBJECT_POSITION_LEFT = 0;

	/** The second position object */
	public static final int OBJECT_POSITION_MIDDLE = 1;

	/** The third position object */
	public static final int OBJECT_POSITION_RIGHT = 2;

	public static final int OBJECT_POSITION_BOTTLE = 3;

	public static final int OBJECT_POSITION_CAN = 4;

	protected int redBatteryPosition;

	protected int blueBatteryPosition;

	protected int whiteCirclePosition;

	public GameBoard2007(double xLength, double yLength, double gridSize) {
		super(xLength, yLength, gridSize);
		redBatteryPosition = OBJECT_POSITION_RIGHT;
		blueBatteryPosition = OBJECT_POSITION_MIDDLE;
		whiteCirclePosition = OBJECT_POSITION_LEFT;
	}

	public Point2D.Double getPosition(int objectPosition) {
		switch (objectPosition) {
		case OBJECT_POSITION_LEFT:
			return new Point2D.Double(350, 1500);
		case OBJECT_POSITION_MIDDLE:
			return new Point2D.Double(700, 1500);
		case OBJECT_POSITION_RIGHT:
			return new Point2D.Double(1050, 1500);
		case OBJECT_POSITION_BOTTLE:
			return new Point2D.Double(1400, 1500);
		case OBJECT_POSITION_CAN:
			return new Point2D.Double(1750, 1500);
		}
		return null;
	}

	public int getBlueBatteryPosition() {
		return blueBatteryPosition;
	}

	public void setBlueBatteryPosition(int blueBatteryPosition) {
		this.blueBatteryPosition = blueBatteryPosition;
	}

	public int getWhiteCirclePosition() {
		return whiteCirclePosition;
	}

	public void setWhiteCirclePosition(int whiteCirclePosition) {
		this.whiteCirclePosition = whiteCirclePosition;
	}

	public int getRedBatteryPosition() {
		return redBatteryPosition;
	}

	public void setRedBatteryPosition(int redBatteryPosition) {
		this.redBatteryPosition = redBatteryPosition;
	}
}
