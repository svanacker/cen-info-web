package org.cen.cup.cup2011.robot.match;

import org.cen.geom.Point2D;

public class CheckPoint {
	
	private Point2D position;
	
	private String action;

	public CheckPoint(Point2D pPosition, String pAction){
		this.position = pPosition;
		
		this.action = pAction;
	}

	public Point2D getPosition() {
		return position;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	
}
