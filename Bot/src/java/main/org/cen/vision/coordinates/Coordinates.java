package org.cen.vision.coordinates;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import javax.vecmath.Vector3d;

import org.cen.math.Size2D;
import org.cen.robot.RobotPosition;

/**
 * Conversion of screen coordinates to 3D coordinates.
 * 
 * @author Emmanuel ZURMELY
 */
public class Coordinates implements CoordinatesTransform {
	public static void main(String[] args) {
		Coordinates coordinates = new Coordinates();

		coordinates.setImageDimension(new Dimension(320, 240));
		coordinates.setInclination(Math.toRadians(40d));
		coordinates.setOrigin(new Vector3d(0, 0, 380));
		coordinates.setVisionAngles(new Size2D(Math.toRadians(45), Math.toRadians(45 * 0.75)));

		int x = 165;
		int y = 0;

		Point2D p1 = coordinates.screenToGameBoard(x, y);
		Point2D p2 = coordinates.gameBoardToScreen((int) p1.getX(), (int) p1.getY());
		System.out.println(p1);
		System.out.println(p2);
	}

	/**
	 * Dimension of the image in pixels.
	 */
	private Dimension imageDimension;

	/**
	 * Inclination of the webcam relative to the horizontal plan.
	 */
	private double inclination;

	/**
	 * Position of the origin.
	 */
	private Vector3d origin;

	/**
	 * Angles of vision.
	 */
	private Size2D visionAngles;

	/**
	 * Orientation.
	 */
	private double orientation;

	@Override
	public Point2D absoluteToScreen(RobotPosition position, Point2D p) {
		Point2D cp = position.getCentralPoint();
		double xx = p.getX() - cp.getX();
		double yy = p.getY() - cp.getY();
		double d = Math.sqrt(xx * xx + yy * yy);
		double a = Math.atan2(yy, xx) - position.getAlpha() - orientation;
		p = gameBoardToScreen(d * Math.cos(a), d * Math.sin(a));
		return p;
	}

	@Override
	public Point2D gameBoardToScreen(double x, double y) {
		double ax = Math.atan2(y, x);
		double ay = Math.atan2(x, origin.z);

		double gy = ay + inclination - Math.PI / 2;
		int h = imageDimension.height;
		double yy = (h / 2) - (gy * h / visionAngles.getHeight());

		int w = imageDimension.width;
		double xx = (w / 2) - (ax * w / visionAngles.getWidth());
		return new Point2D.Double(xx, yy);
	}

	private double getAngleX(int x) {
		int w = imageDimension.width;
		return visionAngles.getWidth() * (w / 2 - x) / w;
	}

	private double getAngleY(int y) {
		int h = imageDimension.height;
		return visionAngles.getHeight() * (h / 2 - y) / h;
	}

	public Dimension getImageDimension() {
		return imageDimension;
	}

	public double getInclination() {
		return inclination;
	}

	public Vector3d getOrigin() {
		return origin;
	}

	public Size2D getVisionAngles() {
		return visionAngles;
	}

	@Override
	public Point2D screenToAbsolute(RobotPosition position, int x, int y) {
		Point2D p = screenToGameBoard(x, y);
		double xx = p.getX();
		double yy = p.getY();
		double d = Math.sqrt(xx * xx + yy * yy);
		Point2D cp = position.getCentralPoint();
		double a = position.getAlpha() + orientation + Math.atan2(yy, xx);
		p.setLocation(d * Math.cos(a) + cp.getX(), d * Math.sin(a) + cp.getY());
		return p;
	}

	@Override
	public Point2D screenToGameBoard(int x, int y) {
		double ax = getAngleX(x);
		double ay = Math.PI / 2 - inclination + getAngleY(y);
		double yy = Math.tan(ay) * origin.z;
		double xx = -Math.tan(ax) * yy;
		return new Point2D.Double(yy, -xx);
	}

	public void setImageDimension(Dimension imageDimension) {
		this.imageDimension = imageDimension;
	}

	public void setInclination(double inclination) {
		this.inclination = inclination;
	}

	/**
	 * Sets the orientation of the webcam around the z axis. An orientation of
	 * 0° points towards the increasing x-axis, an orientation of +90° points
	 * towards the increasing y-axis.
	 * 
	 * @param orientation
	 */
	public void setOrientation(double orientation) {
		this.orientation = orientation;
	}

	public void setOrigin(Vector3d origin) {
		this.origin = origin;
	}

	public void setVisionAngles(Size2D visionAngles) {
		this.visionAngles = visionAngles;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{originX=" + origin.x + ", originY=" + origin.y + ", originZ=" + origin.z + "\n" + ", inclination=" + inclination + "\n" + ", visionAngles.Height=" + visionAngles.getHeight() + " visionAngles.width=" + visionAngles.getWidth() + "\n" + ", image.height=" + imageDimension.height + ", image.width=" + imageDimension.width + "}";
	}
}
