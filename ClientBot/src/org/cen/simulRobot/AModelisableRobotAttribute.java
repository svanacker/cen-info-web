package org.cen.simulRobot;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Point2D;

import org.cen.robot.AdvancedRobotAttribute;

public abstract class AModelisableRobotAttribute extends AdvancedRobotAttribute {

	protected Point2D bottomLeft;
	protected Point2D bottomRight;
	protected Point2D relativeCentralPoint;
	protected Shape shape;
	protected Point2D upLeft;
	protected Point2D upRight;

	public AModelisableRobotAttribute(){

	}

	protected void computeRelativeCentralPoint(){
		computeRelativeCorners();
		computeShape();
		relativeCentralPoint = new Point2D.Double(shape.getBounds().getCenterX(), shape.getBounds().getCenterY());
	}

	abstract protected void  computeRelativeCorners();

	protected void computeShape(){
		// creation de la shape
		int[] cx = new int[4];
		int[] cy = new int[4];

		cx[0] = (int) bottomLeft.getX();
		cx[1] = (int) bottomRight.getX();
		cx[2] = (int) upRight.getX();
		cx[3] = (int) upLeft.getX();

		cy[0] = (int) bottomLeft.getY();
		cy[1] = (int) bottomRight.getY();
		cy[2] = (int) upRight.getY();
		cy[3] = (int) upLeft.getY();

		shape = new Polygon(cx, cy, 4);
	}

	public Point2D getRelativeCentralPoint() {
		return relativeCentralPoint;
	}

	public Shape getRelativeShape() {
		computeRelativeCorners();
		computeShape();
		return shape;
	}
}
