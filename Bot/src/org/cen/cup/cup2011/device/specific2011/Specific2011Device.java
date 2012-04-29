package org.cen.cup.cup2011.device.specific2011;

import org.cen.com.IComService;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.robot.device.AbstractRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;
import org.cen.robot.device.RobotDeviceRequest;
import org.cen.robot.device.navigation.com.StopOutData;

public class Specific2011Device extends AbstractRobotDevice implements InDataListener {
	public static final String NAME = "specific2011";

	public Specific2011Device() {
		super(NAME);
	}

	@Override
	public void debug(String debugAction) {
		IRobotDevicesHandler handler = servicesProvider.getService(IRobotDevicesHandler.class);
		if (debugAction.equals("nextStep")) {
			handler.getRequestDispatcher().nextStep();
		} else if (debugAction.equals("stepByStepOn")) {
			handler.getRequestDispatcher().setStypByStep(true);
		} else if (debugAction.equals("stepByStepOff")) {
			handler.getRequestDispatcher().setStypByStep(false);
		} else if (debugAction.equals("resetPosition")) {
			IComService comService = servicesProvider.getService(IComService.class);
			comService.writeOutData(new StopOutData());
		}
	}

	@Override
	public void onInData(InData data) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void internalHandleRequest(RobotDeviceRequest request) {
		// TODO Auto-generated method stub
	}
}
