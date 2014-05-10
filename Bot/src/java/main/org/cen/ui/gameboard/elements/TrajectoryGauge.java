package org.cen.ui.gameboard.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import org.cen.geom.Point2D;
import org.cen.ui.gameboard.AbstractGameBoardElement;

/**
 * Graphical representation of the robot's gauge on a trajectory path.
 * 
 * @author Emmanuel ZURMELY
 */
public class TrajectoryGauge extends AbstractGameBoardElement {
    private static final Color GAUGE_COLOR = new Color(0x20ffff00, true);
    private static final Stroke GAUGE_STROKE = new BasicStroke(320, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final Stroke OUTLINE_STROKE = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10,
            new float[] { 15, 15 }, 0);
    private final Area gauge;

    public TrajectoryGauge(String name, ITrajectoryPath... paths) {
        super(name, new Point2D.Double(0, 0));
        // always drawn on top
        order = 1000;

        AffineTransform t = new AffineTransform();
        gauge = new Area();
        for (ITrajectoryPath path : paths) {
            Point2D p = path.getPosition();
            t.setToTranslation(p.getX(), p.getY());
            Shape s = path.getPath();
            Area a = new Area(GAUGE_STROKE.createStrokedShape(s));
            a.transform(t);
            gauge.add(a);
        }
    }

    @Override
    public void paint(Graphics2D g) {
        Graphics2D g2d = g;
        g.setColor(GAUGE_COLOR);
        g2d.fill(gauge);
        g.setColor(Color.WHITE);
        g2d.setStroke(OUTLINE_STROKE);
        g2d.draw(gauge);
    }
}
