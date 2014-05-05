package org.cen.navigation;

import static org.junit.Assert.assertEquals;

import java.awt.geom.Point2D;
import java.util.List;

import org.cen.robot.TrajectoryCurve;
import org.cen.robot.TrajectoryCurve.Direction;
import org.junit.Before;
import org.junit.Test;

public class TrajectoryCurveComputerTest {

    private TrajectoryCurveComputer computer;

    private double delta;

    @Before
    public void setUp() {
        computer = new TrajectoryCurveComputer();
        computer.setWidth(1);
        delta = 0.001;
    }

    private void testCurve(TrajectoryCurve curve, double expectedAngle, double expectedRadius, double expectedDistance,
            Direction expectedDirection) {
        computer.compute(curve);
        double angle = curve.getAngle();
        Direction direction = curve.getDirection();
        double distance = curve.getDistance();
        double radius = curve.getRadius();
        assertEquals("angle", expectedAngle, angle, delta);
        assertEquals("direction", expectedDirection, direction);
        assertEquals("distance", expectedDistance, distance, delta);
        assertEquals("radius", expectedRadius, radius, delta);
    }

    private void testControlPoint(TrajectoryCurve curve, double expectedStartOrientation,
            double expectedEndOrientation, Point2D expectedStartPosition, Point2D expectedEndPosition,
            Point2D expectedICR) {
        computer.addCurve(curve);
        List<TrajectoryControlPoint> cp = computer.getTrajectory(new Point2D.Double(0, 0), 0);
        TrajectoryControlPoint point = cp.get(0);
        assertEquals("start orientation", expectedStartOrientation, point.getStartOrientation(), delta);
        assertEquals("end orientation", expectedEndOrientation, point.getEndOrientation(), delta);
        assertEquals("start position x", expectedStartPosition.getX(), point.getStartPosition().getX(), delta);
        assertEquals("start position y", expectedStartPosition.getY(), point.getStartPosition().getY(), delta);
        assertEquals("end position x", expectedEndPosition.getX(), point.getEndPosition().getX(), delta);
        assertEquals("end position y", expectedEndPosition.getY(), point.getEndPosition().getY(), delta);
        assertEquals("icr x", expectedICR.getX(), point.getICR().getX(), delta);
        assertEquals("icr y", expectedICR.getY(), point.getICR().getY(), delta);
    }

    @Test
    public void testControlPointStraight_1() {
        TrajectoryCurve curve = new TrajectoryCurve(15.707963267948966192313216916398,
                17.278759594743862811544538608037);
        testControlPoint(curve, 0, Math.PI / 2d, new Point2D.Double(0, 0), new Point2D.Double(-10.5, 10.5),
                new Point2D.Double(-10.5, 0));
    }

    @Test
    public void testLeft_1() {
        TrajectoryCurve curve = new TrajectoryCurve(-1d, -2d);
        testCurve(curve, -1d, 1.5d, -1.5d, Direction.LEFT);
    }

    @Test
    public void testLeft1() {
        TrajectoryCurve curve = new TrajectoryCurve(1d, 2d);
        testCurve(curve, 1d, 1.5d, 1.5d, Direction.LEFT);
    }

    @Test
    public void testRight_1() {
        TrajectoryCurve curve = new TrajectoryCurve(-2d, -1d);
        testCurve(curve, -1d, 1.5d, -1.5d, Direction.RIGHT);
    }

    @Test
    public void testRight1() {
        TrajectoryCurve curve = new TrajectoryCurve(2d, 1d);
        testCurve(curve, 1d, 1.5d, 1.5d, Direction.RIGHT);
    }

    @Test
    public void testStraight_1() {
        TrajectoryCurve curve = new TrajectoryCurve(-1d, -1d);
        testCurve(curve, 0d, Double.POSITIVE_INFINITY, -1d, Direction.LEFT);
    }

    @Test
    public void testStraight1() {
        TrajectoryCurve curve = new TrajectoryCurve(1d, 1d);
        testCurve(curve, 0d, Double.POSITIVE_INFINITY, 1d, Direction.LEFT);
    }
}
