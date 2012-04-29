package org.cen.cup.cup2011.simulRobot;

import java.util.Properties;

import org.cen.cup.cup2011.robot.Robot2011;
import org.cen.cup.cup2011.robot.match.MatchData2011;
import org.cen.robot.AbstractRobot;
import org.cen.robot.AbstractRobotFactory;
import org.cen.robot.IRobot;
import org.cen.robot.RobotDimension;
import org.cen.robot.control.RobotControlEngine;
import org.cen.robot.match.MatchData;
import org.cen.robot.match.MatchSide;
import org.cen.simulRobot.RobotSwitches;
import org.cen.vision.dataobjects.WebCamProperties;

public class SimulRobotFactory2011 extends AbstractRobotFactory {
	private static final String PREFIX_CONTROL = "control";

	private static final String PREFIX_DIMENSIONS = "dimensions";

	private static final String PREFIX_MATCH = "match";

	private static final String SWITCHES = "switches";

	@Override
	public Class<? extends IRobot> getObjectType() {
		return Robot2011.class;
	}

	@Override
	protected void initializeAttributes(IRobot robot) {
		Properties properties = robot.getConfiguration().getProperties();

		// match data
		MatchData2011 data = new MatchData2011();
		data.setFromProperties(properties, PREFIX_MATCH + ".");
		String aside = properties.getProperty(SWITCHES + ".8");
		data.setSide(MatchSide.VIOLET);
		if (aside.equals("0")) {
			data.setSide(MatchSide.RED);
		}

		// motors properties
		RobotDimension dimensions = new RobotDimension(properties, PREFIX_DIMENSIONS + ".");

		// TODO retirer ??
		// PID
		RobotControlEngine controlEngine = new RobotControlEngine(properties, PREFIX_CONTROL + ".");

		// Webcam
		WebCamProperties wcp = new WebCamProperties();
		wcp.set(properties);
		VisionArea visionArea = new VisionArea(wcp);

		// Gripper
		GripperArea gripperArea = new GripperArea(properties);

		// Sonar
		SonarArea sonarArea = new SonarArea(properties);

		// Interrupteur
		RobotSwitches robotSwitches = new RobotSwitches(properties);

		AbstractRobot r = (AbstractRobot) robot;
		r.addAttribute(MatchData.class, data);
		r.addAttribute(RobotSwitches.class, robotSwitches);
		r.addAttribute(RobotControlEngine.class, controlEngine);
		r.addAttribute(RobotDimension.class, dimensions);
		r.addAttribute(VisionArea.class, visionArea);
		r.addAttribute(GripperArea.class, gripperArea);
		r.addAttribute(SonarArea.class, sonarArea);
	}

	@Override
	protected IRobot newRobotInstance() {
		return new Robot2011();
	}
}
