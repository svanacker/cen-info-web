package org.cen.ui.gameboard.robot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotUtils;
import org.cen.robot.match.Opponent;
import org.cen.ui.gameboard.ShapeData;

public class OpponentRobotPainter {

	protected IRobotServiceProvider servicesProvider;

	private ShapeData[] opponentShapeData;

	public OpponentRobotPainter(IRobotServiceProvider servicesProvider) {
		this.servicesProvider = servicesProvider;
	}

	public ShapeData[] getOpponentShapeData() {
		if (opponentShapeData == null) {
			Paint gaugeColor = new Color(0x80ffc0c0, true);
			Shape gaugeShape = new Area(new Ellipse2D.Double(-175, -175, 350, 350));
			Paint outlineColor = Color.PINK;
			Stroke outlineStroke = new BasicStroke(5, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, new float[] {
					15, 15 }, 0);
			Shape outlineShape = new Area(outlineStroke.createStrokedShape(gaugeShape));

			opponentShapeData = new ShapeData[] { new ShapeData(gaugeShape, null, gaugeColor),
					new ShapeData(outlineShape, outlineStroke, outlineColor) };
		}
		return opponentShapeData;
	}

	public void paint(Graphics2D g) {
		Opponent opponent = RobotUtils.getRobotAttribute(Opponent.class, servicesProvider);
		if (opponent == null) {
			return;
		}
		Point2D opponentPosition = opponent.getLastLocation();
		if (opponentPosition == null) {
			return;
		}

		AffineTransform oldTx = g.getTransform();

		AffineTransform newTx = new AffineTransform(oldTx);

		newTx.translate(opponentPosition.getX(), opponentPosition.getY());

		g.setTransform(newTx);

		ShapeData[] shapeData = getOpponentShapeData();
		for (ShapeData data : shapeData) {
			Paint paint = data.getPaint();
			Stroke stroke = data.getStroke();
			if (paint != null) {
				g.setPaint(paint);
			}
			if (stroke != null) {
				g.setStroke(stroke);
			}
			g.fill(data.getShape());
		}

		// restore AffineTransform
		g.setTransform(oldTx);
	}
}
