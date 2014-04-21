package org.cen.robot.control;

import org.junit.Test;

/**
 * @see RobotControlEngine
 */
public class RobotControlEngineTest {

	@Test
	public void should_initialize_robot_control_engine() {
		RobotControlEngine controlEngine = new RobotControlEngine();

		for (int pidIndex = 0; pidIndex < RobotControlEngine.PID_COUNT; pidIndex++) {
			PIDData pidData = controlEngine.getPIDData(pidIndex);
			System.out.println(pidData);
		}
	}
}
