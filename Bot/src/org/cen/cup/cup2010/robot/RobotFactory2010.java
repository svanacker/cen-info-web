package org.cen.cup.cup2010.robot;

import java.util.Properties;

import org.cen.cup.cup2010.robot.match.MatchData2010;
import org.cen.robot.AbstractRobot;
import org.cen.robot.AbstractRobotFactory;
import org.cen.robot.IRobot;
import org.cen.robot.RobotDimension;
import org.cen.robot.control.RobotControlEngine;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.Opponent;
import org.cen.vision.dataobjects.WebCamProperties;

/**
 * Implementation of the robot factory for the cup 2010.
 * 
 * @author Emmanuel ZURMELY
 */
public class RobotFactory2010 extends AbstractRobotFactory {
	private static final String PREFIX_MATCH = "match";

	private static final String PREFIX_DIMENSIONS = "dimensions";

	private static final String PREFIX_CONTROL = "control";

	@Override
	protected IRobot newRobotInstance() {
		return new Robot2010();
	}

	@Override
	protected void initializeAttributes(IRobot robot) {
		super.initializeAttributes(robot);
		Properties properties = robot.getConfiguration().getProperties();

		// match data
		MatchData2010 data = new MatchData2010();
		data.setFromProperties(properties, PREFIX_MATCH + ".");

		// motors properties
		RobotDimension dimensions = new RobotDimension(properties, PREFIX_DIMENSIONS + ".");

		// PID
		RobotControlEngine controlEngine = new RobotControlEngine(properties, PREFIX_CONTROL + ".");

		// Webcam
		WebCamProperties wcp = new WebCamProperties();
		wcp.set(properties);

		AbstractRobot r = (AbstractRobot) robot;
		r.addAttribute(MatchData.class, data);
		r.addAttribute(RobotControlEngine.class, controlEngine);
		r.addAttribute(RobotDimension.class, dimensions);
		r.addAttribute(WebCamProperties.class, wcp);
		r.addAttribute(Opponent.class, new Opponent());
	}

	@Override
	public Class<? extends IRobot> getObjectType() {
		return Robot2010.class;
	}
}
