package org.cen.simulRobot;

import java.awt.geom.Point2D;
import java.util.Properties;

import javax.vecmath.Vector3d;

import org.cen.math.PropertiesMathUtils;

public class SonarArea extends AModelisableRobotAttribute {

    protected static final String KEY_SONAR_ANGlE = "sonar.angle";

    protected static final String KEY_SONAR_POSITION = "sonar.position";

    protected static final String KEY_SONAR_SCOPE = "sonar.scope";

    private double angle;

    private final double portee;

    private final Vector3d relativePosition;

    public SonarArea(Properties pProperties) {
        angle = PropertiesMathUtils.getSize(pProperties, KEY_SONAR_ANGlE).getWidth();
        angle = Math.toRadians(angle) / 2 + Math.PI / 2;
        portee = Double.parseDouble((String) pProperties.get(KEY_SONAR_SCOPE));
        relativePosition = (PropertiesMathUtils.getVector(pProperties, KEY_SONAR_POSITION));

        computeRelativeCentralPoint();
    }

    @Override
    protected void computeRelativeCorners() {
        upLeft = new Point2D.Double(relativePosition.y + portee * Math.sin(angle), relativePosition.x - portee
                * Math.cos(angle));
        upRight = new Point2D.Double(relativePosition.y + portee * Math.sin(angle), relativePosition.x + portee
                * Math.cos(angle));
        bottomLeft = new Point2D.Double(relativePosition.y + 0, relativePosition.x + 0);
        bottomRight = new Point2D.Double(relativePosition.y + 0, relativePosition.x + 0);
    }
}
