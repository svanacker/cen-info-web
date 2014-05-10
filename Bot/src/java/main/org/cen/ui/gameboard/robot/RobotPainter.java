package org.cen.ui.gameboard.robot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;

import org.cen.robot.CollisionConfiguration;
import org.cen.robot.attributes.IRobotDimension;
import org.cen.robot.attributes.impl.RobotPosition;
import org.cen.robot.services.IRobotServiceProvider;
import org.cen.robot.utils.RobotUtils;
import org.cen.ui.gameboard.ShapeData;

public class RobotPainter {

	protected IRobotServiceProvider servicesProvider;

	private ShapeData[] robotShapeData;

	public RobotPainter(IRobotServiceProvider servicesProvider) {
		this.servicesProvider = servicesProvider;
	}

	private ShapeData[] buildShapeData(IRobotServiceProvider servicesProvider) {
		ShapeData[] result = null;

		// builds the default robot shape
		int xPoints[] = new int[] { 62, -62, -150, -150, -62, 62 };
		int yPoints[] = new int[] { -150, -150, -62, 62, 150, 150 };
		Shape shape = new Polygon(xPoints, yPoints, xPoints.length);
		Stroke stroke = new BasicStroke(10f);
		Paint paint = new Color(220, 220, 192);

		CollisionConfiguration configuration = RobotUtils.getRobotAttribute(CollisionConfiguration.class,
				servicesProvider);
		if (configuration != null) {
			IRobotDimension dimensions = RobotUtils.getRobotAttribute(IRobotDimension.class, servicesProvider);
			double d = configuration.getDistance();
			double w = dimensions.getWidth() + d * 2;
			double h = dimensions.getDepth() + d * 2;
			double o = configuration.getOffset();
			Arc2D.Double arc = new Arc2D.Double(-w / 2, -h / 2 + o, w, h, configuration.getStartAngle(),
					configuration.getExtent(), Arc2D.PIE);
			Shape area = new Area(arc);
			Paint areaPaint = new Color(50, 50, 50, 128);

			result = new ShapeData[] { new ShapeData(area, null, areaPaint), new ShapeData(shape, stroke, paint) };
		} else {
			result = new ShapeData[] { new ShapeData(shape, stroke, paint) };
		}
		return result;
	}

	public ShapeData[] getRobotShapeData() {
		if (robotShapeData == null) {
			robotShapeData = buildShapeData(servicesProvider);
		}
		return robotShapeData;
	}

	public void paint(Graphics2D g2d) {
		RobotPosition robotPosition = RobotUtils.getRobotAttribute(RobotPosition.class, servicesProvider);
		if (robotPosition == null) {
			return;
		}

		AffineTransform oldTx = g2d.getTransform();

		AffineTransform newTx = new AffineTransform(oldTx);

		newTx.translate(robotPosition.getCentralPoint().getX(), robotPosition.getCentralPoint().getY());
		newTx.rotate(robotPosition.getAlpha());

		g2d.setTransform(newTx);

		ShapeData[] shapeData = getRobotShapeData();
		for (ShapeData data : shapeData) {
			Paint paint = data.getPaint();
			Stroke stroke = data.getStroke();
			if (paint != null) {
				g2d.setPaint(paint);
			}
			if (stroke != null) {
				g2d.setStroke(stroke);
			}
			g2d.fill(data.getShape());
		}

		// restore AffineTransform
		g2d.setTransform(oldTx);
	}
}
