package org.cen.ui.gameboard.trajectory;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.List;

import org.cen.navigation.Location;
import org.cen.ui.gameboard.elements.util.DrawHelper;

public class TrajectoryPainter {

	private final List<Location> trajectory;

	public TrajectoryPainter(List<Location> trajectory) {
		this.trajectory = trajectory;
	}

	public void paint(Graphics2D g2d) {
		// Draws the trajectory
		g2d.setColor(Color.PINK);
		g2d.setStroke(new BasicStroke(4));
		Location prec = null;
		for (Location l : trajectory) {
			if (prec != null) {
				int x1 = l.getX();
				int y1 = l.getY();
				int x2 = prec.getX();
				int y2 = prec.getY();
				g2d.drawLine(x1, y1, x2, y2);
			}
			prec = l;
		}
		// Draws the start and end of points that the robot must reach
		if (trajectory.size() > 0) {
			paintMapPoint(g2d, trajectory.get(0).getPosition(), Color.GREEN);
			paintMapPoint(g2d, trajectory.get(trajectory.size() - 1).getPosition(), Color.RED);
		}
	}

	private void paintMapPoint(Graphics2D g, Point2D point, Color color) {
		g.setColor(color);
		int x = (int) point.getX();
		int y = (int) point.getY();
		DrawHelper.drawCenteredCircle(g, x, y, 25d);
	}
}
