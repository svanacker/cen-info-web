package org.cen;

import org.cen.com.ComManagerTest;
import org.cen.robot.control.RobotControlEngineTest;
import org.cen.robot.device.RobotDeviceFactoryTest;
import org.cen.robot.device.navigation.analysis.com.MotionAnalysisDecoderTest;
import org.cen.robot.device.navigation.com.NavigationDataDecoderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(value = { ComManagerTest.class,  
		NavigationDataDecoderTest.class, RobotDeviceFactoryTest.class, 
		RobotControlEngineTest.class, MotionAnalysisDecoderTest.class })
public class CenTestSuite {

}
