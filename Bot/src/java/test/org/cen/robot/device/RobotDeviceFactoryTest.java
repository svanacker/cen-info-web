package org.cen.robot.device;

import org.cen.cup.cup2011.device.gripper2011.Gripper2011Device;
import org.junit.Assert;
import org.junit.Test;

/**
 * @see RobotDeviceFactory
 */
public class RobotDeviceFactoryTest {

	@Test
	public void should_find_device() throws Exception {
		RobotDeviceFactory factory = new RobotDeviceFactory(null);
		IRobotDevice gripper2011Device = factory.getNewInstance("cup.cup2011.device.gripper2011.Gripper2011");
		Assert.assertEquals(Gripper2011Device.class, gripper2011Device.getClass());
	}
}
