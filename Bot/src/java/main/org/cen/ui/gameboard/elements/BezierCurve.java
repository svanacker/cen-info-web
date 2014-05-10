package org.cen.ui.gameboard.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import org.cen.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cen.ui.gameboard.AbstractGameBoardElement;

/**
 * Graphical representation of a bezier curve.
 * 
 * @author Emmanuel ZURMELY
 */
public class BezierCurve extends AbstractGameBoardElement implements ITrajectoryPath {
	private Point2D controlPoint1;

	private Point2D controlPoint2;

	private Point2D end;

	private Path2D path;

	private Path2D trajectory;

	private BasicStroke stroke;

	private Point2D start;

	private double initialAngle;

	private double finalAngle;

	public BezierCurve(String name, Point2D start, Point2D end, double cp1Distance, double cp2Distance, double initialAngle, double finalAngle) {
		super(name, start);
		this.start = start;
		this.end = end;
		this.initialAngle = initialAngle;
		this.finalAngle = finalAngle;

		// Distance
		double d = cp1Distance;

		// P1
		double x = start.getX() + d * Math.cos(initialAngle);
		double y = start.getY() + d * Math.sin(initialAngle);
		this.controlPoint1 = new Point2D.Double(x, y);

		// P2
		d = cp2Distance;
		x = end.getX() - d * Math.cos(finalAngle);
		y = end.getY() - d * Math.sin(finalAngle);
		this.controlPoint2 = new Point2D.Double(x, y);

		buildData();
	}

	public BezierCurve(String name, Point2D start, Point2D end, Point2D controlPoint1, Point2D controlPoint2, double initialAngle, double finalAngle) {
		super(name, start);
		this.start = start;
		this.end = end;
		this.controlPoint1 = controlPoint1;
		this.controlPoint2 = controlPoint2;
		this.initialAngle = initialAngle;
		this.finalAngle = finalAngle;
		buildData();
	}

	private void buildData() {
		double x = start.getX();
		double y = start.getY();
		Point2D end = new Point2D.Double(this.end.getX() - x, this.end.getY() - y);
		Point2D controlPoint1 = new Point2D.Double(this.controlPoint1.getX() - x, this.controlPoint1.getY() - y);
		Point2D controlPoint2 = new Point2D.Double(this.controlPoint2.getX() - x, this.controlPoint2.getY() - y);

		Dimension dimension = new Dimension(20, 20);
		path = new Path2D.Double();
		trajectory = new Path2D.Double();

		// Points
		addPoint(trajectory, start, dimension);
		addPoint(trajectory, end, dimension);
		addPoint(trajectory, controlPoint1, dimension);
		addPoint(trajectory, controlPoint2, dimension);

		// Curve
		path.moveTo(0, 0);
		path.curveTo(controlPoint1.getX(), controlPoint1.getY(), controlPoint2.getX(), controlPoint2.getY(), end.getX(), end.getY());

		trajectory.append(path, false);

		stroke = new BasicStroke(5);
	}

	private void addPoint(Path2D path, Point2D point, Dimension dimension) {
		double x = point.getX();
		double y = point.getY();
		path.append(new Rectangle2D.Double(x - dimension.width / 2, y - dimension.height / 2, dimension.width, dimension.height), false);
	}

	@Override
	public double getRobotFinalAngle() {
		return finalAngle;
	}

	@Override
	public double getRobotInitialAngle() {
		return initialAngle;
	}

	public Shape getPath() {
		return path;
	}

	@Override
	public Point2D[] getTrajectoryControlPoints() {
		return new Point2D[] { controlPoint1, controlPoint2 };
	}

	@Override
	public Point2D getTrajectoryEnd() {
		return end;
	}

	@Override
	public Point2D getTrajectoryStart() {
		return start;
	}

	@Override
	public void paint(Graphics2D g) {
		g.setStroke(stroke);
		g.setColor(Color.BLUE);
		g.draw(trajectory);
	}
}
