package org.cen;

import org.cen.com.ComDataUtilsTest;
import org.cen.com.ComManagerTest;
import org.cen.com.DefaultDecoderTest;
import org.cen.math.MathUtilsTest;
import org.cen.robot.control.RobotControlEngineTest;
import org.cen.robot.device.RobotDeviceFactoryTest;
import org.cen.robot.device.lcd.LcdDeviceTest;
import org.cen.robot.device.navigation.analysis.com.MotionAnalysisDecoderTest;
import org.cen.robot.device.navigation.com.NavigationDataDecoderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(value = { ComDataUtilsTest.class, ComManagerTest.class, MathUtilsTest.class, LcdDeviceTest.class,
		NavigationDataDecoderTest.class, RobotDeviceFactoryTest.class, DefaultDecoderTest.class,
		RobotControlEngineTest.class, MotionAnalysisDecoderTest.class })
public class CenTestSuite {

}
