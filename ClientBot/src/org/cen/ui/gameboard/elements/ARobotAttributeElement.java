package org.cen.ui.gameboard.elements;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.cen.simulRobot.AModelisableRobotAttribute;

public abstract class ARobotAttributeElement extends AMovableElement {

	private Point2D relativePosition;
	protected ARobotElement robotElement;

	public ARobotAttributeElement(String NAME, AModelisableRobotAttribute pModelisableRobotArea,
			ARobotElement pRobotElement) {
		super(NAME, pModelisableRobotArea.getRelativeCentralPoint(), pRobotElement.getOrientation());
		robotElement = pRobotElement;
		position = relativeToAbsolute(pRobotElement, pModelisableRobotArea.getRelativeCentralPoint());
		this.relativePosition = pModelisableRobotArea.getRelativeCentralPoint();
		bounds = pModelisableRobotArea.getRelativeShape();

		AffineTransform transform = new AffineTransform();
		transform.translate(- relativePosition.getX(), - relativePosition.getY());
		bounds = transform.createTransformedShape(bounds);
	}

	@Override
	public double getOrientation() {
		return robotElement.getOrientation();
	}

	@Override
	public Point2D getPosition() {
		return relativeToAbsolute(robotElement, relativePosition);
	}

	@Override
	public void paint(Graphics2D g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setPaint(color);
		g2d.setStroke(stroke);
		AffineTransform transform = new AffineTransform();
		transform.translate(relativePosition.getX(), relativePosition.getY());
		g2d.fill(transform.createTransformedShape(bounds));

	}

	private Point2D relativeToAbsolute(ARobotElement robotElement, Point2D pRelative){
		double xx = pRelative.getX();
		double yy = pRelative.getY();
		double d = Math.sqrt(xx * xx + yy * yy);
		Point2D cp = robotElement.getPosition();
		double a = robotElement.getOrientation()  + Math.atan2(yy, xx) ;
		Point2D absolute = new Point2D.Double(d * Math.cos(a) + cp.getX(), d * Math.sin(a) + cp.getY());
		return absolute;
	}
}
