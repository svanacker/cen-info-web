package org.cen.cup.cup2012.robot;

import java.util.Properties;

import org.cen.actions.GameActionAttribute;
import org.cen.cup.cup2012.robot.match.MatchData2012;
import org.cen.robot.AbstractRobot;
import org.cen.robot.AbstractRobotFactory;
import org.cen.robot.CollisionConfiguration;
import org.cen.robot.IRobot;
import org.cen.robot.RobotDimension;
import org.cen.robot.control.RobotControlEngine;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.Opponent;
import org.cen.vision.dataobjects.WebCamProperties;

/**
 * Implementation of the robot factory for the cup 2012.
 */
public class RobotFactory2012 extends AbstractRobotFactory {

	private static final String PREFIX_MATCH = "match";

	private static final String PREFIX_DIMENSIONS = "dimensions";

	private static final String PREFIX_CONTROL = "control";

	private static final String PREFIX_COLLISION = "collision";

	@Override
	protected IRobot newRobotInstance() {
		return new Robot2012();
	}

	@Override
	protected void initializeAttributes(IRobot robot) {
		super.initializeAttributes(robot);
		Properties properties = robot.getConfiguration().getProperties();

		// match data
		MatchData data = new MatchData2012();
		data.setFromProperties(properties, PREFIX_MATCH + ".");

		// motors properties
		RobotDimension dimensions = new RobotDimension(properties, PREFIX_DIMENSIONS + ".");

		// Collision properties
		CollisionConfiguration configuration = new CollisionConfiguration(properties, PREFIX_COLLISION + ".");

		// PID
		RobotControlEngine controlEngine = new RobotControlEngine(properties, PREFIX_CONTROL + ".");

		// Webcam
		WebCamProperties wcp = new WebCamProperties();
		wcp.set(properties);

		// action handlers
		GameActionAttribute handlers = new GameActionAttribute();

		AbstractRobot r = (AbstractRobot) robot;
		r.addAttribute(MatchData.class, data);
		r.addAttribute(RobotControlEngine.class, controlEngine);
		r.addAttribute(RobotDimension.class, dimensions);
		r.addAttribute(WebCamProperties.class, wcp);
		r.addAttribute(Opponent.class, new Opponent());
		r.addAttribute(CollisionConfiguration.class, configuration);
		r.addAttribute(GameActionAttribute.class, handlers);
	}

	@Override
	public Class<? extends IRobot> getObjectType() {
		return Robot2012.class;
	}
}
