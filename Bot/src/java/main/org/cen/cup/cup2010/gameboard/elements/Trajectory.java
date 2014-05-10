package org.cen.cup.cup2010.gameboard.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.util.List;

import org.cen.adapter.AwtAdapterUtils;
import org.cen.geom.Point2D;
import org.cen.ui.gameboard.AbstractGameBoardElement;

public class Trajectory extends AbstractGameBoardElement {
    private static final Color GAUGE_COLOR = new Color(0x20ffff00, true);

    private static final Stroke GAUGE_STROKE = new BasicStroke(320, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private static final Stroke OUTLINE_STROKE = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10,
            new float[] { 15, 15 }, 0);

    private static final Color[] TRAJECTORY_COLOR = { Color.BLUE, Color.PINK, Color.ORANGE };

    private static final Stroke TRAJECTORY_STROKE = new BasicStroke(5);

    private final int index;

    private final List<Point2D> positions;

    public Trajectory(String name, List<Point2D> positions, int index) {
        super(name, new Point2D.Double(0, 0));
        this.positions = positions;
        this.index = index;
        order = 100;
    }

    @Override
    public void paint(Graphics2D g) {
        Graphics2D g2d = g;
        Area area = new Area();
        Point2D start = null;
        g.setColor(TRAJECTORY_COLOR[index]);
        g2d.setStroke(TRAJECTORY_STROKE);
        for (Point2D end : positions) {
            if (start != null) {
                Line2D line = new Line2D.Double(AwtAdapterUtils.toAwtPoint2D(start), AwtAdapterUtils.toAwtPoint2D(end));
                area.add(new Area(GAUGE_STROKE.createStrokedShape(line)));
                g2d.draw(line);
            }
            start = end;
        }
        g.setColor(GAUGE_COLOR);
        g2d.fill(area);
        g.setColor(Color.WHITE);
        g2d.setStroke(OUTLINE_STROKE);
        g2d.draw(area);
    }
}
