package org.cen.ui.gameboard.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.Properties;

import org.cen.robot.match.MatchData;

public class SimulRobotElement extends ARobotElement {

	public static final String NAME = "robot";

	public SimulRobotElement(Properties pProperties, MatchData pData) {
		super(NAME, pProperties, pData);
		setFromProperties( pProperties, "initialPosition" + "." + pData.getSide() + ".");
		int xPoints[] = new int[] { 62, -62, -150, -150, -62, 62 };
		int yPoints[] = new int[] { -150, -150, -62, 62, 150, 150 };
		bounds = new Polygon(xPoints, yPoints, xPoints.length);
		color = new Color(220, 220, 192);
		stroke = new BasicStroke(10f);

	}

	@Override
	public boolean isObstacle() {
		return false;
	}


	public void setAlpha(double pAlpha){
		orientation = pAlpha;
	}

	public void setPosition(double x, double y){
		position = new Point2D.Double(x, y);
	}

	@Override
	public void setPosition(Point2D pPosition){
		position = pPosition;
	}

}
