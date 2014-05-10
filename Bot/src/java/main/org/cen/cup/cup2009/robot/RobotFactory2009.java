package org.cen.cup.cup2009.robot;

import java.util.Properties;

import org.cen.cup.cup2009.robot.match.MatchData2009;
import org.cen.robot.AbstractRobotFactory;
import org.cen.robot.IRobot;
import org.cen.robot.attributes.IRobotDimension;
import org.cen.robot.attributes.impl.RobotDimension;
import org.cen.robot.control.RobotControlEngine;
import org.cen.robot.impl.AbstractRobot;
import org.cen.robot.match.MatchData;
import org.cen.vision.dataobjects.WebCamProperties;

public class RobotFactory2009 extends AbstractRobotFactory {
    private static final String PREFIX_MATCH = "match";

    private static final String PREFIX_DIMENSIONS = "dimensions";

    private static final String PREFIX_CONTROL = "control";

    @Override
    public Class<? extends IRobot> getObjectType() {
        return Robot2009.class;
    }

    @Override
    protected void initializeAttributes(IRobot robot) {
        super.initializeAttributes(robot);
        Properties properties = robot.getConfiguration().getProperties();

        // match data
        MatchData2009 data = new MatchData2009();
        data.setFromProperties(properties, PREFIX_MATCH + ".");

        // motors properties
        IRobotDimension dimensions = new RobotDimension(properties, PREFIX_DIMENSIONS + ".");

        // PID
        RobotControlEngine controlEngine = new RobotControlEngine(properties, PREFIX_CONTROL + ".");

        // Webcam
        WebCamProperties wcp = new WebCamProperties();
        wcp.set(properties);

        AbstractRobot r = (AbstractRobot) robot;
        r.addAttribute(MatchData.class, data);
        r.addAttribute(RobotControlEngine.class, controlEngine);
        r.addAttribute(IRobotDimension.class, dimensions);
        r.addAttribute(WebCamProperties.class, wcp);
    }

    @Override
    protected IRobot newRobotInstance() {
        IRobot robot = new Robot2009();
        return robot;
    }
}
