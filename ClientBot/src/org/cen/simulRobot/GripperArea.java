package org.cen.simulRobot;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.Properties;

import javax.vecmath.Vector3d;

import org.cen.math.PropertiesMathUtils;

public class GripperArea extends AModelisableRobotAttribute {

    protected static final String KEY_GRIPPER_DIMENSION = "gripper.dimension";

    protected static final String KEY_GRIPPER_POSITION = "gripper.position";

    private final Dimension dimension;

    private final Vector3d relativePosition;

    public GripperArea(Properties pProperties) {
        dimension = PropertiesMathUtils.getDimension(pProperties, KEY_GRIPPER_DIMENSION);
        relativePosition = (PropertiesMathUtils.getVector(pProperties, KEY_GRIPPER_POSITION));
        computeRelativeCentralPoint();
    }

    @Override
    protected void computeRelativeCorners() {
        upLeft = new Point2D.Double(relativePosition.y + dimension.height / 2, relativePosition.x - dimension.width / 2);
        upRight = new Point2D.Double(relativePosition.y + dimension.height / 2, relativePosition.x + dimension.width
                / 2);
        bottomLeft = new Point2D.Double(relativePosition.y - dimension.height / 2, relativePosition.x - dimension.width
                / 2);
        bottomRight = new Point2D.Double(relativePosition.y - dimension.height / 2, relativePosition.x
                + dimension.width / 2);
    }
}
